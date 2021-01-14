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
   <!-- ������, ���� �� -->
   <!-- var varName = varValue -->
    <c:set var="varName" value="varValue" />
    <!-- �Ѹ��� el�� «��.. ��Ʈ�� �ƴϴ� ${} ����Ѵ�. -->
    varName : <c:out value="${varName}" />    
    
    
    <br />
    
    <!-- ���� set�Ѱ� �޸𸮿��� �����? -->
    <c:remove var="varName" />
    varName : <c:out value="${varName}" /> 
    
    <c:catch var="error">
      <%= 2/0 %>
    </c:catch>
    <br />
    <c:out value="${error}" /> 
</body>
</html>
