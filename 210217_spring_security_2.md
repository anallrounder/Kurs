# 2021.02.17_수_Spring security(2)

## 어제 수업 복습 + filter

### 1. 가장 기본적인 세팅(1)

로그인 낚아채기 + 어디서?

```xml
<http> 
  <form-login />
</http> 

<!-- provider --> 
<authentication-manager>

</authentication-manager>
```

이 자체가 객체생성이다.  
이 상태에서 로그인을 하면 낚아채는걸 확인했다.  
그러면 내부적으로 어떤식으로 낚아채는건지 알아보자.

- 스프링영역에서 Interceptor도 response, request 낚아챘다. 그런데 낚아채는 위치가 스프링 시큐리티와 위치가 다르다.  
- 스프링 시큐리티는 어디서 작동할까? -> 필터에서 (DispatcherServlet가기 전에 로그인을 낚아 챔 - 12개 필터!)

- 낚아챌때 핵심은 Spring이나 JSP 핵심 객체는 똑같이 다음 두 개이다.
  1. HttpServletRequest
  2. HttpServletResponse

  이 두 객체를 가져와서 모델로 만들고 하는것!

#### web.xml에 우리가 어제 필터를 적용해놨다.

```xml
<!--한글처리 필터를 위에 먼저 두고, 
    이 다음에 아래에 스프링 시큐리티 필터 적용!-->

<!-- Spring Security Filter -->
    <filter>
        <filter-name>springSecurityFilterChain</filter-name>
        <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
    </filter>
 
    <filter-mapping>
          <filter-name>springSecurityFilterChain</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
```

---

## 오늘 수업!

### 2. 가장 기본적인 셋팅 (설명2)

이 세팅 방법은 "이제부터 권한을 주겠다."는 것!

- all - 모든사람한테 다 권한을 줌
- security에서 member로 들어오면 roll이 meber인 사람에게만 권한을 주겠다.

```xml
<http> 
  <intercept-url pattern="/security/all" access="permitAll" />
  <intercept-url pattern="/security/member" access="hasRole('ROLE_MEMBER'" />  
  <form-login />
</http> 

<!-- provider --> 
<authentication-manager>

</authentication-manager>
```

### Controller

#### SecurityController.java 

```java
package edu.bit.ex;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j;

/**
 * Handles requests for the application home page.
 */
@Log4j
@Controller
@RequestMapping("/security*")
public class SecurityController {

  @GetMapping("/all")
  public void doAll() {
    log.info("do all can access everybody");
  }

  @GetMapping("/member")
  public void doMember() {

    log.info("logined member");
  }

}
```

#### security-context.xml

```xml
<intercept-url pattern="/security/all" access="permitAll" />
<intercept-url pattern="/security/member" access="hasRole('ROLE_MEMBER'" />
```

이 두줄 추가

- 로그인, 로그아웃은 디폴트로 가져감
- 그러나 이 두 주소는 디폴트가 아니라서 지정을 해 줘야한다.

jsp 추가

> src > main > webapp > WEB-INF > views > security

#### all.jsp

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>    
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<!-- all or member or admin -->
<h1>/sample/all page</h1>


<sec:authorize access="isAnonymous()"> <!-- user가 익명이면 >> permitAll을 말함 -->

  <a href="/customLogin">로그인</a>

</sec:authorize>

<sec:authorize access="isAuthenticated()">  <!-- 인증된 유저면 //is니까 true of false -->

  <a href="/customLogout">로그아웃</a>

</sec:authorize>

</body>
</html>
```

#### member.jsp

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>/sample/member page</h1>
</body>
</html>
```

#### index.jsp

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <title>메이페이지</title>
</head>

<body>

<h1>메인페이지</h1>

<sec:authorize access="isAnonymous()">
  <p><a href="<c:url value="/security/login/loginForm" />">로그인</a></p>
</sec:authorize>

<sec:authorize access="isAuthenticated()">
  <form:form action="${pageContext.request.contextPath}/logout" method="POST">
      <input type="submit" value="로그아웃" />
  </form:form>
  <p><a href="<c:url value="/loginInfo" />">로그인 정보 확인 방법3 가지</a></p>
</sec:authorize>

<h3>
    [<a href="<c:url value="/user/userHome" />">유저 홈</a>]
    [<a href="<c:url value="/admin/adminHome" />">관리자 홈</a>]
