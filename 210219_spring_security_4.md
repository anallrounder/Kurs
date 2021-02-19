# 2021.02.19_금_암호화

customNoOpPasswordEncoder -> 여기 암호화 모듈넣음

## 패스워드 암호화

- 암호화를 시키기 위해서는 암호화 알고리즘이 필요하다.
- 암호화를 시키는 것을 encoding / 원래대로 되돌리는 것을 decoding 이라고 함
- 모든 암호화는 encoding, decoding 단계를 거치게 되어있다.
- 공개키는 나중에 개인적으로 찾아볼 것

- **CustomNoOpPasswordEncoder.java**   
    (이게 암호화 과정을 보여주는 어제 만든 클래스)

  ```java
  package edu.bit.ex.security;

  import org.springframework.security.crypto.password.PasswordEncoder;

  import lombok.extern.log4j.Log4j;

  @Log4j
  public class CustomNoOpPasswordEncoder implements PasswordEncoder {

    public String encode(CharSequence rawPassword) { //인코딩

        log.warn("before encode :" + rawPassword);

        return rawPassword.toString();
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) { //디코딩

        log.warn("matches: " + rawPassword + ":" + encodedPassword);

        return rawPassword.toString().equals(encodedPassword);
    }

  }
  ```

---

### 실습

#### UserController.java 생성

```java
package edu.bit.ex;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import edu.bit.ex.service.UserService;
import edu.bit.ex.vo.UserVO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

/**
 * Handles requests for the application home page.
 */
//링크 받아올 것
@Log4j
@Controller
@AllArgsConstructor
public class UserController {  

   @GetMapping("/user/userForm")
   public void userForm() {
      log.info("Welcome userFForm!");      
   }   
}
```

#### userForm.jsp 생성

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>회원가입</title>
</head>

<body>

<h1>회원가입</h1>

<c:url value="/user/addUser" var="addUserUrl" />
<p>${addUserUrl}</p>
<form:form name="frmMember" action="${addUserUrl}" method="POST">
    <p>
        <label for="username">아이디</label>
        <input type="text"  name="username" />
    </p>
    <p>
        <label for="password">비밀번호</label>
        <input type="password" name="password"/>
    </p>
    <button type="submit" class="btn">가입하기</button>
</form:form>
</body>
</html>
```

> edu.bit.ex

#### UserController.java - addUser 맵핑추가

```java
@PostMapping("/user/addUser")
public String addUser(UserVO userVO) {
  log.info("post resister");      

  userService.addUser(userVO);
  
  return "redirect:/";   
} 
```

> edu.bit.ex.vo

#### UserVO.java

```java
package edu.bit.ex.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j;

@Log4j
@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserVO {
   private String username;
   private String password;
   private int enabled;
   
   public UserVO(){
      this("user", "1111", 1); //이건 아무것도 없으면 디폴트로 넣으라는 말임
   }
   
}
```

> edu.bit.ex

#### UserController.java

```java
private UserService userService;
```

#### UserService.java생성

```java
package edu.bit.ex.service;

import javax.inject.Inject;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import edu.bit.ex.vo.UserVO;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@NoArgsConstructor
@Service
public class UserService {
   
   @Inject
   private BCryptPasswordEncoder passEncoder;
   
   @Inject
   private UserMapper userMapper;
   
   public void addUser(UserVO userVO){
      String password = userVO.getPassword();
      String encode = passEncoder.encode(password);
      
      userVO.setPassword(encode);
      
      userMapper.insertUser(userVO);
      userMapper.insertAuthorities(userVO);
   }
   
}
```

맵퍼생성

> edu.bit.ex.mapper

#### UserMapper.java (interface)

```java
package edu.bit.ex.mapper;

import org.apache.ibatis.annotations.Insert;

import edu.bit.ex.vo.UserVO;

/**
 * Handles requests for the application home page.
 */
public interface UserMapper {
	//xml만들지않고 여기에 넣어줬음
   @Insert("insert into users(username,password,enabled) values(#{username},#{password},#{enabled})")
   public int insertUser(UserVO userVO);
   
