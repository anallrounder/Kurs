# 2021.02.09_화_transaction

## Transaction이란?

Transaction은 DB에 관련된 용어이다.

논리적 단위로 어떤 한 부분의 작업이 완료되었다 하더라도, 다른 부분의 작업이 완료되지 않을 경우 전체 취소되는 것. 이때, 작업이 완료되는 것을 커밋(commit)이라고 하고, 작업이 취소되는 것을 롤백(rollback)이라고 함

- commit -> 영속적인 저장
- rollbaock -> 작업 취소

예시)  
ATM에서 돈을 출금할 때 모든 은행 거래에는 log를 남긴다.(참고_은행은 모두 오라클 씀)  
카드 거래시 은행에서는 출금이 되었으나 상대방에게 입금이 되지 않은 상황이라면?  
모든 작업이 완료되지 않았기 때문에 이 전체 트랜잭션에 대해 rollback(작업취소)가 되어야한다.

https://ko.wikipedia.org/wiki/%EB%8D%B0%EC%9D%B4%ED%84%B0%EB%B2%A0%EC%9D%B4%EC%8A%A4_%ED%8A%B8%EB%9E%9C%EC%9E%AD%EC%85%98
https://mommoo.tistory.com/62

---

### 실습

트랜잭션을 하기위한 환경을 꾸민다.

#### TransactionController.java

```java
package edu.bit.ex.board.controller;

import java.sql.SQLException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import edu.bit.ex.board.service.TransactionService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

/**
 * Handles requests for the application home page.
 */

@Log4j
@AllArgsConstructor
@Controller
public class TransactionController {

  private TransactionService tranService;

  @GetMapping("/tran/{num}") //RESTful형태 
  public void transiotn(@PathVariable("num") int num) throws SQLException {

    if (num == 1) {
      log.info("transionTest1");
      tranService.transionTest1();
    } else if (num == 2) {
      log.info("transionTest2");
      tranService.transionTest2();
    } else if (num == 3) {
      log.info("transionTest3");
      tranService.transionTest3();
    } else if (num == 4) {
      log.info("transionTest4");
      tranService.transionTest4();
    } else if (num == 5) {
      log.info("transionTest5");
      tranService.transionTest5();
    } else if (num == 6) {
      log.info("transionTest6");
      tranService.transionTest6();
    } else if (num == 7) {
      log.info("transionTest7");
      tranService.transionTest7();
    }
  }
}
```

#### TransactionService.java

```java
package edu.bit.ex.board.service;

import java.sql.SQLException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.bit.ex.board.mapper.BoardMapper;
import edu.bit.ex.board.vo.BoardVO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@Service
@AllArgsConstructor
public class TransactionService {
   private BoardMapper mapper;

   @Transactional
   public void transionTest1() {

      log.info("transionTest1()테스트 ");
      BoardVO boardVO = new BoardVO();
      boardVO.setbContent("트랜잭션1");
      boardVO.setbName("트랜잭션1");
      boardVO.setbTitle("트랜잭션1");

      mapper.insertBoard(boardVO);

      boardVO.setbContent("트랜잭션2");
      boardVO.setbName("트랜잭션2");
      boardVO.setbTitle("트랜잭션2");

      mapper.insertBoard(boardVO);
   }

   public void transionTest2() {

      log.info("transionTest2()테스트 ");
      BoardVO boardVO = new BoardVO();
      boardVO.setbContent("트랜잭션1");
      boardVO.setbName("트랜잭션1");
      boardVO.setbTitle("트랜잭션1");

      mapper.insertBoard(boardVO);

      boardVO.setbContent("트랜잭션2");
      boardVO.setbName("트랜잭션2");
      boardVO.setbTitle("트랜잭션2");

      // 일부러 트랜잭션을 위한 것 -> 여기서 널이라 아래거는 안들어간다.
      boardVO = null;
      mapper.insertBoard(boardVO);
   }

   @Transactional //함수내에서 에러가 나게 되면 해당 디비를 함수 있던 이전으로 되돌리는 것.
   public void transionTest3() {

      log.info("transionTest3()테스트 ");
      BoardVO boardVO = new BoardVO();

      boardVO.setbContent("트랜잭션1");
      boardVO.setbName("트랜잭션1");
      boardVO.setbTitle("트랜잭션1");

      mapper.insertBoard(boardVO);

      boardVO.setbContent("트랜잭션2");
      boardVO.setbName("트랜잭션2");
      boardVO.setbTitle("트랜잭션2");

      // 일부러 에러를 내게 함 //트랜잭션을 위한 것 
      boardVO = null;
      mapper.insertBoard(boardVO);
   }

   //uncheckedExeption(롤백 함)
   @Transactional
   public void transionTest4() {
      BoardVO boardVO = new BoardVO();
      boardVO.setbContent("트랜잭션4");
      boardVO.setbName("트랜잭션4");
      boardVO.setbTitle("트랜잭션4");

      mapper.insertBoard(boardVO);

      throw new RuntimeException("RuntimeException for rollback");
   }

   //CheckedExeption 테스트(롤백 안함)
   //
   @Transactional
   public void transionTest5() throws SQLException {
   
      BoardVO boardVO = new BoardVO();
      boardVO.setbContent("트랜잭션5");
      boardVO.setbName("트랜잭션5");
      boardVO.setbTitle("트랜잭션5");

      mapper.insertBoard(boardVO);

      throw new SQLException("SQLException for rollback");
   }
   
   //@Transactional의 rollbackFor 옵션을 이용하면 Rollback이 되는 클래스를 지정가능함.
   // Exception예외로 롤백을 하려면 다음과 같이 지정하면됩니다. 
   //@Transactional(rollbackFor = Exception.class) 
   // 여러개의 예외를 지정할 수도 있습니다. @Transactional(rollbackFro = {RuntimeException.class, Exception.class})
   //
   @Transactional(rollbackFor = Exception.class) 
   public void transionTest6() throws SQLException {
      BoardVO boardVO = new BoardVO();
      boardVO.setbContent("트랜잭션6");
      boardVO.setbName("트랜잭션6");
      boardVO.setbTitle("트랜잭션6");

      mapper.insertBoard(boardVO);

      throw new SQLException("SQLException for rollback");
   }
   
   @Transactional(rollbackFor = SQLException.class) 
   public void transionTest7() throws SQLException {
      BoardVO boardVO = new BoardVO();
      boardVO.setbContent("트랜잭션7");
      boardVO.setbName("트랜잭션7");
      boardVO.setbTitle("트랜잭션7");

      mapper.insertBoard(boardVO);

      throw new SQLException("SQLException for rollback");
   }

}
```

