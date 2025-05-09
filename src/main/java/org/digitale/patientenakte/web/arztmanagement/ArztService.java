package org.digitale.patientenakte.web.arztmanagement;

import java.util.List;

import org.digitale.patientenakte.web.ArztRepository;
import org.digitale.patientenakte.web.TerminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//später ändern in DB Anbindung von aerzte liste
//FUnktion wurde immer kleiner, weil immer mehr Steuerung etfernt wurde --> Umbenennung zu Service
//FUnktionen, die Zustandslos sind --> alles was sie brauchen bekommen sie per Parameter
//Es reicht eine Instanz dieser Klasse zu haben, da sie sich ja eh immer gleich verhält --> ZUstand ist ja eh immer gleich 
//Zum start der applikation gehe ich durch gesamte Klassen und gebe ihnen bei implementierung merkmal (@component/@service) 
//dadurch erkennt spring, dass es nur eine isntanz ertselln muss u nd in "Kochtopf" werfen kann (umgspr: Proxy BEans)
//Überall wo Arztsteuerng verwendet werden soll (dafürr @Autowired) kann ich diese nutzen 
//In den Topf: @Component/Service, aus dem Topf: @Autowired --> bekommt dann überall die gleiche Referenz
//d.h. static nicht mehr nötig und keine new ArztSteuerung zB , d.h. auch zB für repo kann ich dann @AutoWired nutzen, um sie nicht immer mit new anzulegen (Kombi dann möglich)

@Service
public class ArztService {

	// Für den Datenbankzugriff
	@Autowired
	private ArztRepository arztRepo;
	private TerminService terminService;

	// Liste von aerzten anlegen, damit zB TerminSteuerung Zugriff hat
	public List<Arzt> ladeAerzte() {
		return arztRepo.findAll();
	}

	// Methode zum Löschen eines Arztes
	public void arztLoeschen(Arzt arzt) {
		arztRepo.delete(arzt);
	}
	
	public void arztSpeichern(Arzt arzt) {
		arztRepo.save(arzt);
	}

	// Arzt löschbar

	public boolean arztLoeschbar(Arzt arzt) {
		for (Termin termin : terminService.ladeTermine()) {
			if (termin.getBehandelnderArzt().equals(arzt)) {
				return false;
			}
		}
		return true;
	}

}
