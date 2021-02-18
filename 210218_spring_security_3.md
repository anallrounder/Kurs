# 2021.02.18_목_spring security(3)

## spring security 3번째 시간

### 5. 가장기본적인 셋팅(설명5)- 에러페이지 추가

- 403 에러 처리 : 권한 처리 하는 방법

#### security-context.xml

```xml
<http> 
  <intercept-url pattern="/security/all" access="permitAll" />
  <intercept-url pattern="/security/member" access="hasRole('ROLE_MEMBER')" />  
  <intercept-url pattern="/security/admin" access="hasRole('ROLE_ADMIN')" />  
  <form-login />
  <!-- 403 에러 처리 -->
  <access-denied-handler error-page="/security/accessError"/>
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

- 지금 멤버로 로그인했는데 security/admin을 치고 들어가면 403 에러가 난다.
- 이때 에러처리 방법 제공: access-denied-handler
- 권한 에러가 나게되면 `error-page="/security/accessError"` 이거로 치고 들어가라.
- 이 주소를 컨트롤러한테 넘겨준다. (개발자한테 맡긴다.) -> 그래서 컨트롤러에서 처리해준다.

#### SecuritySampleController.java

다음 내용 추가

```java
@GetMapping("/accessError")
public void accessError(Authentication auth, Model model) {
  log.info("accessd denied" + auth); //이건 써먹을수있다고 알려주려고 써본것
  model.addAttribute("msg", "Access Denied"); // 메세지로 접근 거절당했다고 알려줌
}
```

Authentication -> 세션 객체다. (컨트롤러에서 써먹을 수있다.)
(+ Principal 객체도 컨트롤러에서 써먹을 수 있다.)

##### 컨트롤러에서 사용할 수 있는 객체:

<img src="https://github.com/anallrounder/Images/blob/main/securitycontextholder.png?raw=true">

#### accessError.jsp 추가!

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ page import="java.util.*" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>Access Denied Page</h1>

<h2><c:out value="${SPRING_SECURITY_403_EXCEPTION.getMessage()}"/></h2>

<h2><c:out value="${msg}"/></h2>

</ul>
</body>
</html>
```

- `${SPRING_SECURITY_403_EXCEPTION.getMessage()}`  
  : 이것도 이런게 있다고 보여주려고 사용한 것! 내장된 전역 객체이다.

- `${msg}`  
  : 우리가 넘겨준것이다.

```console
[2021-02-18 11:09:40] INFO : edu.bit.ex.HomeController - Welcome home! The client locale is ko_KR.
[2021-02-18 11:12:52] INFO : edu.bit.ex.SecurityController - logined member
[2021-02-18 11:12:59] INFO : edu.bit.ex.SecurityController - accessd deniedorg.springframework.security.authentication.UsernamePasswordAuthenticationToken@8d0a4d: Principal: org.springframework.security.core.userdetails.User@bfc28a9a: Username: member; Password: [PROTECTED]; Enabled: true; AccountNonExpired: true; credentialsNonExpired: true; AccountNonLocked: true; Granted Authorities: ROLE_MEMBER; Credentials: [PROTECTED]; Authenticated: true; Details: org.springframework.security.web.authentication.WebAuthenticationDetails@2cd90: RemoteIpAddress: 0:0:0:0:0:0:0:1; SessionId: EA3847B64AEC059D937B5C669A8C7E2D; Granted Authorities: ROLE_MEMBER
[2021-02-18 11:13:01] INFO : edu.bit.ex.SecurityController - accessd deniedorg.springframework.security.authentication.UsernamePasswordAuthenticationToken@8d0a4d: Principal: org.springframework.security.core.userdetails.User@bfc28a9a: Username: member; Password: [PROTECTED]; Enabled: true; AccountNonExpired: true; credentialsNonExpired: true; AccountNonLocked: true; Granted Authorities: ROLE_MEMBER; Credentials: [PROTECTED]; Authenticated: true; Details: org.springframework.security.web.authentication.WebAuthenticationDetails@2cd90: RemoteIpAddress: 0:0:0:0:0:0:0:1; SessionId: EA3847B64AEC059D937B5C669A8C7E2D; Granted Authorities: ROLE_MEMBER
```

