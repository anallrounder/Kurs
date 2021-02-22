# 202102.22_월_spring security(5)마지막 | kakao login

## Spring Security 마지막

loadUserByUsername -> UserDetails로 리턴함

UserDetails로 맞추는 두 가지 방법?

1) UserDetails를 direct로 상속하는 방법
(5개 함수 이용) : MemberUser2 implements UserDetails
2) 우리가 사용한 방법 : MemberUser extends User

---

### 로그인 정보 확인 방법3 가지

userDetails - Princial : 세션 객체

#### Home.jsp

```jsp
<sec:authorize access="isAuthenticated()">
   <form:form action="${pageContext.request.contextPath}/logout" method="POST">
       <input type="submit" value="로그아웃" />
   </form:form>
   <p><a href="<c:url value="/loginInfo" />">로그인 정보 확인 방법3 가지</a></p>
   <!--이게 세션?-->
</sec:authorize>
```

### 스프링시큐리티가 세션 객체를 관리하는 방법

#### UserController.java

```java
@GetMapping("/loginInfo")
public String loginInfo(Principal principal) {
  
  // 1.Controller를 통하여 Pincipal객체로 가져오는 방법
  String user_id = principal.getName();
  System.out.println("유저 아이디:" + user_id);

  // 2.SpringContextHolder를 통하여 가져오는 방법(일반적인 빈에서 사용 할수있음 )
  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
  user_id = auth.getName();
  System.out.println("유저 아이디:" + user_id);

  // 3.
  UserDetails userDetails = (UserDetails) auth.getPrincipal();
  System.out.println(userDetails.getUsername());


  //4.
  MemberUser meberUser = (MemberUser) auth.getPrincipal();
  System.out.println(meberUser.getUsername());

  
  // 4.User 클래스로 변환 하여 가져오는 방법
  User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  user_id = user.getUsername();
  System.out.println("유저 아이디:" + user_id);

  return "home";

}
```

<img src="https://github.com/anallrounder/Images/blob/main/securitycontextholder.png?raw=true">

- 이미지 참고: https://flyburi.com/584
- SecurityContext 스태택 -> Authentication 이걸가져옴
UserDetails가 Princial에 있다.
- 소스코드 상에서 이 그림 순서대로 가져온다.

Principal이 문제다. 핵심은 4번. 

```java
//4.
MemberUser meberUser = (MemberUser) auth.getPrincipal();
System.out.println(meberUser.getUsername());
```

userDetails는 우리가 만든 memvberVO를 가지고있다는 말이다.
이게 핵심이다. 이제부터 위의 4번 예시와 같이 꺼낼 수 있다.

getPrincipal()안에 userDetails를 가지고 있고, 그안에 memvberVO를 가지고 있기 때문에 가지고 올 수 있는것
User도 Principal안에 가지고있다.

왜 상속관계가 memberVO - User - UserDetails 이고 Principal이 UserDetails를 가지고있다.
그래서 계속 DownCating(다운캐스팅-형변환)하는것

- 업캐스팅 (부모=자식) / 다운캐스팅(자식=부모): Child child = (Child)parent; 
- 업캐스팅이 선행된 경우, 다운캐스팅이 성립되는 경우가 존재한다.  
  출처: https://mommoo.tistory.com/51 [개발자로 홀로 서기]
  
4번이 핵심이다.

핵심내용 -> 노트 옮기기

---

지금 하는 내용 : 세션객체는 어떤거를 가지고 있고 소스코딩을 어떻게 해서 빼와서 써먹는지 알아보고 있는중임!

---

#### jsp에서 사용하는 방법

```jsp
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

...

<p>principal: <sec:authentication property="principal"/></p>
<p>principal: <sec:authentication property="principal.member.username"/></p>
<!-- 확장된 상태라 member.사용할 수 있다.유저는  -->
<p>principal: <sec:authentication property="principal.member.password"/></p>
<p>principal: <sec:authentication property="principal.member.enabled"/></p>


<!-- 로그인 하지 않은 모든 사용자(로그인 중인 사용자에게는 보이지 않음 -->
<sec:suthorize access="isAnonymous()">
  <a herf="/login">로그인</a>
  회원가입
</sec:suthorize>

<!-- 로그인 중인 사용자 -->
<sec:authorize access="isAuthenticated()">
  로그아웃
  회원정보보기
</sec:authorize>

<!-- 관리자 페이지 -->
<sec:authorize access="hasRole('admin')">
  관리자 페이지
</sec:authorize>
```

