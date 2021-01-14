<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="grade" class="edu.bit.ex.grade.Grade" scope="page" />
<%-- <jsp:setProperty name="grade" property="*" /> --%>
<jsp:setProperty name="grade" property="sNum" value="${param.sNum}" />
<jsp:setProperty name="grade" property="java" value="${param.java}" />
<jsp:setProperty name="grade" property="db" value="${param.db}" />
<jsp:setProperty name="grade" property="jsp" value="${param.jsp}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>avg</title>
</head>
<body>
<%-- 	<% 
	request.setCharacterEncoding("UTF-8");	
	int sNum = Integer.parseInt(request.getParameter("sNum"));
	int java = Integer.parseInt(request.getParameter("java"));
	int db = Integer.parseInt(request.getParameter("db"));
	int jsp = Integer.parseInt(request.getParameter("jsp"));
	double avg = Double.parseDouble("avg");
	%>
 --%>
	<h3>평균성적</h3>
	<form action="inputGrade.html">
	<table border="1">
		<tr>
			<td colspan="2">학번</td>
			<td width="100px">${grade.sNum}</td>
		</tr>
		<tr>
			<td rowspan="3">과목</td>
			<td>Java</td>
			<td>${grade.java}</td>
		</tr>
		<tr>
			<td>Database</td>
			<td>${grade.db}</td>
		</tr>
		<tr>
			<td>JSP</td>
			<td>${grade.jsp}</td>
		</tr>
		<tr>
			<td colspan="2">평균점수</td>
			<td>${grade.avg()}</td>
		</tr>
		<tr>
			<td colspan="3"><input type="submit" value="입력화면"></td>
		</tr>
	</table>
	</form>
</body>
</html>