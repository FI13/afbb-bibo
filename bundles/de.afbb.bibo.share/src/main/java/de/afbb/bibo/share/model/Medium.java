package de.afbb.bibo.share.model;

public class Medium {
	private Integer id;
	private String isbn;
	private String title;
	private String author;
	private String language;
	private Integer typId;
	private String publisher;

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public Integer getTypId() {
		return typId;
	}

	public void setTypId(final Integer typId) {
		this.typId = typId;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(final String publisher) {
		this.publisher = publisher;
	}

	public Integer getId() {
		return id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(final String isbn) {
		this.isbn = isbn;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(final String author) {
		this.author = author;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(final String language) {
		this.language = language;
	}

}