가장 대표적인게 이것. 태그라이브러리 사용해주고 ToString이 정의되어있으면 갖다 뿌릴수있다.

- 로그인하지 않은 사용자 isAnonymous()
- 로그인중인 사용자 -로그인중인걸 체크함 isAuthenticatioed()
- role 관리자 페이지 - hasRole('admin')

---

소스코드상에서 어떻게 표현되는지도 꼭 알자. 그리고 이런 것들에 대해서 개인적으로 더 공부하자! 중요한게 많음!!!

### 해킹 방법

- CSRF (반드시 알아야 함)

아래 두 개는 뒤에서 나오면 봄!

- XSS (실무에서 많이 씀)
- 동일출처원칙
- Sql injection  
<br>
- 이것들은 Spring security가 공식적으로 지원해주는 솔루션이 있다.

<br>

> 해킹의 최종 목적: admin 계정 탈취

- 해킹에서는 해당 시스템 버전 정보가 중요하다.  
  Linux, Windows, macOS (반드시 버전 취약점이 나온다.)  
- 해커는 서버 버전에 맞는 취약점을 공격한다. 서버 관리자가 업데이트(보안패치) 안해놨으면 다 털리게되는 것...!(매우 위험)
- 그래서 보안관련 패치는 반드시 해야함!

### CSRF

- 해킹방법중 하나인 CSRF와 이것을 방어하는 방법 (반드시 알아야 함)

#### security-custom-context.xml

```xml
<http>
  <csrf disabled="false" /> 
  <!-- true는 csrf를 적용하지 않겠다. 그래서 default는 false이다. 
        생략하더라도 이게(<csrf disabled="false" />) 적용이된다. 
        꼭 사용하도록 하자!!! -->
  <intercept-url pattern="/login/loginForm" access="permitAll" />
      <intercept-url pattern="/" access="permitAll" />
      <intercept-url pattern="/user/**" access="permitAll" />
      <intercept-url pattern="/admin/**" access="hasRole('ROLE_ADMIN')" />
      <intercept-url pattern="/**" access="hasAnyRole('ROLE_USER, ROLE_ADMIN')" />
  ...
```

#### loginForm.jsp

```jsp
...
<form:form name="f" action="${loginUrl}" method="POST">
    <c:if test="${param.error != null}">
        <p>아이디와 비밀번호가 잘못되었습니다.</p>
    </c:if>
    <c:if test="${param.logout != null}">
        <p>로그아웃 하였습니다.</p>
    </c:if>
    <p>
        <label for="username">아이디</label>
        <input type="text" id="id" name="id" />
    </p>
    <p>
        <label for="password">비밀번호</label>
        <input type="password" id="password" name="password"/>
    </p>
    <!--<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /> -->
    <!--이부분 주석처리를 했음에도 불구하고 개발자도구에서 보면 들어가있는것을 확인할 수 있다. -->
    <button type="submit" class="btn">로그인</button>
</form:form>
...
```

<img src="https://github.com/anallrounder/Images/blob/main/csrf_jsp.jpg?raw=true">

이 부분을 넣지 않았음에도 불구하고 들어가있다.

### CSRF개념

- Cross-site request forgery 사이트간 위조 요청
- POST방식에서만 적용됨  (POST로 날리는건 다 들어있다.)
  - form, ajax 이 두개만 주의하면 된다.
  - 이 두 개에서 처리하는 방법은?
  
#### 나오게 된 배경

- 가장 대표적인 서버 공격 방법이다. (디도스 등 도 많지만)  
- 옥션 공격이 대표적인 예시

##### [CSRF공격이란? _참고](https://oggwa.tistory.com/20)

- 예) 옥션 - 서버는 다른데 있다.

- 해커가 은행직원을 가장해서 email을 사용자에게 보냄  
  이 때 해커가 아는것은 파라미터를 아는 것(예시: www.aution.co.kr/id=1234&pw=9080)  
  admin, id, pw등 이 권한, 계정은 모른다. 하지만 어떻게 하면 id, pw를 즉 이 권한을 어떻게 생성시킬 수 있는지 url을 알고있었던 것

- 그래서 사용자가 링크를 클릭하면 자기가 보내는 id,pw를 insert함  
  이런 방법으로

  패스워드 변경방법 두가지 - 오라클에 sql날리는것/ 서버 관리자는 그거 할줄모름. 주소

<img src="https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FE48Lo%2FbtqzZLt9mL4%2Ftmpp2lntlks5DwM47gYIE0%2Fimg.png"> 이미지 출처 : https://oggwa.tistory.com/20

