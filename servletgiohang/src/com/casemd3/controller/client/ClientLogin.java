package com.trungtamjava.controller.client;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.trungtamjava.dao.UserDao;
import com.trungtamjava.dao.impl.UserDaoImpl;
import com.trungtamjava.model.User;

@WebServlet(urlPatterns = "/login-member") //
public class ClientLoginController extends HttpServlet {
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String code = req.getParameter("errCode");

		if (code != null && code.equals("100")) {
			req.setAttribute("msg", "Tai khoan hoac mat khau sai");
		}
		RequestDispatcher dispathcer = req.getRequestDispatcher("/views/client/login.jsp");
		dispathcer.forward(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String username = req.getParameter("username");
		String pass = req.getParameter("password");

		UserDao userDao = new UserDaoImpl();
		User user = userDao.getByUsername(username);

		if (user != null && user.getPassword().equals(pass)) {
			System.out.println("Log in success");

			if (user.getLoginCounter() < 10) {

				HttpSession session = req.getSession();

				session.setAttribute("loginUser", user);


				resp.sendRedirect(req.getContextPath() + "/client/product/search");
			} else {
				System.out.println("Da dang nhap cho khac roi");
				resp.sendRedirect(req.getContextPath() + "/login-member?errCode=400");
			}

		} else {
			System.out.println("Fail");
			resp.sendRedirect(req.getContextPath() + "/login-member?errCode=100");
		}
	}
}
