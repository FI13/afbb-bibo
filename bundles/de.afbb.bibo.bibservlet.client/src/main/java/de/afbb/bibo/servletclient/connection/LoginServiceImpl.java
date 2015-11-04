/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.afbb.bibo.servletclient.connection;

import de.afbb.bibo.servletclient.interfaces.ILoginService;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author fi13.pendrulat
 */
public class LoginServiceImpl implements ILoginService {

    @Override
    public String requestSaltForUserName(String userName) throws ConnectException {
        Map<String, String> params = new HashMap();
        params.put("name", userName);

        HttpResponse resp
                = ServerConnection.getInstance().request("/login/requestSalt", "GET", params, null);
        if (resp.getStatus() == HttpServletResponse.SC_OK) {
            return resp.getData();
        } else {
            throw new ConnectException("Wrong status code. Recieved was: " + resp.getStatus());
        }
    }

    @Override
    public boolean loginWithHash(String userName, String hashedPassword) throws ConnectException {
        return ServerConnection.getInstance().login(userName, hashedPassword);

    }

    @Override
    public void invalidateSession() {
        try {
            ServerConnection.getInstance().logout();
        } catch (ConnectException ex) {
            Logger.getLogger(LoginServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
