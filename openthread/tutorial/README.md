
# Tutorial

* [Get Started](https://openthread.io/guides) 문서에 있는 Codelabs
* Hardware Codelab
  * FTD를 make할 때 USB=1 옵션이 추가되어야함
    * `make -f examples/Makefile-nrf52840 COMMISSIONER=1 JOINER=1 USB=1`
  * screen으로 FTD의 USB serial을 접근할 때 문제가 있음
    * 다른 serial 툴의 사용이 필요
* Border Router
  * NCP를 make할 때 USB=1 LINK_RAW=1 옵션이 추가되어야함
    * `make -f examples/Makefile-nrf52840 BORDER_AGENT=1 BORDER_ROUTER=1 COMMISSIONER=1 UDP_FORWARD=1 USB=1 LINK_RAW=1`
  * Network Name이 기본값과는 다른 값으로 변경되어야 Android App에서 Border Router를 인식
  * joiner start J01NU5가 동작하지 않고, 이후 부분도 동작하지 않음
    * `Join failed [NotFound]`
    * NCP를 make할 때 TMF_PROXY=1 옵션이 추가되어도 여전히 동작하지 않음
* Border Router with Docker
  * Run OTBR Docker의 Emulated NCP
    * Raspberry Pi가 사용될 경우 x86_64-unknown-linux-gnu 대신 armv7l-unknown-linux-gnueabihf
      * `~/openthread/output/armv7l-unknown-linux-gnueabihf/bin/ot-ncp-ftd 1 > /dev/pts/2 < /dev/pts/2`
  * Test Connectivity의 Emulated Thread node
    * 오타
      * `./output/x86_64-unknown-linux-gnu/bin/ot-cli-ftd 2`
    * Raspberry Pi가 사용될 경우 x86_64-unknown-linux-gnu 대신 armv7l-unknown-linux-gnueabihf
      * `./output/armv7l-unknown-linux-gnueabihf/bin/ot-cli-ftd 2`
  * 결과
    * x86_64 + emulated: 2번 FTD의 state가 router로 변경되지 않음
    * x86_64 + nRF52840: 2번 FTD에서 외부로 ping이 되지 않음 (내부는 되는 것 같음)
    * armv71 + emulated: OTBR docker가 시작되지 않음
    * armv71 + nRF52840: OTBR docker가 시작되지 않음
* NCP Support
  * Packet Sniffing
    * Pre-Built NCP Firmware에 나온 것처럼 node가 설정되어도 괜찮음
      * `make -f examples/Makefile-nrf52840 BORDER_AGENT=1 BORDER_ROUTER=1 COMMISSIONER=1 UDP_FORWARD=1 USB=1 LINK_RAW=1`
      * ot-ncp-ftd.hex 사용


## [API Codelab 코드](api_codelab/)