스프링시큐리티에서 제공하는걸 최대한 활용하기 위해서 403에러 처리도 해봤다. 이제 로그인 페이지를 커스텀해보자.

### 6. 가장기본적인 셋팅(설명6) - 로그인 페이지 커스텀 화

- `<form-login />` 이건 디폴트! 기본 로그인 페이지
  그래서 이걸 자기걸로 커스터마이징 하겠다는 것!

```xml
<http> 
  <intercept-url pattern="/security/all" access="permitAll" />
  <intercept-url pattern="/security/member" access="hasRole('ROLE_MEMBER')" />  
  <intercept-url pattern="/security/admin" access="hasRole('ROLE_ADMIN')" />
  
  <!-- 로그인 페이지 커스텀 화 -->
  <form-login login-page="/login/loginForm"
              default-target-url="/"
              authentication-failure-url="/login/loginForm?error"
              username-parameter="id"
              password-parameter="password" />

  <logout logout-url="/logout" logout-success-url="/" /> 
  
  <!-- 403 에러 처리 -->
  <access-denied-handler error-page="/security/accessError"/>
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

- 로그아웃 하면 세션은 날려버린다.

로그인 페이지는 홈에서 만들자!! 루트주소에서 바로 연결되게!
(이거 실수로 홈에 안만들고 로그인컨트롤러에 만들었다가 오류났었다...!)

> edu.bit.ex

#### HomeController.java

- 이 부분을 homeController에 추가한다.
  
```java
@GetMapping("/login/loginForm")
public String loginForm() {
  log.info("welcome login form");
  return "login/loginForm2";
}
```

```java
package edu.bit.ex;

import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.extern.log4j.Log4j;

/**
 * Handles requests for the application home page.
 */

@Log4j
@Controller
public class HomeController {

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String home(Locale locale, Model model) {
    log.info("Welcome home! The client locale is {}." + locale);
    return "home";
  }

  @GetMapping("/login/loginForm")
  public String loginForm() {
    log.info("welcome login form");
    return "login/loginForm2";
  }
}
```

### 로그인 view pages

> src > main > webapp > WEB-INF > views > login

#### loginForm.jsp

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>로그인 페이지</title>
</head>

<body onload="document.f.id.focus();">

<h3>아이디와 비밀번호를 입력해주세요.</h3>

<c:url value="/login" var="loginUrl" />
<p>${loginUrl}</p>
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
    <!--<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />  -->
    <button type="submit" class="btn">로그인</button>
</form:form>

</body>
</html>
```

#### loginForm2.jsp

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/WEB-INF/include/header.jspf"  %>
<title>Login</title>
</head>
<body onload="document.f.id.focus();">
    <br><br>
    <div class="container text-center">
        <h1>로그인 페이지</h1><br>
    </div>
    <c:url value="/login" var="loginUrl" />
    <div class="container col-md-4">
      <form:form name ="f" class="px-4 py-3" action="${loginUrl}" method="post">
            <c:if test="${param.error != null}">
            <p>아이디와 비밀번호가 잘못되었습니다.</p>
        </c:if>
        
        <c:if test="${param.logout != null}">
            <p>로그아웃 하였습니다.</p>
        </c:if>
        
          <div class="form-group">
              <label for="exampleDropdownFormEmail1">ID</label>
              <input type="text" class="form-control" name="id" placeholder="example">
          </div>
          <div class="form-group">
              <label for="exampleDropdownFormPassword1">Password</label>
              <input type="password" class="form-control" name="password" placeholder="Password">
          </div>
          <div class="form-check">
              <label class="form-check-label">
              <input type="checkbox" class="form-check-input">
              Remember me
              </label>
          </div>
  <!-- <input name="${_csrf.parameterName}" type="hidden" value="${_csrf.token}"/>  -->
          <button type="submit" class="btn btn-primary">Sign in</button>
    </form:form>
      <div class="dropdown-divider"></div>
      <a class="dropdown-item" href="#">New around here? Sign up</a>
      <a class="dropdown-item" href="#">Forgot password?</a>
  </div>

