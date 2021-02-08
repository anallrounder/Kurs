# 2021_02_08_금_RESTful

비동기통신 _ 쓰레드  
json -> ajax -> RESTful  
RESTful? REST API?

## REST(REpresentational State Transfer)란?

- 로이 필딩(Roy Fielding)의 2000년 박사학위 논문에서 소개  
- **URI설계**!!! (-> 자원의 식별)
- 스프링과 관련있는게 아니고 자체적인 개념이다.

- 기본적으로 url  location 찾아서 img, html등 서비스함 --> url을 활용하기 시작함

  `/board/board.jpg`는 해당 리소스를 달라는 말이었다.  
  이제는  
  `/board/1000`  
  이 말은? 1000번째 글

  옛날에는 로케이션을 끌고오기 위했는데 이제는 시멘틱으로 보드의 1000번째 글로 식별한다.  
  이 identification(이제 location아님)을  

    1000 + select 하려는건지,  
    1000 + update 하려는건지,  
    1000 + delete 하려는건지,  
    1000 + insert 하려는건지,

  이렇게 같이 의미를 주기위해서 http protocol에 의미를 줘보자는 것!  
  예전에는 get, post밖에 type이 없었다.(1.0 까지, 1.1부터는 4+2(옵션)= 6개)

### 대표적으로 다음과 같은 방식이 사용된다.(CURD)

- CRUD: 소프트웨어(Software)가 가지는 기본적인 데이터 처리 기능을 묶어서 일컫는 말

1) **GET** : 정보를 요청하기위해 사용 (Read -> sql의 select)
2) **POST** : 정보를 입력하기위해 사용 (Create -> sql의 insert)
3) **PUT** : 정보를 업데이트하기위해 사용 (Update -> sql의 update)
4) **DELETE** : 정보를 삭제하기위해 사용 (Delete -> sql의 delete)

### REST API 설계

|         이름         |             조작             | Method | REST URI     | 기존 URI      |
| :------------------: | :--------------------------: | :----: | :----------- | :------------ |
| Read(또는 Retrieve)  |       게시판 전체조회        |  GET   | /board       | /list         |
| Read(또는 Retrieve)  | 게시판 컨텐츠조회 (1개 조회) |  GET   | /borad/{bid} | /content_view |
|        create        |       게시판  글 생성        |  POST  | /borad       | -             |
|        Update        |       게시판  글 수정        |  PUT   | /borad/{bid} | /write        |
| Delete(또는 Destroy) |       게시판  글 삭제        | DELETE | /borad/{bid} | -             |

- 넘겨줄 때 주로 json을 사용! (표준이라서)

- 카카오 API면 카카오 팀이 설계한 것을 JSON 파일로 받아와서 파싱을 하면 된다.
- 이제 get, post만 쓰지 말고, 이렇게 다른 방식을 더 사용해보자!! ++ (어렵지 않고 좋다!!)

- uri 설계!  
  board/delet/1000 --> 실행 할 의미를 다이렉트로 나타내는 주소를 사용하면 안된다. 소문자 사용 권장됨

---

## 실습 board_test_5

### List 뿌리고, 삭제버튼 눌러서 delete시키기

#### RestBoardController.java  

```java
package edu.bit.ex.board.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import edu.bit.ex.board.service.BoardService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

// REST: REpresentational State Transfer
// 하나의 URI가 하나의 고유한 리소스를 대표하도록 설계된 개념 

//http://localhost/spring02/list?bno=1 ==> url+파라미터
//http://localhost/spring02/list/1 ==> url

// RestController를 스프링 4.0부터 지원
// @Controller, @RestController 차이점은 메서드가 종료되면 화면 전환의 유무

@Log4j
@AllArgsConstructor
@RestController //기존하고 다르게 처리하겠다. -> 리턴값 뷰아니고 다른거로 하겠다.
@RequestMapping("/restful/*") //이걸로 치고 들어오는 모든걸 이 클래스가 해결해라.
public class RestBoardController {

  private BoardService boardService;

  //1. list(처음 진입 화면이므로 화면이 깜박여도 상관 없으므로 @Controller방식으로 접근 -view(화면)을 리턴
  @GetMapping("/board") 
  public ModelAndView list(ModelAndView mav) { //모델앤뷰 중요!
  //기존방식으로 넘기려면 ModelAndView로 넘기면된다. 
  //제이슨으로 넘길수도있긴하다.(그런데 꼭 제이슨으로 넘겨야만 하는 것은 아님)
    mav.setViewName("rest_list");
    mav.addObject("list", boardService.getList());
    
    return mav;
  }
}
```

