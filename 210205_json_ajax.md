# 2021_02_05_금_JSON & Ajax

앞으로 하게 될 것은  
 `xml / json + ajax -> RESTful` --> 이것을 이용해서 게시판 짜기!

---

## xml 이란?

### 나오게 된 배경

전제 : 데이터를 주고 받기 위해서 통신을 함  
클라이언트 -- 서버 : 예를 들어 멤버 가입을 할 경우 클라이언트가 아이디, 패스워드를 서버로 보낼 때 id: / id- / id_ / id= 이런 다양한 형태의 통일되지 않는 형식의 메시지를 보내던 것을 하나로 통일시켜 통신할 수 있도록 마크업 형식 자체를 표준으로 만든 것

### 특징

- EXtensible Markup Language
- HTML과 매우 유사한 마크업 언어(text-based markup language)
- 데이터 저장과 전송을 위해 설계됨 (데이터를 보여주는 목적이 아님)
- HTML은 데이터를 표시하는데 (데이터의 모양에) 중점을 두고 설계됨
- XML 태그는 HTML 태그처럼 미리 정의되어 있지 않고, 사용자가 직접 정의할 수 있음
- W3C 표준 권고안에 포함되어있음
- 사람과 기계가 동시에 읽기 편한 구조
- xml의 태그는 어떤 정보를 포함하는지 설명함

---

## Json 이란?

- 웹이 아닌 앱 등의 통신은 xml이아니고 json으로 통신한다.  
  (html기반이 아니기 때문에 json을 사용하게 됨)
- json은 자바스크립트를 확장해 만들어졌다.
- 자바스크립트 객체 표기법을 따른다. (자바스크립트 객체 표기는 ->  [ ] )
- 사람과 기계가 모두 읽기 편하도록 고안되었다.
- Json은 프로그래밍 언어와 운영체제에 독립적이다.  
  : 예를들면 프로그래머 입장에서 생성한 자바 객체를 인터넷으로 보내서 그대로 불러들이는게 best! 그런데 서버가 php면? json을 통해서 보내게 된다. 그런데 어차피 프로그래밍 언어와 관계없이 자바 객체를 변환시킬 수 밖에 없으니까 JSON이 독립적이라는 말이다. 그래서 그걸 php가 파싱한다. (파싱: 데이터를 읽어들인다.)

- 지금은 json이 거의 표준이 됨
- 자바에서 배열이 json 객체 { }

xml 문서는 xml dom(document object model)을 이용하여 해당 문서에 접근한다.
json은 문자열을 전송받은 후 해당문자열을 바로 파싱하므로, 더욱 빠른 처리속도를 보여준다.

---

### 코딩 실습 1. spring  v4.0 이전 스프링에서의 JSON

> edu.bit.ex.board.controller

#### RestBoardSpring4BeforeController.java

- spring  v4.0 이전 스프링에서의 JSON (@Controller + @ResponseBody)  
- 예전엔 리턴타입을 view를 적어줬지만 @ResponseBody를 붙이면서 지금까지 문법사항과 달라지는 것!

```java
package edu.bit.ex.board.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.bit.ex.board.page.Criteria;
import edu.bit.ex.board.page.PageVO;
import edu.bit.ex.board.service.BoardService;
import edu.bit.ex.board.vo.BoardVO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

/**
 * Handles requests for the application home page.
 */
  
//우리는 5쓰고있다?
//spring  v4.0 이전 스프링에서의 JSON (@Controller + @ResponseBody) 
@Log4j
@AllArgsConstructor
@Controller
public class BoardController {

  private BoardService boardService;

  @ResponseBody // 새로 달아줌 ->기존에 했던 문법사항과 완전히 달라짐
  @GetMapping("/rest/before")
  public List<BoardVO> before(Model model) throws Exception { 
    //예전엔 리턴타입을 view를 적어줬다. 
    //responseBody를 붙이면서 지금까지 문법사항과 달라지는 것!
    
    log.info("before");
    //model.addAttribute("list",  boardService.getList());
    List<BoardVO> list = boardService.getList();
    
    return boardService.getList();
  }

}

```

 -> 이제 컨트롤러에서 맵핑한 주소로 들어가서 확인을 해보자.

http://localhost:8282/ex/rest/before  
처음엔 텍스트 형태로 보이다가 -> xml방식으로 보여짐  
(내 크롬브라우저에서는 xml로 보이지가 않는다. 왜일까 ㅜㅜ)


http://localhost:8282/ex/rest/before.json  
.json을 붙이면 아래와 같이 json 문법으로 리턴해 준 것을 확인해 볼 수 있다.

