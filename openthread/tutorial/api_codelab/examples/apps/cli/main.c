/*
 *  Copyright (c) 2016, The OpenThread Authors.
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *  1. Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *  3. Neither the name of the copyright holder nor the
 *     names of its contributors may be used to endorse or promote products
 *     derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */

#include <assert.h>
#include <openthread-core-config.h>
#include <openthread/config.h>

#include <openthread/cli.h>
#include <openthread/diag.h>
#include <openthread/tasklet.h>
#include <openthread/platform/logging.h>


#include <openthread/instance.h>
#include <openthread/thread.h>
#include <openthread/thread_ftd.h>


#include <string.h>

#include <openthread/message.h>
#include <openthread/udp.h>

#include "utils/code_utils.h"


#include <openthread/dataset_ftd.h>


#include "openthread-system.h"

#if OPENTHREAD_EXAMPLES_POSIX
#include <setjmp.h>
#include <unistd.h>

jmp_buf gResetJump;

void __gcov_flush();
#endif


void OTCALL handleNetifStateChanged(uint32_t aFlags, void *aContext);


#define UDP_PORT 1212

static const char UDP_DEST_ADDR[] = "ff03::1";
static const char UDP_PAYLOAD[]   = "Hello OpenThread World!";


static void setNetworkConfiguration(otInstance *aInstance);


#if OPENTHREAD_ENABLE_MULTIPLE_INSTANCES
void *otPlatCAlloc(size_t aNum, size_t aSize)
{
    return calloc(aNum, aSize);
}

void otPlatFree(void *aPtr)
{
    free(aPtr);
}
#endif

void otTaskletsSignalPending(otInstance *aInstance)
{
    OT_UNUSED_VARIABLE(aInstance);
}


static void initUdp(otInstance *aInstance);
static void sendUdp(otInstance *aInstance);

static void handleButtonInterrupt(otInstance *aInstance);

void handleUdpReceive(void *aContext, otMessage *aMessage, 
                      const otMessageInfo *aMessageInfo);

static otUdpSocket sUdpSocket;


int main(int argc, char *argv[])
{
    otInstance *instance;

#if OPENTHREAD_EXAMPLES_POSIX
    if (setjmp(gResetJump))
    {
        alarm(0);
#if OPENTHREAD_ENABLE_COVERAGE
        __gcov_flush();
#endif
        execvp(argv[0], argv);
    }
#endif

#if OPENTHREAD_ENABLE_MULTIPLE_INSTANCES
    size_t   otInstanceBufferLength = 0;
    uint8_t *otInstanceBuffer       = NULL;
#endif

pseudo_reset:

    otSysInit(argc, argv);

#if OPENTHREAD_ENABLE_MULTIPLE_INSTANCES
    // Call to query the buffer size
    (void)otInstanceInit(NULL, &otInstanceBufferLength);

    // Call to allocate the buffer
    otInstanceBuffer = (uint8_t *)malloc(otInstanceBufferLength);
    assert(otInstanceBuffer);

    // Initialize OpenThread with the buffer
    instance = otInstanceInit(otInstanceBuffer, &otInstanceBufferLength);
#else
    instance = otInstanceInitSingle();
#endif
    assert(instance);

    otCliUartInit(instance);


    /* Register Thread state change handler */
    otSetStateChangedCallback(instance, handleNetifStateChanged, instance);


    /* Override default network credentials */
    setNetworkConfiguration(instance);


    /* init GPIO LEDs and button */
    otSysLedInit();
    otSysButtonInit(handleButtonInterrupt);


    /* Start the Thread network interface (CLI cmd > ifconfig up) */
    otIp6SetEnabled(instance, true);

    /* Start the Thread stack (CLI cmd > thread start) */
    otThreadSetEnabled(instance, true);


    initUdp(instance);


#if OPENTHREAD_ENABLE_DIAG
    otDiagInit(instance);
#endif

    while (!otSysPseudoResetWasRequested())
    {
        otTaskletsProcess(instance);
        otSysProcessDrivers(instance);


	otSysButtonProcess(instance);


    }

    otInstanceFinalize(instance);
#if OPENTHREAD_ENABLE_MULTIPLE_INSTANCES
    free(otInstanceBuffer);
#endif

    goto pseudo_reset;

    return 0;
}


void OTCALL handleNetifStateChanged(uint32_t aFlags, void *aContext)
{
   if ((aFlags & OT_CHANGED_THREAD_ROLE) != 0)
   {
       otDeviceRole changedRole = otThreadGetDeviceRole(aContext);

       switch (changedRole)
       {
       case OT_DEVICE_ROLE_LEADER:
           otSysLedSet(1, true);
           otSysLedSet(2, false);
           otSysLedSet(3, false);
           break;

       case OT_DEVICE_ROLE_ROUTER:
           otSysLedSet(1, false);
           otSysLedSet(2, true);
           otSysLedSet(3, false);
           break;
       
       case OT_DEVICE_ROLE_CHILD:
           otSysLedSet(1, false);
           otSysLedSet(2, false);
           otSysLedSet(3, true);
           break;
       
       case OT_DEVICE_ROLE_DETACHED:
       case OT_DEVICE_ROLE_DISABLED:
           /* Clear LED4 if Thread is not enabled. */
           otSysLedSet(4, false);
           break;
        }
    }
}


/**
 * Function to handle button push event
 */
void handleButtonInterrupt(otInstance *aInstance)
{
    sendUdp(aInstance);
}


/**
 * Initialize UDP socket
 */
