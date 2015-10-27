package de.afbb.bibo.share.model;

public class Borrower {
	private Integer id;
	private String surname;
	private String firstName;
	private String fiClass;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getFiClass() {
		return fiClass;
	}
	public void setFiClass(String fiClass) {
		this.fiClass = fiClass;
	}


}