```json
[{"bId":2143,"bName":"test","bTitle":"테스트","bContent":"테스트","bDate":1612030726000,
"bHit":0,"bGroup":2143,"bStep":0,"bIndent":0},{"bId":2142,"bName":"test","bTitle":"테스트",
"bContent":"테스트","bDate":1612030555000,"bHit":0,"bGroup":2142,"bStep":0,"bIndent":0},
{"bId":2141,"bName":"test","bTitle":"테스트","bContent":"테스트","bDate":1612030555000,
"bHit":0,"bGroup":2141,"bStep":0,"bIndent":0},{"bId":2140,"bName":"test","bTitle":"테스트",
"bContent":"테스트","bDate":1612030555000,"bHit":0,"bGroup":2140,"bStep":0,"bIndent":0},
{"bId":2139,"bName":"test","bTitle":"테스트","bContent":"테스트","bDate":1612030555000,
"bHit":0,"bGroup":2139,"bStep":0,"bIndent":0},{"bId":2138,"bName":"test","bTitle":"테스트",
"bContent":"테스트","bDate":1612030555000,"bHit":0,"bGroup":2138,"bStep":0,"bIndent":0},
{"bId":2137,"bName":"test","bTitle":"테스트","bContent":"테스트","bDate":1612030555000,
"bHit":0,"bGroup":2137,"bStep":0,"bIndent":0},{"bId":2136,"bName":"test","bTitle":"테스트",
"bContent":"테스트","bDate":1612030555000,"bHit":0,"bGroup":2136,"bStep":0,"bIndent":0},
{"bId":2135,"bName":"test","bTitle":"테스트","bContent":"테스트","bDate":1612030555000,
"bHit":0,"bGroup":2135,"bStep":0,"bIndent":0},{"bId":2134,"bName":"test","bTitle":"테스트",
"bContent":"테스트","bDate":1612030555000,"bHit":0,"bGroup":2134,"bStep":0,"bIndent":0},
{"bId":2133,"bName":"test","bTitle":"테스트","bContent":"테스트","bDate":1612030555000,
"bHit":0,"bGroup":2133,"bStep":0,"bIndent":0},{"bId":2132,"bName":"test","bTitle":"테스트",
"bContent":"테스트","bDate":1612030555000,"bHit":0,"bGroup":2132,"bStep":0,"bIndent":0},
{"bId":2131,"bName":"test","bTitle":"테스트","bContent":"테스트","bDate":1612030555000,
"bHit":0,"bGroup":2131,"bStep":0,"bIndent":0}, 
.....

```

이렇게 xml, json으로 보일 수 있도록 하기 위해서는 아래 pom.xml에 다음의 세 가지 dependency를 추가해야한다. (우리는 이전에 이미 설정 해뒀다.)  
그 다음 컨트롤러에서 어노테이션 붙이면 xml로 내부적으로 알아서 바꿔준다.

```xml
<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-databind</artifactId>
  <version>2.9.6</version>
</dependency>

<!-- 자바객체를 xml으로 -->
<!-- https://mvnrepository.com/artifact/com.fasterxml.jackson.dataformat/jackson-dataformat-xml -->
<dependency>
  <groupId>com.fasterxml.jackson.dataformat</groupId>
  <artifactId>jackson-dataformat-xml</artifactId>
  <version>2.9.6</version>
</dependency>

<!-- 자바객체를 Json으로 -->
<!-- https://mvnrepository.com/artifact/com.google.code.gson/gson -->
<dependency>
  <groupId>com.google.code.gson</groupId>
  <artifactId>gson</artifactId>
  <version>2.8.2</version>
</dependency>
```

---

### 코딩 실습 2. spring  v4.0 이후 스프링에서의 JSON

> edu.bit.ex.board.controller

#### RestBoardSpring4AfterController.java

spring v4.0에서 부터 `@RestController`라는 어노테이션을 추가해서 해당 Controller의 모든 메서드의 리턴타입을 기존과 르게 처리한다는 것을 명시

```java
package edu.bit.ex.board.controller;

import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.bit.ex.board.service.BoardService;
import edu.bit.ex.board.vo.BoardVO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

/**
 * Handles requests for the application home page.
 */

// spring v4.0에서 부터 @RestController라는 어노테이션을 추가해서 
// 해당 Controller의 모든 메서드의 리턴타입을 기존과 르게 처리한다는 것을 명시
@Log4j
@AllArgsConstructor
@RestController
public class RestBoardSpring4AfterController {

  private BoardService boardService;

  //@ResponseBody // 이것은 더이상 안 적어줘도 된다.
  @GetMapping("/rest/after")
  public List<BoardVO> after(Model model) throws Exception { 
    log.info("/rest/after");
    List<BoardVO> list = boardService.getList();
    
    return boardService.getList();
  }
  //기존에는 view이용했지만 @RestController 사용하면서 xml, json으로 리턴하겠다. 
}

```

