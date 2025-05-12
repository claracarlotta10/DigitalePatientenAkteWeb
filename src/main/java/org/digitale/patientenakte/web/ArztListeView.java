package org.digitale.patientenakte.web;

import java.util.Optional;

import org.digitale.patientenakte.web.arztmanagement.Arzt;
import org.digitale.patientenakte.web.arztmanagement.ArztService;
import org.digitale.patientenakte.web.arztmanagement.Fachrichtungen;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.PostConstruct;

//Landing Page
@Route(" ")
@PageTitle("Arzt Liste")

//zeigt die Ansicht in der Toolbar an der Seite an 
//repräsentiert Maske 

public class ArztListeView extends Main {

	// tabelle zur Ansicht auf der Seite
	private Grid<Arzt> arztGrid;

	// Elemente zum Anzeigen der Arzt propertiess
	private TextField vornameField;
	private TextField nachnameField;
	private ComboBox<Fachrichtungen> fachrichtungComboBox;

	// Button um Arzt zu erstellen
	private Button arztSpeichernBtn;

	// Überprüfung ob column angeklickt wurde
	private Arzt gewaehlterArzt = null;

	private BeanValidationBinder<Arzt> binderArzt;

	@Autowired
	private ArztService arztService;

	// Konstruktor in dem die felder und das grid gesetzt werden
	// Lifecycle scope beachten
	public ArztListeView() {

	}

