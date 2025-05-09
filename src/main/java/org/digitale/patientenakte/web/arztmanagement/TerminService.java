package org.digitale.patientenakte.web.arztmanagement;

import java.time.LocalDate;
import java.util.List;

import org.digitale.patientenakte.web.TerminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TerminService {

	@Autowired
	private TerminRepository terminRepo;



	public List<Termin> ladeTermine() {
		return terminRepo.findAll();
	}

	public void terminLoeschen(Termin termin) {
		terminRepo.delete(termin);
	}
	
	public void terminSpeichern(Termin termin) {
		terminRepo.save(termin);
	}
}
