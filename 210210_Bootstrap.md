# 2021.02.10_Bootstrap

부트스트랩은 이제 일반화가 되어버렸다.
반응형을 완벽하게 지원한다.
비슷한게 너무 많은게 단점

- 트위터에서 만든 html5, css3, javascript 오픈소스 툴킷
- 반응형, 모바일위주의 웹 개발을 위한 웹 프레임워크
- 전문가들이 미리 만들어둔 framework를 사용해 높은 품질의 웹 페이지를 손쉽게 개발 가능해짐

### 사용방법

- https://bootswatch.com  
- https://www.w3school.com/bootscrtp4/

- cdn형태로 받거나 소스를 직접 다운로드 받아서 사용할 수 있다.
- 여기서 실습하자.
https://www.w3schools.com/bootstrap4/bootstrap_get_started.asp

### Responsive Containers 

그리드 시스템에 대해서 아는것이 가장 중요함!!

[container 실습 -> w3schools.com](https://www.w3schools.com/bootstrap4/bootstrap_containers.asp)

You can also use the .container-sm|md|lg|xl classes to create responsive containers.  
The max-width of the container will change on different screen sizes/viewports:

|     Class     | Extra small | Small  | Medium | Large  | Extra large |
| :-----------: | :---------: | :----: | :----: | :----: | :---------: |
|       -       |   <576px    | ≥576px | ≥768px | ≥992px |   ≥1200px   |
| .container-sm |    100%     | 540px  | 720px  | 960px  |   1140px    |
| .container-md |    100%     |  100%  | 720px  | 960px  |   1140px    |
| .container-lg |    100%     |  100%  |  100%  | 960px  |   1140px    |
| .container-xl |    100%     |  100%  |  100%  |  100%  |   1140px    |

[부트스트랩 사이트에서 반응형 확인해보기](https://getbootstrap.com/docs/5.0/examples/)


w3schools에서 부트스트랩 실습을 할 수 있다.
https://www.w3schools.com/bootstrap4/bootstrap_grid_basic.asp

http://bootstrapk.com/css/
이거를 참고하자

그 외에 참고사항은 거의 대부분 [v5.0](https://getbootstrap.com/docs/5.0/getting-started/introduction/) <- 이 사이트 부트스트랩 document 페이지에서 참고 하면 된다. 5.0.이하 버전에 대한 내용도 볼 수 있다.  
CDN도 이곳에서 받으면 된다.

---

### 실습

로그인 페이지 아래 사이트 참고해서 부트스트랩으로 스스로 만들어보자.  
[부트스트랩 로그인 예제 사이트](https://getbootstrap.com/docs/4.0/examples/sign-in/)