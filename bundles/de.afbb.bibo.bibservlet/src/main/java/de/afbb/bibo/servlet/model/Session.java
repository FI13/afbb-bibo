/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.afbb.bibo.servlet.model;

import de.afbb.bibo.servlet.db.DBConnector;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fi13.pendrulat
 */
public class Session {
    String name;
    int id;
    Calendar date;

    public Session(String name, Calendar date) {
        this.name = name;
        this.date = date;
        try {
            this.id = DBConnector.getInstance().getCuratorId(name);
        } catch (SQLException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Calendar getDate() {
        return date;
    }
}
