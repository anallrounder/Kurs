# 2021.02.24 수요일 Spring Boot(2), Test, 형상관리

## Spring Boot(2)

### Spring Boot로 Board list 뽑기

#### pom.xml

```xml
<!-- 마이바티스 -->
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>2.1.4</version>
</dependency>
```

#### application.properties

```properties
#xml location
mybatis.mapper-locations=classpath:mappers/**/*.xml
```

> edu.bit.ex

#### BoardController.java

```java
package edu.bit.ex;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import edu.bit.ex.service.BoardService;
import lombok.extern.slf4j.Slf4j;

@Slf4j //Log4j보다 좋은것!이거 쓰자. 기본적으로 이거 제공함
@Controller
//@RequestMapping("board/*")
public class BoardController {

  @Autowired
  private BoardService boardService;

  @GetMapping("list")
  public String list(Model model) {
    log.debug("controller -- list -- 실행");
    log.info("controller -- list -- 실행");
    model.addAttribute("list", boardService.getlist());
    return "list2";
  }

}
```

> edu.bit.ex.service

#### BoardService.java (interface)

```java
package edu.bit.ex.service;

import java.util.List;

import edu.bit.ex.vo.BoardVO;

public interface BoardService {

  public List<BoardVO> getlist();

}
```

---

#### Service단에서 interface로 만들어주는데 왜 인터페이스와 클래스를 나눠서 만드는걸까?

