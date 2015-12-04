package de.afbb.bibo.servletclient.internal.stub;

import java.net.ConnectException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

import de.afbb.bibo.share.ICopyService;
import de.afbb.bibo.share.callback.EventListener;
import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.share.model.Curator;
import de.afbb.bibo.share.model.Medium;
import de.afbb.bibo.share.model.MediumType;

public class CopyStubService implements ICopyService {

	final HashMap<String, Copy> copies = new HashMap<>();

	public CopyStubService() {
		// final Borrower b = ((ArrayList<Borrower>) ((BorrowerStubService)
		// ServiceLocator.getInstance()
		// .getBorrowerService()).borrowers).get(0);
		final Borrower b = (Borrower) new BorrowerStubService().borrowers.toArray()[0];
		final Curator c = new CuratorStubService().curator;
		final MediumType t = (MediumType) new TypStubService().types.toArray()[0];

		int currentCopyId = 0;
		final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"));//$NON-NLS-1$

		final Copy c1 = new Copy(1, "1.Auflage", "1", new Date(), "Kaffeefleck", new Date(), new Date(), -1, b, b, c, c,
				1, "9780553582024", "Game of Thrones", "George R. R. Martin", "en", t, "Bantam Books");
		calendar.set(2015, 5, 15, 12, 0, 0);
		c1.setInventoryDate(calendar.getTime());
		calendar.set(2015, 5, 20, 12, 0, 0);
		c1.setLastBorrowDate(calendar.getTime());
		calendar.set(2015, 6, 01, 12, 0, 0);
		c1.setBorrowDate(calendar.getTime());
		copies.put(c1.getBarcode(), c1);

		final Copy c2 = new Copy();
		c2.getMedium().setMediumId(++currentCopyId);
		c2.getMedium().setTitle("IT-Handbuch");
		c2.getMedium().setAuthor("Autor");
		c2.getMedium().setLanguage("de");
		c2.setBarcode(c2.getMedium().getMediumId().toString());
		calendar.set(2015, 5, 15);
		c2.setInventoryDate(calendar.getTime());

		final Copy c3 = new Copy();
		c3.getMedium().setMediumId(++currentCopyId);
		c3.getMedium().setTitle("Kusch: Mathematik");
		c3.getMedium().setAuthor("Rudolf Borgmann, Jost Knapp, Rolf Schöwe");
		c3.getMedium().setIsbn("3464413055");
		c3.setBarcode(c3.getMedium().getMediumId().toString());
		c3.getMedium().setLanguage("de");
		c3.setEdition("Aktuelle Ausgabe: Band K");
		c3.getMedium().setPublisher("Cornelsen Verlag");
		c3.setCondition("- Seite 42 angerissen\n- Seite 101 fehlt");
		calendar.set(2015, 10, 3);
		c3.setInventoryDate(calendar.getTime());

		copies.put(c2.getBarcode(), c2);
		copies.put(c3.getBarcode(), c3);
	}

	@Override
	public void update(final Copy copy) throws ConnectException {
		if (copy != null && copies.values().contains(copy)) {
			copies.put(copy.getBarcode(), copy);
		}
	}

	@Override
	public Copy get(final String barcode) throws ConnectException {
		return copies.get(barcode);
	}

	@Override
	public Collection<Copy> getGroupedCopies(final Integer id) throws ConnectException {
		final Set<Copy> result = new HashSet<>();
		if (id != null) {
			for (final Copy copy : copies.values()) {
				if (id.equals(copy.getGroupId())) {
					result.add(copy);
				}
			}
		}
		return result;
	}

	@Override
	public void registerCopies(final Collection<Copy> copies) throws ConnectException {
		if (copies != null && !copies.isEmpty()) {
			for (final Copy copy : copies) {
				if (copy != null) {
					this.copies.put(copy.getBarcode(), copy);
				}
			}
		}
	}

	@Override
	public void returnCopies(final Collection<Copy> copies) throws ConnectException {
		if (copies != null && !copies.isEmpty()) {
			for (final Copy copy : copies) {
				if (copy != null) {
					this.copies.put(copy.getBarcode(), copy);
				}
			}
		}
	}

	@Override
	public Collection<Copy> listAll() throws ConnectException {
		return copies.values();
	}

	@Override
	public Collection<Copy> listCopies(final Medium medium) throws ConnectException {
		final Set<Copy> result = new HashSet<>();
		if (medium != null) {
			for (final Copy copy : copies.values()) {
				if (medium.equals(copy.getMedium())) {
					result.add(copy);
				}
			}
		}
		return result;
	}

	@Override
	public void delete(final Copy copy) throws ConnectException {
		if (copy != null && copies.values().contains(copy)) {
			copies.remove(copy.getBarcode());
		}
	}

	@Override
	public void lendCopies(final Collection<Copy> copies, final boolean printList) throws ConnectException {
		if (copies != null && !copies.isEmpty()) {
			for (final Copy copy : copies) {
				if (copy != null) {
					this.copies.put(copy.getBarcode(), copy);
				}
			}
		}
	}

	/**
	 * package private method only for debug purposes in stub services
	 *
	 * @return
	 * @deprecated don't implement
	 */
	@Deprecated
	HashMap<String, Copy> getCopies() {
		return copies;
	}

	@Override
	public boolean exists(final String barcode) throws ConnectException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void register(final EventListener listener) {
		// nothing to do in stub service
	}

}