</body>
</html>
```

### 부트스트랩 적용

> src > main > webapp > WEB-INF > include

#### header.jspf

선생님 코드 복붙

```jspf
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<meta charset="utf-8">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css" integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js" integrity="sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js" integrity="sha384-h0AbiXch4ZDo7tp9hKZ4TsHbi047NrKGLO3SEJAg45jXxnGIfYzk4Si90RDIqNm1" crossorigin="anonymous"></script>
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.4.1/css/all.css" integrity="sha384-5sAR7xN1Nv6T6+dT2mhtzEpVJvfS3NScPQTrOxhwjIuvcA67KV2R5Jz6kr4abQsz" crossorigin="anonymous">
```

이제 돌려서 확인해보면 된다.

---

### 정리

spring security는 세션-쿠키방식으로 인증한다.  
여기서는 전통적인 쿠키-세션 방식을 사용한다. (JWT이런거는 spring-security-oauth2를..)  

1. 유저가 로그인을 시도 (http request)  
2. AuthenticationFilter 에서부터 위와같이 user DB까지 타고 들어감
3. DB에 있는 유저라면 UserDetails 로 꺼내서 유저의 session 생성
4. spring security의 인메모리 세션저장소인 SecurityContextHolder 에 저장
5. 유저에게 session ID와 함께 응답을 내려줌
6. 이후 요청에서는 요청쿠키에서 JSESSIONID를 까봐서 검증 후 유효하면 Authentication를 쥐어준다.

---

## DB처리

### USER & AUTORITIES TABLE

DB에서 가져오기위해서 DB부터 만들어야한다. -> 이건 우리가 이미 만들어뒀음

#### security-db-context.xml

이제 security-context.xml 복붙!

user 지우고

```xml
<user-service> <!-- 유저도 객체다. -->
  <user name="member" password="{noop}member" authorities="ROLE_MEMBER" />
  <user name="admin" password="{noop}admin" authorities="ROLE_MEMBER, ROLE_ADMIN" />
  <!-- 유저를 생성시켜서 authorities을 줌, 권한을 두 개도 줄 수 있다. -->
</user-service>
```

이 부분을 넣음

```xml
<beans:bean id="userDetailsService" class="org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl">
  <beans:property name="dataSource" ref="dataSource" />
</beans:bean>

  <beans:bean id="customNoOpPasswordEncoder" class="edu.bit.ex.security.CustomNoOpPasswordEncoder"/>

<!-- provider -->
<authentication-manager> <!-- 이건 내부에 있는 생기는 객체이다. -->
  <authentication-provider>
  <password-encoder ref="customNoOpPasswordEncoder"/>
  <jdbc-user-service 
      data-source-ref="dataSource"
      role-prefix=""
      users-by-username-query="select username, password, enabled from users where username = ?"
      authorities-by-username-query="select username, authority from authorities where username = ?" />
  </authentication-provider>
</authentication-manager>
```

넣어서 이렇게!

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans:beans
  xmlns="http://www.springframework.org/schema/security"
  xmlns:beans="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://www.springframework.org/schema/security http://www.springframework.org/schema/security/spring-security.xsd
    http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

  <http>
    <intercept-url pattern="/security/all" access="permitAll" />
    <intercept-url pattern="/security/member" access="hasRole('ROLE_MEMBER')" />
    <intercept-url pattern="/security/admin" access="hasRole('ROLE_ADMIN')" />
            
  <!-- 	login-page: 로그인 인증을 처리할 주소는 /security/login/loginForm이다.
    default-target-url: 로그인이 완료되면 
    username-parameter: 로그인 페이지 form에 있는 username을 저장한 변수이름 지정
    password-parameter: 로그인 페이지 form에 있는 password을 저장한 변수이름 지정 
    login-processing-url: 로그인 페이지 form action에 입력할 주소 지정
    authentication-failure-url : 로그인 실패인 경우 호출할 주소 지정 -->
    
    <!--로그인 페이지 커스텀 화   -->
        <form-login login-page="/login/loginForm"
                    default-target-url="/"
                    authentication-failure-url="/login/loginForm?error"
                    username-parameter="id"
                    password-parameter="password" />
      
      <logout logout-url="/logout" logout-success-url="/" /> 
                    
      <!-- 403 에러 처리 -->
    <access-denied-handler error-page="/security/accessError"/>
  </http>

  <beans:bean id="userDetailsService" class="org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl">
    <beans:property name="dataSource" ref="dataSource" />
  </beans:bean>

    <beans:bean id="customNoOpPasswordEncoder" class="edu.bit.ex.security.CustomNoOpPasswordEncoder"/>

  <!-- provider -->
  <authentication-manager> <!-- 이건 내부에 있는 생기는 객체이다. -->
    <authentication-provider>
    <password-encoder ref="customNoOpPasswordEncoder"/>
    <jdbc-user-service 
        data-source-ref="dataSource"
        role-prefix=""
        users-by-username-query="select username, password, enabled from users where username = ?"
        authorities-by-username-query="select username, authority from authorities where username = ?" />
    </authentication-provider>
  </authentication-manager>

  </beans:beans>
```

