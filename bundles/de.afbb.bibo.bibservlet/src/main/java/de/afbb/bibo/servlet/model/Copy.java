/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.afbb.bibo.servlet.model;

import java.sql.Date;

/**
 *
 * @author fi13.pendrulat
 */
public class Copy extends Medium{
    private int id;
    private String edition;
    private String barcode;
    private Date inventoryDate;
    private String condition;
    private Date borrowDate;
    private Date lastBorrowDate;
    private int groupId;
    private int borrowerId;
    private int lastBorrowerId;
    private int curatorId;
    private int lastCuratorId;

    public Copy(int id, String edition, String barcode, Date inventoryDate, String condition, Date borrowDate, Date lastBorrowDate, int groupId, int borrowerId, int lastBorrowerId, int curatorId, int lastCuratorId, int mediumId, String isbn, String title, String author, String language, int typeId, String publisher) {
        super(mediumId, isbn, title, author, language, typeId, publisher);
        this.id = id;
        this.edition = edition;
        this.barcode = barcode;
        this.inventoryDate = inventoryDate;
        this.condition = condition;
        this.borrowDate = borrowDate;
        this.lastBorrowDate = lastBorrowDate;
        this.groupId = groupId;
        this.borrowerId = borrowerId;
        this.lastBorrowerId = lastBorrowerId;
        this.curatorId = curatorId;
        this.lastCuratorId = lastCuratorId;
    }

    public int getId() {
        return id;
    }

    public String getEdition() {
        return edition;
    }

    public String getBarcode() {
        return barcode;
    }

    public Date getInventoryDate() {
        return inventoryDate;
    }

    public String getCondition() {
        return condition;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public Date getLastBorrowDate() {
        return lastBorrowDate;
    }

    public int getGroupId() {
        return groupId;
    }

    public int getBorrowerId() {
        return borrowerId;
    }

    public int getLastBorrowerId() {
        return lastBorrowerId;
    }

    public int getUserId() {
        return curatorId;
    }

    public int getLastCuratorId() {
        return lastCuratorId;
    }

    @Override
    public String toString() {
        return "Copy{" + "id=" + id + ", edition=" + edition + ", barcode=" + barcode + ", inventoryDate=" + inventoryDate + ", condition=" + condition + ", borrowDate=" + borrowDate + ", lastBorrowDate=" + lastBorrowDate + ", groupId=" + groupId + ", borrowerId=" + borrowerId + ", lastBorrowerId=" + lastBorrowerId + ", curatorId=" + curatorId + ", lastCuratorId=" + lastCuratorId + '}';
    }
}