   @Insert("insert into AUTHORITIES (username,AUTHORITY) values(#{username},'ROLE_USER')")
   public void insertAuthorities(UserVO UserVO);
} 
```

#### root-context.xml 맵퍼 경로 알려줘야함

```xml
<!-- Mapper Interface -->
<mybatis-spring:scan base-package="edu.bit.ex.mapper" />
```

#### security-db-context.xml 내용 추가

```xml
<beans:bean id="bcryptPasswordEncoder" class="org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder" />
```

회원가입 창 열리고 db에 회원가입 정보 저장되게 연결해보자.

```xml
	<http auto-config="true" use-expressions="true">
		<intercept-url pattern="/login/loginForm" access="permitAll" />
        <intercept-url pattern="/" access="permitAll" />
        <intercept-url pattern="/user/**" access="permitAll" />
        <intercept-url pattern="/admin/**" access="hasRole('ROLE_ADMIN')" />
        <intercept-url pattern="/**" access="hasAnyRole('ROLE_USER, ROLE_ADMIN')" />
```

이거 쓰는걸 추천한다.

```xml
<beans:bean id="bcryptPasswordEncoder" class="org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder" />
```

#### UserService.java 설명

우리가 서비스에서 inject해서 받아오니까 쓸수있다.

```java
@Inject
private BCryptPasswordEncoder passEncoder;

@Inject
private UserMapper userMapper;

@Transactional(rollbackFor = Exception.class) //이거 추가!!!
public void addUser(UserVO userVO){
  String password = userVO.getPassword();
  String encode = passEncoder.encode(password); //얘만의 해쉬알고리즘 적용한거를 아래에
  
  userVO.setPassword(encode); //암호화시킨것을 여기에 set함
  
  userMapper.insertUser(userVO); //그리고 여기에 insert로 집어넣음
  userMapper.insertAuthorities(userVO);
}
```

---

> Q. 이제 암호화되어있는거 로그인할 수있게 해보자. 어떻게 하면 자동 디코딩해서 로그인할 수 있게 할까?

impl객체는 로그인때만 쓰는거지 저 프로바이더 에 있는거에 레퍼런스를 

```xml
<!-- provider -->
<authentication-manager> <!-- 이건 내부에 있는 생기는 객체이다. -->
  <authentication-provider>
  <password-encoder ref="bcryptPasswordEncoder"/> <-- 이부분-->
  <jdbc-user-service 
      data-source-ref="dataSource"
      role-prefix=""
      users-by-username-query="select username,password,enabled from users where username = ?"
      authorities-by-username-query="select username, authority from authorities where username = ?" /> 
  </authentication-provider>
</authentication-manager>

  <beans:bean id="customNoOpPasswordEncoder" class="edu.bit.ex.security.CustomNoOpPasswordEncoder"/>
  <beans:bean id="bcryptPasswordEncoder" class="org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder" />
```

우리 지금까지 커스텀으로 썼는데 제공해주는걸 bcryptPasswordEncoder이거 를 사용하면 된다.
이게 정답이었음.....

```xnml
  <password-encoder ref="bcryptPasswordEncoder"/>
