<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
   <!-- �Ѹ����� ������ �޾���  -->
   ${applicationScope.application_name}<br />
   ${sessionScope.session_name}<br />
   ${pageScope.page_name}<br />
   ${requestScope.request_name}<br />
</body>
</html>