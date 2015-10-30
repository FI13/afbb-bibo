package de.afbb.bibo.share;

import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;

import javax.swing.JEditorPane;

public class printExampleList {

	static MessageFormat head = new MessageFormat("");
	static MessageFormat foot = new MessageFormat("");

	public static void main(final String[] args) throws Exception {

		final String s = "<!DOCTYPE html><html><body><h1>My First Heading</h1><p>My first paragraph.</p></body></html>";
		final PrinterJob pj = PrinterJob.getPrinterJob();

		final JEditorPane text = new JEditorPane("text/html", "text");
		text.read(new BufferedReader(new InputStreamReader(new FileInputStream(new File("h:/test.html")))), "");
		// text.setText(s);
		text.repaint();
		pj.setPrintable(text.getPrintable(head, foot));
		pj.print();

	}
}
