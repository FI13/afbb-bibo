package de.afbb.bibo.share.model;

import java.sql.Date;

public class Exemplar extends Medium {
	private Integer id;
	private String edition;
	private String barcode;
	private Date inventoryDate;
	private String condition;
	private Date borrowDate;
	private Date lastBorrowDate;
	private int curatorId;
	private int lastCuratorId;
	private int borrowerId;
	private int lastBorrowerId;
	private int groupElements;

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(final Integer id) {
		this.id = id;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(final String edition) {
		this.edition = edition;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(final String barcode) {
		this.barcode = barcode;
	}

	public Date getInventoryDate() {
		return inventoryDate;
	}

	public void setInventoryDate(final Date inventoryDate) {
		this.inventoryDate = inventoryDate;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(final String condition) {
		this.condition = condition;
	}

	public Date getBorrowDate() {
		return borrowDate;
	}

	public void setBorrowDate(final Date borrowDate) {
		this.borrowDate = borrowDate;
	}

	public Date getLastBorrowDate() {
		return lastBorrowDate;
	}

	public void setLastBorrowDate(final Date lastBorrowDate) {
		this.lastBorrowDate = lastBorrowDate;
	}

	public int getCuratorId() {
		return curatorId;
	}

	public void setCuratorId(final int curatorId) {
		this.curatorId = curatorId;
	}

	public int getLastCuratorId() {
		return lastCuratorId;
	}

	public void setLastCuratorId(final int lastCuratorId) {
		this.lastCuratorId = lastCuratorId;
	}

	public int getBorrowerId() {
		return borrowerId;
	}

	public void setBorrowerId(final int borrowerId) {
		this.borrowerId = borrowerId;
	}

	public int getLastBorrowerId() {
		return lastBorrowerId;
	}

	public void setLastBorrowerId(final int lastBorrowerId) {
		this.lastBorrowerId = lastBorrowerId;
	}

	public int getGroupElements() {
		return groupElements;
	}

	public void setGroupElements(final int groupElements) {
		this.groupElements = groupElements;
	}

}
