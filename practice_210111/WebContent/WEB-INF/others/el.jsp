<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<jsp:useBean id="member" class="MemberInfo" scope="page" />
<jsp:setProperty name="member" property="name" value="홍길동" />"
<jsp:setProperty name="member" property="id" value="abc" />"
<jsp:setProperty name="member" property="pw" value="123" />"
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
   ${10}<br />
   <%= 10 %>
   ${99.99}<br />
   ${"ABC"}<br />
   ${true}<br />
   
   ${ 1+2 }<br />
   ${ 1-2 }<br />
   
   ${ 1>2 }<br />
   ${ 1<2 }<br />

   ${ (1>2) ? 1 : 2 }<br />
   ${ (1>2) || (1<2) }<br />
   
   이름 : <jsp:getProperty name="member" property="name" /><br />
   아이디 : <jsp:getProperty name="member" property="id" /><br />
   비밀번호 : <jsp:getProperty name="member" property="pw" /><br />
   
   이름 :  ${member.name}<br />
   <!-- 안에있는 데이터멤버바로가져오는게 아니고 getName함수 가져오는것이다.중요!!잊지말것!! -->
   아이디 :  ${member.id}<br />
   비밀번호 :  ${member.pw}<br />
   
</body>
</html>