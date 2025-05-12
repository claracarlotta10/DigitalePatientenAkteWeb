package org.digitale.patientenakte.web.arztmanagement;

import java.time.LocalDate;
import java.util.List;

import org.digitale.patientenakte.web.TerminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaadin.flow.component.datepicker.DatePicker;

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
	
	public void deutschesDatumErstellen(DatePicker datePicker) {
		DatePicker.DatePickerI18n germanDatePicker = new DatePicker.DatePickerI18n();
		germanDatePicker.setMonthNames(List.of("Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August",
				"September", "Oktober", "November", "Dezember"));
		germanDatePicker
				.setWeekdays(List.of("Sonntag", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag"));
		germanDatePicker.setWeekdaysShort(List.of("So", "Mo", "Di", "Mi", "Do", "Fr", "Sa"));
		germanDatePicker.setToday("Heute");
		germanDatePicker.setCancel("Abbrechen");
		datePicker.setI18n(germanDatePicker);
	}

}