	// Aufruf, wenn Kosntruktion des Objektes selber abgeschlossen ist
	// Henne Ei Problem, weil arztService noch nicht verwendet werden kann -->
	// AutoWired läuft erst wenn Objekt erstellt wurde
	// Im Konstruktor kann nicht auf AUtoWired VAriablen zugegriffen werden
	@PostConstruct
	public void init() {

		vornameField = new TextField("Vorname");
		// bei jeder änderung an feld neu eübermittlung an server --> cobobox
		// automatisch
		vornameField.setValueChangeMode(ValueChangeMode.EAGER);
		nachnameField = new TextField("Nachname");
		nachnameField.setValueChangeMode(ValueChangeMode.EAGER);

		// ComboBox Element (entspricht DropDown menu) um die Fachrichtungen zur Wahl zu
		// stellen
		fachrichtungComboBox = new ComboBox<>("Fachrichtung");
		// Zugriff auf Fachrichtungen mit .values() - array aller möglichen enum
		// Werte/Konstanten
		fachrichtungComboBox.setItems(Fachrichtungen.values());
		// für jede Fachrichtung die Beschreibung anzeigen lassen (statt KARDIOLOGIE
		// Kardiologie)
		fachrichtungComboBox.setItemLabelGenerator(fachrichtung -> fachrichtung.getBeschreibung());

		binderArzt = new BeanValidationBinder<Arzt>(Arzt.class);

		// Binder konfiguration --> einmalig
		// forFIeld ermöglicht zusätzliche optionnen
		binderArzt.forField(vornameField).asRequired("Vorname ist ein Pflichtfeld").bind(Arzt::getVorname,
				Arzt::setVorname);
		binderArzt.forField(nachnameField).asRequired("Nachname ist ein Pflichtfeld").bind(Arzt::getNachname,
				Arzt::setNachname);
		binderArzt.forField(fachrichtungComboBox).asRequired("Fachrichtung ist ein Pflichtfeld")
				.bind(Arzt::getFachrichtung, Arzt::setFachrichtung);
		// Start initalisierung
		binderArzt.setBean(new Arzt());

		// reagiert immer wenn sich bean (also ARzt) im hintergdun ändert
		// prüft mit isValid, ob gesamter arzt valide ist
		// button erst enebled wenn alle eingaben valide sind als kombi
		binderArzt.addValueChangeListener(change -> {
			/*
			 * if(binderArzt.isValid()) { arztSpeichernBtn.setEnabled(true); }else {
			 * arztSpeichernBtn.setEnabled(false); }
			 */
			arztSpeichernBtn.setEnabled(binderArzt.isValid());
		});

		// wichtig: genau benennung wie im Arzt --> nachteil: würde man variable selbst
		// ändern würde fehler erst zur laufzeit auffallen
		// binderArzt.forField(vornameField).bind("vorname");
		// binderArzt.bind(vornameField, Arzt::getVorname, Arzt::setVorname);
		// binderArzt.bindInstanceFields(this); //dafür notwendig, dass die felder genau
		// so heißen wie in Arztklasse, also vornameFIeld dann vorname

		// Erstellung der Arzt-Übersicht
		arztGrid = new Grid<>(Arzt.class, false);

		// Für jeden Arzt den Vornamen und Nachnamen ermitteln und mit Überschriften zu
		// Grid hinzufügen

		arztGrid.addColumn(arzt -> arzt.getVorname()).setHeader("Vorname");
		arztGrid.addColumn(arzt -> arzt.getNachname()).setHeader("Nachname");
		arztGrid.addColumn(arzt -> arzt.getFachrichtung().getBeschreibung()).setHeader("Fachrichtung");

		// löschen Button für jeden arzt hinzufügen. Button ist enabled, wenn arzt
		// löschbar, sonst disabled
		arztGrid.addComponentColumn(arzt -> {
			Button arztLoeschen = new Button(new Icon(VaadinIcon.TRASH));
			arztLoeschen.addClickListener(click -> {
				arztService.arztLoeschen(arzt);
				arztGrid.setItems(arztService.ladeAerzte());
			});
			// wenn arztloschbar false, dann ist button disabled, wenn true, dann
			// setEnabled(true)
			arztLoeschen.setEnabled(arztService.arztLoeschbar(arzt));

			return arztLoeschen;
		}).setHeader("Arzt entfernen");

		// Grid befüllen mit liste der ärzte, size setzen
		arztGrid.setItems(arztService.ladeAerzte());
		arztGrid.setSizeFull();
		arztGrid.setEmptyStateText("Keine Ärzte gefunden.");

		// Bei Auswahl des Buttons wird event ausgeführt. Wenn Arzt angewählt dann
		// arztBeabrieten sonst arztAnlegen
		arztSpeichernBtn = new Button("Arzt hinzufügen", click -> {
			arztSpeichern();
			if (gewaehlterArzt != null) {
				// arzt bearbeiten
				arztGrid.deselectAll();
			} else {
				// arzt anlegen
				binderArzt.setBean(new Arzt());
				arztSpeichernBtn.setEnabled(false);
			}
		});
		arztSpeichernBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		// Ausgangszustand des buttons ausgegraut
		arztSpeichernBtn.setEnabled(false);

		// selection mode des grids auf single rows setzen
		arztGrid.setSelectionMode(SelectionMode.SINGLE);// sicherstellen, dass nur eine Zeile geklickt werden kann

		// Selection repäräsentiert null oder angewähltes Objekt (Arzt in der
		// angewählten Zeile)
		// Optional hilft bei Arbeit mit null --> zwingt consumer von Methode mit null
		// handling besser umzugehen
		arztGrid.addSelectionListener(selection -> {
			// bildet alle Zustände ab (anwählen/abwählen), d.h. optionalArzt ist ein
			// ArztObjekt oder null
			Optional<Arzt> optionalArzt = selection.getFirstSelectedItem();

			// true -> arzt ist angewählt
			if (optionalArzt.isPresent()) {

				gewaehlterArzt = optionalArzt.get();

				// text vom button ändern sobald column geklickt wurde
				arztSpeichernBtn.setText("Arzt speichern");

				// Felder befüllen mit Infos des ausgewählten Arztes
				binderArzt.setBean(gewaehlterArzt);
				// wenn bean vaid auf enabled setzen
				arztSpeichernBtn.setEnabled(binderArzt.isValid());

				// keine zeile wurde angewählt egal was davor angewählt war
			} else {
				gewaehlterArzt = null;

				// Felder leeren (sttat clear() und vorbereiten für die Anlage neuer Arzt
				binderArzt.setBean(new Arzt());
				arztSpeichernBtn.setText("Neuen Arzt anlegen");
				arztSpeichernBtn.setEnabled(false);

			}
		});

		// layout anlegen
		setSizeFull();
		VerticalLayout content = new VerticalLayout();
		HorizontalLayout arztErfassenLayout = new HorizontalLayout();
		arztErfassenLayout.add(vornameField, nachnameField, fachrichtungComboBox, arztSpeichernBtn);
		// default wäre oben alignen
		arztErfassenLayout.setAlignSelf(FlexComponent.Alignment.END, arztSpeichernBtn);
		content.add(arztErfassenLayout, arztGrid);
		content.setSizeFull();
		add(content);
	}

	// Methode um neuen Arzt hinzuzufügen
	private void arztSpeichern() {
		arztService.arztSpeichern(binderArzt.getBean());

		// grid aktualisieren durch Datenbank
		arztGrid.setItems(arztService.ladeAerzte());
		arztGrid.getDataProvider().refreshAll();
	}

}
