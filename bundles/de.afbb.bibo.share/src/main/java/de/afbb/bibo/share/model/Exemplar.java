package de.afbb.bibo.share.model;

import java.sql.Blob;
import java.sql.Date;

public class Exemplar {
	private Integer id;
	private Integer edition;
	private Integer barcode;
	private Date inventoryDate;
	private String condition;
	private Date borrowDate;
	private Date lastBorrowDate;
	private Curator curatorName;
	private Curator lastCuratorName;
	private Medium mediumTyp;
	private Group groupElements;
	
	
	public Group getGroupElements() {
		return groupElements;
	}
	public void setGroupElements(Group groupElements) {
		this.groupElements = groupElements;
	}
	public Medium getMediumTyp() {
		return mediumTyp;
	}
	public void setMediumTyp(Medium mediumTyp) {
		this.mediumTyp = mediumTyp;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getEdition() {
		return edition;
	}
	public void setEdition(Integer edition) {
		this.edition = edition;
	}
	public Integer getBarcode() {
		return barcode;
	}
	public void setBarcode(Integer barcode) {
		this.barcode = barcode;
	}
	public Date getInventoryDate() {
		return inventoryDate;
	}
	public void setInventoryDate(Date inventoryDate) {
		this.inventoryDate = inventoryDate;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public Date getBorrowDate() {
		return borrowDate;
	}
	public void setBorrowDate(Date borrowDate) {
		this.borrowDate = borrowDate;
	}
	public Date getLastBorrowDate() {
		return lastBorrowDate;
	}
	public void setLastBorrowDate(Date lastBorrowDate) {
		this.lastBorrowDate = lastBorrowDate;
	}
	public Curator getCuratorName() {
		return curatorName;
	}
	public void setCuratorName(Curator curatorName) {
		this.curatorName = curatorName;
	}
	public Curator getLastCuratorName() {
		return lastCuratorName;
	}
	public void setLastCuratorName(Curator lastCuratorName) {
		this.lastCuratorName = lastCuratorName;
	}
	
	

}