- 데이터소스는 root-context.xml에 있는 커넥션풀 데이터소스다.
- **UserDetailsService**는 핵심!객체이다. (interface)

이제 provider 아래 유저 소스지우고 jdbc user service 내용 넣음

- 실제로는 이건 테스트용이고 나중에 커스트마이징 할 수있다.

```xml
<beans:bean id="userDetailsService" class="org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl">
```

-> `JdbcDaoImpl` -> userDetailsService객체 생성!

- 어디에 있나 보자.

#### 📚 Maven Dependencies

##### 🍯 spring-security-core-5.0.6.RELEASE.jar

###### 📦 org.springframework.security.core

###### 📄 Authentication.class

```java
public class JdbcDaoImpl extends JdbcDaoSupport//이거 실행
    implements UserDetailsService, MessageSourceAware {
  // ~ Static fields/initializers
  // =====================================================================================

  public static final String DEF_USERS_BY_USERNAME_QUERY = "select username,password,enabled "
      + "from users " + "where username = ?";
  public static final String DEF_AUTHORITIES_BY_USERNAME_QUERY = "select username,authority "
      + "from authorities " + "where username = ?";
  public static final String DEF_GROUP_AUTHORITIES_BY_USERNAME_QUERY = "select g.id, g.group_name, ga.authority "
      + "from groups g, group_members gm, group_authorities ga "
      + "where gm.username = ? " + "and g.id = ga.group_id "
      + "and g.id = gm.group_id";

  // ~ Instance fields
  // ================================================================================================

  protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

  private String authoritiesByUsernameQuery;
  private String groupAuthoritiesByUsernameQuery;
  private String usersByUsernameQuery;
  private String rolePrefix = "";
  private boolean usernameBasedPrimaryKey = true;
  private boolean enableAuthorities = true;
  private boolean enableGroups;

    ...

 //이부분이 핵심
  protected List<UserDetails> loadUsersByUsername(String username) {
  return getJdbcTemplate().query(this.usersByUsernameQuery,
      new String[] { username }, new RowMapper<UserDetails>() {
        @Override
        public UserDetails mapRow(ResultSet rs, int rowNum)
            throws SQLException {
          String username = rs.getString(1);
          String password = rs.getString(2);
          boolean enabled = rs.getBoolean(3);
          return new User(username, password, enabled, true, true, true,
              AuthorityUtils.NO_AUTHORITIES);
        }

      });
}
```

그리고

###### 📦 org.springframework.security.core.userdetails

###### 📄 User.class

```java
public class User implements UserDetails, CredentialsContainer {

  private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

  private static final Log logger = LogFactory.getLog(User.class);

  // ~ Instance fields
  // ================================================================================================
  private String password;
  private final String username;
  private final Set<GrantedAuthority> authorities;
  private final boolean accountNonExpired;
  private final boolean accountNonLocked;
  private final boolean credentialsNonExpired;
  private final boolean enabled;

  // ~ Constructors
  // ===================================================================================================

  /**
   * Calls the more complex constructor with all boolean arguments set to {@code true}.
   */
  public User(String username, String password,
      Collection<? extends GrantedAuthority> authorities) {
    this(username, password, true, true, true, true, authorities);
  }

...
```

#### web.xml -> 경로 security-db-context.xml 변경 할 것

