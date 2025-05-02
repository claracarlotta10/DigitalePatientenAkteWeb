package org.digitale.patientenakte.web.arztmanagement;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TerminSteuerung {

	private static final List<Termin> termine = new ArrayList<>();

	// Erstellt und speichert einen neuen Termin
	public static void terminAnlegen(Arzt behandelnderArzt, LocalDate datum) {
		if (behandelnderArzt == null || datum == null ) {
			throw new IllegalArgumentException("Behandelnder Arzt und Datum müssen angegeben werden.");
		}

		Termin neuerTermin = new Termin(behandelnderArzt, datum);
		termine.add(neuerTermin);
	}

	public static List<Termin> getTermine() {
		return termine;
	}

	public static void terminLoeschen(Termin termin) {
		termine.remove(termin);
	}
}
