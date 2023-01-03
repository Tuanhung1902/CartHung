package com.trungtamjava.controller.client;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.trungtamjava.dao.BillDao;
import com.trungtamjava.dao.BillProductDao;
import com.trungtamjava.dao.impl.BillDaoImpl;
import com.trungtamjava.dao.impl.BillProductDaoImpl;
import com.trungtamjava.model.Bill;
import com.trungtamjava.model.BillProduct;
import com.trungtamjava.model.User;

@WebServlet(urlPatterns = "/member/add-order")
public class AddOrderController extends HttpServlet {
	BillDao billDao = new BillDaoImpl();
	BillProductDao billProductDao = new BillProductDaoImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession();
		Object obj = session.getAttribute("cart");

		if (obj != null) {
			Map<String, BillProduct> map = (Map<String, BillProduct>) obj;


			Bill bill = new Bill();

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			bill.setBuyDate(sdf.format(new Date()));


			User buyer = (User) session.getAttribute("loginUser");
			bill.setBuyer(buyer);

			billDao.create(bill);

			long total = 0;
			

			for (Entry<String, BillProduct> entry : map.entrySet()) {
				BillProduct billProduct = entry.getValue();
				
				billProduct.setBill(bill);

				billProductDao.create(billProduct);

				total += billProduct.getQuantity() * billProduct.getUnitPrice();
			}
			

			bill.setPriceTotal(total);
			billDao.update(bill);


			session.removeAttribute("cart");
			resp.sendRedirect(req.getContextPath() + "/member/bills");
		} else {

			resp.sendRedirect(req.getContextPath() + "/client/product/search");
		}

	}
}
