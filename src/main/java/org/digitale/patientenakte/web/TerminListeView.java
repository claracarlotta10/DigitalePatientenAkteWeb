package org.digitale.patientenakte.web;

import java.time.LocalDate;
import java.util.List;

import org.digitale.patientenakte.web.arztmanagement.Arzt;
import org.digitale.patientenakte.web.arztmanagement.ArztSteuerung;
import org.digitale.patientenakte.web.arztmanagement.Termin;
import org.digitale.patientenakte.web.arztmanagement.TerminSteuerung;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.data.binder.Binder;


@Route("termine")
public class TerminListeView extends Main {

	// tabelle zur Ansicht auf der Seite
	private final Grid<Termin> terminGrid;

	// Variablen des Termins als Felder sowie button um Termine zu erstellen
	private final ComboBox<Arzt> behandelndeAerzteComboBox;
	private final DatePicker datePicker;
	private final Button terminAnlegenBtn;
	private final Binder<Termin> binder; 
	
	// Konstruktor in dem die felder und das grid gesetzt werden
	// Lifecycle scope beachten
	public TerminListeView() {

		binder = new Binder<>(Termin.class);

		// Datumsauswahl aus Kalender
		datePicker = new DatePicker("Datum");
		//Setzen des Datums auf deutsche Standards per internationalization (von vaadin doku)
		//DatePickerI18n ist eine innere statische klasse von DatePicker, deshlab dieser Aufruf
		DatePicker.DatePickerI18n germanDatePicker = new DatePicker.DatePickerI18n();
		germanDatePicker.setMonthNames(List.of("Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August",
				"September", "Oktober", "November", "Dezember"));
		germanDatePicker
				.setWeekdays(List.of("Sonntag", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag"));
		germanDatePicker.setWeekdaysShort(List.of("So", "Mo", "Di", "Mi", "Do", "Fr", "Sa"));
		germanDatePicker.setToday("Heute");
		germanDatePicker.setCancel("Abbrechen");

		datePicker.setI18n(germanDatePicker);

		// ComboBox Element um die Ärzte zur Wahl zu stellen
		behandelndeAerzteComboBox = new ComboBox<>("Behandelnder Arzt");
		// Zugriff auf erfasste ärzte
		behandelndeAerzteComboBox.setItems(ArztSteuerung.getAerzte());
		// für jeden arzt den nachnamen anzeigen lassen und dr. davor hängen
		behandelndeAerzteComboBox.setItemLabelGenerator(arzt -> "DR. " + arzt.getNachname());
		
		//TODO Binder einfügen und die variablen datePicker an Termin.datum und ...combo an 
		binder.bind(behandelndeAerzteComboBox, Termin::getBehandelnderArzt, Termin::setBehandelnderArzt);
		

		// Bei Auswahl des Buttons wird event ausgeführt (Methodenaufruf von
		// terminAnlegen())
		terminAnlegenBtn = new Button("Termin hinzufügen", event -> terminAnlegen());
		terminAnlegenBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		// Erstellung der Arzt-Übersicht
		// false lasse, um erstellen für jedes Arztattribut zu vermeiden --> besser
		// selber anlegen
		terminGrid = new Grid<>(Termin.class, false);

		// Für jeden Termin das datum und die id ermitteln und mit Überschriften zu Grid
		// hinzufügen
		terminGrid.addColumn(termin -> termin.getLocalDate()).setHeader("Datum");
		terminGrid.addColumn(termin -> termin.getId()).setHeader("Termin ID");

		// zeige für jeden termin den zugehörigen behnandelnden arzt an
		terminGrid.addColumn(termin -> termin.getBehandelnderArzt().getNachname()).setHeader("Behandelnder Arzt");

		// grid mit terminen befüllen
		terminGrid.setItems(TerminSteuerung.getTermine());
		terminGrid.setSizeFull();

		// Platzhalter, wenn liste leer
		terminGrid.setEmptyStateText("Keine Termine gefunden.");
		setSizeFull();

		// layout anlegen
		VerticalLayout content = new VerticalLayout();
		HorizontalLayout terminErfassenLayout = new HorizontalLayout();
		terminErfassenLayout.add(datePicker, behandelndeAerzteComboBox, terminAnlegenBtn);
		terminErfassenLayout.setAlignSelf(FlexComponent.Alignment.END, terminAnlegenBtn);
		content.add(terminErfassenLayout, terminGrid);
		add(content);
	}

	// Methode um neuen Termin hinzuzufügen
	private void terminAnlegen() {
		// Infos zu Termin aus Nutzereingabe einholen
		//TODO hier stattdessen mit Binder arbeiten, da binder dann ja die Varibalen des Termins enthält
		//binder.writeBean(null);;
		Arzt behandelnderArzt = behandelndeAerzteComboBox.getValue();
		LocalDate localDate = datePicker.getValue();

		TerminSteuerung.terminAnlegen(behandelnderArzt, localDate);

		// Um Änderungen am Grid sofort sichtbar zu machen (also neu hinzugefügten
		// Termin sofort anzeigen)
		terminGrid.getDataProvider().refreshAll();

		// EIngabefelder wieder leeren
		datePicker.clear();
		behandelndeAerzteComboBox.clear();

		Notification.show("Termin erfolgreich hinzugefügt!", 3000, Notification.Position.BOTTOM_END)
				.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
	}
}
