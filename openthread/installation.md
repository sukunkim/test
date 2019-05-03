# 설치

* [Get Started](https://openthread.io/guides) 문서에 따라 설치

* 오래되어 없어진 패키지의 경우, 대신 이후 버전을 설치
  * libreadline6 -> libreadline8
  * lib32ncurses5 -> lib32ncurses6

* openthread/script/bootstrap 에서 에러가 나면
  * gcc-arm-embedded가 최신 Ubuntu 버전인 disco용은 아직 없어서, 이전 Ubuntu 버전인 cosmic이나 bionic용 gcc-arm-embedded가 설치될 수 있도록 /etc/apt/sources.list.d/team-gcc-arm-embedded-ubuntu-ppa-disco.list 끝에 다음을 추가
    * deb http://ppa.launchpad.net/team-gcc-arm-embedded/ppa/ubuntu cosmic main
    * deb http://ppa.launchpad.net/team-gcc-arm-embedded/ppa/ubuntu bionic main
  * arm-none-eabi-addr2line가 이미 설치되어 있다는 에러는 무시
