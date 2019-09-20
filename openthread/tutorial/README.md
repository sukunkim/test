
# Tutorial

* [Get Started](https://openthread.io/guides) 문서에 있는 Codelabs
* Hardware Codelab
  * FTD를 make할 때 USB=1 옵션이 추가되어야함
    * make -f examples/Makefile-nrf52840 COMMISSIONER=1 JOINER=1 USB=1
  * screen으로 FTD의 USB serial을 접근할 때 문제가 있음
    * 다른 serial 툴의 사용이 필요
* Border Router
  * NCP를 make할 때 USB=1 LINK_RAW=1 옵션이 추가되어야함
    * make -f examples/Makefile-nrf52840 BORDER_AGENT=1 BORDER_ROUTER=1 COMMISSIONER=1 UDP_FORWARD=1 USB=1 LINK_RAW=1


## [API Codelab 코드](api_codelab/)
