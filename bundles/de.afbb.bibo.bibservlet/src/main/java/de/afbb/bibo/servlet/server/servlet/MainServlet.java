/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.afbb.bibo.servlet.server.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.afbb.bibo.servlet.server.SessionContainer;
import de.afbb.bibo.servlet.server.Utils;

/**
 *
 * @author philipp
 */
@MultipartConfig
public class MainServlet extends HttpServlet {

	private static final long serialVersionUID = -4080327750481043213L;
	private static final Logger log = LoggerFactory.getLogger(MainServlet.class);

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 *
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws NumberFormatException
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws java.lang.InterruptedException
	 * @throws org.apache.commons.fileupload.FileUploadException
	 */
	protected void processRequest(final HttpServletRequest request, final HttpServletResponse response)
			throws NumberFormatException, IOException {
		boolean valid = true;
		final String requestRoot = Utils.getRequestPart(request, 0);
		log.debug("entering MAIN Servlet...");

		if (!requestRoot.equals("/login")) {
			valid = SessionContainer.getInstance().validate(request.getHeader("sessionId"));
		}
		log.info("new request: " + request.getRequestURI());
		if (!valid) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} else {

			response.setContentType("application/json");

			try {
				switch (requestRoot) {
				case "/login":
					new LoginServlet(request, response).processRequest();
					break;
				case "/user":
					new UserServlet(request, response).processRequest();
					break;
				case "/stock":
					new StockServlet(request, response).processRequest();
					break;
				case "/borrow":
					new BorrowServlet(request, response).processRequest();
					break;
				default:
					Utils.returnErrorMessage(MainServlet.class, request, response);
					break;
				}
			} catch (final SQLException | IOException e) {
				log.debug(e.getMessage());
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		}
	}

	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			processRequest(request, response);
		} catch (final NumberFormatException | IOException e) {
			log.debug(e.getMessage());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			processRequest(request, response);
		} catch (final NumberFormatException | IOException e) {
			log.debug(e.getMessage());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

}
