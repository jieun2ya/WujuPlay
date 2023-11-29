# 🎾 Wujuplay! 🏸
혼자 운동하기 싫을 때, 같이 운동할 사람을 찾고 싶을때 빠르고 간단하게 사용할 수 있는 운동 모임 플랫폼 우주플레이입니다.

## 👨‍💻 참여자 👩‍💻
- SL01223
- crong-ccrong
- jieun2ya
- hwukjunwoo

## 📅 기간
- 전체 기간 : 2023.10 - 2023.11
- 아이디어 회의 : 10.23 - 10.25
- 요구사항 및 개발내용 정리 : 10.25 - 10.30
- 개발 : 10.31 - 11.16
- 테스트 및 오류 개선: 11.17 - 11.23
- 서버 배포 : 11.24


## ❓ 주요기능

- 홈페이지 회원가입 후 로그인, 소셜 로그인(카카오) 가능
- 지도 (카카오맵API)에서 원하는 장소 검색 및 해당 장소에서 모임 모집 가능
- 모임 모집 시작시 채팅방 자동 개설
- 다른 사용자는 모임 정보를 확인 후 모임 참여 가능
- 마이페이지에서 풀캘린더를 통해 내 모임스케쥴 확인 가능
- 모임 모집 / 참여 횟수 유저 프로필에 반영 
- 모임 참여시 채팅방 자동 참여
- 모임시간 1시간 경과시 모임 자동 비활성화, 비활성화 상태 모임에는 참여 불가능 및 채팅방 자동 삭제
- 비활성화된 모임 참여자는 해당 모임에 대한 리뷰작성 가능
- 모임 활성화 상태에는 참여/탈퇴가 자유로움
- 모임 탈퇴시 채팅방 자동 탈퇴
- 활성화 상태 모임의 방장은 동일 종목,장소, -1시간 ~ -1시간 차이의 다른 모임에 모임 합류신청 혹은 합류승인 가능
- 채팅방 새로운 메시지가 왔을때, 합류신청이 들어왔을 때 푸시 알림


## 💻 DB 스키마
![wujuplayERD](https://github.com/final-project-universe/wujuplay/assets/97634089/3df4f2b6-d309-4478-ad47-260dcb63a590)



## 🛠 기술스택

![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)

프론트엔드:
![HTML5](https://img.shields.io/badge/html5-%23E34F26.svg?style=for-the-badge&logo=html5&logoColor=white)
![JavaScript](https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E)
![CSS3](https://img.shields.io/badge/css3-%231572B6.svg?style=for-the-badge&logo=css3&logoColor=white)
![Bootstrap](https://img.shields.io/badge/bootstrap-%238511FA.svg?style=for-the-badge&logo=bootstrap&logoColor=white)
![jQuery](https://img.shields.io/badge/jquery-%230769AD.svg?style=for-the-badge&logo=jquery&logoColor=white)


백엔드: 
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-%23005C0F.svg?style=for-the-badge&logo=Thymeleaf&logoColor=white)


데이터베이스:
![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)

클라우드:
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Oracle](https://img.shields.io/badge/Oracle-F80000?style=for-the-badge&logo=oracle&logoColor=white)



## 🌌 구현 결과  💫
회원가입 및 로그인 <details><summary>
</summary>

| title1 | title2 |
| --- | --- |
| 회원가입 | 로그인 | 
| 상세정보 | 5 | 
</details>

모임 모집및 참여 <details><summary>
</summary>

| title1 | title2 | 
| --- | --- |
| 지도왼쪽모임리스트 | 지도에서 검색 |
| 모임모아보기 | 모아보기검색 |
| 모임만들기 |  모임참여 |
</details>

마이페이지 <details><summary>
</summary>

| title1 | title2 |
| --- | --- |
| 내모임 | 내가연모임 |
| 내가연모임 | 내정보 |
| 7 | 8 | 
</details>

채팅, 푸시알림 <details><summary>
</summary>

| title1 | title2 | 
| --- | --- |
| 1 | 2 | 
| 4 | 5 | 
| 7 | 8 | 
</details>

후기 <details><summary>
</summary>

| title1 | title2 |
| --- | --- | 
| 검색(모아보기) | 작성 |
| 수정 | 삭제 |

</details>

## 🤔 이런점이 아쉬워요
