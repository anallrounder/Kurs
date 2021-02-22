## 스프링 게시판 필수 암기 사항

### xml setting

```
- pom.xml
- web.xml
- root-context.xml
- servlet-context.xml
```

#### 순서

```
controller -> service -> impl -> mapper interface -> mapper xml(sql) -> jsp(view)
```

### class/interface

#### BoardController.java 

```
(annotation: @Log4j  @AllArgsConstructor @Controller -> 주입 -> 함수@GetMapping/@PostMapping throws Exception ) 
받는거 modelAttribute() / 넣는거 boardService / 번호필요한거 bid (콘텐츠뷰,리플라이뷰,델리트) / redirect:list (수정,삭제,댓글,글작성 후)

- list: modelAttribute
- write_view: x
- write: boardService boardVO
- content_view: modelAttribute bid   
 uphit: boardService boardVO
- modify: boardService boardVO
- delete: boardService bid
- reply_view: modelAttribute bid
- reply: boardService boardVO
(replyshape??)
```

#### BoardService.java _interface : public!!

#### BoardServiceImpl.java _구현 : 

```
(+ @Service), 주입, return mapper.-()
```

#### BoardMapper.java _interface : public!!

#### BoardVO.java 

### xml

#### BoardMapper.xml

```xml
<!DOCTYPE mapper
  PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="edu.bit.board.mapper.BoardMapper">

<![CDATA[ sql ]]> ( ; -> x )
```

##### list - select (resultType vo) :

```sql
select * from mvc_board order by bgroup desc, bstep asc
```

##### getListWithPaging :

```sql
select * from (select rownum as rnum, a.* from ( select * from mvc_board order by bGroup desc, bStep asc)a 
where rownum <= #{pageNum} * #{amount}) where rnum > (#{pageNum}-1) * #{amount} 
```

##### getTotalCount (resultType int) :

```sql
select count(*) from mvc_board
```

##### write_view (x)

##### write (insert) :

```sql
insert into mvc_board (bId, bName, bTitle, bContent, bHit, bGroup, bStep, bIndent) 
values (mvc_board_seq.nextval, #{bName}, #{bTitle}, #{bContent}, 0, mvc_board_seq.currval, 0, 0)
```

##### content_view(read) /resultType=vo 

```sql
select * from mvc_board where bId = #{bId}
```

##### hitupdate :

```sql
update mvc_board set bHit = bHit +1 where bId = #{bId}
```

##### modify :

```sql
update mvc_board set bName = #{bName}, bTitle = #{bTitle}, bContent = #{bContent} where bId = #{bId}
```

##### delete :

```sql
delete from mvc_board where bId = #{bId}
```

##### reply_view(readyToReply):

```sq;
select * from mvc_board where bId = #{bId}
```

##### reply (insertReply) :

```sql
insert into mvc_board (bId, bName, bTitle, bContent, bHit, bGroup, bStep, bIndent) 
values (mvc_board_seq.nextval, #{bName},#{bTitle}, #{bContent}, 0, #{bGroup}, #{bStep}+1, #{bIndent}+1)
```

##### replyshape(updateShape):

```sql
update mvc_board set bStep = bStep + 1 where bGroup = #{bGroup} and bStep > #{bStep}
```

### view

```jsp
<%@ page language="java" contentType="text/html; charset=UTF8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
```

#### list.jsp 

```
번호,이름,제목(중요),날짜,히트/글작성(colspan)/페이징  
c:forEach - items, var / begin-end / href!! 
el신경쓸것(특히 btitle)
```

##### write_view.jsp 

```
이름,제목(size),내용(rows) /입력,목록
```

##### conent_view.jsp 

```
글번호,조회수(hit)/이름,제목,내용(->수정해야하니까 값불러오기)/수정,목록,삭제,답변 (->colspan!)

(http://localhost:8282/ex/board/content_view?bId=2135)(수정 뷰!!-이름 제목 내용)
뭘지우고 어디 댓글달지 생각해보자. 그럼 주소를 어떻게 쓸까!!
```

##### reply_view.jsp  

```
글번호,조회수/이름,제목,내용/답변,목록
```
