
# 2021.02.10_수_Interceptor

전자정부 프레임워크에서 사용 

sql developer에서 scott계정에 다음 sql문으로 테이블을 생성하자. 

```sql
create table users(
   username varchar2(50) not null primary key,
   password varchar2(100) not null,
   enabled char(1) DEFAULT '1'
);
create table authorities (
   username varchar2(50) not null,
   authority varchar2(50) not null,
   constraint fk_authorities_users foreign key(username) references users(username)
);
create unique index ix_auth_username on authorities (username,authority);

commit;

insert into users (username,password) values('user','user');
insert into users (username,password) values('member','member');
insert into users (username,password) values('admin','admin');

commit;

insert into AUTHORITIES (username,AUTHORITY) values('user','ROLE_USER');
insert into AUTHORITIES (username,AUTHORITY) values('member','ROLE_MANAGER');
insert into AUTHORITIES (username,AUTHORITY) values('admin','ROLE_MANAGER');
insert into AUTHORITIES (username,AUTHORITY) values('admin','ROLE_ADMIN');

commit;
```

sql에서 확인해보기

```sql
select * from user;
```

---

## 실습

**주의사항!!** root-context.xml에서 정확하게 맵퍼 인터페이스 주소를 지정해줘야한다!! 그래야 @Mapper를 달아주지 않더라도 알아서 잘 스캔할 수 있다.
만약 mapper위치가 두군데 있다면, 콤마로 연결해서 둘 다 작성해주면 된다.

```xml
<mybatis-spring:scan base-package="edu.bit.ex.board.mapper, edu.bit.ex.mapper" />
```

### Interceptor

> edu.bit.ex.board.mapper

#### LoginMapper.java

(BoardMapper.java 복붙)

```java
package edu.bit.ex.board.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import edu.bit.ex.board.vo.UserVO;

@Mapper
public interface LoginMapper {

  @Select("select * from users where username = #{username} and password = #{password}") 
  public UserVO logInUser(@Param("username") String username,@Param("password") String password);
  // 패스워드를위로넘겨준다. 아래 스트링 패스워드 
  //자바문법을 마이바티스문법에 연결해야한다. 파라미터터 이름을,@Param("password") 위에 #{password}로 넘겨라 
  //변수가 두개 이상일때는 @Param꼭 쓰자.
}
```

> edu.bit.board.vo

#### UserVO.java

```java
package edu.bit.ex.board.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {
  String username;
  String password;	
  char enabled;

}
```

> edu.bit.ex.board.service

#### LoginService.java

```java
package edu.bit.ex.board.service;

import org.springframework.stereotype.Service;

import edu.bit.ex.board.mapper.LoginMapper;
import edu.bit.ex.board.vo.UserVO;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LoginService {	

  LoginMapper loginMapper;

  public UserVO loginUser(String id,String pw)  {
    return loginMapper.logInUser(id,pw) ;
  }

}
```

> edu.bit.ex.board.interceptor

#### BoardInterceptor.java

