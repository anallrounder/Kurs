# 2021.02.03_수_단위테스트

tdd

맵퍼 테스트는 어제로 끝.

## ServiceTest

```java
package edu.bit.ex.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import edu.bit.ex.board.service.BoardService;
import lombok.extern.log4j.Log4j;

@RunWith(SpringRunner.class) 
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class BoardServiceTest {
	
	@Autowired
	BoardService boardService;
	
	@Test
	public void testBoardService() {
		assertNotNull(boardService);
	}
}
```

## ControllerTest

#### 컨트롤러 테스트 하는 것은 조금 까다롭다. 어느부분이?

해당 페이지를 뿌리는 부분인 view를 확인하기 위해서는 url을 처리해줘야한다. 테스트는 톰캣을 톨리는게 아니고 JUnit자체에서 제공하는 컨테이너를 사용해서 테스트 환경을 만드는 것 뿐이다.

단위테스트 : 기본적으로 함수를 테스트하는것

junit에서 view페이지가 나오는지를 테스트하려고할 때 get방식, url로 들어오는 것 등 http 프로토콜을 처리해야한다.  
이러한 처리가 그냥 가상환경으로는 부족하기 때문에 그 환경을 억지로 만드는것이다. 그래서 자세히 테스트 하는 것은 불가능하지만 흉내를 내는것은 가능하다.  
이것을 위해서 우리는 mock이라는 가상의 환경을 만드는 것을 사용해야한다. (JUnit이 제공하는 객체다.)

### 컨트롤러 테스트 방법

#### 어노테이션 붙는게 다르다.

```java
@ContextConfiguration({ "file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml" })
```

컨트롤러가 관여하는게 servlet-context.xml(모델), servlet-context.xml(웹) 이었다. 두 주소를 나눠서 다 작성해줘서 둘 다 불러와야 한다.
그러면 context.xml 두개의 ioc컨테이너 만든다. 루트, 서블렛따로.(두개 스프링 만든다.)
요걸 읽는다. 이거를 junit이 흉내를 내줘야한다.

#### MockMvc란?  

실제 객체와 비슷하지만 테스트에 필요한 기능만 가지는 가짜 객체를 만들어서 애플리케이션 서버에 배포하지 않고도 스프링 MVC 동작을 재현할 수 있는 클래스를 의미한다.

- MockMvcBuilders : 스태틱
- webAppContextSetup(ctx) : MockMvc가 ctx와 연결돼서 객체를 가져옴
- forwardedUrl : 웹브라우저에 띄운다는 것이 아니다. 그냥 테스트 한다는 거다.

Mock test(컨트롤러 함수) 몇 개 만들어서 통합 테스트를 진행한다.


```java
package edu.bit.ex.board.contorller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({ "file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml" })
@Log4j
public class BoardControllerTest {

	@Setter(onMethod_ = { @Autowired }) 
	private WebApplicationContext ctx;

	private MockMvc mockMvc;

	@Before // @Test하기전에 미리 실행하는것을 의미한다. = 테스트 초기화
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build(); //이렇게 객체 생성한다. 
	}
	
	@Test // 컨트롤러에서 하는걸 흉내내는것
	public void testList() throws Exception {
		mockMvc.perform(get("/board/list"))//get방식으로 리스트르 ㄹ받는다.
		.andExpect(status().isOk())//응답검증 200ok -서버
		.andDo(print())//print 뿌리는거. 
		.andExpect(forwardedUrl("/WEB-INF/views/board/list.jsp")); 
		//포워딩해서 가상환경에서 돌리는거. 
		// 콘솔에 넘어오는걸 보고 확인해야한다.
	}
}
```

결과는 콘솔에 뜨는 이 부분을 보고 확인한다.  


```
MockHttpServletRequest:
      HTTP Method = GET
      Request URI = /board/list
       Parameters = {}
          Headers = {}
             Body = <no character encoding set>
    Session Attrs = {}

Handler:
             Type = edu.bit.ex.board.controller.BoardController
           Method = public void edu.bit.ex.board.controller.BoardController.list(org.springframework.ui.Model) throws java.lang.Exception

Async:
    Async started = false
     Async result = null

Resolved Exception:
             Type = null

ModelAndView:
        View name = board/list
             View = null
        Attribute = list

FlashMap:
       Attributes = null

MockHttpServletResponse:
           Status = 200
    Error message = null
          Headers = {Content-Language=[en]}
     Content type = null
             Body = 
    Forwarded URL = /WEB-INF/views/board/list.jsp
   Redirected URL = null
          Cookies = []
```
