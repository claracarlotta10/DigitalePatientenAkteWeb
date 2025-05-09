package org.digitale.patientenakte.web;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.digitale.patientenakte.web.arztmanagement.Arzt;
import org.digitale.patientenakte.web.arztmanagement.ArztService;
import org.digitale.patientenakte.web.arztmanagement.Termin;
import org.digitale.patientenakte.web.arztmanagement.TerminService;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.Route;

@Route("termine")
public class TerminListeView extends Main {

	// tabelle zur Ansicht auf der Seite
	private Grid<Termin> terminGrid;

	// Variablen des Termins als Felder sowie button um Termine zu erstellen
	private ComboBox<Arzt> behandelndeAerzteComboBox;
	private DatePicker datePicker;
	private Button terminAnlegenBtn;
	private BeanValidationBinder<Termin> binderTermin;

	private Termin gewaehlterTermin = null;
	@Autowired
	private ArztService arztService;
	@Autowired
	private TerminService terminService;

	// Konstruktor in dem die felder und das grid gesetzt werden
	// Lifecycle scope beachten
	public TerminListeView() {
	}

	public void init() {
		// Datumsauswahl aus Kalender
		datePicker = new DatePicker("Datum");
		// Setzen des Datums auf deutsche Standards per internationalization (von vaadin
		// doku)
		// DatePickerI18n ist eine innere statische klasse von DatePicker, deshlab
		// dieser Aufruf
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
		behandelndeAerzteComboBox.setItems(arztService.ladeAerzte());
		// für jeden arzt den nachnamen anzeigen lassen und dr. davor hängen
		behandelndeAerzteComboBox.setItemLabelGenerator(arzt -> "DR. " + arzt.getNachname());

		// TODO Binder einfügen und die variablen datePicker an Termin.datum und
		// ...combo an
		binderTermin = new BeanValidationBinder<Termin>(Termin.class);

		binderTermin.forField(behandelndeAerzteComboBox).asRequired().bind(Termin::getBehandelnderArzt,
				Termin::setBehandelnderArzt);
		binderTermin.forField(datePicker).asRequired().bind(Termin::getDatum, Termin::setDatum);

		binderTermin.setBean(new Termin());

		terminGrid = new Grid<>(Termin.class, false);

		// Für jeden Termin das datum und die id ermitteln und mit Überschriften zu Grid
		// hinzufügen
		terminGrid.addColumn(termin -> termin.getDatum()).setHeader("Datum");
		terminGrid.addColumn(termin -> termin.getId()).setHeader("Termin ID");
		// zeige für jeden termin den zugehörigen behnandelnden arzt an
		terminGrid.addColumn(termin -> termin.getBehandelnderArzt().getNachname()).setHeader("Behandelnder Arzt");
		// grid mit terminen befüllen
		terminGrid.setItems(terminService.ladeTermine());
		terminGrid.setSizeFull();
		// Platzhalter, wenn liste leer
		terminGrid.setEmptyStateText("Keine Termine gefunden.");

		// Bei Auswahl des Buttons wird event ausgeführt (Methodenaufruf von
		// terminAnlegen())
		terminAnlegenBtn = new Button("Termin hinzufügen", click -> {
			terminSpeichern();
			if (gewaehlterTermin != null) {
				terminGrid.deselectAll();
			} else {
				// arzt anlegen
				binderTermin.setBean(new Termin());
				terminAnlegenBtn.setEnabled(false);
			}
		});
		terminAnlegenBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		terminAnlegenBtn.setEnabled(false);

		// selection mode des grids auf single rows setzen
		terminGrid.setSelectionMode(SelectionMode.SINGLE);// sicherstellen, dass nur eine Zeile geklickt werden kann

		terminGrid.addSelectionListener(selection -> {
			// bildet alle Zustände ab (anwählen/abwählen), d.h. optionalArzt ist ein
			// ArztObjekt oder null
			Optional<Termin> optionalTermin = selection.getFirstSelectedItem();

			// true -> arzt ist angewählt
			if (optionalTermin.isPresent()) {

				gewaehlterTermin = optionalTermin.get();

				// text vom button ändern sobald column geklickt wurde
				terminAnlegenBtn.setText("Arzt speichern");

				// Felder befüllen mit Infos des ausgewählten Arztes
				binderTermin.setBean(gewaehlterTermin);
				// wenn bean vaid auf enabled setzen
				terminAnlegenBtn.setEnabled(binderTermin.isValid());

				// keine zeile wurde angewählt egal was davor angewählt war
			} else {
				gewaehlterTermin = null;

				// Felder leeren (sttat clear() und vorbereiten für die Anlage neuer Arzt
				binderTermin.setBean(new Termin());
				terminAnlegenBtn.setText("Neuen Arzt anlegen");
				terminAnlegenBtn.setEnabled(false);

			}
		});

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
	private void terminSpeichern() {
		terminService.terminSpeichern(binderTermin.getBean());

		// Um Änderungen am Grid sofort sichtbar zu machen (also neu hinzugefügten Arzt
		// sofort anzeigen)

		// grid aktualisieren durch Datenbank
		terminGrid.setItems(terminService.ladeTermine());
		terminGrid.getDataProvider().refreshAll();

		// Um Änderungen am Grid sofort sichtbar zu machen (also neu hinzugefügten
		// Termin sofort anzeigen)
		terminGrid.getDataProvider().refreshAll();

		Notification.show("Termin erfolgreich hinzugefügt!", 3000, Notification.Position.BOTTOM_END)
				.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
	}
}