```java
package edu.bit.ex.board.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import edu.bit.ex.board.vo.UserVO;
import lombok.extern.log4j.Log4j;

/**
 * Handles requests for the application home page.
 */

@Log4j // 여기 컨트롤러 이런 다른 annotation은 달면 안된다.
public class BoardInterceptor extends HandlerInterceptorAdapter { 
  // 상속하면 두개 함수를 상속받을 수 있다. 프리핸들러, 포스트 핸들러
  // 컨트롤러 가기전에 중간에서 리퀘스트, 리스펀스 객체를 중간에서 가로챌 수 있는 클래스를 제공하는 것
  // 이 인터셉터 객체는 로드존슨이 만들어서 제공(spring)
  // 리퀘스트 객체 안에 세션부터 다 있다.


  //내부적으로 프리핸들러, 포스트 핸들러 언제실행? 둘다 스프링이 실행시켜 주는 것
  // 어떻게 객체 생성?-xml -> 객체 생성하면 프리, 포스트는 누가 호출? 스프링이 호출!
  // (인터셉터는 객체생성하는 방법이 정해져있다. servlet-context.xml에서 설정)
  // 포스트는 컨트롤러 지나서 돌아올때 거친다. 부모한테 다 넘겨줌.
  

  // preHandle() : 컨트롤러보다 먼저 수행되는 메서드
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) // 함수가 세 개 넘어온다.
      throws Exception {
    System.out.println("preHandle 실행");
    // session 객체를 가져옴
    HttpSession session = request.getSession();

    // login처리를 담당하는 사용자 정보를 담고 있는 객체를 가져옴
    UserVO user = (UserVO) session.getAttribute("user");

    if (user == null) {
      log.info("user가 null");
      // 로그인이 안되어 있는 상태이므로 로그인 폼으로 다시 돌려보냄(redirect)
      response.sendRedirect(request.getContextPath()); // getContextPath -> /ex 를 말함 

      return false; // 더이상 컨트롤러 요청으로 가지 않도록 false로 반환함 (더이상 진행하지 못하게 만든다.)
    }

    // preHandle의 return은 컨트롤러 요청 uri로 가도 되냐 안되냐를 허가하는 의미임
    // 따라서 true로하면 컨트롤러 uri로 가게 됨.
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {

    super.postHandle(request, response, handler, modelAndView);
    System.out.println("postHandle 실행");
  }
}

```

---

인터셉터를 사용하기 위해서는 객체를 생성해야한다.

> src > main > webapp > WEB-INF > spring > appServlet

#### servlet-context.xml

```xml
<!-- 인터셉터 객체 생성 -->
  <beans:bean id="boardInterceptor"
    class="edu.bit.ex.board.interceptor.BoardInterceptor">
  </beans:bean>

  <!-- Interceptor 설정 -->
  <interceptors>
    <interceptor>
      <mapping path="/list" /> <!-- http://localhost:8282/ex/list -->
      <exclude-mapping path="/resources/**" />
      <beans:ref bean="boardInterceptor" />
    </interceptor>
  </interceptors>
```

주소를 넣어서 객체를 생성 그리고 그것에 대해 설정할 수 있다.  
맵핑 -> list 

#### 왜 root-context.xml에도 넣을 수 있는데, servlet-context.xml에 setting할까?  

컨트롤러 이전에 있는 부분을 서블릿이라고 표현(디스패쳐 서블릿)  
그래서 servlet-context.xm에 데이타소스 커넥션풀에 넣고,  
DB관련 같이 컨트롤러 뒷 부분에 있는거는 root-context.xml에 넣는다.

---

> src > main > wepapp > WEB-INF > views > login.jsp

#### login.jsp

```jsp
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
   <title>로그인</title>
</head>
<body>
<%
   String path = request.getContextPath();
%>
<%=path%> 
<!-- el로 처리하는게 더 좋다. ${pageContex.request.contextPath} - 이건 그냥 확인용-->

<c:if test="${user == null}"> 
<!-- 유저가 없으면 (컨트롤러에서 모델로 넘어옴) 이걸 태운다.아니면 아래거를 태움 -->

<form role="form" method="post" autocomplete="off" action="<%=path%>/login">
<!-- ${pageContex.request.contextPath}/login -->
   <p>
      <label for="userId">아이디</label> 
      <input type="text" id="userId" name="id" />
   </p>
   <p>
      <label for="userPass">패스워드</label>
      <input type="password" id="userPass" name="pw" />
   </p>
   <p><button type="submit">로그인</button></p>
<!--    <p><a href="/member/register">회원가입</a></p> -->
</form>
</c:if>

<c:if test="${msg == false}">
   <p style="color:#f00;">로그인에 실패했습니다. 아이디 또는 패스워드를 다시 입력해주십시오.</p>
</c:if>

<c:if test="${user != null}">
   <p>${user.username}님 환영합니다.</p>
   
   <!-- <a href="member/modify">회원정보 수정</a>, <a href="member/withdrawal">회원탈퇴</a><br/> -->
   <a href="<%=path%>/board/list">게시판 리스트</a><br>
   <a href="<%=path%>/logout">로그아웃</a>   
</c:if>

</body>
</html>
```
