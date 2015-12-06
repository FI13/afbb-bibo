/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.afbb.bibo.servlet.server.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.afbb.bibo.servlet.db.DBConnector;
import de.afbb.bibo.servlet.server.SessionContainer;
import de.afbb.bibo.servlet.server.Utils;

/**
 *
 * @author fi13.pendrulat
 */
public class BorrowServlet {

	HttpServletRequest request;
	HttpServletResponse response;

	protected BorrowServlet(final HttpServletRequest request, final HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	protected void processRequest() throws SQLException, IOException {
		final String stockAction = Utils.getRequestPart(request, 1);

		switch (stockAction) {
		case "/doBorrow":
			doBorrow();
			break;
		case "/return":
			doReturn();
			break;
		}
	}

	private void doBorrow() throws SQLException, IOException {
		final int curatorId = SessionContainer.getInstance().getSession(request.getHeader("sessionId")).getId();
		final String barcode = request.getParameter("barcode");
		final int borrowerId = Integer.valueOf(request.getParameter("borrower"));
		final String condition = request.getParameter("condition");

		DBConnector.getInstance().setNewBorrower(barcode, borrowerId, curatorId);
		if (condition != null) {
			DBConnector.getInstance().setCondition(barcode, condition);
		}
		response.setStatus(HttpServletResponse.SC_OK);
	}

	private void doReturn() throws SQLException, IOException {
		final int curatorId = SessionContainer.getInstance().getSession(request.getHeader("sessionId")).getId();
		final String barcode = request.getParameter("barcode");
		final String condition = request.getParameter("condition");

		DBConnector.getInstance().returnBook(barcode, curatorId);
		if (condition != null) {
			DBConnector.getInstance().setCondition(barcode, condition);
		}
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
