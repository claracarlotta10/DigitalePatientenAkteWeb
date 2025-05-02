package org.digitale.patientenakte.web.arztmanagement;

import java.util.ArrayList;
import java.util.List;

//später ändern in DB Anbindung von aerzte liste
public class ArztSteuerung {

	private static final List<Arzt> aerzte = new ArrayList<>();

	// statische Hilfsmethoden

	// Methode um einen neuen Arzt anzulegen durch Nutzerinegabe in der Konsole
	public static void arztAnlegen(String vorname, String nachname, Fachrichtungen fachrichtung) {

		if (vorname == null || nachname == null || fachrichtung == null) {
			throw new IllegalArgumentException("Alle Felder müssen ausgefüllt werden.");
		}
		Arzt neuerArzt = new Arzt(vorname, nachname, fachrichtung);
		aerzte.add(neuerArzt);
	}

	// Liste von aerzten anlegen, damit zB TerminSteuerung Zugriff hat
	public static List<Arzt> getAerzte() {
		return aerzte;
	}

	// Methode zum Löschen eines Arztes
	public static void arztLoeschen(Arzt arzt) {
		aerzte.remove(arzt);
	}

	// Arzt löschbar
	public static boolean arztLoeschbar(Arzt arzt) {
		for (Termin termin : TerminSteuerung.getTermine()) {
			if (termin.getBehandelnderArzt().equals(arzt)) {
				return false;
			}
		}
		return true;
	}

}
