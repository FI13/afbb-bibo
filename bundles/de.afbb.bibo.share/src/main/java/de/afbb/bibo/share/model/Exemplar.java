package de.afbb.bibo.share.model;

import java.sql.Date;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

public class Exemplar extends Medium implements IEditorInput {

	public static final String FIELD_EDITION = "edition";//$NON-NLS-1$
	public static final String FIELD_BARCODE = "barcode";//$NON-NLS-1$
	public static final String FIELD_DATE_INVENTORY = "inventoryDate";//$NON-NLS-1$
	public static final String FIELD_CONDITION = "condition";//$NON-NLS-1$
	public static final String FIELD_DATE_BORROW = "borrowDate";//$NON-NLS-1$
	public static final String FIELD_DATE_LAST_BORROW = "lastBorrowDate";//$NON-NLS-1$

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
		changeSupport.firePropertyChange(FIELD_EDITION, this.edition, this.edition = edition);
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(final String barcode) {
		changeSupport.firePropertyChange(FIELD_BARCODE, this.barcode, this.barcode = barcode);
	}

	public Date getInventoryDate() {
		return inventoryDate;
	}

	public void setInventoryDate(final Date inventoryDate) {
		changeSupport.firePropertyChange(FIELD_DATE_INVENTORY, this.inventoryDate, this.inventoryDate = inventoryDate);
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(final String condition) {
		changeSupport.firePropertyChange(FIELD_CONDITION, this.condition, this.condition = condition);
	}

	public Date getBorrowDate() {
		return borrowDate;
	}

	public void setBorrowDate(final Date borrowDate) {
		changeSupport.firePropertyChange(FIELD_DATE_BORROW, this.borrowDate, this.borrowDate = borrowDate);
	}

	public Date getLastBorrowDate() {
		return lastBorrowDate;
	}

	public void setLastBorrowDate(final Date lastBorrowDate) {
		changeSupport.firePropertyChange(FIELD_DATE_LAST_BORROW, this.lastBorrowDate, this.lastBorrowDate = lastBorrowDate);
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

	@Override
	public Object getAdapter(final Class adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return barcode;
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return barcode;
	}

}
