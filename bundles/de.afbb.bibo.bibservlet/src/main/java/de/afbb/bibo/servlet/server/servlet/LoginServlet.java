/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.afbb.bibo.servlet.server.servlet;

import de.afbb.bibo.servlet.db.DBConnector;
import de.afbb.bibo.servlet.server.SessionContainer;
import de.afbb.bibo.servlet.server.Utils;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author fi13.pendrulat
 */
public class LoginServlet {

    HttpServletRequest request;
    HttpServletResponse response;
    private static final Logger log = LoggerFactory.getLogger(LoginServlet.class);

    protected LoginServlet(
            HttpServletRequest request,
            HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    protected void processRequest() throws Exception {
        String loginAction = Utils.getRequestPart(request, 1);
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
        String userName = request.getParameter("name");
        String salt = DBConnector.getInstance().requestSalt(userName);
        response.getWriter().write(salt);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void doLogin() throws SQLException, IOException {
        String userName = request.getParameter("name");
        String password = request.getParameter("hash");
        if (DBConnector.getInstance().checkPassword(userName, password)) {
            response.getWriter().println(SessionContainer.getInstance().createNewConnection(userName));
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
    
    private void doLogout() {
        String token = request.getHeader("sessionId");
        SessionContainer.getInstance().invalidate(token);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
