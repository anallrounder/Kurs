# 2021.02.01 월 Spring

## 페이징

두 가지 부분으로 나눠진다.

1. ui 처리를 위한 설계 - 변수 7개
2. DB 쿼리 처리 (mysql, oracle11g이하, 12이상)  
    - mysql은 간단하게 처리됨 (limit으로 처리-간단, 한 줄로 끝남)  
    - oracle에서 limit은 12 이상에서만 제공(11g에서는 지원하지 않음)  

---

> 진짜 페이징을 위해 어떤 변수가 필요하고 어떻게 작용하는지 생각해봐야한다.  
- 추상적인것들을 변수로 표현해 내야 한다. 무엇을 변수로 표현해야할까.
- 무엇을 변수로 표현해 내야할 지를 모르기 때문에 어렵다.  
- 여기서는 7개 변수로 표현한다.  
- 1, 2, 3, 각각 눌렀을 때 들어갈 요소들

> edu.bit.ex.board.controller  

- BoardController.java

```java
//페이징 처리 리스트
@GetMapping("/board/list2")
public void list2(Criteria cri, Model model) throws Exception {	
	log.info("list2 호출");
	log.info(cri);
	System.out.println(cri);
	
	model.addAttribute("list", boardService.getList(cri)); //이 부분이 핵심부분이다.
	
	int total = boardService.getTotal(cri);
	log.info("total"+total);
	model.addAttribute("pageMaker", new PageVO(cri, total)); //밑에 뿌리는건 얘가 결정함
	//처음에 최신에서 10개를 가져와야함 
	//total로 전체를 받아온다음 객체를 생성해서 pageMaker로 넘김 (리스트로)
}
```
> edu.bit.ex.board.page

- Criteria.java

```java
package edu.bit.ex.board.page;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//게시판 페이징 전용 클래스
//Criteria : 사전적 의미로는 검색기준, 분류기준

@Setter
//@Getter
@ToString
public class Criteria {
	// 페이징 처리를 위해서 페이지 번호와 한 페이지 당 몇 개의 데이터를 보여줄 지 결정되어야만 한다.
	private int pageNum; // 페이지 번호
	private int amount; // 한 페이지당 보여줄 게시글의 개수 : 몇 개의 데이터를 보여줄 것인가

	public Criteria() { // 기본 세팅
		this(1, 10);	//1 페이지 10개로 지정 // this함수는 생성자 호출
	}

	public Criteria(int pageNum, int amount) {
		this.pageNum = pageNum;
		this.amount = amount;
	}

	public int getPageNum() {
		return pageNum;
	}

	public int getAmount() {
		return amount;
	}
}
```

- PageVO.java

```java
package edu.bit.ex.board.page;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PageVO {
	// 페이징 처리 할 때 필요한 정보들

	// private int displayPageCount = 10; // 이렇게도 가능하다
	private int startPage; // 화면에 보여지는 시작 번호
	private int endPage; // 화면에 보여지는 끝 번호
	private boolean prev, next; // 이전과 다음으로 이동 가능한 링크 표시

	private int total; // 전체 글 개수
	private Criteria cri; // criteria 가져옴

	public PageVO(Criteria cri, int total) {
		this.cri = cri;
		this.total = total; // 전체 데이터 수

		// Total을 통한 endPage의 재계산
		// 예1) 10개씩 보여주는 경우 전체 데이터 수가 80개라고 가정하면 끝번호는 10이 아닌 8이 됨
		// 예2) 현재 페이지가 13이면 13/10 = 1.3 올림 ->2 끝페이지는 2*10=20
		// Math.ceil : 올림 // 10은 스태틱 변수로 선언해서 사용해도 된다. 여기선 10으로 그냥 지정함
		// (페이지번호 /10)을 올림한 값에 10을 곱해준다.
		this.endPage = (int) (Math.ceil(cri.getPageNum() / 10.0) * 10);
		// this.endPage = (int) (Math.ceil(cri.getPageNum() / displayPageCount) *
		// displayPageCount);

		// 시작 페이지는 무조건 마지막 페이지 -9 -> (endPage - (amount-1))
		this.startPage = this.endPage - 9;

		// 실제 페이지가 작으면 8을 end페이지에 들어가게 해줌 (전체 게시글 수 / 페이지당 글의 갯수)
		int realEnd = (int) (Math.ceil((total * 1.0) / cri.getAmount()));

		// 만약에 진짜 마지막 페이지가 endPage보다 작으면
		// 끝번호는 마지막 페이지가 되어야 하므로 그게 endPage가 되도록 설정
		if (realEnd < this.endPage) {
			this.endPage = realEnd;
		}

		// startPage(시작번호)가 1보다 큰 경우 존재함
		this.prev = this.startPage > 1;

		// realEnd가 (끝번호)endPage보다 큰 경우에만 존재함
		this.next = this.endPage < realEnd;
	}

	public String makeQuery(int page) { //클릭을 할 때 마다 서버에서 받아야해서 좀 더 쉽게 넘기기 위해서 겟방식으로 쿼리를 만들어 쉽게 달아주기 위해 만든것. 
		UriComponents uriComponentsBuilder = UriComponentsBuilder.newInstance().queryParam("pageNum", page) // pageNum = 3
				.queryParam("amount", cri.getAmount()) // pageNum=3&amount=10
				.build(); // ?pageNum=3&amount=10
		return uriComponentsBuilder.toUriString(); // ?pageNum=3&amount=10 리턴
	}
}
```