</h3>
</body>
</html>
```

- jsp에서 시큐 리티 태그사용하는방법(5번째) 알아야함 -> 나중에 다시보자.
- 내부 스프링 시큐리티 동작은 체크 안된 권한에 대해서는 로그인으로 돌아서 로그인을 다시 시키는 매커니즘을 가지고있다는 것을 기억하자.

#### 인증-> 권한 -> 리소스

반드시 이 순서로 진행이 된다. 이 세가지를 관리하는게 스프링 시큐리티이다.

- 무엇에 대한 권한? : .jsp, .html(파일)에 접근할 수 있는 권한
- 리소스 = .jsp, .html -> 권한을 얻었을 때 접근할 수 있는 페이지들(파일들)을 말함
- 유저가 리소스에 접근할 수 있는 방법은 url밖에 없다.

---

### 3. 가장기본적인 셋팅(설명3)

#### security-context.xml (설정)

1.스프링 5 부터  PasswordEncoder 를 사용하도록 강제하고 있음
  만일 패스워드 인코딩 없이 사용 하고 싶다면 {noop}을 추가 함

```xml
<http> 
  <intercept-url pattern="/security/all" access="permitAll" />
  <intercept-url pattern="/security/member" access="hasRole('ROLE_MEMBER')" />  
  <form-login />
</http> 

<!-- provider --> 
<authentication-manager> <!-- 이건 내부에 있는 생기는 객체이다. -->
  <authentication-provider> 
    <user-service> 
      <user name="member" password="{noop}member" authorities="ROLE_MEMBER" /> 
    </user-service> 
  </authentication-provider>
</authentication-manager>
```

- 멤버도 이제 접근을 하게 해보고싶다.  
- 실무에서는 이렇게하지않고 인메모리방식으로한다(?)  
- 여기서 `{noop}`을 안주면 에러난다. 그래야 패스워드를 멤버로 집어넣을수있음.

##### 만약에 암호화 없이 사용하고 싶다면 no operation?

- **{noop}** 패스워드 인코딩 적용안하고 쓰겠다. no operation  
  참고 : https://spring.io/blog/2017/11/01/spring-security-5-0-0-rc1-released#password-encoding

  스프링 시큐리티(5.0)부터는 passwordEncoder를 통해서 패스워드를 암호화 시키게 되어있다. 그래서 임시로 `{noop}`을 추가해서 기능을 사용하지 않도록 만든것

##### 동작원리는?

- user에 name, password, authority를 로그인을 누르면 내부적으로 체크를한다 자기가 가진 유저중에 권한이 있는지 확인한다.
- 로그인하면 quthentication-manger가 가지고있는거하고 매칭을 시킨다.

---

#### 문제

로그인창에서 member아니고 manager로 로그인하면 member페이지가 보이게 하세요.

```xml
  <http>
    <intercept-url pattern="/security/all" access="permitAll" />
    <intercept-url pattern="/security/member" access="hasRole('ROLE_MANAGER')" />
    <form-login />
  </http>

  <!-- provider -->

  <authentication-manager> <!-- 이건 내부에 있는 생기는 객체이다. -->
    <authentication-provider>
        <user-service> <!-- 유저도 객체다. -->
          <user name="manager" password="{noop}manager" authorities="ROLE_MANAGER" />
          <!-- 유저를 생성시켜서 authorities을 줌 -->
        </user-service>
    </authentication-provider>
  </authentication-manager>

```

- 유저를 메모리에 바로 띄우는 걸 인 메모리 방식이다 라고 함 (인 메모리: 메모리에 띄운단거라는 것만 알면 된다.)

---

### 4. 가장 기본적인 셋팅(설명4)

```xml
<http> 
  <intercept-url pattern="/security/all" access="permitAll" />
  <intercept-url pattern="/security/member" access="hasRole('ROLE_MEMBER')" />  
  <intercept-url pattern="/security/admin" access="hasRole('ROLE_ADMIN')" />  
  <form-login />
</http> 

<!-- provider --> 
<authentication-manager>
  <authentication-provider> 
    <user-service> 
      <user name="member" password="{noop}member" authorities="ROLE_MEMBER" /> 
      <user name="admin" password="{noop}admin" authorities="ROLE_MEMBER,ROLE_ADMIN" /> 
    </user-service> 
  </authentication-provider>
</authentication-manager>
```

#### admin.jsp 추가

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>/sample/admin page</h1>

<p>principal : <sec:authentication property="principal" /></p>
<%-- <p>사용자이름 : <sec:authentication property="principal.userName" /></p> --%>
<a href="/customLogout">Logout</a>

</body>
</html>
```

#### view - http://localhost:8282/ex/security/admin 확인

##### console

```console
/sample/admin page
principal : org.springframework.security.core.userdetails.User@586034f: Username: admin; Password: [PROTECTED]; Enabled: true; AccountNonExpired: true; credentialsNonExpired: true; AccountNonLocked: true; Granted Authorities: ROLE_ADMIN,ROLE_MEMBER

Logout
```

```xml
<user name="admin" password="{noop}admin" authorities="ROLE_MEMBER, ROLE_ADMIN" />
```

- 인증정보 username같은거를 view 페이지에서 사용하고 싶을 때 예전엔 DB에서 가져와서 Model에 넣어서 jsp로 넘겼다.  
  그런데 스프링 시큐리티를 쓰게되면 그럴 필요가 없다. 유저 정보를 메모리에 올려두는데, (기본적으로 세션이다.) 세션 안에 시큐리티 관련된 유저정보들을 올려둔다. 세션 시간이 지나서 날아가면 유저 정보가 필요 없어서 날린다.  
  이 설정만으로 알아서 몇 가지 세션 객체안에 몇가지 객체를 만들어놓고 관리를하는데 그 중에 하나가 principal이다. 이건 jsp에서 간단한 username, email 등등 써먹을 수 있도록 꺼내올 수 있다. 그래서 유저 정보를 가지고 있는 객체가 principal이라는 걸 보여주는거다.
