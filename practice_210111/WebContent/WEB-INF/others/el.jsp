<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<jsp:useBean id="member" class="MemberInfo" scope="page" />
<jsp:setProperty name="member" property="name" value="ȫ�浿" />"
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
   
   �̸� : <jsp:getProperty name="member" property="name" /><br />
   ���̵� : <jsp:getProperty name="member" property="id" /><br />
   ��й�ȣ : <jsp:getProperty name="member" property="pw" /><br />
   
   �̸� :  ${member.name}<br />
   <!-- �ȿ��ִ� �����͸���ٷΰ������°� �ƴϰ� getName�Լ� �������°��̴�.�߿�!!��������!! -->
   ���̵� :  ${member.id}<br />
   ��й�ȣ :  ${member.pw}<br />
   
</body>
</html>