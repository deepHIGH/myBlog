package com.cos.controller.member;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cos.action.Action;
import com.cos.dao.MemberDAO;
import com.cos.dto.MemberVO;
import com.cos.util.SHA256;
import com.cos.util.Script;

public class MemberJoinAction implements Action{
	private static String naming = "MemberJoinAction : ";
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "/Blog/gmail/emailSendAction.jsp";
		
		MemberVO member = new MemberVO();
		MemberDAO dao = new MemberDAO();
		
		String id = null;
		String password = null;
		String username = null;
		String roadFullAddr = null;
		String email = null;
		String salt = SHA256.generateSalt();
		
		if(request.getParameter("id") != null) {
			id = request.getParameter("id");
		}
		if (request.getParameter("password") != null) {
			password = request.getParameter("password");
			// 패스워드를 SHA256 으로 해시하기
			password = SHA256.getEncrypt(password, salt);
		}
		if (request.getParameter("username") != null) {
			username = request.getParameter("username");
		}
		if (request.getParameter("roadFullAddr") != null) {
			roadFullAddr = request.getParameter("roadFullAddr");
		}
		if (request.getParameter("email") != null) {
			email = request.getParameter("email");
		}
		
		member.setId(id);
		member.setPassword(password);
		member.setUsername(username);
		member.setRoadFullAddr(roadFullAddr);
		member.setEmail(email);
		member.setSalt(salt);
		
		
		int result = dao.insert(member);
		
		if (result == 1) {
			HttpSession session = request.getSession();
			session.setAttribute("id",member.getId());
			Script.moving(response, "구글인증페이지", url);
			
		}else if (result == -1) {
			Script.moving(response, "DB 에러");
		}
	}
}