- `<p>principal : <sec:authentication property="principal"/></p>`
  
- 지금 로그인된 상태는 이거기 때문에 얘는 member도, admin도 치고 들어갈 수 있다. (권한이 두개기 때문에) 

```xml
<user name="admin" password="{noop}admin" authorities="ROLE_MEMBER, ROLE_ADMIN" />`
```

- 우리가 쓸수있는게 있고 스프링만 쓸수있는게 있는데 그 중에는 principal 객체에는 저 정보들을 꺼내쓸 수 있다는 말이다.
- 자세하게 쓰는 방법은  나중에 다시 나옴. 객체도 많다.
- 스프링 시큐리티가 세션에서 제공하는 객체들, 제공하는 함수가 세 개정도 있긴하다. username, passowrd(당연히 안보여줌)

- 꺼내오는건 기본적으로 유저네임 세션에 존재하기 때문에 꺼내올 수 있다.

- 우리가 예전에 손으로하던걸 스프링 시큐리티가 어느정도 해주는데, 어떤 부분을 자동적으로 해주는지 감을 잡아야한다. 내부원리는 똑같다.
그러면서 시큐리티가 어디까지 해주는지도 감을 잡아야하고 
로그인 낚아챈다. login, id pw 컨트롤 세션에 컨트롤했는데 
그 저장하는 이름이 있더라.  
얘도 똑같이 세션에 저장을 하는데 그 저장한거를 해당 객체가 당연히 있을거다 라는것 까지 감을 잡아야한다. (객체가 몇개 있다.)

어디안에 누가 들어있는지 4-5가지 객체가있는데 더 배워야한다.

세션객체에서 우리가 써먹을수 있는 객체이름 지금 1개 배운게 principal이다.

동그라미 그림 안에 principal, user더 있어야한다.

---

### 어제 숙제 풀이 :  마이바티스에서 1:N처리 방법(Join)

- 처리 방법이 2가지가 있다.
- sql과 처리 방법에는 1:n은 전혀 관계가 없다.

<br>

- 일단 두 테이블을 묶어주면 카티시프로덕트로 48개 나올것. join을 써야 조건(where) 줘서 쓸모없는 데이터를 걷어내서 12개가 나온다.
- 1:N의 관계는 join문으로 가져오는데 이 자체를 자바 객체로 옮기는데 두가지 방법이 있다는 것!

#### 첫 번째 방법 - 하나의 객체로 만들어서 리스트로 가져오기

- 컬럼 하나를 다 묶는 방법이 있다. 하나의 객체로 만들어서 리스트로 가져오는것. 컬럼 자체를 한 개, 한 개 맵핑시켜서 VO에서 컬럼을 다 가져오는 방법이 있고(VO구성자체를 애초에 이렇게 하는 것), join을 썼으면 이름을 하나 하나 다 맵핑시키는 방법이 있다.
- 이렇게한다고 나쁜 방법인 것은 아니다. collection 안쓰고 실제로 이렇게도 많이 쓴다.
- 이렇게 칼럼이 쭉 나오는(SQL에서)것을 VO로 만들어서 getlist()로 한꺼번에 가져오는게 가장 단순한 방법이다.

#### 두 번재 방법 - 마이바티스가 공식적으로 제공하는 1:N

- collection + assosiation 방법이다.
- 마이바티스가 1:N 제공, Framework에서 이걸 받아들이고 제공하기 시작했다.

<br>

- emp는 그냥 적어주고 리스트(EmpVO를 그대로 끌고옴)를 dept로 받아온다.
  
- resultMap 으로 return해라
- prpperty 변수명 - (자바 변수명 매칭)
- column 칼럼명 - (DB 칼럼명 매칭)  
  -> 이 두 개 매칭시켜줌

- key는 id로 적어줌 (result로 적어도 상관은 없음)
- 자바타입은 linkedList, arrayList 상관없다.
- resultMap은 위에거 맵핑

- 써먹어야 하니까 컨트롤러 수정!!  
- @toString : 부모에(Object)있는거 overriding(기억안나면 찾아보기) -> sysout

---

#### 질문

> /security/memeber 등이 아니고 login창에서 바로 member로 로그인을 하는 경우 왜 memeber 페이지로 안가고 home.jsp로 갈까?

디폴트로 루트로 보내버린다. 처음 치고 들어올 때 `/security/memeber` 등의  주소로 들어오면 권한을 체크하기 위해서 로그인 페이지로 보낸다. 로그인에서 로그인하면 all로 권한 체크 할게 없어서 루트로 보내기 때문에 home.jsp가 나오는 것이다.  