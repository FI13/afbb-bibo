/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.afbb.bibo.servlet.server.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.afbb.bibo.servlet.db.DBConnector;
import de.afbb.bibo.servlet.server.Utils;
import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.share.model.Medium;
import de.afbb.bibo.share.model.MediumType;

/**
 * @author fi13.pendrulat
 * @author David Becker
 */
public class StockServlet {

	HttpServletRequest request;
	HttpServletResponse response;
	private static final Logger log = LoggerFactory.getLogger(StockServlet.class);

	protected StockServlet(final HttpServletRequest request, final HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	protected void processRequest() throws IOException, SQLException {
		final String stockAction = Utils.getRequestPart(request, 1);
		log.debug("entering STOCK Servlet...");

		switch (stockAction) {
		case "/addMediaType":
			addMediaType();
			break;
		case "/getMediaType":
			getMediaType();
			break;
		case "/listMediaTypes":
			listMediaTypes();
			break;
		case "/existCopy":
			existCopy();
			break;
		case "/addCopies":
			addCopies();
			break;
		case "/addGroupedCopies":
			addGroupedCopies();
			break;
		case "/getCopy":
			getCopy();
			break;
		case "/listMedia":
			listMedia();
			break;
		case "/countLendCopies":
			countLendCopiesByBorrower();
			break;
		case "/listLendCopies":
			listLendCopies();
			break;
		case "/getMedium":
			getMedium();
			break;
		case "/getMediumInformation":
			aggregateCopiesInformationByMedium();
			break;
		default:
			Utils.returnErrorMessage(StockServlet.class, request, response);
			break;
		}
	}

	private void addMediaType() throws IOException, SQLException {
		final MediumType type = Utils.gson.fromJson(request.getReader(), MediumType.class);
		final int mediumId = DBConnector.getInstance().createMediumType(type.getName(), type.getIcon().getCode());
		response.setStatus(mediumId != -1 ? HttpServletResponse.SC_OK : HttpServletResponse.SC_NOT_FOUND);
		response.getWriter().println(mediumId);
		response.setContentType("text/plain");
	}

	private void getMediaType() throws IOException, SQLException {
		final String id = request.getParameter("id");
		MediumType mediumType;
		mediumType = DBConnector.getInstance().getMediumType(id);
		if (mediumType != null) {
			response.getWriter().println(Utils.gson.toJson(mediumType));
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private void listMediaTypes() throws IOException, SQLException {
		final List<MediumType> mediaTypes = DBConnector.getInstance().getMediumTypes();
		for (final MediumType mediumType : mediaTypes) {
			response.getWriter().println(Utils.gson.toJson(mediumType));
		}
		response.setStatus(HttpServletResponse.SC_OK);
	}

	private void addCopies() throws IOException, NumberFormatException, SQLException {
		final Copy[] copies = setMediumInformation();
		DBConnector.getInstance().createCopies(copies);
		response.setStatus(HttpServletResponse.SC_OK);
	}

	private void addGroupedCopies() throws IOException, SQLException {
		final Copy[] copies = setMediumInformation();
		final int groupId = DBConnector.getInstance().createCopyGroup(copies);
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println(groupId);
		response.setContentType("text/plain");
	}

	private Copy[] setMediumInformation() throws NumberFormatException, SQLException, IOException {
		final Copy[] copies = Utils.gson.fromJson(request.getReader(), Copy[].class);
		final Map<String, Integer> checkedIsbns = new HashMap<String, Integer>();
		// avoid allocation inside loop
		String isbn;
		Integer mediumId;
		for (final Copy copy : copies) {
			isbn = copy.getMedium().getIsbn();
			// try to get id from cache first
			if (checkedIsbns.containsKey(isbn)) {
				mediumId = checkedIsbns.get(isbn);
			} else {
				// try to get id from database
				final Medium medium = DBConnector.getInstance().getMedium(isbn);
				mediumId = medium != null ? medium.getMediumId() : null;
				if (mediumId == null) {
					// medium not found, create new
					mediumId = DBConnector.getInstance().createMedium(isbn, copy.getMedium().getTitle(),
							copy.getMedium().getAuthor(), copy.getMedium().getLanguage(),
							copy.getMedium().getType().getId());
				}
				checkedIsbns.put(isbn, mediumId);
			}
			copy.getMedium().setMediumId(mediumId);
		}
		return copies;
	}

	private void getCopy() throws IOException, SQLException {
		final String barcode = request.getParameter("barcode");
		Copy copy;
		copy = DBConnector.getInstance().getCopy(barcode);
		if (copy != null) {
			response.getWriter().println(Utils.gson.toJson(copy));
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private void getMedium() throws IOException, SQLException {
		final String isbn = request.getParameter("ISBN");
		Medium medium;
		medium = DBConnector.getInstance().getMedium(isbn);
		if (medium != null) {
			response.getWriter().println(Utils.gson.toJson(medium));
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private void listMedia() throws NumberFormatException, SQLException, IOException {
		final List<Medium> media = DBConnector.getInstance().getMedium();
		for (final Medium medium : media) {
			if (medium != null) {
				response.getWriter().println(Utils.gson.toJson(medium));
			}
		}
		response.setStatus(HttpServletResponse.SC_OK);
	}

	private void existCopy() throws NumberFormatException, SQLException, IOException {
		final String barcode = request.getParameter("barcode");
		final boolean exists = DBConnector.getInstance().existsCopy(barcode);
		response.getWriter().println(exists ? 1 : 0);
		response.setStatus(HttpServletResponse.SC_OK);
	}

	private void listLendCopies() throws NumberFormatException, SQLException, IOException {
		final Integer id = Integer.valueOf(request.getParameter("id"));
		final List<Copy> copies = DBConnector.getInstance().listLendCopies(id);
		for (final Copy copy : copies) {
			if (copy != null) {
				response.getWriter().println(Utils.gson.toJson(copy));
			}
		}
		response.setStatus(HttpServletResponse.SC_OK);
	}

	private void countLendCopiesByBorrower() throws NumberFormatException, SQLException, IOException {
		final Integer id = Integer.valueOf(request.getParameter("id"));
		response.getWriter().println(DBConnector.getInstance().countLendCopiesByBorrower(id));
		response.setStatus(HttpServletResponse.SC_OK);
	}

	private void aggregateCopiesInformationByMedium() throws NumberFormatException, SQLException, IOException {
		final Integer id = Integer.valueOf(request.getParameter("id"));
		response.getWriter().println(DBConnector.getInstance().countCopiesByMedium(id));
		response.getWriter().println(DBConnector.getInstance().countLendCopiesByMedium(id));
		response.setStatus(HttpServletResponse.SC_OK);
	}

}