#### BoardMapper.java

```java
public void insertBoard(BoardVO boardVO); //public void insert(BoardVO boardVO);와 같은거.
```

#### BoardMapper.xml

```java
<!-- insert board -->
<insert id="insertBoard">
    <![CDATA[
    insert into mvc_board (bId, bName, bTitle, bContent, bHit, bGroup, bStep, bIndent) 
    values (mvc_board_seq.nextval, #{bName}, #{bTitle}, #{bContent}, 0, mvc_board_seq.currval, 0, 0)
  ]]>
</insert>
```

---

### 실습 해설

- sql developer에서 확인
  
```sql
select * from mvc_board order by bid desc;
```

#### 1. transionTest1

http://localhost:8282/ex/tran/1  
이 주소로 들어가면 페이지가 나오지 않지만 sql developer에서 확인해 보면 다음과 같이 입력된 것을 확인할 수 있다. -> 두 가지 모두 입력이 되었다.

|  BID  |   BNAME   |  BTITLE   | BCONTENT  |
| :---: | :-------: | :-------: | :-------: |
| 2193  | 트랜잭션2 | 트랜잭션2 | 트랜잭션2 |
| 2192  | 트랜잭션1 | 트랜잭션1 | 트랜잭션1 |

#### 2. transionTest2

http://localhost:8282/ex/tran/2  
sql developer에서 확인해 보면 다음과 같이 트랜잭션 1만 추가적으로 입력된 것을 볼 수 있다.

|  BID  |   BNAME   |  BTITLE   | BCONTENT  |
| :---: | :-------: | :-------: | :-------: |
| 2194  | 트랜잭션1 | 트랜잭션1 | 트랜잭션1 |
| 2193  | 트랜잭션2 | 트랜잭션2 | 트랜잭션2 |
| 2192  | 트랜잭션1 | 트랜잭션1 | 트랜잭션1 |

왜 1만 입력될까? 

```java
 boardVO = null;
```

-> 트랜잭션2를 넣은 후에 null로 다시 넣어서 에러가 나게 만든 것이기 때문이다.
그래서 위의 1은 입력이 되고, 2는 입력이 안 된 것이다.

#### 3. transionTest3

http://localhost:8282/ex/tran/3  
이 경우에는 sql developer에서 아무것도 입력되지 않는다. 왜일까?

```java
@Transactional //함수내에서 에러가 나게 되면 해당 디비를 함수 있던 이전으로 되돌리는 것.
public void transionTest3() {
```

->  @Transactional annotation을 붙였기 때문에 에러가 발생한 것을 감지하고 DB를 함수 이전으로 되돌렸기 때문이다.

