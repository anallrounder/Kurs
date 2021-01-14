package edu.bit.ex.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.bit.ex.Command;
//import edu.bit.ex.BContentCommand;
//import edu.bit.ex.BDeleteCommand;
import edu.bit.ex.ListCommand;
//import edu.bit.ex.BModifyCommand;
//import edu.bit.ex.BReplyCommand;
//import edu.bit.ex.BReplyViewCommand;
import edu.bit.ex.InsertCommand;

/**
 * Servlet implementation class BoardFrontController
 */
@WebServlet("*.do")
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FrontController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("doGet");
		actionDo(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("doPost");
		actionDo(request, response);
	}

	private void actionDo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("actionDo");

		request.setCharacterEncoding("EUC-KR");

		String viewPage = null; // 어떤 뷰를 보여줄지
		Command command = null; // 어떤 로직을 수행할 지

		String uri = request.getRequestURI();
		String conPath = request.getContextPath();
		String com = uri.substring(conPath.length());// uri에서 contextPath 앞까지 자른다.

		System.out.println(uri);
		System.out.println(conPath);
		System.out.println(com);

		if (com.equals("/insert.do")) {
			command = new InsertCommand();
			command.execute(request, response); // 리스트 목록이 보여지는 화면
			viewPage = "insert.jsp";
		} else if (com.equals("/list.do")) {
			command = new ListCommand();
			command.execute(request, response);
			viewPage = "list.jsp";
		}

		RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
		dispatcher.forward(request, response);
	}
}