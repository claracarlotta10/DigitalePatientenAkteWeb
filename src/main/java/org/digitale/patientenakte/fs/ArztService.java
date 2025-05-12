package org.digitale.patientenakte.fs;

import java.util.List;

import org.digitale.patientenakte.fo.Arzt;

public interface ArztService {
	
	// Liste von aerzten anlegen, damit zB TerminSteuerung Zugriff hat
	public List<Arzt> ladeAerzte();

	// Methode zum Löschen eines Arztes
	public void arztLoeschen(Arzt arzt); 
	
	public void arztSpeichern(Arzt arzt);

	// Arzt löschbar

	public boolean arztLoeschbar(Arzt arzt);

}