```xml
<context-param>
  <param-name>contextConfigLocation</param-name>
  <param-value>
    /WEB-INF/spring/root-context.xml
    /WEB-INF/spring/security-db-context.xml
  </param-value>
</context-param>
```

#### root-context.xml 설정하고 (db연결)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xmlns:mybatis-spring="http://mybatis.org/schema/mybatis-spring"
   xmlns:context="http://www.springframework.org/schema/context"
   xmlns:aop="http://www.springframework.org/schema/aop"
   xmlns:tx="http://www.springframework.org/schema/tx"
   xsi:schemaLocation="http://mybatis.org/schema/mybatis-spring http://mybatis.org/schema/mybatis-spring-1.2.xsd
      http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans.xsd
      http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.3.xsd
      http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-4.3.xsd
      http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-4.3.xsd">
   
   <!-- Root Context: defines shared resources visible to all other web components -->
   
   <bean id="hikariConfig" class="com.zaxxer.hikari.HikariConfig">
      <property name="driverClassName"
         value="net.sf.log4jdbc.sql.jdbcapi.DriverSpy"></property>
      <property name="jdbcUrl"
         value="jdbc:log4jdbc:oracle:thin:@localhost:1521:XE"></property>
      <property name="username" value="scott"></property>
      <property name="password" value="tiger"></property>
   </bean>

   <!-- HikariCP configuration -->
   <bean id="dataSource" class="com.zaxxer.hikari.HikariDataSource"
      destroy-method="close">
      <constructor-arg ref="hikariConfig" />
   </bean>

   <!-- 1.번방법을 위하여 mapperLocations 을 추가 함 -->
   <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
      <property name="dataSource" ref="dataSource"/>

   </bean>
   <!-- 1번 방식 사용을 위한 sqlSession -->
   <bean id="sqlSession" class="org.mybatis.spring.SqlSessionTemplate">
      <constructor-arg index="0" ref="sqlSessionFactory" />
   </bean>
   
   <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource" />
    </bean>
    
    <tx:annotation-driven transaction-manager="transactionManager" />
      
</beans>
```

#### 패스워드 인코딩 (버전5.0부터는 encoding 필수)

패스워드 없이 돌아갈수있게 해주는거. (실무에서는 아마도 안쓰겠지만.)

-> 클래스 만들기

> edu.bit.ex.board.security

#### CustomNoOpPasswordEncoder.java

```java
package edu.bit.ex.security;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.extern.log4j.Log4j;

@Log4j
public class CustomNoOpPasswordEncoder implements PasswordEncoder {

   public String encode(CharSequence rawPassword) {

      log.warn("before encode :" + rawPassword);

      return rawPassword.toString();
   }

   public boolean matches(CharSequence rawPassword, String encodedPassword) {

      log.warn("matches: " + rawPassword + ":" + encodedPassword);

      return rawPassword.toString().equals(encodedPassword);
   }

}
```

#### security-db-context.xml 에 아래 내용 추가

```xml
 <beans:bean id="customNoOpPasswordEncoder" class="edu.bit.ex.security.CustomNoOpPasswordEncoder"/>

  <password-encoder ref="customNoOpPasswordEncoder"/>
```

그리고 이제 돌려보자.

---

오후!

### 이제 주소를 맵핑하고 각각 권한을 어떻게 주는지 보자.

> edu.bit.ex

#### HomeController.java 수정

```java
package edu.bit.ex;

import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.extern.log4j.Log4j;

/**
 * Handles requests for the application home page.
 */

@Log4j
@Controller
public class HomeController {

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String home(Locale locale, Model model) {
    log.info("Welcome home! The client locale is {}." + locale);
    return "home";
  }

  @GetMapping("/login/loginForm")
  public String loginForm() {
    log.info("welcome login form");
    return "login/loginForm2";
  }
}
```

> src > main > webapp > WEB-INF > views

#### home.jsp 수정

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
   <p><a href="<c:url value="/login/loginForm" />">로그인</a></p>
</sec:authorize>

<sec:authorize access="isAuthenticated()">
   <form:form action="${pageContext.request.contextPath}/logout" method="POST">
       <input type="submit" value="로그아웃" />
   </form:form>
   <p><a href="<c:url value="/loginInfo" />">로그인 정보 확인 방법3 가지</a></p>
</sec:authorize>

<h3>
    [<a href="<c:url value="/user/userForm" />">회원가입</a>]
    [<a href="<c:url value="/user/userHome" />">유저 홈</a>]
    [<a href="<c:url value="/admin/adminHome" />">관리자 홈</a>]
</h3>
</body>
</html>
```

