package edu.bit.ex;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.bit.ex.dao.EmpDAO;
import edu.bit.ex.vo.EmpVO;

public class ListCommand implements Command {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
		EmpDAO dao = new EmpDAO();
		
		ArrayList<EmpVO> dtos = dao.list();
		request.setAttribute("list", dtos);
	}
}