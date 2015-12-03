package de.afbb.bibo.servletclient.internal.stub;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import de.afbb.bibo.servletclient.ServiceLocator;
import de.afbb.bibo.share.IBorrowerService;
import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.share.model.Copy;

public class BorrowerStubService implements IBorrowerService {

	Collection<Borrower> borrowers = new ArrayList<Borrower>();
	int currentBorrowerId = 0;

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

	@SuppressWarnings("deprecation")
	@Override
	public Collection<Copy> listLent(final Borrower borrower) throws ConnectException {
		final Set<Copy> result = new HashSet<Copy>();
		if (borrower != null) {
			for (final Copy copy : ((CopyStubService) ServiceLocator.getInstance().getCopyService()).getCopies()
					.values()) {
				if (copy != null && borrower.equals(copy.getBorrower())
						&& (copy.getBorrowDate() != null && copy.getLastBorrowDate() == null
								|| copy.getBorrowDate().after(copy.getLastBorrowDate()))) {
					result.add(copy);
				}
			}
		}
		return result;
	}

}
