package de.afbb.bibo.print;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.MessageFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Date;

import javax.swing.JEditorPane;

import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.share.model.Curator;

/**
 * prints over the method print a list of Exemplars to the default printer; No
 * PrintDialog is shown
 *
 * @author fi13.melberling
 */

public class PrintCopyList {

	public static void print(final Copy[] list, final Curator cur, final Borrower bor) throws PrinterException {
		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

		final MessageFormat head = new MessageFormat("Ausleihübersicht");
		final MessageFormat foot = new MessageFormat("created by AfbB-Bibo on " + dateFormat.format(new Date()));

		final PrinterJob pj = PrinterJob.getPrinterJob();

		final JEditorPane text = new JEditorPane("text/html", "text");
		text.setText(CopyListToHtmlString.convert(list, cur, bor));
		text.repaint();
		pj.setPrintable(text.getPrintable(head, foot));
		pj.print();
	}

}
