package de.afbb.bibo.share.model;

import java.util.List;

public class Group {
	
	private Integer id;
	private List<Exemplar> exemplars;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public List<Exemplar> getExemplars() {
		return exemplars;
	}
	public void setExemplars(List<Exemplar> exemplars) {
		this.exemplars = exemplars;
	}
	
}