#### 4. transionTest4

http://localhost:8282/ex/tran/4
sql developer에서 아무것도 입력되지 않는다. 왜일까?

```java
throw new RuntimeException("RuntimeException for rollback");
```

-> 일부러 RuntimeException 에러를 발생시켰기 때문이다.  
RuntimeException은 UnCheckedException이다. UnCheckedException은 실행단계에서(runtime)에서 확인을 하며 예외처리를 강제하지 않는다. 그래서 트랜잭션 처리에서도 Rollback을 해준다.

- UnCheckedException(= RuntimeException과 자손)에는 NullpointerException, ArithmethicExcetpin등이 있다.

#### 5. transionTest5

http://localhost:8282/ex/tran/5  
아래와 같이 예외를 발생시켰음에도 불구하고 sql developer에서 "트랜잭션5"가 입력된다. 왜일까?

```java
throw new SQLException("SQLException for rollback");
```

|  BID  |   BNAME   |  BTITLE   | BCONTENT  |
| :---: | :-------: | :-------: | :-------: |
| 2195  | 트랜잭션5 | 트랜잭션5 | 트랜잭션5 |
| 2194  | 트랜잭션1 | 트랜잭션1 | 트랜잭션1 |
| 2193  | 트랜잭션2 | 트랜잭션2 | 트랜잭션2 |
| 2192  | 트랜잭션1 | 트랜잭션1 | 트랜잭션1 |

여기서 우리가 주목해야할 점은 @Transactional을 달았다고 해서 무조건 rollback하는게 아니라는 것이다.  
여기서 발생시킨 예외는 SQLException이다. 이것은 대표적인 CheckedException 으로 반드시 개발자가 명시적으로 예외처리를 해야한다.(방법 1. try-catch 2. throws / 에러 자체를 개발자한테 맡김) 그렇기 때문에 트랜잭션 처리에서도 예외처리를 하지 않으면 rollback을 하지 않아서 '트랜잭션5'가 입력되는 결과를 보게 되는 것이다.

- CheckedException에는 IOException, SqlException 등이 있다.

#### 6-7. transionTest6, 7

http://localhost:8282/ex/tran/6  
http://localhost:8282/ex/tran/7  
이 경우에는 에러처리를 해줬기 때문에 DB에 입력이 되지 않는다.

```java
@Transactional(rollbackFor = Exception.class) 
public void transionTest6() throws SQLException {
  ...
  throw new SQLException("SQLException for rollback");
}
```

다음과 같이 정확하기 어떤 Exception인지 특정해서 에러처리를 할 수도 있다.

```java
@Transactional(rollbackFor = SQLException.class) 
public void transionTest7() throws SQLException {
  ...
  throw new SQLException("SQLException for rollback");
}
```

---

### 절대 @Transactional은 컨트롤러에 붙이면 안된다. 왜 안될까?  

컨트롤러에서느 일단 잘 안먹는다. (이것은 부차적인 것이긴 함)  
**중요한 것은!**  
설계할 때 컨트롤러는 기본적으로 하는 역할이 view 정도만 결정하는 것이다.  
나머지 로직, 내가 구현해야 할 것(비지니스 로직은)은 서비스단에 들어가야한다. 즉, 트랜잭션의 대상이 컨트롤러가 아니고 서비스 비지니스 로직단에 붙는게 정석이다.

**BaordController.java** <- @Transactional은 여기 붙이지말고!

```java
@RequestMapping("/reply")
public String reply(BoardVO boardVO, Model model) throws Exception {
  
  log.info("reply()");
  boardService.writeReply(boardVO);
  
  return "redirect:list";
}
```

**BoardService.java** <- 여기에 붙여야한다!!!

```java
@Transactional
@Override
public void writeReply(BoardVO boardVO) {
  mapper.updateShape(boardVO);
  mapper.insertReply(boardVO);
}
```

#### 중요!

1) 익셉션 처리!
2) @Transactional는 서비스에 붙인다!

---

### @Transactional annotaion을 사용하려면 기본적으로 설정이 필요하다.

#### root-context.xml

```xml
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager"> 
        <!-- 트랜 젝션매니저 -트랜잭션객체 (이 이름도 바뀌면 안된다.매니저.) -->
        <property name="dataSource" ref="dataSource" /> 
        <!--객체생성=> 데이타소스를 커넥션풀에 넣어주고 -->
    </bean>
    
    <tx:annotation-driven transaction-manager="transactionManager" />
    <!--  @ 사용할수있게함 -- >
```

---

프로파간다?(전파? - 나중에 찾아보자...)