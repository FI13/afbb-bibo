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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import de.afbb.bibo.servlet.db.DBConnector;
import de.afbb.bibo.servlet.server.Utils;
import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.share.model.Medium;
import de.afbb.bibo.share.model.MediumType;

/**
 *
 * @author fi13.pendrulat
 */
public class StockServlet {

    HttpServletRequest request;
    HttpServletResponse response;
    Gson gson;
    private static final Logger log = LoggerFactory.getLogger(StockServlet.class);

    protected StockServlet(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.gson = new Gson();
    }

    protected void processRequest()
            throws Exception {
        String stockAction = Utils.getRequestPart(request, 1);
        log.debug("entering STOCK Servlet...");

        switch (stockAction) {
            case "/addMediaType":
                addMediaType();
                break;
            case "/addCopies":
                addCopyGroup();
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
        MediumType type = gson.fromJson(request.getReader(), MediumType.class);
        int mediumId = DBConnector.getInstance().createMediumType(type.getName(), type.getIconPath());
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(mediumId);
        response.setContentType("text/plain");
    }

    private void addCopyGroup() throws IOException, SQLException {
        Copy[] copies = gson.fromJson(request.getReader(), Copy[].class);
        for (Copy copy : copies) {
            try {
                DBConnector.getInstance().getMedium(copy.getIsbn()).getMediumId();
            } catch (SQLException ex) {
                //medium not found, create new
                copy.setMediumId(
                        DBConnector.getInstance().createMedium(
                                copy.getIsbn(), 
                                copy.getTitle(), 
                                copy.getAuthor(), 
                                copy.getLanguage(), 
                                copy.getTypeId()
                        )
                );
            }
        }
        int groupId = DBConnector.getInstance().createCopyGroup(copies);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(groupId);
        response.setContentType("text/plain");
    }
    
    private void getMedium() throws IOException, SQLException {
        String isbn = request.getParameter("ISBN");
        Medium medium;
        try {
            medium = DBConnector.getInstance().getMedium(isbn);
            response.getWriter().println(gson.toJson(medium));
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException ex) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
