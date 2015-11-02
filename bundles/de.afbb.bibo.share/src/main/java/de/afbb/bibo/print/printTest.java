package de.afbb.bibo.print;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.share.model.Curator;

public class printTest {

	static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

	static MessageFormat head = new MessageFormat("Ausleihübersicht");
	static MessageFormat foot = new MessageFormat("created by AfbB-Bibo on " + dateFormat.format(new Date()));

	public static void main(final String[] args) throws Exception {

		// final String s = "<!DOCTYPE html><html><body><h1>My First
		// Heading</h1><p>My first paragraph.</p></body></html>";
		// final PrinterJob pj = PrinterJob.getPrinterJob();
		//
		// final JEditorPane text = new JEditorPane("text/html", "text");
		// text.read(new BufferedReader(new InputStreamReader(new
		// FileInputStream(new File("h:/test.html")))), "");
		// // text.setText(s);
		// text.repaint();
		// pj.setPrintable(text.getPrintable(head, foot));
		// pj.print();

		final Copy[] e = new Copy[22];
		e[0] = new Copy();
		e[0].setBarcode("12345");
		e[0].setTitle("Test123");
		e[1] = new Copy();
		e[1].setBarcode("244421");
		e[1].setTitle("Bla Blü bBälbBlaBlaDumdidudmlänge immer länger");
		for (int i = 2; i < 22; i++) {
			e[i] = new Copy();
			e[i].setBarcode("98836");
			e[i].setTitle("Heil Satan!");
		}
		final Curator c = new Curator();
		c.setName("Hans");
		final Borrower b = new Borrower();
		b.setFirstName("Jäns");
		b.setSurname("Hähnoch");

		printExemplarList.print(e, c, b);

	}
}
