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
import com.google.gson.GsonBuilder;

import de.afbb.bibo.servlet.db.DBConnector;
import de.afbb.bibo.servlet.server.Utils;
import de.afbb.bibo.share.beans.BeanExclusionStrategy;
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
	Gson gson;
	private static final Logger log = LoggerFactory.getLogger(StockServlet.class);

	protected StockServlet(final HttpServletRequest request, final HttpServletResponse response) {
		this.request = request;
		this.response = response;
		gson = new GsonBuilder().addSerializationExclusionStrategy(new BeanExclusionStrategy()).create();
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
		case "/addCopies":
			addCopyGroup();
			break;
		case "/listMedia":
			listMedia();
			break;
		case "/getMedium":
			getMedium();
			break;
		default:
			Utils.returnErrorMessage(StockServlet.class, request, response);
			break;
		}
	}

	private void addMediaType() throws IOException, SQLException {
		final MediumType type = gson.fromJson(request.getReader(), MediumType.class);
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
			response.getWriter().println(gson.toJson(mediumType));
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private void listMediaTypes() throws IOException, SQLException {
		final List<MediumType> mediaTypes = DBConnector.getInstance().getMediumTypes();
		for (final MediumType mediumType : mediaTypes) {
			response.getWriter().println(gson.toJson(mediumType));
		}
		response.setStatus(HttpServletResponse.SC_OK);
	}

	private void addCopyGroup() throws IOException, SQLException {
		final Copy[] copies = gson.fromJson(request.getReader(), Copy[].class);
		for (final Copy copy : copies) {
			if (DBConnector.getInstance().getMedium(copy.getMedium().getIsbn()).getMediumId() == null) {
				// medium not found, create new
				copy.getMedium()
						.setMediumId(DBConnector.getInstance().createMedium(copy.getMedium().getIsbn(),
								copy.getMedium().getTitle(), copy.getMedium().getAuthor(),
								copy.getMedium().getLanguage(), copy.getMedium().getType().getId()));
			}
		}
		final int groupId = DBConnector.getInstance().createCopyGroup(copies);
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println(groupId);
		response.setContentType("text/plain");
	}

	private void getMedium() throws IOException, SQLException {
		final String isbn = request.getParameter("ISBN");
		Medium medium;
		medium = DBConnector.getInstance().getMedium(isbn);
		if (medium != null) {
			response.getWriter().println(gson.toJson(medium));
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private void listMedia() throws NumberFormatException, SQLException, IOException {
		final List<Medium> media = DBConnector.getInstance().getMedium();
		for (final Medium medium : media) {
			if (medium != null) {
				response.getWriter().println(gson.toJson(medium));
			}
		}
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
