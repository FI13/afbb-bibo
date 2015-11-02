package de.afbb.bibo.share;

import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.share.model.Curator;
import de.afbb.bibo.share.model.Exemplar;

public class ExemplarListToHtmlString {
	private static String column(final Exemplar ex) {
		String columnString = "";
		columnString += "<tr><th>" + ex.getBarcode() + "</th><th>"
				+ ex.getTitle().substring(0, Math.min(ex.getTitle().length(), 15)) + "</th><th>" + "</th><th>"
				+ "</th><th>";
		return columnString;
	}

	public static String convert(final Exemplar[] list, final Curator cur, final Borrower bor) {
		String s = "<!doctype html><html><head><meta charset=\"utf-8\"><title>Ausleihuebersicht</title><style>td, th { border-bottom: thin short } table { border: solid 1px } tr { border-bottom: solid 1px }</style></head><body>";
		// Create Header
		s += "<h3>Ausgeliehen an: <u>" + cur.getName() + "</u> durch: <u>" + bor.getFirstName() + " " + bor.getSurname()
				+ "</u></h3>"
				+ "<table WIDTH=100%><tr><th>ID</th><th>Titel</th><th>Name Schüler</th><th>Unterschrift</th></tr>";

		// End Header
		for (int i = 0; i < list.length; i++) {
			s += column(list[i]);
		}
		s += "</table><br><br><br></body></html>";
		return s;
	}

}
