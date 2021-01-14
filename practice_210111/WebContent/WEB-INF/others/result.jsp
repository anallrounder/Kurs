<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
   <!-- 원래 하던 방식 -->
   <%
      String id = request.getParameter("id");
      String pw = request.getParameter("pw");
   %>
   아이디 : <%= id %><br />
   비밀번호 : <%= pw %><br />
   
   아이디 : ${ param.id }<br />
   비밀번호 :  ${ param.pw }<br />
   <!-- 
   String id = request.getParameter("id");
   String pw = request.getParameter("pw"); 
   이거 호출함 -->
   
   <!-- 위와 같은 문법이다. -->
   아이디 : ${param["id"] }<br />
   비밀번호 :  ${param["pw"] }<br />
   
   <form action="result2.jsp" >
      <input type="submit" value="결과" />
   </form>
   
   <hr />
   
   <!-- 아래 다 내장 객체임 -->
   <%
      application.setAttribute("application_name","application_value");
      session.setAttribute("session_name","session_value" );
      pageContext.setAttribute("page_name","pageContext_value" );
      request.setAttribute("request_name","request_value" );
   %>
   
   <!-- 뿌릴때는 스코프 달아줌  -->
   ${applicationScope.application_name}<br />
   ${sessionScope.session_name}<br />
   ${pageScope.page_name}<br />
   ${requestScope.request_name}<br />
   
   <jsp:forward page="result2.jsp" />
</body>
</html>