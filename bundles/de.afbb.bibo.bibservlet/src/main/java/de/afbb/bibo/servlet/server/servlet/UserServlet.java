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

import de.afbb.bibo.servlet.db.DBConnector;
import de.afbb.bibo.servlet.server.Utils;
import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.share.model.Curator;

/**
 *
 * @author fi13.pendrulat
 * @author David Becker
 */
public class UserServlet {

	HttpServletRequest request;
	HttpServletResponse response;

	private static final Logger log = LoggerFactory.getLogger(UserServlet.class);

	protected UserServlet(final HttpServletRequest request, final HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	protected void processRequest() throws SQLException, IOException {
		final String userAction = Utils.getRequestPart(request, 1);
		log.debug("entering USER Servlet...");

		switch (userAction) {
		case "/newCurator":
			addCurator();
			break;
		case "/getCurator":
			getCurator();
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
		case "/getBorrowers":
			getBorrowers();
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
		final Curator curator = Utils.gson.fromJson(request.getReader(), Curator.class);

		DBConnector.getInstance().createCurator(curator);
		response.setStatus(HttpServletResponse.SC_OK);
	}

	private void getCurator() throws SQLException, IOException {
		final String name = request.getParameter("name");
		final Curator curator = DBConnector.getInstance().getCurator(name);
		if (curator != null) {
			response.getWriter().println(Utils.gson.toJson(curator));
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	private void hasCurator() throws SQLException, IOException {
		final String curatorName = request.getParameter("name");
		if (DBConnector.getInstance().getCuratorId(curatorName) != -1) {
			response.getWriter().println(1);
		} else {
			response.getWriter().println(0);
		}
		response.setStatus(HttpServletResponse.SC_OK);
	}

	private void updateCurator() throws SQLException, IOException {
		final Curator curator = Utils.gson.fromJson(request.getReader(), Curator.class);

		DBConnector.getInstance().updateCurator(curator);
		response.setStatus(HttpServletResponse.SC_OK);
	}

	private void addBorrower() throws SQLException, IOException {
		final Borrower borrower = Utils.gson.fromJson(request.getReader(), Borrower.class);

		if (DBConnector.getInstance().createBorrower(borrower) != -1) {
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	private void hasBorrower() throws IOException, NumberFormatException, SQLException {
		final String fName = request.getParameter("forename");
		final String sName = request.getParameter("surname");

		if (DBConnector.getInstance().getBorrowerId(fName, sName) != -1) {
			response.getWriter().println(1);
		} else {
			response.getWriter().println(0);
		}
		response.setStatus(HttpServletResponse.SC_OK);

	}

	private void getBorrower() throws SQLException, IOException {
		final Integer id = Integer.valueOf(request.getParameter("id"));
		Borrower borrower;
		borrower = DBConnector.getInstance().getBorrower(id);
		if (borrower != null) {
			response.getWriter().println(Utils.gson.toJson(borrower));
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private void getBorrowers() throws SQLException, IOException {
		final List<Borrower> borrowers = DBConnector.getInstance().getBorrowers();
		for (final Borrower borrower : borrowers) {
			final String json = Utils.gson.toJson(borrower);
			response.getWriter().println(json);
		}
		response.setStatus(HttpServletResponse.SC_OK);
	}

	private void updateBorrower() throws SQLException, IOException {
		final Borrower b = Utils.gson.fromJson(request.getReader(), Borrower.class);

		DBConnector.getInstance().updateBorrower(b);
		;
		response.setStatus(HttpServletResponse.SC_OK);
	}

	private void deleteBorrower() throws SQLException, IOException {
		final Borrower b = Utils.gson.fromJson(request.getReader(), Borrower.class);

		DBConnector.getInstance().deleteBorrower(b.getId());
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
