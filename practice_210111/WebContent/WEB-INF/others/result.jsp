<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
   <!-- ���� �ϴ� ��� -->
   <%
      String id = request.getParameter("id");
      String pw = request.getParameter("pw");
   %>
   ���̵� : <%= id %><br />
   ��й�ȣ : <%= pw %><br />
   
   ���̵� : ${ param.id }<br />
   ��й�ȣ :  ${ param.pw }<br />
   <!-- 
   String id = request.getParameter("id");
   String pw = request.getParameter("pw"); 
   �̰� ȣ���� -->
   
   <!-- ���� ���� �����̴�. -->
   ���̵� : ${param["id"] }<br />
   ��й�ȣ :  ${param["pw"] }<br />
   
   <form action="result2.jsp" >
      <input type="submit" value="���" />
   </form>
   
   <hr />
   
   <!-- �Ʒ� �� ���� ��ü�� -->
   <%
      application.setAttribute("application_name","application_value");
      session.setAttribute("session_name","session_value" );
      pageContext.setAttribute("page_name","pageContext_value" );
      request.setAttribute("request_name","request_value" );
   %>
   
   <!-- �Ѹ����� ������ �޾���  -->
   ${applicationScope.application_name}<br />
   ${sessionScope.session_name}<br />
   ${pageScope.page_name}<br />
   ${requestScope.request_name}<br />
   
   <jsp:forward page="result2.jsp" />
</body>
</html>