package org.digitale.patientenakte.web.arztmanagement;

import java.time.LocalDate;
import java.util.Date;

public class Termin {

	private Arzt behandelnderArzt;
	//private Date date;
	private LocalDate datum;
	
	public LocalDate getLocalDate() {
		return datum;
	}

	public void setLocalDate(LocalDate datum) {
		this.datum = datum;
	}

	private int id;

	static int idCounter = 0;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Arzt getBehandelnderArzt() {
		return behandelnderArzt;
	}

	public void setBehandelnderArzt(Arzt behandelnderArzt) {
		this.behandelnderArzt = behandelnderArzt;
	}


	public Termin(Arzt behandelnderArzt, LocalDate datum) {
		this.behandelnderArzt = behandelnderArzt;
		this.datum = datum;
		this.id = ++idCounter;
	}

	public Termin() {
		this.id = ++idCounter;
		// TODO Auto-generated constructor stub
	}

}
