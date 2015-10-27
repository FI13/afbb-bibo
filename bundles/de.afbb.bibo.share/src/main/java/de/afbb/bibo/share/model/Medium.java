package de.afbb.bibo.share.model;

public class Medium {
	private Integer id;
	private String isbn;
	private String titel;
	private String author;
	private String language;
	private Typ typus;
	
	
	public Typ getTypus() {
		return typus;
	}
	public void setTypus(Typ typus) {
		this.typus = typus;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getTitel() {
		return titel;
	}
	public void setTitel(String titel) {
		this.titel = titel;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}

	

}