browser에서 확인 해보면 before와 마찬가지로 xml, json 형식으로 보인다.  

http://localhost:8282/ex/rest/after
처음엔 텍스트 형태로 보이다가 -> xml방식으로 보여짐  
(내 크롬브라우저에서는 xml로 보이지가 않는다. 왜일까 ㅜㅜ)
`//192.168.0.93`

http://localhost:8282/ex/rest/after.json  
.json을 붙이면 아래와 같이 json 문법으로 리턴해 준 것을 확인해 볼 수 있다.

```json
[{"bId":2143,"bName":"test","bTitle":"테스트","bContent":"테스트","bDate":1612030726000,
"bHit":0,"bGroup":2143,"bStep":0,"bIndent":0},{"bId":2142,"bName":"test","bTitle":"테스트",
"bContent":"테스트","bDate":1612030555000,"bHit":0,"bGroup":2142,"bStep":0,"bIndent":0},
{"bId":2141,"bName":"test","bTitle":"테스트","bContent":"테스트","bDate":1612030555000,
"bHit":0,"bGroup":2141,"bStep":0,"bIndent":0},{"bId":2140,"bName":"test","bTitle":"테스트",
"bContent":"테스트","bDate":1612030555000,"bHit":0,"bGroup":2140,"bStep":0,"bIndent":0},
{"bId":2139,"bName":"test","bTitle":"테스트","bContent":"테스트","bDate":1612030555000,
"bHit":0,"bGroup":2139,"bStep":0,"bIndent":0},{"bId":2138,"bName":"test","bTitle":"테스트",
"bContent":"테스트","bDate":1612030555000,"bHit":0,"bGroup":2138,"bStep":0,"bIndent":0},
{"bId":2137,"bName":"test","bTitle":"테스트","bContent":"테스트","bDate":1612030555000,
"bHit":0,"bGroup":2137,"bStep":0,"bIndent":0},{"bId":2136,"bName":"test","bTitle":"테스트",
"bContent":"테스트","bDate":1612030555000,"bHit":0,"bGroup":2136,"bStep":0,"bIndent":0},
{"bId":2135,"bName":"test","bTitle":"테스트","bContent":"테스트","bDate":1612030555000,
"bHit":0,"bGroup":2135,"bStep":0,"bIndent":0},{"bId":2134,"bName":"test","bTitle":"테스트",
"bContent":"테스트","bDate":1612030555000,"bHit":0,"bGroup":2134,"bStep":0,"bIndent":0},
{"bId":2133,"bName":"test","bTitle":"테스트","bContent":"테스트","bDate":1612030555000,
"bHit":0,"bGroup":2133,"bStep":0,"bIndent":0},{"bId":2132,"bName":"test","bTitle":"테스트",
"bContent":"테스트","bDate":1612030555000,"bHit":0,"bGroup":2132,"bStep":0,"bIndent":0},
{"bId":2131,"bName":"test","bTitle":"테스트","bContent":"테스트","bDate":1612030555000,
"bHit":0,"bGroup":2131,"bStep":0,"bIndent":0}, 
.....

```

---

## Ajax 란?

- 정리  
  - 개념적 이해: 비동기통신  
  - 문법 사용: $() -> jQuery 함수의 객체를 만들어서 넘겨주는 것
- Asynchronous JavaScript and XML
- 자바스크립트를 사용한 비동기 통신, 클라이언트와 서버 간 데이터를 주고 받는 기술  
  - xml, xslt, xmlhttprequest  
  - Ajax 애플리케이션은 xml/xslt 대신 미리 정의된 html이나 일반 텍스트, json, json-rpc를 이용할 수있다.
- 모든 페이지를 다시 로드하는 수고를 하지 않고도 페이지의 일부만을 변경할 수 있음 (깜박이지 않음)
- 웹 서버와 비동기 통신 (안 기다림)
- 실무에서는 장점이 더 많기 때문에 실제 ajax처리를 많이 한다.

### 동기식 (기다림)

클라이언트는 전송을 서버로 했으면 그다음으로 반드시 응답을 받아야 한다.  
= 응답을 받을 때 까지 기다려야한다.

### 비동기식 (안 기다림)

서버로 A에 대한 요청을 보냈지만 응답이 오는것을 기다리지 않고 B를 처리할 수 있다.  
그러나 응답을 받기는 받아야한다. 응답이 오면 응답을 받았을 때, 응답받은 내용을 처리한다.

#### 장점

