package edu.bit.ex.command;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.bit.ex.dao.BDao;
import edu.bit.ex.dto.BDto;

public class BListCommand implements BCommand {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {

		BDao dao = new BDao();

		ArrayList<BDto> dtos = dao.list(); // 디비에 있는걸 이제 여기로 가져옴
		request.setAttribute("list", dtos); // 리스트이름으로 첫번째 주소를 리퀘스트 객체에 넣는다.ㅣ
		// 여기서 4개 영역있다고하는데 세션, 다른거로 해도 똑같이 사용할 수 있다.
	}

}
