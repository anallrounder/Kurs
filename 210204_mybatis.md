# 2021.02.04_목_mybatis 사용방법 4가지

## 내일부터 배울 내용

1) xml, json (현대 모든 데이터전송은 거의 json으로 함(표준 데이터 전송 방식) )  
2) -> ajax(비동기 통신) - jQuery기반의 함수  
3) -> RESTful  
4) -> 스프링시큐리티

---

## 마이바티스 사용법 4가지

- 중요한 것은 방법을 통일해서 사용하는 것이 가장 좋은 방법이라는 것이다. (섞어쓰지 말고)

### 마이바티스 사용법 : 첫 번째 방법

root-context.xml

```xml
 <!-- 1.번방법을 위하여 mapperLocations 을 추가 함 -->
   <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
      <property name="dataSource" ref="dataSource"/>
      <property name="mapperLocations" value="classpath:/edu/bit/ex/board/mapper/*.xml" />
   </bean>
```

- sqlSessionFactory 이게 마이바티스다.  
예전 소스는 거의 이랬다. 보고 이해할 수 있도록 하자.
  
```xml
 <mybatis-spring:scan
      base-package="edu.bit.ex.board.mapper" />
```

3버전 부터는 위에 마이바티스 스캔이 지원되지만, 1버전은 지원이 안되기 때문에 이거 말고 위에서 property에 맵퍼 주소를 써줘야한다.

```xml
 <property name="mapperLocations" value="classpath:/edu/bit/ex/board/mapper/*.xml" />
```

BService1.java

```java
package edu.bit.board.one;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import edu.bit.board.vo.BoardVO;

@Service
public class BService1 {

  @Inject //@Autowired로 주입
  SqlSession sqlSession; 
  // root-context.xml에서 ioc컨테이너에 생성된 이 객체를 끌고온다.(받아온다.)

    public List<BoardVO> selectBoardList()throws Exception {
      IBDao dao = sqlSession.getMapper(IBDao.class);
      // sqlSession.getMapper()에 해당 인터페이스(IBDao.class) 써줘서 가져온다.
      return dao.listDao();
    }
}

```

Board1.xml 설정

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
  PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="edu.bit.ex.board.mapper.IBDao"> <!-- 경로확인할것 -->
  <!--namespace 해당인터페이스의 경로 -->

  <!-- list -->
  <select id="listDao" resultType="edu.bit.ex.board.vo.BoardVO"> 
    <![CDATA[ 
      select * from mvc_board order by bGroup desc, bStep asc 
    ]]> 
  </select> 

</mapper>

```

BController.java 설정

```java
package edu.bit.board.one;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

  //1.interface IBDao를  XML namespac에 매핑 <mapper namespace="edu.bit.ex.board1.IBDao"> 
  //2.sqlSession.getMapper(IBDao.class)를 이용.

  // 롬복 쓰기 전
@Controller
public class BController1 {

  @Inject 
  BService1 bservice;

  @RequestMapping("/list1") //리스트 1로 들어오면
  public String list(Model model) throws Exception{
    System.out.println("list1()");
    
    model.addAttribute("list", bservice.selectBoardList());
    // 비서비스에 있는 셀렉트 보드리스트를 가져와라
    return "list";    //여기로 리턴해라.
  }

  // 컨트롤러 안에 서비스 안통하고 이렇게 다 때려넣으면 안된다. -> 아래는 잘못된 예제
  /*
    * @RequestMapping("/write_view") public String write_view(Model model) {
    * System.out.println("write_view()");
    * 
    * return "write_view"; }
    * 
    * @RequestMapping("/content_view") public String
    * content_view(HttpServletRequest request, Model model){
    * System.out.println("content_view()");
    * 
    * String bid = request.getParameter("bId");
    * 
    * IBDao dao = sqlSession.getMapper(IBDao.class);
    * model.addAttribute("content_view", dao.ContentDao(bid));
    * 
    * 
    * return "content_view"; }
*/
}

```

---

### 마이바티스 사용법 : 두 번째 방법

1. interface는 필요가 없음
2. sqlSession에서 제공하는 함수(selectList,selectOne)를 이용함
    ( mapper.getListWithPaging()..으로 하는게 아니다.= 맵퍼 인터페이스 없다.)
3. 쿼리구현을 위한 XML이 필요. 해당 XML의 namespace는  개발자가가 정함 -> 이게 핵심이다.  

-> 이 방식 소스코드도 많이 남아있다.

> edu.bit.board.two

BoardService2.java

```java
package edu.bit.board.two;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BController2 {

  @Inject
  BService2 bservice;

  @RequestMapping("/list2")
  public String list(Model model) throws Exception{
    System.out.println("list2()");	
    
    model.addAttribute("list", bservice.selectBoardList()); 
    // 이 부분이 복붙??? ????
    return "list";
  }
}

