
<p align="center">
    <img src="https://user-images.githubusercontent.com/33251241/229570766-d7883bb1-4d0b-40b6-931e-0bbc0bbb0721.png" alt="full-logo" width="400" height="80">
</p>
<h2 align="middle">프로젝트 소개</h2>
<p align="middle">개발자를 위한 책 추천/기록 서비스입니다⚡</p>
<p align="middle">프로젝트 url 👉 https://books4dev.me </p>

<br><br>
## 기획 의도 💡
📚 비전공자로 시작하면서 연차에 맞는 책을 고르기가 어려웠습니다.  
📚 언제 읽는게 좋을지 알려준다면 도움이 되지 않을까 하는 생각으로 만들었습니다 :)    
<br><br>
## 기술 스택 💡
백엔드 : Java 11, Spring Boot 2.5  
프론트엔드 : vue 2  
데이터베이스 : MySQL(RDS), Redis(ElastiCache)  
인프라 : AWS EC2, ALB, AutoScaling, CloudWatch, S3, CloudFront, Route53  
CI/CD : Jenkins, Docker, AWS CodeDeploy, S3  
<br><br>
## Infra Architecture 💡
<img width="1337" alt="infa" src="https://user-images.githubusercontent.com/33251241/229568750-4a3544dc-299c-4fc2-9870-4b7a8b1fbc9e.png">

## Entity 설계 💡
<img width="791" alt="entity" src="https://user-images.githubusercontent.com/33251241/230027114-db6d1e6c-af58-42aa-ac48-0dd27d21d6aa.png">

## 기능 소개
## 1. 조회 
📚 유저들이 읽은 책들을 볼 수 있습니다.  
📚 직군, 추천 시기 등으로 필터링이 가능합니다!  
<br>
![main](https://user-images.githubusercontent.com/33251241/230155767-7a682b4c-8d00-4f64-8bf6-13391ab8cfda.gif)

<br><br>
## 2. 검색
📚 카카오 책검색 API를 이용해 책 데이터를 가져옵니다.  
📚 유저들이 읽은 책들은 추천 시기, 평점 등 리뷰 정보가 표시됩니다!  
<br>
<img width="767" alt="search" src="https://user-images.githubusercontent.com/33251241/230157622-823eb9e6-5c90-4ff3-b7c3-3d2c08ed4862.png">

<br><br>
## 3. 내 서재
📚 책들이 읽은 시기에 따라 분류되어 보여집니다!  
📚 같은 책이라도 읽은 시기가 다르면 여러번 등록 가능합니다!  
<br>
<img width="765" alt="memberpage" src="https://user-images.githubusercontent.com/33251241/230158161-5cb915b8-2f6f-4a6a-a368-c7deac6440ab.png">

<br><br>
## 4. 댓글
📚 댓글을 통해 유저간 소통이 가능합니다!  
📚 유저의 닉네임을 클릭하여 유저 프로필을 볼 수 있습니다!  
<br>
<img width="768" alt="comment" src="https://user-images.githubusercontent.com/33251241/230162264-ed13e542-7881-47be-94f9-64cb0d3db4bc.png">
<br><br>
## 프론트엔드 Repository 
👉 https://github.com/mincheolkk/Books4dev_front
