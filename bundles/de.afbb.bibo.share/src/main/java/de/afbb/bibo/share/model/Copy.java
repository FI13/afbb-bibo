package de.afbb.bibo.share.model;

import java.util.Date;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

/**
 * one copy of a medium
 */
public class Copy extends Medium implements IEditorInput {

	public static final String FIELD_EDITION = "edition";//$NON-NLS-1$
	public static final String FIELD_BARCODE = "barcode";//$NON-NLS-1$
	public static final String FIELD_DATE_INVENTORY = "inventoryDate";//$NON-NLS-1$
	public static final String FIELD_CONDITION = "condition";//$NON-NLS-1$
	public static final String FIELD_DATE_BORROW = "borrowDate";//$NON-NLS-1$
	public static final String FIELD_DATE_LAST_BORROW = "lastBorrowDate";//$NON-NLS-1$
	public static final String FIELD_CURATOR = "curator";//$NON-NLS-1$
	public static final String FIELD_LAST_CURATOR = "lastCurator";//$NON-NLS-1$
	public static final String FIELD_BORROWER = "borrower";//$NON-NLS-1$
	public static final String FIELD_LAST_BORROWER = "lastBorrower";//$NON-NLS-1$

	private Integer id;
	private String edition;
	private String barcode = "";
	private Date inventoryDate;
	private String condition;
	private Date borrowDate;
	private Date lastBorrowDate;
	private Curator curator;
	private Curator lastCurator;
	private Borrower borrower;
	private Borrower lastBorrower;
	private int groupId = -1;

	public Copy(final int id, final String edition, final String barcode, final Date date, final String condition,
			final Date date2, final Date date3, final int groupId, final Borrower borrower, final Borrower lastBorrower,
			final Curator curator, final Curator lastCurator, final int mediumId, final String isbn, final String title,
			final String author, final String language, final MediumType type, final String publisher) {
		super(mediumId, isbn, title, author, language, type, publisher);
		this.id = id;
		this.edition = edition;
		this.barcode = barcode;
		inventoryDate = date;
		this.condition = condition;
		borrowDate = date2;
		lastBorrowDate = date3;
		this.groupId = groupId;
		this.borrower = borrower;
		this.lastBorrower = lastBorrower;
		this.curator = curator;
		this.lastCurator = lastCurator;
	}

	public Copy() {
		super();
	}

	@Override
	public Integer getMediumId() {
		return id;
	}

	@Override
	public void setMediumId(final Integer id) {
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
		changeSupport.firePropertyChange(FIELD_DATE_LAST_BORROW, this.lastBorrowDate,
				this.lastBorrowDate = lastBorrowDate);
	}

	public Curator getCurator() {
		return curator;
	}

	public void setCurator(final Curator curator) {
		changeSupport.firePropertyChange(FIELD_CURATOR, this.curator, this.curator = curator);
	}

	public Curator getLastCurator() {
		return lastCurator;
	}

	public void setLastCurator(final Curator lastCurator) {
		changeSupport.firePropertyChange(FIELD_LAST_CURATOR, this.lastCurator, this.lastCurator = lastCurator);
	}

	public Borrower getBorrower() {
		return borrower;
	}

	public void setBorrower(final Borrower borrower) {
		changeSupport.firePropertyChange(FIELD_BORROWER, this.borrower, this.borrower = borrower);
	}

	public Borrower getLastBorrower() {
		return lastBorrower;
	}

	public void setLastBorrower(final Borrower lastBorrower) {
		changeSupport.firePropertyChange(FIELD_LAST_BORROWER, this.lastBorrower, this.lastBorrower = lastBorrower);
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(final int groupElements) {
		groupId = groupElements;
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

	@Override
	public Object clone() {
		final Copy clone = (Copy) super.clone();
		clone.setCurator(curator != null ? (Curator) curator.clone() : null);
		clone.setLastCurator(lastCurator != null ? (Curator) lastCurator.clone() : null);
		clone.setBorrower(borrower != null ? (Borrower) borrower.clone() : null);
		clone.setLastBorrower(lastBorrower != null ? (Borrower) lastBorrower.clone() : null);
		clone.setInventoryDate(inventoryDate != null ? (Date) inventoryDate.clone() : null);
		clone.setLastBorrowDate(borrowDate != null ? (Date) borrowDate.clone() : null);
		clone.setLastBorrowDate(lastBorrowDate != null ? (Date) lastBorrowDate.clone() : null);
		return clone;
	}

	@Override
	public String toString() {
		return "Copy{" + "id=" + id + ", edition=" + edition + ", barcode=" + barcode + ", inventoryDate="
				+ inventoryDate + ", condition=" + condition + ", borrowDate=" + borrowDate + ", lastBorrowDate="
				+ lastBorrowDate + ", groupId=" + groupId + ", borrowerId=" + borrower + ", lastBorrowerId="
				+ lastBorrower + ", curator=" + curator + ", lastCurator=" + lastCurator + '}';
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((barcode == null) ? 0 : barcode.hashCode());
		result = prime * result + ((condition == null) ? 0 : condition.hashCode());
		result = prime * result + ((edition == null) ? 0 : edition.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof Copy)) {
			return false;
		}
		Copy other = (Copy) obj;
		if (barcode == null) {
			if (other.barcode != null) {
				return false;
			}
		} else if (!barcode.equals(other.barcode)) {
			return false;
		}
		if (condition == null) {
			if (other.condition != null) {
				return false;
			}
		} else if (!condition.equals(other.condition)) {
			return false;
		}
		if (edition == null) {
			if (other.edition != null) {
				return false;
			}
		} else if (!edition.equals(other.edition)) {
			return false;
		}
		return true;
	}
}
