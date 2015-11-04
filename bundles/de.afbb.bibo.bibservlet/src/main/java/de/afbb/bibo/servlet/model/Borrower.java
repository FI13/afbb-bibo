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
public class Borrower {
    private int id;
    private String forename;
    private String surname;
    private String info;
    private String email;
    private String phoneNumber;

    public Borrower(int id, String forename, String surname, String info, String email, String phoneNumber) {
        this.id = id;
        this.forename = forename;
        this.surname = surname;
        this.info = info;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public String getForename() {
        return forename;
    }

    public String getSurname() {
        return surname;
    }

    public String getInfo() {
        return info;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return "Borrower{" + "id=" + id + ", forename=" + forename + ", surname=" + surname + ", info=" + info + ", email=" + email + ", phoneNumber=" + phoneNumber + '}';
    }
}
