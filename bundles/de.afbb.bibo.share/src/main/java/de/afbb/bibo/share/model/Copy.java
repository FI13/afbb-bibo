package de.afbb.bibo.share.model;

import java.util.Date;

import de.afbb.bibo.share.beans.AbstractPropertyChangeSupport;

/**
 * one copy of a medium
 */
public class Copy extends AbstractPropertyChangeSupport implements Cloneable {

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
	public static final String FIELD_MEDIUM = "medium";//$NON-NLS-1$

	private final Integer id;
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
	private Medium medium = new Medium();
	private int groupId = -1;

	/**
	 * default constructor. will set id to -1
	 */
	public Copy() {
		super();
		id = -1;
	}

	public Copy(final int id, final String edition, final String barcode, final Date inventoryDate,
			final String condition, final Date borrowDate, final Date lastBorrowDate, final int groupId,
			final Borrower borrower, final Borrower lastBorrower, final Curator curator, final Curator lastCurator,
			final int mediumId, final String isbn, final String title, final String author, final String language,
			final MediumType type, final String publisher) {
		super();
		this.id = id;
		this.edition = edition;
		this.barcode = barcode;
		this.inventoryDate = inventoryDate;
		this.condition = condition;
		this.borrowDate = borrowDate;
		this.lastBorrowDate = lastBorrowDate;
		this.groupId = groupId;
		this.borrower = borrower;
		this.lastBorrower = lastBorrower;
		this.curator = curator;
		this.lastCurator = lastCurator;
		medium.setMediumId(mediumId);
		medium.setIsbn(isbn);
		medium.setTitle(title);
		medium.setAuthor(author);
		medium.setLanguage(language);
		medium.setType(type);
		medium.setPublisher(publisher);
	}

	public Integer getId() {
		return id;
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

	/**
	 * @return the medium
	 */
	public Medium getMedium() {
		return medium;
	}

	/**
	 * @param medium
	 *            the medium to set
	 */
	public void setMedium(final Medium medium) {
		changeSupport.firePropertyChange(FIELD_MEDIUM, this.medium, this.medium = medium);
	}

	@Override
	public Object clone() {
		Copy clone = null;
		try {
			clone = (Copy) super.clone();
			clone.setCurator(curator != null ? (Curator) curator.clone() : null);
			clone.setLastCurator(lastCurator != null ? (Curator) lastCurator.clone() : null);
			clone.setBorrower(borrower != null ? (Borrower) borrower.clone() : null);
			clone.setLastBorrower(lastBorrower != null ? (Borrower) lastBorrower.clone() : null);
			clone.setInventoryDate(inventoryDate != null ? (Date) inventoryDate.clone() : null);
			clone.setLastBorrowDate(borrowDate != null ? (Date) borrowDate.clone() : null);
			clone.setLastBorrowDate(lastBorrowDate != null ? (Date) lastBorrowDate.clone() : null);
			clone.setMedium(medium != null ? (Medium) medium.clone() : null);
		} catch (final CloneNotSupportedException e) {
			// swallow exception and return null
		}
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
		int result = 1;
		result = prime * result + (barcode == null ? 0 : barcode.hashCode());
		result = prime * result + (condition == null ? 0 : condition.hashCode());
		result = prime * result + (edition == null ? 0 : edition.hashCode());
		return result;
	}

	/**
	 * empty strings should be considered equals to null for easier validation
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Copy)) {
			return false;
		}
		final Copy other = (Copy) obj;
		if (barcode == null || "".equals(barcode)) {
			if (other.barcode != null && !"".equals(other.barcode)) {
				return false;
			}
		} else if (!barcode.equals(other.barcode)) {
			return false;
		}
		if (condition == null || "".equals(condition)) {
			if (other.condition != null && !"".equals(other.condition)) {
				return false;
			}
		} else if (!condition.equals(other.condition)) {
			return false;
		}
		if (edition == null || "".equals(edition)) {
			if (other.edition != null && !"".equals(other.edition)) {
				return false;
			}
		} else if (!edition.equals(other.edition)) {
			return false;
		}
		return true;
	}
}
