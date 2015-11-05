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

	private Integer id;
	private String edition;
	private String barcode = "";
	private Date inventoryDate;
	private String condition;
	private Date borrowDate;
	private Date lastBorrowDate;
	private int curatorId;
	private int lastCuratorId;
	private int borrowerId;
	private int lastBorrowerId;
	private int groupId;

	public Copy(final int id, final String edition, final String barcode, final Date date, final String condition, final Date date2,
			final Date date3, final int groupId, final int borrowerId, final int lastBorrowerId, final int curatorId,
			final int lastCuratorId, final int mediumId, final String isbn, final String title, final String author, final String language,
			final int typeId, final String publisher) {
		super(mediumId, isbn, title, author, language, typeId, publisher);
		this.id = id;
		this.edition = edition;
		this.barcode = barcode;
		inventoryDate = date;
		this.condition = condition;
		borrowDate = date2;
		lastBorrowDate = date3;
		this.groupId = groupId;
		this.borrowerId = borrowerId;
		this.lastBorrowerId = lastBorrowerId;
		this.curatorId = curatorId;
		this.lastCuratorId = lastCuratorId;
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
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (barcode == null ? 0 : barcode.hashCode());
		result = prime * result + (borrowDate == null ? 0 : borrowDate.hashCode());
		result = prime * result + borrowerId;
		result = prime * result + (condition == null ? 0 : condition.hashCode());
		result = prime * result + curatorId;
		result = prime * result + (edition == null ? 0 : edition.hashCode());
		result = prime * result + groupId;
		result = prime * result + (id == null ? 0 : id.hashCode());
		result = prime * result + (inventoryDate == null ? 0 : inventoryDate.hashCode());
		result = prime * result + (lastBorrowDate == null ? 0 : lastBorrowDate.hashCode());
		result = prime * result + lastBorrowerId;
		result = prime * result + lastCuratorId;
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof Copy)) {
			return false;
		}
		final Copy other = (Copy) obj;
		if (barcode == null) {
			if (other.barcode != null) {
				return false;
			}
		} else if (!barcode.equals(other.barcode)) {
			return false;
		}
		if (borrowDate == null) {
			if (other.borrowDate != null) {
				return false;
			}
		} else if (!borrowDate.equals(other.borrowDate)) {
			return false;
		}
		if (borrowerId != other.borrowerId) {
			return false;
		}
		if (condition == null) {
			if (other.condition != null) {
				return false;
			}
		} else if (!condition.equals(other.condition)) {
			return false;
		}
		if (curatorId != other.curatorId) {
			return false;
		}
		if (edition == null) {
			if (other.edition != null) {
				return false;
			}
		} else if (!edition.equals(other.edition)) {
			return false;
		}
		if (groupId != other.groupId) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (inventoryDate == null) {
			if (other.inventoryDate != null) {
				return false;
			}
		} else if (!inventoryDate.equals(other.inventoryDate)) {
			return false;
		}
		if (lastBorrowDate == null) {
			if (other.lastBorrowDate != null) {
				return false;
			}
		} else if (!lastBorrowDate.equals(other.lastBorrowDate)) {
			return false;
		}
		if (lastBorrowerId != other.lastBorrowerId) {
			return false;
		}
		if (lastCuratorId != other.lastCuratorId) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Copy{" + "id=" + id + ", edition=" + edition + ", barcode=" + barcode + ", inventoryDate=" + inventoryDate + ", condition="
				+ condition + ", borrowDate=" + borrowDate + ", lastBorrowDate=" + lastBorrowDate + ", groupId=" + groupId + ", borrowerId="
				+ borrowerId + ", lastBorrowerId=" + lastBorrowerId + ", curatorId=" + curatorId + ", lastCuratorId=" + lastCuratorId + '}';
	}

}
