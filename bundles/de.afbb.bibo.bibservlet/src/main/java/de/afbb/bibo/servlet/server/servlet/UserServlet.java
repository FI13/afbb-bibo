/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.afbb.bibo.servlet.server.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import de.afbb.bibo.servlet.db.DBConnector;
import de.afbb.bibo.servlet.server.Utils;
import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.share.model.Curator;

/**
 *
 * @author fi13.pendrulat
 */
public class UserServlet {

	HttpServletRequest request;
	HttpServletResponse response;
	Gson gson;
	private static final Logger log = LoggerFactory.getLogger(UserServlet.class);

	protected UserServlet(final HttpServletRequest request, final HttpServletResponse response) {
		this.request = request;
		this.response = response;
		this.gson = new Gson();
	}

	protected void processRequest() throws SQLException, IOException {
		final String userAction = Utils.getRequestPart(request, 1);
		log.debug("entering USER Servlet...");

		switch (userAction) {
		case "/newCurator":
			addCurator();
			break;
		case "/curatorExists":
			hasCurator();
			break;
		case "/updateCurator":
			updateCurator();
			break;
		case "/newBorrower":
			addBorrower();
			break;
		case "/getBorrower":
			getBorrower();
			break;
		case "/borrowerExists":
			hasBorrower();
			break;
		case "/updateBorrower":
			updateBorrower();
			break;
		case "/deleteBorrower":
			deleteBorrower();
			break;
		default:
			Utils.returnErrorMessage(UserServlet.class, request, response);
			break;

		}
	}

	private void addCurator() throws SQLException, IOException {
		final Curator curator = gson.fromJson(request.getReader(), Curator.class);

		DBConnector.getInstance().createCurator(curator);
		response.setStatus(HttpServletResponse.SC_OK);
	}

	private void hasCurator() throws SQLException, IOException {
		final String curatorName = request.getParameter("name");
		try {
			DBConnector.getInstance().getCuratorId(curatorName);
			response.getWriter().println(1);
		} catch (final SQLException ex) {
			response.getWriter().println(0);
		}
		response.setStatus(HttpServletResponse.SC_OK);
	}

	private void updateCurator() throws SQLException, IOException {
		final Curator curator = gson.fromJson(request.getReader(), Curator.class);

		DBConnector.getInstance().updateCurator(curator);
		response.setStatus(HttpServletResponse.SC_OK);
	}

	private void addBorrower() throws SQLException, IOException {
		final Borrower borrower = gson.fromJson(request.getReader(), Borrower.class);

		DBConnector.getInstance().createBorrower(borrower);
		response.setStatus(HttpServletResponse.SC_OK);
	}

	private void hasBorrower() throws SQLException, IOException {
		final String fName = request.getParameter("forename");
		final String sName = request.getParameter("surname");
		try {
			DBConnector.getInstance().getBorrowerId(fName, sName);
			response.getWriter().println(1);
		} catch (final SQLException ex) {
			response.getWriter().println(0);
		}
		response.setStatus(HttpServletResponse.SC_OK);
	}

	private void getBorrower() throws SQLException, IOException {
		final List<Borrower> borrowers = DBConnector.getInstance().getBorrower();
		response.getWriter().println(gson.toJson(borrowers));
		response.setStatus(HttpServletResponse.SC_OK);
	}

	private void updateBorrower() throws SQLException, IOException {
		final Borrower b = gson.fromJson(request.getReader(), Borrower.class);

		DBConnector.getInstance().updateBorrower(b);
		;
		response.setStatus(HttpServletResponse.SC_OK);
	}

	private void deleteBorrower() throws SQLException, IOException {
		final Borrower b = gson.fromJson(request.getReader(), Borrower.class);

		DBConnector.getInstance().deleteBorrower(b.getId());
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