#### form 에서 방어 방법 (CAPTCHA같은거 생각하면 된다.)

- 변경 요청을 할 때 클릭 한 번으로 변경할 수 없고, 패스워드를 하나 더 줘서 그거를 알아야만 로그인이 되게 만든 것 (스프링 시큐리티가 보내줌)
- POST발행 할 때 마다 hidden으로 패스워드(토큰)를 하나씩 더 보낸다. 그래서 그 패스워드가 없으면 변경 요청이 들어와도 무효를 시킨다.
- 즉 Token(패스워드)를 추가로 줌으로서 해당 사이트 자체를 체크한다. 그래서 사이트가 위조되지 않게끔 해준다.
- 그래서 해커입장에서는 서버에서 발행하는 Token을 알 수 있는 방법이 없게 된다.

```html
<form:form name="f" action="${loginUrl}" method="POST">
  ...
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
  <button type="submit" class="btn">로그인</button>
</form:form>
```

- form태그를 jstl로 쓰면(form:form) 자동으로 아래 내용을 넣어준다.
  
  ```html
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
  ```
  
  그러나 `<form>`만 쓰면 직접 작성해줘야한다.  

- method가 get일때는 상관없지만 post일때는 token(`value="${_csrf.token}"`)을 hidden으로 묶어서 같이 보내게 되어있다. **왜?** 스프링 시큐리티에서는 얘는 이미 서버에서 가지고있기 때문에 보내야한다.

#### ajax로 방어하는 방법

[참고:  CSRF AJAX 전송 방법](https://hyunsangwon93.tistory.com/28)

```javascript
$.ajax({
      type: 'POST',
      contentType: "application/json",
      url:'/csrf/ajax',
      data: JSON.stringify(jsonData), // String -> json 형태로 변환

      //이 두줄을 넣어준다.!!!!!
      beforeSend : function(xhr)
            {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
        xhr.setRequestHeader(header, token);
},
```

---

#### XXS

- 태그 안에 스크립트를 심어서 정보를 빼내는 방법
- 이제는 스프링 시큐리티를 사용하면 서버가 이런것들을 다 방어해 낸다.

---

### 카카오 로그인 (social login)

소셜 로그인 : 실무에서 많이 씀

- 카카오, 네에버, 페이스북, 트위터, 구글...
- 이것의 기본적인 내용은 인증과 권한 리소스!
- document를 읽어내고 활용할 수 있어야한다.  
  - [카카오 REST API - document](https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api)

1) 구글, 페이스북: 스프링에서 라이브러리가 존재함(OAuth2)
  소셜 로그인 쉽게 구현하기 위한 라이브러리 제공함
2) 네이버, 카카오: 스프링에서 제공x, 직접 구현함

- 전체적으로 적용하는 라이브러리도 있기는 함

#### 나오게된 배경

- 각각의 회사에서 디비를 관리함
- 인증 - 권한 주는 것을 전부다 각각의 작은 회사여도 책임져서 했어야 했다.
- 대형 포털기업 구글, 페이스북, 트위터 등장했고, 인증과 권한에 대한 것들을 이 기업들에게 맡기기 시작했다.
  - 이들의 특징은 최고의 엔지니어들이 (돈이있는 기업) 최고의 보안을 보장한다는 믿음이 깔려있어야한다.
  - 이 글로벌 대기업 서버에 인증, 인가를 맡기면 간편하게 로그인을 할 수 있으면서도 보안쪽으로도 낫다고 판단함(물론 여기서 뚤리면 끝남...!)
- 이러면서 Open auth 개념이 나오기 시작했다. - OAuth

리소스 오너(사용자) --- 클라이언트(내 서버) --- 인증서버(구글, 카카오) --- 자원서버(구글, 카카오)

((그림넣기))

---

카카오로 부터 인가 코드를 받아야 한다.

#### Request

##### URL

```java
GET /oauth/authorize?client_id={REST_API_KEY}&redirect_uri={REDIRECT_URI}&response_type=code HTTP/1.1
Host: kauth.kakao.com
```

실제로 스프링으로 api key , redirect_uri 고대로 스프링에서 집어넣고 텍스트로 저렇게 보내면 response로 응답받음 (request, response 응답을 주고받음)

http프로토콜 그대로이다.

```jsp

href="${kakaoUrl}"

```

document를 보자.

#### Sample

##### Request: prompt=none 파라미터 전달

```java
https://kauth.kakao.com/oauth/authorize?response_type=code&client_id={REST_API_KEY}&redirect_u
```

