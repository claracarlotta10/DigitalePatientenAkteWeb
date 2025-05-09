package org.digitale.patientenakte.web.arztmanagement;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "TERMIN")
public class Termin implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	//@ManyToOne
	//@JoinColumn(name = "ARZT_ID") // Fremdschlüssel zur Arzt-Tabelle
	@Column(name="BEHANDELNDER ARZT")
	private Arzt behandelnderArzt;

	@Column(name = "DATUM")
	private LocalDate datum;

	// Konstruktoren
	public Termin() {
	}

	public Termin(Arzt behandelnderArzt, LocalDate datum) {
		this.behandelnderArzt = behandelnderArzt;
		this.datum = datum;
	}

	// Getter und Setter
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Arzt getBehandelnderArzt() {
		return behandelnderArzt;
	}

	public void setBehandelnderArzt(Arzt behandelnderArzt) {
		this.behandelnderArzt = behandelnderArzt;
	}

	public LocalDate getDatum() {
		return datum;
	}

	public void setDatum(LocalDate datum) {
		this.datum = datum;
	}
}
