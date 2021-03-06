/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.afbb.bibo.servlet.server.servlet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.afbb.bibo.servlet.db.DBConnector;
import de.afbb.bibo.servlet.server.SessionContainer;
import de.afbb.bibo.servlet.server.Utils;
import de.afbb.bibo.share.model.Curator;

/**
 *
 * @author fi13.pendrulat
 */
public class LoginServlet {

	HttpServletRequest request;
	HttpServletResponse response;
	private static final Logger log = LoggerFactory.getLogger(LoginServlet.class);

	protected LoginServlet(final HttpServletRequest request, final HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	protected void processRequest() throws SQLException, IOException {
		final String loginAction = Utils.getRequestPart(request, 1);
		log.debug("entering LOGIN Servlet...");

		response.setContentType("text/plain");
		switch (loginAction) {
		case "/requestSalt":
			requestSalt();
			break;
		case "/login":
			doLogin();
			break;
		case "/logout":
			doLogout();
			break;
		default:
			Utils.returnErrorMessage(LoginServlet.class, request, response);
			break;
		}
	}

	private void requestSalt() throws SQLException, IOException {
		final String userName = request.getParameter("name");
		final String salt = DBConnector.getInstance().requestSalt(userName);
		if (salt != null) {
			response.getWriter().write(salt);
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private void doLogin() throws SQLException, IOException {
		final String userName = request.getParameter("name");
		final String password = request.getParameter("hash");
		try {
			if (DBConnector.getInstance().checkPassword(userName, password)) {
				response.getWriter().println(SessionContainer.getInstance().createNewConnection(userName));
				final Curator curator = DBConnector.getInstance().getCurator(userName);
				if (curator != null) {
					response.getWriter().println(Utils.gson.toJson(curator));
					response.setStatus(HttpServletResponse.SC_OK);
				} else {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
			} else {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}
		} catch (final FileNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	private void doLogout() {
		final String token = request.getHeader("sessionId");
		log.debug("invalidate session: " + token);
		SessionContainer.getInstance().invalidate(token);
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