- 웹페이지의 속도향상
- 서버의 처리가 완료 될때까지 기다리지 않고 처리 가능
- 데이터 전체를 새로 로드할 필요가 없다.  
  예전에 만든 게시판은 삭제버튼을 눌렀을 때, 전체 화면이 깜박거리면서 데이터가 로드된다.  
    **그런데 이걸 비동기통신으로 하게되면?**  
    다시 데이터를 받을 필요가 없이,그 부분만 삭제된다. 그게 장점!!  
    = 다른부분을 로드할 필요가 없는것!!!! ( = 화면이 깜박이지 않는다. )  
  
---

### 코딩 실습 3. list.jsp를 Ajax + json으로 뿌리기

> src/main/java > edu.bit.ex.board.controller

#### RestBoardSpring4AfterController.java

```java
package edu.bit.ex.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.bit.ex.board.page.Criteria;
import edu.bit.ex.board.page.PageVO;
import edu.bit.ex.board.service.BoardService;
import edu.bit.ex.board.vo.BoardVO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

/**
 * Handles requests for the application home page.
 */
  
@Log4j
@AllArgsConstructor
@Controller
public class BoardController {

  private BoardService boardService;

  /**
    * Simply selects the home view to render by returning its name.
    */


    //ajaxList
    @RequestMapping("/rest/list") 
    public String restList(Model model)throws Exception { 
      log.info("akaxList"); 
      return "board/ajaxList"; 
    }
}
```

> src > main > webapp > WEB-INF > views 

#### ajaxList.jsp

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Insert title here</title>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <script type="text/javascript">

    /* $.ajax({
    url : '서비스 주소'
      , data : '서비스 처리에 필요한 인자값'
      , type : 'HTTP방식' (POST/GET 등)
      , dataType : 'return 받을 데이터 타입' (json, text 등)
      , success : function('결과값'){
      // 서비스 성공 시 처리 할 내용
      }, error : function('결과값'){
      // 서비스 실패 시 처리 할 내용
      }
    }); */

    function getList() {
      var url ="${pageContext.request.contextPath}/rest/after.json"; // 아까 컨트롤러에서 처리함

      $.ajax({
              type: 'GET',
              url: url,
              cache : false, // 이걸 안쓰거나 true하면 수정해도 값반영이 잘안댐
              dataType: 'json',// 데이터 타입을 제이슨 꼭해야함, 다른방법도 2가지있음
            success: function(result) { //통신에 성공하면 이result로 제이슨이 들어옴

          var htmls="";
          
              $("#list-table").html("");	

          $("<tr>" , {
            html : "<td>" + "번호" + "</td>"+  // 컬럼명들
                "<td>" + "이름" + "</td>"+
                "<td>" + "제목" + "</td>"+
                "<td>" + "날짜" + "</td>"+				
                "<td>" + "히트" + "</td>"
          }).appendTo("#list-table") // 이것을 테이블에붙임

          if(result.length < 1){
            htmls.push("등록된 댓글이 없습니다.");
          } else {

                        $(result).each(function(){			                    			                    
                          htmls += '<tr>';
                          htmls += '<td>'+ this.bId + '</td>';
                          htmls += '<td>'+ this.bName + '</td>';
                          htmls += '<td>'
                    for(var i=0; i < this.bIndent; i++) { //for 문은 시작하는 숫자와 종료되는 숫자를 적고 증가되는 값을 적어요. i++ 은 1씩 증가 i+2 는 2씩 증가^^
                      htmls += '-'	
                    }
                          htmls += '<a href="${pageContext.request.contextPath}/content_view?bId=' + this.bId + '">' + this.bTitle + '</a></td>';
                          htmls += '<td>'+ this.bDate + '</td>'; 
                          htmls += '<td>'+ this.bHit + '</td>';	
                          htmls += '</tr>';			                    		                   
                      });	//each end

                      htmls+='<tr>';
                      htmls+='<td colspan="5"> <a href="${pageContext.request.contextPath}/write_view">글작성</a> </td>';		                	
                      htmls+='</tr>';
                      
          }

          $("#list-table").append(htmls);
          
            }

      });	// Ajax end
    
    }//end	getList()	
  </script>

  <script>
    $(document).ready(function(){
      getList();
    });
  </script>

  </head>
  <body>

  <table id="list-table" width="500" cellpadding="0" cellspacing="0" border="1">
  </table>

</body>
</html>
```

-> 이제 주소로 들어가서 리스트가 뿌려지는지 확인 해 보자.  
http://localhost:8282/ex/rest/list  
처음에 리스트가 로드 되기 전에 줄이 살짝 보이는 것은 Ajax로 테이블을 불러오기 전에 아래 테이블 그려준게 보이는 것이다.
