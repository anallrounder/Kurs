<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="empDAO" class="edu.bit.ex.dao.EmpDAO" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>사원 정보 입력</h1>
	<hr />
	
	<form action="insert.do" method="post">
		사원 번호 <input type="text" name="empno" size="15" /><br />
		사원 이름 <input type="text" name="ename" size="15" /><br />
		사원 직급 <input type="text" name="job" size="15" /><br />
		매니저  
			<select name=mgr>
				<c:forEach var="manager" items="${empDAO.getManager()}">
				<option value="${manager.empno}">(${manager.empno})${manager.ename}</option>
			</c:forEach>
			</select> 
			<br />
		입사일 <input type="date" name="hiredate" size="15" /><br />
		급여  <input type="text" name="sal" size="15" /><br />
		커미션 <input type="text" name="comm" size="15" /><br />
		부서    
			<select name=deptno >
				<c:forEach var="dprtn" items="${empDAO.getDepartment()}">
				<option value="${dprtn.deptno}">(${dprtn.deptno})${dprtn.dname} </option>
			</c:forEach>
			</select> 
			<br />
		<input type="submit" value="입력" />	
	</form>
</body>
</html>