> edu.bit.ex.board.service

- BoardService.java (interface)
  
```java
public List<BoardVO> getList(Criteria cri);// 글 목록 조회
public int getTotal(Criteria cri); //글 갯수
```

- BoardServiceImpl.java (구현)

```java
@Override
public List<BoardVO> getList(Criteria cri) {
	log.info("get List with criteria: " + cri);
	return mapper.getListWithPaging(cri);
}

@Override
public int getTotal(Criteria cri) {
	log.info("get total count");
	return mapper.getTotalCount(cri);
}
```

> edu.bit.ex.board.mapper

- BoardMapper.java (interface)

```java
public List<BoardVO> getListWithPaging(Criteria cri);
public int getTotalCount(Criteria cri);
```

> src/main/resources > edu.bit.ex.board.mapper

- BoardMapper.xml

```xml
<!-- list -->
<!-- <select id="getList" resultType="edu.bit.ex.board.vo.BoardVO"> <![CDATA[ 
	select * from mvc_board order by bGroup desc, bStep asc ]]> </select> -->

<!-- pagination -->
<select id="getListWithPaging" resultType="edu.bit.ex.board.vo.BoardVO">
		<![CDATA[
	select * from (
				select rownum as rnum, a.* 
					from ( 
							select * from mvc_board order by bGroup desc, bStep asc
						)a where rownum <= #{pageNum} * #{amount}
		)where rnum > (#{pageNum}-1) * #{amount} 
		]]>
</select>
<!-- ROWNUM에 대한 이해가 중요하다. 이렇게 하는게 정석이다. 이 자체에도 에러가 있지만 이게 정석. 다르게하면 에러가 
	반드시 난다. 이 ROWNUM 때문에 삼중 sql문이 되는거다. 이 특성을 잘 이해해야한다. -->

<!-- 게시물 총 개수 출력 -->
<select id="getTotalCount" resultType="int">
	<![CDATA[
					select count(*) from mvc_board
		]]>
</select>
```

> src > main > webapp > WEB-INF > views > board 

- list2.jsp 

```html
<c:if test="${pageMaker.prev}">
	<a href="list2${pageMaker.makeQuery(pageMaker.startPage - 1) }"> « </a>
</c:if>

<c:forEach begin="${pageMaker.startPage }" end="${pageMaker.endPage }" var="idx">
	<c:out value="${pageMaker.cri.pageNum == idx?'':''}" />
	<a href="list2${pageMaker.makeQuery(idx)}">${idx}</a>
</c:forEach>

<c:if test="${pageMaker.next && pageMaker.endPage > 0}">
	<a href="list2${pageMaker.makeQuery(pageMaker.endPage +1) }">	» </a>
</c:if>
<br>
```
테이블 태그 뒤에 이어서 붙여넣는다. 


> 오늘부터 오후에 db설계 2시간씩 진도나감!! (database modeling)