# 설치

## 개요

* 하드웨어와 shell로 연결을 위해서, pip3로 pyserial의 설치가 필요
```
sudo apt update
sudo apt install python3-pip
pip3 install pyserial
```
* 타겟 하드웨어 별로 RIOT/boards 내 디렉터리에 doc.txt가 있음


## nRF52840DK

* SEGGER J-Link 설치 필요
  * [J-Link Software and Documentation pack for Linux, DEB installer, 64-bit](https://www.segger.com/downloads/jlink/#J-LinkSoftwareAndDocumentationPack)
  * `sudo apt install ./JLink_Linux_V650b_x86_64.deb`
* ~~RIOT/dist/tools/nrf52_resetpin_cfg 실행 필요~~
  * ~~`BOARD=nrf52840dk make all flash term`~~
* 참고로, OpenThread와는 달리, 퓨징 설정 (퓨징용 USB 커넥터, nRF power source = VDD) 상태로 shell도 사용 가능


## CC2650STK

* UniFlash 설치 필요
  * [Linux Installer for UniFlash](http://www.ti.com/tool/download/UNIFLASH)
  * `sudo ./uniflash_sl.5.1.0.2397.run`
  * 설치위치가 /opt/ti/uniflash_5.1.0/
* 편의를 위해, RIOT/boards/cc2650stk/Makefile.include에 다음을 추가
  * `export UNIFLASH_PATH = /opt/ti/uniflash_5.1.0/`
* CR2032 배터리가 장착되어야 flash에 write할 수 있음
* shell이 동작하지 않음
