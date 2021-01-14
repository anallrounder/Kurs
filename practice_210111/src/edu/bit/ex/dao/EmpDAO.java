
package edu.bit.ex.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.sql.DataSource;
import edu.bit.ex.vo.EmpVO;

public class EmpDAO {
	DataSource dataSource;

	public EmpDAO() { 
		// TODO Auto-generated constructor stub
		try {
			Context context = new InitialContext();
			dataSource = (DataSource) context.lookup("java:comp/env/jdbc/oracle");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public ArrayList<EmpVO> list() { // 어레이 리스트로 관리

		ArrayList<EmpVO> dtos = new ArrayList<EmpVO>();

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = dataSource.getConnection();
			String query = "select e.*, d.dname, d.loc from emp e, dept d where e.deptno = d.deptno";
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				int empno = resultSet.getInt("empno");
				String ename = resultSet.getString("ename");
				String job = resultSet.getString("job");
				int mgr = resultSet.getInt("mgr");
				String hiredate = resultSet.getString("hiredate");
				int sal = resultSet.getInt("sal");
				int comm = resultSet.getInt("comm");
				int deptno = resultSet.getInt("deptno");
				String dname = resultSet.getString("dname");
				String loc = resultSet.getString("loc");

				EmpVO dto = new EmpVO(empno, ename, job, mgr, hiredate, sal, comm, deptno, dname, loc);
				dtos.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return dtos;
	}
	
	public void insert(int empno, String ename, String job, int mgr, String hiredate, int sal, int comm, int deptno) {
		// TODO Auto-generated method stub
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = dataSource.getConnection();
			String query = "insert into emp (empno, ename, job, mgr, hiredate, sal, comm, deptno) values (?,?,?,?,?,?,?,?)";
			preparedStatement = connection.prepareStatement(query);
			
			preparedStatement.setInt(1, empno);
			preparedStatement.setString(2, ename);
			preparedStatement.setString(3, job);
			preparedStatement.setInt(4, mgr);
			preparedStatement.setString(5, hiredate);
			preparedStatement.setInt(6, sal);
			preparedStatement.setInt(7, comm);
			preparedStatement.setInt(8, deptno);
			
			int rn = preparedStatement.executeUpdate();
			System.out.println(rn);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	public ArrayList<EmpVO> getManager() {
		ArrayList<EmpVO> getMgr = new ArrayList<EmpVO>();

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String query = "select m.EMPNO ,m.ename as manage from emp e , emp m where e.mgr = m.empno group by m.ename ,m.EMPNO";

		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				int empno = resultSet.getInt("empno");
				String ename = resultSet.getString("manage");

				EmpVO dto = new EmpVO(empno, ename);

				getMgr.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return getMgr;
	}

	public ArrayList<EmpVO> getDepartment() {

		ArrayList<EmpVO> dn = new ArrayList<EmpVO>();

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String query = "select * from dept";

		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				int deptno = resultSet.getInt("deptno");
				String dname = resultSet.getString("dname");
				String loc = resultSet.getString("loc");

				EmpVO dto = new EmpVO(deptno, dname, loc);
				dn.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return dn;
	}

	



	

}