```

이거 설정 안되어있다고 왜 생각 못했냐......와.....ㅜ.ㅜ

- [PasswordEncoder 종류 참고](https://velog.io/@corgi/Spring-Security-PasswordEncoder%EB%9E%80-4kkyw8gi)

---

오후!

지금까지는 세팅을 통해서 했지만 실무에서는 이렇게 하지 않는다.
왜일까? 예를들면 usernaem, password, enabled 와같이 해당 db를 만들라는 법이 없기 때문이다.  
datatabe 설계할 때 user에는 address, email, gender, image 등등 여러가지 기타 칼럼이 들어가기 때문에 

로그인 정보를 세션에 올린다.
우리가 로그인을 시키는 이유? login은 부차적인거고 user정보를 메모리에 올리는데 그 대표적 방법이 세션이다. 로그인페이지 말고도, 쇼핑몰이라면 -님 / 장바구니 / 개인정보 페이지 등 다른것들이 많다. 로그인은 기본적인 기능이고 그 이후 정보를 써먹는게 기본적인 목적이다.

멤버 가입/수정하면 이런것도 가능하게 만들어야한다.

- 인터셉터를 로그인목적으로 쓸 필요가 전혀 없다.

  옛날에 세션올려서 쓰는거 el로 썼는데 여기선 어떻게씀?  
  jstl에서는 어떻게 써먹냐? principal이다.

기 세션 객체에 확장시켜서 가져오른건 커스터마이징하는건

- princial.emp
- pincipal.emp.ename
- principal.emp.empno
- principal.emp.cart

```xml
<sec:authentivation property="principal.emp.sal" />
```

이런식으로 하는것이다.

대표적으로 세션쓰는게 쇼핑카트(장바구니) 기능이다.
세션 유지기간은? 로그아웃등의 세션만료시간까지이다. (대략 30분-지정가능)
그러니까 따로 생성하지 않고 세션객체에 principal써서 같이 넣고 들리면 더 좋다.

---

### 실습

> edu.bit.ex.emp.vo

#### EmpVO.java 생성

```java
package edu.bit.ex.vo;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j;


@Log4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmpVO {
   int empno;    //NOT NULL NUMBER(4)    
   String ename; //             VARCHAR2(10) 
   String job;//               VARCHAR2(9)  
   int mgr;//               NUMBER(4)    
   Timestamp hiredate;//          DATE         
   int sal;//               NUMBER(7,2)  
   int comm;//              NUMBER(7,2)  
   int deptno;//            NUMBER(2)  
   
}
```

#### EmpMapper.java (interface)

유저맵퍼 복붙

```java 
package edu.bit.ex.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import edu.bit.ex.vo.UserVO;

/**
 * Handles requests for the application home page.
 */
public interface EmpMapper {
  // xml만들지않고 여기에 넣어줬음

  @Select("select * from emp where ename = #{ename}")
  public EmpVO readUser(String ename);

}
```

#### empService.java

```java
```

> edu.bit.ex.vo

#### CustomUser.java

```java
```

---

### 실습 다시다시다시!!

커스트마이징이 핵심이다.

- 커스텀 유저 만드는 방법은 2가지이다.
  1) MemberUser extends User
  2) MemberUser2 implements UserDetails (오버라이딩)

EmpMapper -> MemberMapper로 변경함

#### MemberMapper.java

```java
package edu.bit.ex.mapper;

import edu.bit.ex.vo.EmpVO;

/**
 * Handles requests for the application home page.
 */
public interface EmpMapper {

  public EmpVO readUser(String ename);

}
```

- CustomUserVO.java 삭제 / EmpVO.java 삭제 EmpService.java 삭제

-> 기존 user/authority table로 실습함

#### MemberVO.java 생성

```java
package edu.bit.ex.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class MemberVO {
   private String username;
   private String password;
   private String enabled;
   
   private List<AuthVO> authList;
}

//user , authority -> 1:N 이다.
```

#### AuthVO.java 생성

```java
package edu.bit.ex.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class AuthVO {
   private String username;
   private String authority;
   
}
```

1:n이라서 처리하기위해 엑셈엘 맵퍼만들어줘야함

> src/main/resources > edu.bit.ex.mapper

#### MemberMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
  PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="edu.bit.ex.mapper.MemberMapper">

  <resultMap id="memberMap" type="edu.bit.ex.vo.MemberVO">
      <result property="username" column="username"/>
      <result property="password" column="password"/>
      <result property="enabled" column="enabled"/>
    <collection property="authList" resultMap="authMap"></collection>
  </resultMap>

  <resultMap id="authMap" type="edu.bit.ex.vo.AuthVO">
    <result property="username" column="username"/>
    <result property="authority" column="authority"/>
  </resultMap>

  <select id="getMember" resultMap="memberMap">
    select * from users , authorities 
    where users.username = authorities.username and users.username = #{username}
  </select>

</mapper>
```