- 예전에 카카오 로그인을 구현할 때는 인터페이스를 만들지 않았었다. (왜?)
- 인터페이스의 장점: 강제시킨다. 표준이다 규약이다.
- [인터페이스 복습](https://zimt.tk/201214_-_016_instanceof-Object-class-final-Override-interface-8bf4265c27174cc4b20f59f375f8b7f9#634c07290f7e43c9a7c33c5b95753eb2)
- 자바에서 매우 중요한 polymorphism이 핵심!! (=오브젝트c의 프로토콜)
  
#### 왜 BoardService를 인터페이스로 만들까? 이유는?

확장성을 염두에 둔것이다.

#### BoardService interface

- -> Oracle service impl
- -> mySQL service impl

```java
BoardService boardServide = new OaracleService();
BoardService boardServide = new MySQLService();
```

소셜 로그인도 카카오, 네이버, 페이스북, 트위터 등 여러가지 소셜로그인을 구현할 경우, SocialLogin interface를 만드는것이 당연히 좋다! 이렇게 해야한다.

```java
SocialLogin socialLogin = new KakaoLogin();
SocialLogin socialLogin = new NaverLogin();
SocialLogin socialLogin = new FacebookLogin();
SocialLogin socialLogin = new TwitterLogin();
SocialLogin socialLogin = new GoogleLogin();
```

Mapper inferface도 마찬가지 이다.

database가 다를 경우 다르게 구현해야하기 때문에 인터페이스를 둠으로써 해당 맵퍼에서 결정할 수 있게 만드는 것이다.

---

#### BoardMapper.java Interface

```java
package edu.bit.ex.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import edu.bit.ex.vo.BoardVO;

@Mapper
public interface BoardMapper {

  //@Select("select * from mvc_board order by bgroup desc bstep asc")
  public List<BoardVO> getlist();

}
```

#### BoardVO.java

```java
package edu.bit.ex.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//bid      not null number(4)     
//bname             varchar2(20)  
//btitle            varchar2(100) 
//bcontent          varchar2(300) 
//bdate             date          
//bhit              number(4)     
//bgroup            number(4)     
//bstep             number(4)     
//bindent           number(4)  


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BoardVO {
  private int bid;
  private String bname;
  private String btitle;
  private String bcontent;
  private Date bdate;
  private int bhit;
  private int bgroup;
  private int bstep;
  private int bindent;

}
```

#### BoardMapper.xml

```xml
<!DOCTYPE mapper
  PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="edu.bit.board.mapper.BoardMapper">

  <!-- list -->
  <select id="getlist" resultType="edu.bit.ex.vo.BoardVO">
    <![CDATA[
      select * from mvc_board order by bgroup desc, bstep asc
    ]]>
  </select>

</mapper>
```

---

#### 뷰 페이지에 사용하는 대표적인것들

- jsp
- velocity
- **thymeleaf**
- vue.js

### 타임리프(Thymeleaf)

#### pon.xml

```xml
<!-- Thymeleaf -->
<dependency> 
    <groupId>org.springframework.boot</groupId> 
    <artifactId>spring-boot-starter-thymeleaf</artifactId> 
</dependency>
```

#### application.properties

```properties
spring.thymeleaf.view-names=thymeleaf/*
```

이거 넣으면 두개 다 사용할수있는 환경이 설정되는것이다.

#### HomeController.java

```java
@Controller
public class HomeController {

  @RequestMapping("/")
  public String home(Model model) {
    BoardVO board = new BoardVO();
    board.setBcontent("컨텐트");
    board.setBtitle("타이틀");
    board.setBname("홍길동");
    
    model.addAttribute("board", board);
    return "thymeleaf/index"; //타임리프로 넘기기 위해서 이렇게 작성함
  }
```

### view page

> src/main/resources > templates > thymeleaf

#### index.html

```html
<html xmlns:th="http://www.thymeleaf.org">
```

타임리프를 쓰려면 이 부분이 꼭 들어가야한다.

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Title</title>
</head>
<body>
    <h1>Hello World</h1>
    <p th:object ="${board}"></p> <!--p태그 안에 객체를 넣어줌-->
    <p th:text ="${board.bname}">여기에 bname에 대한 텍스트가 나오게된다.</p>
    <p th:text ="*{bcontent}">디폴트</p><!--이건 못가져오고있음-->
    
    ===============================
    
</body>
</html> 
```

- $는 변수를 가져오고 *를 통해서 선택적 변수를 가져온다. 
  - 여기서 말하는 변수는 `model.addAttribute("board", board);`여기서 "baord"를 말함

#### 기본 문법 정리

- $ {...} - 변수식(Variable Expression)
- *{...} - 선택적 변수식(Selection Variable Expression): 부모 태그의 th:object에 지정된 객체를 기준으로 해당 객체의 속성에 접근한다.
- #{...} - 메세지식(Message Expression)
- @{...} - 링크식(Link URL Expression): 디폴트로 src/main/resources > static이랑 연결된다.  
   th:href, th:src, th:action 등과 같이 URL이 지정되는 속성에 사용한다.

#### th

- th:object - 객체를 넣어줌
- th:text - ${}안에 있는걸 밖에 써주게됨

#### message.properties

```properties
content.id=star
content.name=kim
```

#### index.html

```html
<h1 th:text="#{content.id}"></h1>
<h1 th:text="#{content.name}"></h1>

<h1 th:text="${#temporals.format(localDateTime, 'yyyy-MM-dd HH:mm')}"></h1>
<!-- 이미 만들어진걸 가져와서 꺼내오는일이 많다. 
  #dates, #calendars #number #strings ...등등등 많다.
-->   
```

---

## Test 

### v모델

<img src="https://github.com/anallrounder/Images/blob/main/v_model.png?raw=true">

- 단위테스트: 함수 하나하나 확인 vo, datasource
- 통합테스트: 컨트롤러 확인(서비스들어있음-mock서비스 맵퍼 다들어잇어서)
- 시스템 테스트: =부하테스트: 실제로 십만명 동접자 만들어서 테스트함(파일백개 동시업로드 가능?)
- 유저테스트: 실제로 유저가 사용 해보는 테스트

### Spring Boot test

#### pom.xml

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

버전5 기본적으로 들어있고 4로 바꾸려면 따로 설정해야함 

> src/test/java > edu.bit.ex

#### BoardTests.java

```java
package edu.bit.ex;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension; //이거 주피터 사용

import edu.bit.ex.mapper.BoardMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(SpringExtension.class)//jupiter
//@RunWith(springRunner.class)/이게 4버전
@SpringBootTest
class BoardTests {

  @Autowired
  private BoardMapper mapper;

  @Autowired
  private DataSource ds;

  @Test //junit5 library 가져올것!
  public void testDataSource() {
    System.out.println("Ds=" + ds);
  }
}
```

---

오후: Tool 깃허브 - 소스트리 

## 형상관리(소스관리)툴

- 협업 - 관리

### 종류

- CVS (거의 안씀)
- SVN
- Github
- 유사한 사이트도 있음
  
### 중앙집중식 vs 분산 버전관리

- 중앙집중식 버전관리(CVCS - Centralized Version Control System)
  - CVS
  - 로컬방식보다 공동작업에 이점이 있다.
  - 중앙 서버에 문제가 발생하게되면 심각한 문제가 생긴다.
  
- 분산 버전 관리 시스템(DVCS - Distributed Version Control System)
  - 깃허브: 분산서버의 이점으로 그래서 작은회사도 깃허브를 쓰기 시작함
  - 중앙 서버 이외에 여러 곳에 동일한 서버의 사본 (엄밀한 의미에서는 사본이 아니지만) 을 유지 및 관리하여 CVCS의 단점을 보완함

---

### Github - Sourcetree 사용

- [여기에 정리했음! 확인하기!](https://zimt.tk/Github-SourceTree-1562ba21b4444ea3a57c40f9f5f26631)