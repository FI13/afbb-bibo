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
		b1.setForename("Philipp");
		b1.setSurname("Widdra");
		b1.setInfo("FI13");
		final Borrower b2 = new Borrower();
		b1.setId(++currentBorrowerId);
		b2.setForename("Michelé");
		b2.setSurname("Lingel");
		b2.setInfo("FI13");
		final Borrower b3 = new Borrower();
		b1.setId(++currentBorrowerId);
		b3.setForename("David");
		b3.setSurname("Becker");
		b3.setInfo("FI13");
		final Borrower b4 = new Borrower();
		b4.setId(++currentBorrowerId);
		b4.setForename("Jens");
		b4.setSurname("Henoch");
		b4.setInfo("Lehrer");

		borrowers.add(b1);
		borrowers.add(b2);
		borrowers.add(b3);
		borrowers.add(b4);

		final Copy c1 = new Copy();
		c1.getMedium().setMediumId(++currentCopyId);
		c1.getMedium().setTitle("IT-Handbuch");
		c1.getMedium().setAuthor("Autor");
		c1.getMedium().setLanguage("de");
		c1.setBarcode(c1.getMedium().getMediumId().toString());
		c1.setInventoryDate(new java.sql.Date(2015, 5, 15));

		final Copy c2 = new Copy();
		c2.getMedium().setMediumId(++currentCopyId);
		c2.getMedium().setTitle("Kusch: Mathematik");
		c2.getMedium().setAuthor("Rudolf Borgmann, Jost Knapp, Rolf Schöwe");
		c2.getMedium().setIsbn("3464413055");
		c2.setBarcode(c2.getMedium().getMediumId().toString());
		c2.getMedium().setLanguage("de");
		c2.setEdition("Aktuelle Ausgabe: Band K");
		c2.getMedium().setPublisher("Cornelsen Verlag");
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
			if (firstName == b.getForename() && surname == b.getSurname()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void create(final Borrower borrower) throws ConnectException {
		try {
			Thread.sleep(5000);
			borrower.setId(++currentBorrowerId);
			borrowers.add(borrower);
		} catch (final InterruptedException e) {
		}
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
		try {
			Thread.sleep(10000);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
		return copies;
	}

}
