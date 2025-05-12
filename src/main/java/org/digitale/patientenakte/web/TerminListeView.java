package org.digitale.patientenakte.web;

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
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.PostConstruct;

@Route("termine")
@PageTitle("Termin Liste")
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
	
	@PostConstruct
	public void init() {
		// Datumsauswahl aus Kalender
		datePicker = new DatePicker("Datum");
		// Setzen des Datums auf deutsche Standards per internationalization
		// DatePickerI18n ist eine innere statische klasse von DatePicker, deshlab
		// dieser Aufruf
		terminService.deutschesDatumErstellen(datePicker);

		// ComboBox Element um die Ärzte zur Wahl zu stellen
		behandelndeAerzteComboBox = new ComboBox<>("Behandelnder Arzt");
		// Zugriff auf erfasste ärzte
		behandelndeAerzteComboBox.setItems(arztService.ladeAerzte());
		// für jeden arzt den nachnamen anzeigen lassen und dr. davor hängen
		behandelndeAerzteComboBox.setItemLabelGenerator(arzt -> "DR. " + arzt.getNachname());

		//Binder konfiguration
		binderTermin = new BeanValidationBinder<Termin>(Termin.class);
		binderTermin.forField(behandelndeAerzteComboBox).asRequired().bind(Termin::getBehandelnderArzt,
				Termin::setBehandelnderArzt);
		binderTermin.forField(datePicker).asRequired().bind(Termin::getDatum, Termin::setDatum);
		
		//Start initalisierung als leerer Termin
		binderTermin.setBean(new Termin());
		
		// reagiert immer wenn sich bean (also Termin) im hintergrunf ändert
		// prüft mit isValid, ob gesamter termin valide ist
		// button erst enebled wenn alle eingaben valide sind als kombi
		binderTermin.addValueChangeListener(change -> {
			terminAnlegenBtn.setEnabled(binderTermin.isValid());
		});

		//Termin Liste/Grid anlegen
		terminGrid = new Grid<>(Termin.class, false);

		// Für jeden Termin das datum und die id ermitteln und mit Überschriften zu Grid
		// hinzufügen
		terminGrid.addColumn(termin -> termin.getDatum()).setHeader("Datum");
		terminGrid.addColumn(termin -> termin.getId()).setHeader("Termin ID");
		// zeige für jeden termin den zugehörigen behnandelnden arzt an
		terminGrid.addColumn(termin -> termin.getBehandelnderArzt().getNachname()).setHeader("Behandelnder Arzt");
		
		terminGrid.addComponentColumn(termin ->{
			Button terminLoeschen = new Button (new Icon(VaadinIcon.TRASH));
			terminLoeschen.addClickListener(clicke ->{
				terminService.terminLoeschen(termin);
				terminGrid.setItems(terminService.ladeTermine());
			});
			return terminLoeschen;
		}).setHeader("Termin entfenren");
		
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
			// bildet alle Zustände ab (anwählen/abwählen), d.h. optionaltermin ist ein
			// terminObjekt oder null
			Optional<Termin> optionalTermin = selection.getFirstSelectedItem();

			// true -> arzt ist angewählt
			if (optionalTermin.isPresent()) {

				gewaehlterTermin = optionalTermin.get();

				// text vom button ändern sobald column geklickt wurde
				terminAnlegenBtn.setText("termin speichern");

				// Felder befüllen mit Infos des ausgewählten Arztes
				binderTermin.setBean(gewaehlterTermin);
				// wenn bean vaid auf enabled setzen
				terminAnlegenBtn.setEnabled(binderTermin.isValid());

				// keine zeile wurde angewählt egal was davor angewählt war
			} else {
				gewaehlterTermin = null;

				// Felder leeren (sttat clear() und vorbereiten für die Anlage neuer Arzt
				binderTermin.setBean(new Termin());
				terminAnlegenBtn.setText("Neuen termin anlegen");
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
		content.setSizeFull();
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


		Notification.show("Termin erfolgreich hinzugefügt!", 3000, Notification.Position.BOTTOM_END)
				.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
	}
}
