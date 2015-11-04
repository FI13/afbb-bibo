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
public class Medium {
    
    private int id;
    private String isbn;
    private String title;
    private String author;
    private String language;
    private int typeId;
    private String publisher;

    public Medium(int id, String isbn, String title, String author, String language, int typeId, String publisher) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.language = language;
        this.typeId = typeId;
        this.publisher = publisher;
    }
    

    public int getMediumId() {
        return id;
    }
    
    public void setMediumId(int id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getLanguage() {
        return language;
    }

    public int getTypeId() {
        return typeId;
    }

    public String getPublisher() {
        return publisher;
    }

    @Override
    public String toString() {
        return "Medium{" + "id=" + id + ", isbn=" + isbn + ", title=" + title + ", author=" + author + ", language=" + language + ", typeId=" + typeId + ", publisher=" + publisher + '}';
    }
}