!! 중요한 파일 두 개 남음

vo 에다가 멤버유저 생성

#### MemberUser.java

```java
package edu.bit.ex.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class MemberUser extends User {

  private MemberVO member;

  // 기본적으로 부모의 생성자를 호출해야만 정상적으로 작동

  public MemberUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
    super(username, password, authorities);
  }

  public MemberUser(MemberVO memberVO) {

    super(memberVO.getUsername(), memberVO.getPassword(), getAuth(memberVO));

    this.member = memberVO;
  }

  // 유저가 갖고 있는 권한 목록
  public static Collection<? extends GrantedAuthority> getAuth(MemberVO memberVO) {

    List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

    for (AuthVO auth : memberVO.getAuthList()) {
      authorities.add(new SimpleGrantedAuthority(auth.getAuthority()));
    }

    return authorities;
  }
}
```

#### MemberDetailsService.java 생성

```java
package edu.bit.ex.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.bit.ex.mapper.MemberMapper;
import edu.bit.ex.vo.MemberUser;
import edu.bit.ex.vo.MemberVO;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class MemberDetailsService implements UserDetailsService {

  // 주입만 시켜주면 됨 인젝트도 상관없다.위에 클래스에 올 컨스트럭터 써도 된다
  @Setter(onMethod_ = @Autowired)
  private MemberMapper memberMapper;

  // 자손이 구현 이거 하나만 하면 됨/ 멤버멥퍼 가져와서 유저 디테일즈로 리턴을 시켜야한다.
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    //loadUserByUsername 이게 핵심! 진짜 중요함 외우자. UserDetails 리턴
    //스프링시큐리티가 객체생성하라고했는데 예전엔 아엠피엘에서 생성했는데 이번에는 안했ㄴ으니까우리가구현해야하는데
    //그때 따라오는게loadUserByUsername이고 (String username)가 로그인할때 유저네임을 말한다.
    // 로그인할때 이 함수를 반드시 호출하게 되어있다. -> 이게 스프링시큐리티의 법칙이다.
    //authentication-provider에서 레퍼런스로 지정했다.
    // 내가만들어서 내가 리턴시키는거다.스프링시큐리티가 저 함수로드저걸 호출해주니까.
    // 스프링이 안에 파라미터 유저네임 넣어줌. 로그인하면 여기에 넣어주겠다. 나머진 니가 알아서해라
    // 유저 디테일즈를 니가 캐스팅해서 알아서해라
    // 유저 디테일즈를 누가 가져감? 스프링이 가져감 .(내가 써먹는게 아님)
    // 유저 디테일즈 형으로 멤버유저를 
    
    log.warn("Load User By MemberVO number: " + username);
    MemberVO vo = memberMapper.getMember(username);
    //멤버브이오 가져옴
    
    log.warn("queried by MemberVO mapper: " + vo);

    return vo == null ? null : new MemberUser(vo);
  }

}
```

#### security-custom-context.xml

이부분 수정 (jdbc는 유물이라... 안쓸거라 지움)

```xml
<beans:bean id="bcryptPasswordEncoder"
  class="org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder" />

  
<beans:bean id="customNoOpPasswordEncoder" class="edu.bit.ex.security.CustomNoOpPasswordEncoder"/>
<beans:bean id="memberDetailsService" class="edu.bit.ex.security.MemberDetailsService" />  <!-- 이거를 아래로 끌고와서 -->

<authentication-manager>
  <authentication-provider user-service-ref="memberDetailsService"> <!-- ㅡMmberDetailsService 객체생성하는것-->
      <password-encoder ref="customNoOpPasswordEncoder"/>      
  </authentication-provider>
</authentication-manager>
```

