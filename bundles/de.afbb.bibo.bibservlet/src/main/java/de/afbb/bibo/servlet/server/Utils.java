package de.afbb.bibo.servlet.server;


import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author fi13.pendrulat
 */
public class Utils {
    
    public static int nthOccurrence(String str, String toFind, int n) {
        int pos = str.indexOf(toFind, 0);
        while (n-- > 0 && pos != -1) {
            pos = str.indexOf(toFind, pos + 1);
        }
        return pos;
    }
    
    public static String getRequestPart(HttpServletRequest request, int part) {
        String[] requestParts = request.getRequestURI().split("/");
        return "/" + (requestParts.length > 1 ? requestParts[part + 1] : "");
    }
    
    public static void returnErrorMessage(Class servlet, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.getWriter().println("the servlet: " + servlet.getSimpleName() + " could not serve your request: " + request.getRequestURI());
    }
}
