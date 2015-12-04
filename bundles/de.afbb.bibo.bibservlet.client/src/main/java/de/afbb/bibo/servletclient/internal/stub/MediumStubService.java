package de.afbb.bibo.servletclient.internal.stub;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import de.afbb.bibo.share.IMediumService;
import de.afbb.bibo.share.callback.EventListener;
import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.share.model.Medium;

public class MediumStubService implements IMediumService {

	private final ArrayList<Medium> media = new ArrayList<Medium>();
	private int currentMediaId = 0;

	public MediumStubService() {
		final Medium m1 = new Medium();
		m1.setMediumId(++currentMediaId);
		m1.setAuthor("George R. R. Martin");
		m1.setIsbn("9780553582024");
		m1.setLanguage("en");
		m1.setPublisher("Bantam Books");
		m1.setTitle("Game of Thrones");
		media.add(m1);

		final Medium m2 = new Medium();
		m2.setMediumId(++currentMediaId);
		m2.setAuthor("Douglas Adams");
		m2.setIsbn("9783453407831");
		m2.setLanguage("de");
		m2.setPublisher("Heyne");
		m2.setTitle("Macht's gut, und danke für den Fisch");
		media.add(m2);

		final Medium m3 = new Medium();
		m3.setMediumId(++currentMediaId);
		m3.setAuthor("Jürgen Hermsen");
		m3.setIsbn("9783804564954");
		m3.setLanguage("de");
		m3.setPublisher("Westermann");
		m3.setTitle("Rechnungswesen und Controlling für IT-Berufe");
		media.add(m3);

		final Medium m4 = new Medium();
		m4.setMediumId(++currentMediaId);
		m4.setAuthor("Michael Benford");
		m4.setIsbn("3190028362");
		m4.setLanguage("en");
		m4.setPublisher("Hueber");
		m4.setTitle("Ways to Trade");
		media.add(m4);

		final Medium m5 = new Medium();
		m5.setMediumId(++currentMediaId);
		m5.setAuthor("Arndt Beiderwieden, Elvira Pürling");
		m5.setIsbn("9783823711773");
		m5.setLanguage("de");
		m5.setPublisher("Bildungsverlag EINS");
		m5.setTitle("Projektmanagement für IT-Projekte");
		media.add(m5);

		final Medium m6 = new Medium();
		m6.setMediumId(++currentMediaId);
		m6.setAuthor("Wolfram Büchel, Cornelius Carey, Mary Schäfer, Dr. Wolfgang Schäfer");
		m6.setIsbn("9783128010403");
		m6.setLanguage("en");
		m6.setPublisher("Klett");
		m6.setTitle("Technical Milestones");
		media.add(m6);

		final Medium m7 = new Medium();
		m7.setMediumId(++currentMediaId);
		m7.setAuthor("Jürgen Gratzke");
		m7.setIsbn("9783804553804");
		m7.setLanguage("de");
		m7.setPublisher("Westermann");
		m7.setTitle("Wirtschafts- und Geschäftsprozesse für IT-Berufe");
		media.add(m7);
	}

	@Override
	public void update(final Medium medium) throws ConnectException {
		return;
	}

	@Override
	public Medium getMedium(final int id) throws ConnectException {
		final Iterator<Medium> i = media.iterator();
		while (i.hasNext()) {
			final Medium m = i.next();
			if (id == m.getMediumId()) {
				return m;
			}
		}
		return null;
	}

	@Override
	public Medium getMedium(final String isbn) throws ConnectException {
		final Iterator<Medium> i = media.iterator();
		while (i.hasNext()) {
			final Medium m = i.next();
			if (isbn == m.getIsbn()) {
				return m;
			}
		}
		return null;
	}

	@Override
	public Collection<Borrower> listLent(final String isbn) throws ConnectException {
		return new ArrayList<Borrower>();
	}

	@Override
	public Collection<Medium> list() throws ConnectException {
		return media;
	}

	@Override
	public void delete(final Medium medium) throws ConnectException {
		media.remove(medium);
	}

	@Override
	public void create(final Medium medium) throws ConnectException {
		medium.setMediumId(++currentMediaId);
		media.add(medium);
	}

	@Override
	public void register(final EventListener listener) {
		// nothing to do in stub service
	}

}
