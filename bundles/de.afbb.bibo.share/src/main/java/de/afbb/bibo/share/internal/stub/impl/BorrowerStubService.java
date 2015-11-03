package de.afbb.bibo.share.internal.stub.impl;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import de.afbb.bibo.share.IBorrowerService;
import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.share.model.Copy;

public class BorrowerStubService implements IBorrowerService {

	Collection<Borrower> borrowers = new ArrayList<Borrower>();
	Collection<Copy> copies = new ArrayList<Copy>();
	int currentBorrowerId = 0;
	int currentCopyId = 0;

	public BorrowerStubService() {
		final Borrower b1 = new Borrower();
		b1.setId(++currentBorrowerId);
		b1.setFirstName("Philipp");
		b1.setSurname("Widdra");
		final Borrower b2 = new Borrower();
		b1.setId(++currentBorrowerId);
		b2.setFirstName("Michelé");
		b2.setSurname("Lingel");
		final Borrower b3 = new Borrower();
		b1.setId(++currentBorrowerId);
		b3.setFirstName("David");
		b3.setSurname("Becker");

		borrowers.add(b1);
		borrowers.add(b2);
		borrowers.add(b3);

		final Copy c1 = new Copy();
		c1.setId(++currentCopyId);
		c1.setTitle("IT-Handbuch");
		c1.setAuthor("Autor");
		c1.setLanguage("de");
		c1.setBarcode(c1.getId().toString());
		c1.setInventoryDate(new java.sql.Date(2015, 5, 15));

		final Copy c2 = new Copy();
		c2.setId(++currentCopyId);
		c2.setTitle("Kusch: Mathematik");
		c2.setAuthor("Rudolf Borgmann, Jost Knapp, Rolf Schöwe");
		c2.setIsbn("3464413055");
		c2.setBarcode(c2.getId().toString());
		c2.setLanguage("de");
		c2.setEdition("Aktuelle Ausgabe: Band K");
		c2.setPublisher("Cornelsen Verlag");
		c2.setCondition("- Seite 42 angerissen\n- Seite 101 fehlt");
		c2.setInventoryDate(new java.sql.Date(2015, 10, 3));

		copies.add(c1);
		copies.add(c2);
	}

	@Override
	public boolean exists(final String firstName, final String surname) throws ConnectException {
		final Iterator<Borrower> i = borrowers.iterator();
		while (i.hasNext()) {
			final Borrower b = i.next();
			if (firstName == b.getFirstName() && surname == b.getSurname()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void create(final Borrower borrower) throws ConnectException {
		borrower.setId(++currentBorrowerId);
		borrowers.add(borrower);
	}

	@Override
	public void update(final Borrower Borrower) throws ConnectException {
		final Iterator<Borrower> i = borrowers.iterator();
		while (i.hasNext()) {
			final Borrower b = i.next();
			if (Borrower.getId() == b.getId()) {
				borrowers.remove(b);
				borrowers.add(Borrower);
				return;
			}
		}
	}

	@Override
	public Collection<Borrower> listAll() throws ConnectException {
		return borrowers;
	}

	@Override
	public Collection<Borrower> listOpen() throws ConnectException {
		return borrowers;
	}

	@Override
	public Collection<Copy> listLent(final Borrower Borrower) throws ConnectException {
		return copies;
	}

}