전체코드

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans:beans xmlns="http://www.springframework.org/schema/security"
    xmlns:beans="http://www.springframework.org/schema/beans"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation="http://www.springframework.org/schema/security http://www.springframework.org/schema/security/spring-security.xsd
      http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd" >
   
  <http auto-config="true" use-expressions="true">
    <intercept-url pattern="/login/loginForm" access="permitAll" />
        <intercept-url pattern="/" access="permitAll" />
        <intercept-url pattern="/user/**" access="permitAll" />
        <intercept-url pattern="/admin/**" access="hasRole('ROLE_ADMIN')" />
        <intercept-url pattern="/**" access="hasAnyRole('ROLE_USER, ROLE_ADMIN')" />
    
    <!--  법칙이있다 적어주는 순서가있다. 논리적으로 제한이 좁은데서 큰쪽으로 가게 써준다. 하나씩 걸러낼 수 있도록. 반대로 작성하면 순환참조(?)가 일어날 수 있다.-->
    
    <intercept-url pattern="/security/all" access="permitAll" />
    <intercept-url pattern="/security/all" access="permitAll" />
    <intercept-url pattern="/security/member" access="hasRole('ROLE_MEMBER')" />
    <intercept-url pattern="/security/admin" access="hasRole('ROLE_ADMIN')" />  
          
      <!--로그인 페이지 커스텀 화    -->
      <form-login login-page="/login/loginForm"
                    default-target-url="/"
                    authentication-failure-url="/login/loginForm?error"
                    username-parameter="id"
                    password-parameter="password" />
      
      <logout logout-url="/logout" logout-success-url="/" /> 
                
      <!-- 403 에러 처리 -->
      <access-denied-handler error-page="/login/accessDenied"/>      
   </http> 
   
   <beans:bean id="bcryptPasswordEncoder"
      class="org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder" />

      
   <beans:bean id="customNoOpPasswordEncoder" class="edu.bit.ex.security.CustomNoOpPasswordEncoder"/>
    <beans:bean id="memberDetailsService" class="edu.bit.ex.security.MemberDetailsService" />  <!-- 이거를 아래로 끌고와서 -->

   <authentication-manager>
      <authentication-provider user-service-ref="memberDetailsService"> <!-- ㅡMmberDetailsService 객체생성하는것-->
         <password-encoder ref="customNoOpPasswordEncoder"/>      
      </authentication-provider>
   </authentication-manager>
    
</beans:beans>
```

#### web.xml

- 아래 패스 수정
- 반드시 두개만 들어가지 세-네 개 들어가면 안된다.

```xml
<!-- The definition of the Root Spring Container shared by all Servlets and Filters -->
<context-param>
  <param-name>contextConfigLocation</param-name>
  <param-value>
    /WEB-INF/spring/root-context.xml
    /WEB-INF/spring/security-custom-context.xml
  </param-value>
</context-param>
```

- 만든건 MemberDetailsService.java랑 MemberUser.java 두개 뿐이다.
  - MemberDetailsService **implements** UserDetailsService    
    -> loadUserByUsername(String username)는 UserDetails를 리턴하게 되어있다.
  - MemberUser **extends** User  
    / User **implements** UserDetails (상속)

이제 JdbcDaoImpl 안쓰고 확장시킬거다.  
jsp에서 Principal뒤에 .member.~~~ 나오게 하는것.

---

#### 오늘 숙제:

- emp 테이블을 스프링 시큐리티에서 커스텀마이징 하시오.

```xml
<p>EmpVO: <sec:authentication property="principal.emp"/></p>
<p>사용자이름: <sec:authentication property="principal.emp.ename"/></p>
<p>사용자월급: <sec:authentication property="principal.emp.sal"/></p>
<p>사용자입사일자: <sec:authentication property="principal.emp.hiredate"/></p>
```

#### 주말 숙제:

- 프로젝트 주제 생각해서 PPT 5장 만들어오기 (발표 10분용)
- 개인 과제 마무리하기

#### 다음주

- csrf, 보안테스트, log, 결재모듈 + 게시판부터 다시 구현....

---