void initUdp(otInstance *aInstance)
{
    otSockAddr  listenSockAddr;

    memset(&sUdpSocket, 0, sizeof(sUdpSocket));
    memset(&listenSockAddr, 0, sizeof(listenSockAddr));

    listenSockAddr.mPort    = UDP_PORT;
    listenSockAddr.mScopeId = OT_NETIF_INTERFACE_ID_THREAD;

    otUdpOpen(aInstance, &sUdpSocket, handleUdpReceive, aInstance);
    otUdpBind(&sUdpSocket, &listenSockAddr);
}


/**
 * Send a UDP datagram
 */
void sendUdp(otInstance *aInstance)
{
    otError       error = OT_ERROR_NONE;
    otMessage *   message;
    otMessageInfo messageInfo;
    otIp6Address  destinationAddr;

    memset(&messageInfo, 0, sizeof(messageInfo));

    otIp6AddressFromString(UDP_DEST_ADDR, &destinationAddr);
    messageInfo.mPeerAddr    = destinationAddr;
    messageInfo.mPeerPort    = UDP_PORT;
    messageInfo.mInterfaceId = OT_NETIF_INTERFACE_ID_THREAD;

    message = otUdpNewMessage(aInstance, NULL);
    otEXPECT_ACTION(message != NULL, error = OT_ERROR_NO_BUFS);

    error = otMessageAppend(message, UDP_PAYLOAD, sizeof(UDP_PAYLOAD));
    otEXPECT(error == OT_ERROR_NONE);

    error = otUdpSend(&sUdpSocket, message, &messageInfo);

 exit:
    if (error != OT_ERROR_NONE && message != NULL)
    {
        otMessageFree(message);
    }
}


/**
 * Function to handle UDP datagrams received on the listening socket
 */
void handleUdpReceive(void *aContext, otMessage *aMessage, 
                      const otMessageInfo *aMessageInfo)
{
    OT_UNUSED_VARIABLE(aContext);
    OT_UNUSED_VARIABLE(aMessage);
    OT_UNUSED_VARIABLE(aMessageInfo);

    otSysLedToggle(4);
}


/**
 * Override default network settings, such as panid, so the devices can join a network
 */
void setNetworkConfiguration(otInstance *aInstance)
{
    static char          aNetworkName[] = "OTCodelab";
    otOperationalDataset aDataset;

    memset(&aDataset, 0, sizeof(otOperationalDataset));
   
    /*
     * Fields that can be configured in otOperationDataset to override defaults:
     *     Network Name, Mesh Local Prefix, Extended PAN ID, PAN ID, Delay Timer,
     *     Channel, Channel Mask Page 0, Network Master Key, PSKc, Security Policy
     */
    aDataset.mActiveTimestamp                      = 1;
    aDataset.mComponents.mIsActiveTimestampPresent = true;
     
    /* Set Channel to 15 */
    aDataset.mChannel                      = 15;
    aDataset.mComponents.mIsChannelPresent = true;
    
    /* Set Pan ID to 2222 */
    aDataset.mPanId                      = (otPanId)0x2222;
    aDataset.mComponents.mIsPanIdPresent = true;

    /* Set Extended Pan ID to C0DE1AB5C0DE1AB5 */
    uint8_t extPanId[OT_EXT_PAN_ID_SIZE] = {0xC0, 0xDE, 0x1A, 0xB5, 0xC0, 0xDE, 0x1A, 0xB5};
    memcpy(aDataset.mExtendedPanId.m8, extPanId, sizeof(aDataset.mExtendedPanId));
    aDataset.mComponents.mIsExtendedPanIdPresent = true;
    
    /* Set master key to 1234C0DE1AB51234C0DE1AB51234C0DE */
    uint8_t key[OT_MASTER_KEY_SIZE] = {0x12, 0x34, 0xC0, 0xDE, 0x1A, 0xB5, 0x12, 0x34, 0xC0, 0xDE, 0x1A, 0xB5};
    memcpy(aDataset.mMasterKey.m8, key, sizeof(aDataset.mMasterKey));
    aDataset.mComponents.mIsMasterKeyPresent = true;

    /* Set Network Name to OTCodelab */
    size_t length = strlen(aNetworkName);
    assert(length <= OT_NETWORK_NAME_MAX_SIZE);
    memcpy(aDataset.mNetworkName.m8, aNetworkName, length);
    aDataset.mComponents.mIsNetworkNamePresent = true;

#if OPENTHREAD_FTD
    otDatasetSetActive(aInstance, &aDataset);
    
    /* Set the router selection jitter to override the 2 minute default.
       CLI cmd > routerselectionjitter 20
       Warning: For demo purposes only - not to be used in a real product */
    uint8_t jitterValue = 20;
    otThreadSetRouterSelectionJitter(aInstance, jitterValue);
#else
    OT_UNUSED_VARIABLE(aInstance);
#endif
}


/*
 * Provide, if required an "otPlatLog()" function
 */
#if OPENTHREAD_CONFIG_LOG_OUTPUT == OPENTHREAD_CONFIG_LOG_OUTPUT_APP
void otPlatLog(otLogLevel aLogLevel, otLogRegion aLogRegion, const char *aFormat, ...)
{
    OT_UNUSED_VARIABLE(aLogLevel);
    OT_UNUSED_VARIABLE(aLogRegion);
    OT_UNUSED_VARIABLE(aFormat);

    va_list ap;
    va_start(ap, aFormat);
    otCliPlatLogv(aLogLevel, aLogRegion, aFormat, ap);
    va_end(ap);
}
#endif