#### list.jsp

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>REST Delete</title>
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script type="text/javascript">
  /* if (typeof jQuery == 'undefined') {  //jQuery동작확인
    var script = document.createElement('script'); 
    script.type = "text/javascript"; 
    script.src = "http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"; 
    document.getElementsByTagName('head')[0].appendChild(script); 
  }  */

  //test : jQuery문제는 아님.
  /* 	$(document).ready(function(){
      $('#test_id').click(function(){
          alert("얍");
      });
  });
  */

    $(document).ready(function() {
    $(".a-del").click(function(event) {  // id아니고 클래스로 주는거 중요..!
      event.preventDefault();
      //alert("a-del click");
      console.log("a-del click");

      var tr = $(this).parent().parent();

      $.ajax({
        type : 'DELETE', //method
        url : $(this).attr("href"), // 여기 url 부분이 핵심이었다.
        cache : false,
        success : function(result) {
        // alert("result: " + result)
          console.log("result: " + result);
          if (result == "SUCCESS") {
            $(tr).remove();
          // alert("삭제되었습니다.");
          }
        },
        errer : function(e) {
          console.log(e);
        }
      }); //end of ajax
    }); //end of $((".a-del").click(){};

  });
</script>

</head>
<body>
  <!--<a id="test_id" href="https://google.com">test</a></p> -->
  <table width="500" cellpadding="0" cellspacing="0" border="1">
    <tr>
      <td>번호</td>
      <td>이름</td>
      <td>제목</td>
      <td>날짜</td>
      <td>히트</td>
      <td>삭제</td>
    </tr>
    <c:forEach items="${list}" var="vo">
      <tr>
        <td>${vo.bId}</td>
        <td>${vo.bName}</td>
        <td><c:forEach begin="1" end="${vo.bIndent}">-</c:forEach> <a
          id="a-content"
          href="${pageContext.request.contextPath}/restful/board/${vo.bId}">${vo.bTitle}</a></td>
        <td>${vo.bDate}</td>
        <td>${vo.bHit}</td>
        <td><a class="a-del" href="${pageContext.request.contextPath}/restful/board/${vo.bId}">삭제</a></td>
      </tr>
    </c:forEach>
    <tr>
      <td colspan="5"><a href="write_view">글작성</a></td>
    </tr>
  </table>
</body>
</html>
```

-> 주소로 가서 확인해보자.
[htpp://localhost:8282/ex/restful/board](http://localhost:8282/ex/restful/board/)
(삭제버튼으로 삭제!)

---

### Conetent_veiw, delete

#### RestBoardController.java (for content_view, delete)

```java
  //2. 컨턴츠뷰는 제목을 눌렀을 때, board/200이렇게 넘어와야한다. 
  @GetMapping("/board/{bId}") //이거 처리하는방법이 1)PathVariable로 처리해도 되고,(아래처럼)
  //public String rest_content_view(@PathVariable("bId") String bId, Model model) {
  public String rest_content_view(BoardVO boardVO, Model model) { // 2)커맨드 객체가 가장 좋다.
    log.info("rest_content_view");
    model.addAttribute("content_view", boardService.get(boardVO.getbId()));
    return "content_view";
  }

  //3. delete
  @DeleteMapping("/board/{bId}") //맵핑 자체가 델리트맵핑
  public ResponseEntity<String> rest_deltete(BoardVO boardVO, Model model) {

    ResponseEntity<String> entity = null; //레스트풀을 위해제공하는 대표적인 것 중 하나
    log.info("rest_delete");
    
    try {
      boardService.remove(boardVO.getbId());
      //삭제가 성공하면 성공 상태 메세지 저장 // 마음대로 전달할 수 있다.
      entity = new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
      //message = "SUCCESS" 이렇게 해도 상관 없다.
    } catch (Exception e) {
      e.printStackTrace();
      // 댓글 삭제가 실패하면 실패 상태메세지 저장
        entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
      // 삭제 처리 HTTP 상태 메시지 리턴
        return entity;

  }
```

<br>

- href 클릭하면 기본적으로 get으로 넘어간다.  
- restful의 단점은 기본적으로 form 태그에서 get, post만 되고 put, delete는 지원하지 않는다는 것이다.  
- 그래서 여기서 '삭제'버튼을 클릭했을 때 delete로 넘기기 위해서는 반드시 자바스크립트로(ajax + jQuery) 처리해야한다. (마찬가지로 댓글삭제는 반드시 ajax로 처리 해줘야한다.)

컨텐츠뷰 -> modify -> put!! 도 해보자. (내일)

---

내일부터: 스프링 / 트랜잭션 / interceptor / AOP / 스프링시큐리티  + 스프링부트 / 부트스트랩 (오후)
!! 이해하는것도 중요하지만 지금은 활용하는게 중요하다. !!