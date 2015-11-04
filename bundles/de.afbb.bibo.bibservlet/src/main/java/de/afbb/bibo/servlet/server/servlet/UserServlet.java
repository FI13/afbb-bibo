/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.afbb.bibo.servlet.server.servlet;

import com.google.gson.Gson;
import de.afbb.bibo.servlet.db.DBConnector;
import de.afbb.bibo.servlet.server.Utils;
import de.afbb.bibo.servlet.model.Borrower;
import de.afbb.bibo.servlet.model.Curator;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author fi13.pendrulat
 */
public class UserServlet {

    HttpServletRequest request;
    HttpServletResponse response;
    Gson gson;
    private static final Logger log = LoggerFactory.getLogger(UserServlet.class);

    protected UserServlet(
            HttpServletRequest request,
            HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.gson = new Gson();
    }

    protected void processRequest()
            throws Exception {
        String userAction = Utils.getRequestPart(request, 1);
        log.debug("entering USER Servlet...");

        switch (userAction) {
            case "/changePassword":
                changePassword();
                break;
            case "/newCurator":
                addCurator();
                break;
            case "/curatorExists":
                hasCurator();
                break;
            case "/newBorrower":
                addBorrower();
                break;
            case "/getBorrower":
                getBorrower();
                break;
            default:
                Utils.returnErrorMessage(UserServlet.class, request, response);
                break;

        }
    }

    private void changePassword() throws SQLException, IOException {
        String userName = request.getParameter("name");
        String passwordHash = request.getParameter("oldHash");
        String newPasswordHash = request.getParameter("newHash");
        if (DBConnector.getInstance().checkPassword(userName, passwordHash)) {
            DBConnector.getInstance().updateUser(userName, newPasswordHash);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private void addCurator() throws SQLException, IOException {
        Curator curator = gson.fromJson(request.getReader(), Curator.class);

        DBConnector.getInstance().createCurator(curator);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void hasCurator() throws SQLException, IOException {
        String curatorName = request.getParameter("name");
        try {
            DBConnector.getInstance().getCuratorId(curatorName);
            response.getWriter().println(1);
        } catch (SQLException ex) {
            response.getWriter().println(0);
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void addBorrower() throws SQLException, IOException {
        Borrower borrower = gson.fromJson(request.getReader(), Borrower.class);

        DBConnector.getInstance().createBorrower(borrower);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void getBorrower() throws SQLException, IOException {
        List<Borrower> borrowers = DBConnector.getInstance().getBorrower();
        response.getWriter().println(gson.toJson(borrowers));
        response.setStatus(HttpServletResponse.SC_OK);

    }
}
