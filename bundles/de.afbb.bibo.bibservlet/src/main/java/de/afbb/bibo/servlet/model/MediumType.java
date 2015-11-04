/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.afbb.bibo.servlet.model;

import java.sql.Blob;

/**
 *
 * @author fi13.pendrulat
 */
public class MediumType {
    private String id;
    private String name;
    private String iconPath;

    public MediumType(String id, String name, String iconPath) {
        this.id = id;
        this.name = name;
        this.iconPath = iconPath;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return iconPath;
    }

    @Override
    public String toString() {
        return "MediumType{" + "id=" + id + ", name=" + name + ", iconPath=" + iconPath + '}';
    }
    
}
