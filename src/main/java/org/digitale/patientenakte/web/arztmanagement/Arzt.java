package org.digitale.patientenakte.web.arztmanagement;

import java.util.List;
import java.util.Scanner;

public class Arzt {

	// Instanzvariablen
	private String vorname;
	private String nachname;
	private Fachrichtungen fachrichtung;
	
	// Konstruktor
	public Arzt(String vorname, String nachname, Fachrichtungen fachrichtung) {
		this.vorname = vorname;
		this.nachname = nachname;
		this.fachrichtung = fachrichtung;
	}

	public Arzt() {
		// TODO Auto-generated constructor stub
	}

	// Getter und setter
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public Fachrichtungen getFachrichtung() {
		return this.fachrichtung;
	}

	public void setFachrichtung(Fachrichtungen fachrichtung) {
		this.fachrichtung = fachrichtung;
	}

	public String getVorname() {
		return vorname;
	}

}