```

board2.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
    PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="board">
  <select id="selectBoardList" resultType="edu.bit.board.vo.BoardVO">
    select bId, bName, bTitle, bContent, bDate, bHit, bGroup, bStep, bIndent from mvc_board order by bGroup desc, bStep asc	
  </select>

</mapper>

```

예전에는 맵퍼가 인터페이스에 들어갔는데 이제 네임스페이스와 아이디를 바꾼다.
기본적으로 인터페이스 위치를 맞춰줬는데 이제는 개발자가 정한다.

BService2.java

```java
package edu.bit.board.two;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import edu.bit.board.vo.BoardVO;


@Service
public class BService2 {

  @Inject
  SqlSession sqlSession;

    public List<BoardVO> selectBoardList()throws Exception {
    return sqlSession.selectList("board.selectBoardList");
    }
}

```

1~2 번째 방법은? 마이바티스 3 ???/

---

### 마이바티스 사용법 : 세 번째 방법

맵퍼 인터페이스를 정의하는 방식 -> 우리가 쓰는 것

기억할 점 : root-context.xml 에서

```xml
 <!-- 1.번방법을 위하여 mapperLocations 을 추가 함 -->
   <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
      <property name="dataSource" ref="dataSource"/>
    <!-- <property name="mapperLocations" value="classpath:/edu/bit/ex/board/mapper/*.xml" /> 
    이 부분을 쓸 필요가 없다.-->
   </bean>
```

이 부분을 쓸 필요가 없고,

```xml
<!-- <property name="mapperLocations" value="classpath:/edu/bit/ex/board/mapper/*.xml" /> 
    이 부분을 쓸 필요가 없다.-->
```
  
아래 부분이 있으면 된다.

```xml
 <mybatis-spring:scan
      base-package="edu.bit.ex.board.mapper" />
```

---

### 마이바티스 사용법 : 네 번째 방법

맵퍼 인터페이스에서 getList();구현한 부분이 xml이다.

xml get list 주석처리하고 3부터는 

boardMapper.xml

```xml
<select id="getList" resultType="edu.bit.board.vo.BoardVO">
  <![CDATA[
    select bId, bName, bTitle, bContent, bDate, bHit, bGroup, bStep, bIndent from mvc_board order by bGroup desc, bStep asc
  ]]>
</select>
```

Mapper interface에서 @select annotation을 제공한다.

```java
package edu.bit.board.mapper;

import java.util.List;

import edu.bit.board.page.Criteria;
import edu.bit.board.vo.BoardVO;

public interface BoardMapper {

  @select("select * from mvc_board order by bGroup desc, bStep asc")
  public List<BoardVO> getList();

}
```

단점: 간단한 건 넣을 수 있지만 긴 sql구문은 넣을 수 없기 때문에 혼용으로 사용할 수 는 있지만 통일해서 사용하는게 보기 좋아서 현업에서는 잘 사용하지 않기도 한다.  
만약에 4번 방법만 사용할거면 xml이 필요 없어서 마이바티스 스캔하는것도 할 필요 없다.

---

### 실습

> emp를 두 번째 방법으로 list로 뽑아 내시오.

#### 결과

<img src="https://github.com/anallrounder/Images/blob/main/mybatis_two_list.png?raw=true">

<br><br>


> edu.bit.ex.emp.two

EmpController2.java

```java
package edu.bit.ex.emp.two;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@AllArgsConstructor
@Controller
public class EmpController2 {

  @Inject
  private EmpService2 empService2;

  @RequestMapping("/emp/list5")
  public String list(Model model) throws Exception {
    log.info("emp list_2()");	
    System.out.println("list2()");	
    model.addAttribute("list", empService2.getlist());
    return "/emp/list";
  }

}

```

EmpService2.java

```java
package edu.bit.ex.emp.two;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import edu.bit.ex.emp.vo.EmpVO;

@Service
public class EmpService2 {

  @Inject
  SqlSession sqlSession;

  public List<EmpVO> getlist() throws Exception {
    return sqlSession.selectList("emp.selectEmpList");
  }
}

```

> src/main/resoures > edu.bit.ex.emp.mapper 

Emp2.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
  PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="emp">

  <!-- getlist -->
  <select id="selectEmpList" resultType="edu.bit.ex.emp.vo.EmpVO">
    <![CDATA[
      select * from emp
    ]]>
  </select>

</mapper>
```
