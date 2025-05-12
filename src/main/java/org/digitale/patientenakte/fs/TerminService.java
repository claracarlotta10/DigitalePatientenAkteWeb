package org.digitale.patientenakte.fs;

import java.util.List;

import org.digitale.patientenakte.fo.Termin;

import com.vaadin.flow.component.datepicker.DatePicker;

public interface TerminService {

	public List<Termin> ladeTermine();

	public void terminLoeschen(Termin termin);

	public void terminSpeichern(Termin termin);

	public void deutschesDatumErstellen(DatePicker datePicker);

}
