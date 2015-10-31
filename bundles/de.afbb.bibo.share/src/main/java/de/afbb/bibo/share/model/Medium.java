package de.afbb.bibo.share.model;

import de.afbb.bibo.share.beans.AbstractPropertyChangeSupport;

public class Medium extends AbstractPropertyChangeSupport {

	public static final String FIELD_ISBN = "isbn";//$NON-NLS-1$
	public static final String FIELD_TITLE = "title";//$NON-NLS-1$
	public static final String FIELD_AUTHOR = "author";//$NON-NLS-1$
	public static final String FIELD_LANGUAGE = "language";//$NON-NLS-1$
	public static final String FIELD_TYP_ID = "typId";//$NON-NLS-1$
	public static final String FIELD_PUBLISHER = "publisher";//$NON-NLS-1$

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
		changeSupport.firePropertyChange(FIELD_TITLE, this.title, this.title = title);
	}

	public Integer getTypId() {
		return typId;
	}

	public void setTypId(final Integer typId) {
		changeSupport.firePropertyChange(FIELD_TYP_ID, this.typId, this.typId = typId);
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(final String publisher) {
		changeSupport.firePropertyChange(FIELD_PUBLISHER, this.publisher, this.publisher = publisher);
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
		changeSupport.firePropertyChange(FIELD_ISBN, this.isbn, this.isbn = isbn);
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(final String author) {
		changeSupport.firePropertyChange(FIELD_AUTHOR, this.author, this.author = author);
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(final String language) {
		changeSupport.firePropertyChange(FIELD_LANGUAGE, this.language, this.language = language);
	}

}
