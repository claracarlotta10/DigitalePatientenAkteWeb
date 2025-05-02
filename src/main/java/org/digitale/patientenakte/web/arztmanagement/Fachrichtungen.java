package org.digitale.patientenakte.web.arztmanagement;



public enum Fachrichtungen {
	KARDIOLOGIE("Kardiologie"), UROLOGIE("Urologie"), INNERE_MEDIZIN("Innere Medizin"), KINDERARZT("Kinderarzt");

	private String beschreibung;

	private Fachrichtungen(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	// per Getter BEschreibung aufrufen als klein geschriebene Version
	public String getBeschreibung() {
		return beschreibung;
	}
	
	//Gibt die Fachrichtung passend zum InputString zurück bzw. null wenn die Fachrichtung nicht vorhanden ist
	public static Fachrichtungen findFachrichtungForMatchingInputString(String inputFachrichtungString) {
		if (inputFachrichtungString == null) {
			return null;
		}
		for (Fachrichtungen fachrichtung : Fachrichtungen.values()) {
			if (fachrichtung.getBeschreibung().equalsIgnoreCase(inputFachrichtungString)) {
				return fachrichtung;
			}
		}
		return null;

	}
    public static String getFachrichtungBeschreibung(Arzt arzt) {
        return arzt.getFachrichtung().getBeschreibung();
    }

}