##### Request: prompt=none 파라미터 전달

```java
https://kauth.kakao.com/oauth/authorize?response_type=code&client_id={REST_API_KEY}&redirect
```

카카오 서버가 받음

##### Response: prompt=none 파라미터 전달, 사용자 동의가 필요해 에러 응답

```java
HTTP/1.1 302 Found
Content-Length: 0
Location: {REDIRECT_URI}?error=consent_required&error_description=user%20consent%20required.
```

callback = {REDIRECT_URI}

accesse코드 인가(권한코드) 리소스에 접근할 수 있음 = authorization_code  

resttemplate -http요청을 간단하게 해줄 수 있는 클래스이다.
(안드로이드 Retrofit객체를 많이쓴다.)

```json
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8
{
    "token_type":"bearer",
    "access_token":"{ACCESS_TOKEN}",
    "expires_in":43199,
    "refresh_token":"{REFRESH_TOKEN}",
    "refresh_token_expires_in":25184000,
    "scope":"account_email profile"
}
```

제이슨을 자바객체로 만들어야한다. -> Gson이 (구글) 그렇게 해준다.

http://www.jsonschema2pojo.org/

<img src="https://github.com/anallrounder/Images/blob/main/jsonschema2pojo.png?raw=true">

- propertiy word delimiters: 안의 내용은 꼭 지워야 함!

```java
public class Example { //Example -> KakaoAuth로 바꾸기

public String token_type; //프로필 가져올수있는 권한코드
public String access_token;
public Integer expires_in;
public String refresh_token;
public Integer refresh_token_expires_in;
```

이렇게 작성함

#### Response: 성공 -> 응답을 이렇게 주겠다.

```java
HTTP/1.1 200 OK
{
  "id":123456789,
  "kakao_account": { 
    "profile_needs_agreement": false,
    "profile": {
      "nickname": "홍길동",
      "thumbnail_image_url": "http://yyy.kakao.com/.../img_110x110.jpg",
      "profile_image_url": "http://yyy.kakao.com/dn/.../img_640x640.jpg"
    },
    "email_needs_agreement":false, 
    "is_email_valid": true,   
    "is_email_verified": true,   
    "email": "sample@sample.com"
    "age_range_needs_agreement":false,
    "age_range":"20~29",
    "birthday_needs_agreement":false,
    "birthday":"1130",
    "gender_needs_agreement":false,
    "gender":"female"
  },  
  "properties":{
     "nickname":"홍길동카톡",
     "thumbnail_image":"http://xxx.kakao.co.kr/.../aaa.jpg",
     "profile_image":"http://xxx.kakao.co.kr/.../bbb.jpg",
     "custom_field1":"23",
     "custom_field2":"여"
     ...
  },
}
```

이 제이슨을 한번더 자바로 바꾸고 클래스 작성함

-> 클래스 안에 클래스 구성할 수 있기는 하다. 하지만 주의할 사항이 있다!!!

```java

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class kakaoProfile { //
  private int id;
  private String connected_at;

  //private Properties properties;
  public class Properties { //<- Static을 붙여줘야함
    //클래스 안에 이너클래서 만들게되면 상호참조로 메모리 누수 발생!
    public String nickname;
    public String thumbnail_image;
    public String profile_image;
  }

  private kakaoAccount kakao_account;
}
```

코딩 법칙에 inner class금지라고 나와있다.  

##### 왜 안될까?  

바깥 클래스의 인스턴스에 대한 참조가 해제되어도 비정적 멤버 클래스는 숨은 외부 참조를 갖고있기에 가비지 컬렉션이 바깥 클래스의 인스턴스를 수거하지 못하여 메모리 누수가 생길 수 있다. (상호참조)

- 참고: https://ckddn9496.tistory.com/104

이런 소스코드 더 좋은방법으로 리팩토링해야한다. -> 잘못된 점에 대한 이유를 쓰고 고치자.  

- **이펙티브자바** 3판 24장 참고 : http://www.yes24.com/Product/Goods/65551284 

컨트롤러에서 프로필 받아옴 - 유저랑 통신함

##### 전체방향

1. 인증코드받기
2. 권한코드받기
3. 코드 받은 후 프로파일 받기
4. 디베에 해당 유저가 존재하는지 체크(kakaoid, nerverid 컬럼을 추가)
5. 존재시 강제 로그인, 미존재시 가입페이지로, 또는 강제 가입

---

오늘 과제: 구글, 페이스북 로그인 구현해보기