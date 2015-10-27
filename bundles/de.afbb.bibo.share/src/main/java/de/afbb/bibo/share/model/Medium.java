package de.afbb.bibo.share.model;

public class Medium {
	private Integer id;
	private String isbn;
	private String titel;
	private String Autor;
	private String Sprache;

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

	public String getAutor() {
		return Autor;
	}

	public void setAutor(String autor) {
		Autor = autor;
	}

	public String getSprache() {
		return Sprache;
	}

	public void setSprache(String sprache) {
		Sprache = sprache;
	}

}
