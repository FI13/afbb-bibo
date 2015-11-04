/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.afbb.bibo.servlet.server.servlet;

import com.google.gson.Gson;
import de.afbb.bibo.servlet.db.DBConnector;
import de.afbb.bibo.servlet.server.SessionContainer;
import de.afbb.bibo.servlet.server.Utils;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author fi13.pendrulat
 */
public class BorrowServlet {
    HttpServletRequest request;
    HttpServletResponse response;
    Gson gson;

    protected BorrowServlet(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.gson = new Gson();
    }

    protected void processRequest()
            throws Exception {
        String stockAction = Utils.getRequestPart(request, 1);

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
        int curatorId = SessionContainer.getInstance().getSession(request.getHeader("sessionId")).getId();
        String barcode = request.getParameter("barcode");
        int borrowerId = Integer.valueOf(request.getParameter("borrower"));
        String condition = request.getParameter("condition");
        
        DBConnector.getInstance().setNewBorrower(barcode, borrowerId, curatorId);
        if (condition != null) {
            DBConnector.getInstance().setCondition(barcode, condition);
        }
    }
    
    private void doReturn() throws SQLException, IOException {
        
    }
}
