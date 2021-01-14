package edu.bit.ex;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.bit.ex.dao.EmpDAO;

public class InsertCommand implements Command {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

		int empno = Integer.parseInt(request.getParameter("empno"));
		String ename = request.getParameter("ename");
		String job = request.getParameter("job");
		int mgr = Integer.parseInt(request.getParameter("mgr"));
		String hiredate = request.getParameter("hiredate");
		int sal = Integer.parseInt(request.getParameter("sal"));
		int comm = Integer.parseInt(request.getParameter("comm"));
		int deptno = Integer.parseInt(request.getParameter("deptno"));

		EmpDAO dao = new EmpDAO();
		dao.insert(empno, ename, job, mgr, hiredate, sal, comm, deptno);
	}
}