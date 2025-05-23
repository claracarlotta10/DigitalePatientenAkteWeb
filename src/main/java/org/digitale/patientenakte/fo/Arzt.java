package org.digitale.patientenakte.fo;

import java.io.Serializable;
import java.util.Objects;

import org.digitale.patientenakte.fo.client.Fachrichtungen;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity //Klasse wird als JPA Entity erkannt
@Table //Tabelle anlegen (name="Arzt"), wenn andere Benennung
//Business Object, der State/Zusatnd repräsentiert --> immer nach Java Beans aufgbeut mit Tabellenverknüpfung, Serializable, hgetter/seter --> keien Logiken
public class Arzt implements Serializable{
	
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="ARZT_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	// Instanzvariablen
	@Column(name="VORNAME") //Spalten in Tabelle anlegen
	private String vorname;
	
	@Column(name="NACHNAME")
	private String nachname;
	
	@Column(name="FACHRICHTUNG")
	@Enumerated(EnumType.STRING)//speichert enums als Strings
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

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Arzt other = (Arzt) obj;
		return Objects.equals(id, other.id);
	}

}
