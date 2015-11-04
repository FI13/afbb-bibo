/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.afbb.bibo.servlet.model;

/**
 *
 * @author fi13.pendrulat
 */
public class Curator {
    private String id;
    private String name;
    private String salt;
    private String passwordHash;

    public Curator(String id, String name, String salt, String passwordHash) {
        this.id = id;
        this.name = name;
        this.salt = salt;
        this.passwordHash = passwordHash;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSalt() {
        return salt;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    @Override
    public String toString() {
        return "Curator{" + "id=" + id + ", name=" + name + ", salt=" + salt + ", passwordHash=" + passwordHash + '}';
    }
    
}
