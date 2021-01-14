<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   

<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
   <!-- 변수명, 변수 값 -->
   <!-- var varName = varValue -->
    <c:set var="varName" value="varValue" />
    <!-- 뿌릴때 el과 짬뽕.. 스트링 아니다 ${} 써야한다. -->
    varName : <c:out value="${varName}" />    
    
    
    <br />
    
    <!-- 변수 set한걸 메모리에서 지운다? -->
    <c:remove var="varName" />
    varName : <c:out value="${varName}" /> 
    
    <c:catch var="error">
      <%= 2/0 %>
    </c:catch>
    <br />
    <c:out value="${error}" /> 
</body>
</html>
