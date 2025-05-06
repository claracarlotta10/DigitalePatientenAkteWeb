package org.digitale.patientenakte.web.arztmanagement;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

//später ändern in DB Anbindung von aerzte liste
public class ArztSteuerung {

	private static final List<Arzt> aerzte = new ArrayList<>();

	// statische Hilfsmethoden

	// Methode um einen neuen Arzt anzulegen durch Nutzerinegabe in der Konsole
	public static void arztAnlegen(String vorname, String nachname, Fachrichtungen fachrichtung) {

		if (vorname == null || nachname == null || fachrichtung == null || vorname.isEmpty() || nachname.isEmpty() || fachrichtung.getBeschreibung().isEmpty()) {
			Notification.show("Es müssen alle Felder ausgefüllt werden", 3000, Notification.Position.TOP_CENTER)
			.addThemeVariants(NotificationVariant.LUMO_ERROR);
			throw new IllegalArgumentException("Alle Felder müssen ausgefüllt werden.");
		}
		Arzt neuerArzt = new Arzt(vorname, nachname, fachrichtung);
		aerzte.add(neuerArzt);

		Notification.show("Arzt erfolgreich hinzugefügt!", 3000, Notification.Position.BOTTOM_END)
				.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
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
	
	public static Arzt arztBearbeiten(Arzt zuBearbeitenderArzt, String vorname, String nachname, Fachrichtungen fachrichtung) {
		// felder des arztes neu setzen auf input von nutzer
		zuBearbeitenderArzt.setVorname(vorname);
		zuBearbeitenderArzt.setNachname(nachname);
		zuBearbeitenderArzt.setFachrichtung(fachrichtung);
		
		Notification.show("Arzt erfolgreich geändert!", 3000, Notification.Position.BOTTOM_END)
		.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		
		return zuBearbeitenderArzt;
	}

}
