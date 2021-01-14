<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="empVO" class="edu.bit.ex.vo.EmpVO" />
<jsp:setProperty name="empVO" property="*" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>emp table</title>
</head>
<body>
	<h1>EMP 테이블 직원 목록</h1>
	<table border="1">
		<tr>
			<td>사원번호</td>
			<td>사원이름</td>
			<td>직급(업무)</td>
			<td>상사(이름)</td>
			<td>입사일</td>
			<td>급여</td>
			<td>커미션</td>
			<td>부서번호</td>
			<td>부서이름</td>
			<td>부서위치</td>
			<td>관리</td>
		</tr>
		<c:forEach items="${list}" var="dtos">
		<tr>
			<td>${dtos.empno}</td>
			<td>${dtos.ename}</td>
			<td>${dtos.job}</td>
			<td>${dtos.mgr}</td>
			<td>${dtos.hiredate}</td>
			<td>${dtos.sal}</td>
			<td>${dtos.comm}</td>
			<td>${dtos.deptno}</td>
			<td>${dtos.dname}</td>
			<td>${dtos.loc}</td>
			<td><a href="#" >수정</a></td>
		</tr>
		</c:forEach>
	</table>
</body>
</html>