c:url : `<c:url value="/user/userForm" />` 
절대 경로 만드는거 앞에컨텍스트를(`${pageContext.request.contextPath}`) 자동으로 넣어준다.

```jsp
[<a href="<c:url value="/user/userForm" />">회원가입</a>]
[<a href="<c:url value="/user/userHome" />">유저 홈</a>]
[<a href="<c:url value="/admin/adminHome" />">관리자 홈</a>]
```

- 이제 위의 경로에 해당하는 컨트롤러 맵핑과 jsp view페이지를 만들어주자.

#### HomeController.java - /user/userHome 맵핑 추가

```java
@GetMapping("/user/userHome")
public void userHome() {
  log.info("userHome....");
}
```

#### userHome.jsp

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
   <title>유저 페이지</title>
</head>

<body>

<h1>유저 페이지 입니다.</h1>

<p>principal: <sec:authentication property="principal"/></p>
 <!-- <p>EmpVO: <sec:authentication property="principal.emp"/></p>
<p>사용자이름: <sec:authentication property="principal.emp.ename"/></p>
<p>사용자월급: <sec:authentication property="principal.emp.sal"/></p>
<p>사용자입사일자: <sec:authentication property="principal.emp.hiredate"/></p>  -->
<p><a href="<c:url value="/" />">홈</a></p>

</body>
</html>
```

#### HomeController.java - /admin/adminHome 맵핑 추가

```java
@GetMapping("/admin/adminHome")
public void adminHome() {
  log.info("adminHome...");
}
```

> src > main > webapp > WEB-INF > views > admin

#### adminHome.jsp

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>관리자 홈</title>
</head>

<body>

<h1>관리자 페이지 입니다.</h1>

<h3>[<a href="<c:url value="/" />">홈</a>]</h3>

</body>
</html>
```

#### HomeController.java - /login/accesDenied 맵핑 추가

```java
@GetMapping("/login/accessDenied")
public void accessDenied(Model model) {
  log.info("welcome Access Denied!");
}
```

> src > main > webapp > WEB-INF > views > login

#### accesDenied.jsp 생성

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>Access Denied</title>
</head>

<body>

<h1>Access Denied!</h1>

<h3>[<a href="<c:url value="/" />">홈</a>]</h3>

</body>
</html>

```

#### HomeController.java - /login/accessDenied 맵핑추가

```java
@GetMapping("/login/accessDenied")
public void accessDenied(Model model) {
  log.info("welcome Access Denied!");
}
```

#### security-db-context.xml 수정

```xml
<!-- 403 에러 처리 -->
<access-denied-handler error-page="/login/accessDenied"/>
```

이 부분 수정

```xml
<http auto-config="true" use-expressions="true">
  <intercept-url pattern="/login/lofinFrom" access="permitAll" />
  <intercept-url pattern="/" access="permitAll" />
  <intercept-url pattern="/admin/**" access="hasRole('ADMIN')" />
  <intercept-url pattern="/**" access="hasAnyRole('USER, ADMIN)" /> 
  <!-- /uesr/userHome은 여기서 걸림 -->
```

- 법칙이있다 적어주는 순서가있다. 논리적으로 제한이 좁은데서 큰쪽으로 가게 써준다. 하나씩 걸러낼 수 있도록. 반대로 작성하면 순환참조(?)가 일어날 수 있다.

---

#### 오늘 과제

emp table에서 정보를 가져와서 로그인할 때 ename(blake)을 id로, empno(7698)를 pw로 사용해서 로그인한 뒤, 매니저는 admin, 나머지는 user 권한을 주자. (blake는 manager니까 user, admin 페이지 모두 접근 가능! 그러나 일반 사원인 smith는 user페이지만 접근 가능)