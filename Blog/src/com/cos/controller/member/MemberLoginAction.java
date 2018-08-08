package com.cos.controller.member;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cos.action.Action;
import com.cos.dao.MemberDAO;
import com.cos.dto.MemberVO;
import com.cos.util.SHA256;
import com.cos.util.Script;

public class MemberLoginAction implements Action {
	private static String naming = "MemberLoginAction : ";
	
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "/Blog/main.jsp";
		
		MemberVO member = new MemberVO();
		MemberDAO dao = new MemberDAO();
		
		String id = null;
		String password = null;
		String salt = null;
		
		if(request.getParameter("id") != null) {
			id = request.getParameter("id");
		}
		
		if (request.getParameter("password") != null) {
			password = request.getParameter("password");
			salt = dao.select_salt(id);
			// 패스워드를 SHA256 으로 해시하기
			password = SHA256.getEncrypt(password, salt);
		}
		
		member.setId(id);
		member.setPassword(password);
		int result = dao.login(id, password);
		
		if (result == 1) {
			HttpSession session = request.getSession();
			session.setAttribute("id",member.getId());
			Script.moving(response, "환영합니다", url);
		} else if (result == 2) {
			Script.moving(response, "회원정보를 찾을수 없습니다");
			
		}else if (result == -1) {
			Script.moving(response, "DB 에러");
		}
	}

}
