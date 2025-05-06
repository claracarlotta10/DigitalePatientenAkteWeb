package org.digitale.patientenakte.web;

import java.util.Optional;

import org.digitale.patientenakte.web.arztmanagement.Arzt;
import org.digitale.patientenakte.web.arztmanagement.ArztSteuerung;
import org.digitale.patientenakte.web.arztmanagement.Fachrichtungen;

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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

//Landing Page
@Route(" ")
@PageTitle("Arzt Liste")

//zeigt die Ansicht in der Toolbar an der Seite an 
public class ArztListeView extends Main {

	// tabelle zur Ansicht auf der Seite
	private final Grid<Arzt> arztGrid;

	// Elemente zum Anzeigen der Arzt propertiess
	private final TextField vornameField;
	private final TextField nachnameField;
	private final ComboBox<Fachrichtungen> fachrichtungComboBox;

	// Button um Arzt zu erstellen
	private final Button arztSpeichernBtn;
	// private final Button arztAnlegenBtn;

	// Überprüfung ob column angeklickt wurde
	private Arzt gewaehlterArzt = null;

	// Konstruktor in dem die felder und das grid gesetzt werden
	// Lifecycle scope beachten
	public ArztListeView() {
		vornameField = new TextField("Vorname");

		nachnameField = new TextField("Nachname");

		// ComboBox Element (entspricht DropDown menu) um die Fachrichtungen zur Wahl zu
		// stellen
		fachrichtungComboBox = new ComboBox<>("Fachrichtung");
		// Zugriff auf Fachrichtungen mit .values() - array aller möglichen enum
		// Werte/Konstanten
		fachrichtungComboBox.setItems(Fachrichtungen.values());
		// für jede Fachrichtung die Beschreibung anzeigen lassen (statt KARDIOLOGIE
		// Kardiologie)
		fachrichtungComboBox.setItemLabelGenerator(fachrichtung -> fachrichtung.getBeschreibung());

		// Erstellung der Arzt-Übersicht
		arztGrid = new Grid<>(Arzt.class, false);

		// Für jeden Arzt den Vornamen und Nachnamen ermitteln und mit Überschriften zu
		// Grid hinzufügen

		arztGrid.addColumn(arzt -> arzt.getVorname()).setHeader("Vorname");
		arztGrid.addColumn(arzt -> arzt.getNachname()).setHeader("Nachname");
		arztGrid.addColumn(arzt -> arzt.getFachrichtung().getBeschreibung()).setHeader("Fachrichtung");

		// Button hinzufügen. Button ist enabled, wenn arzt löschbar, sonst disabled
		arztGrid.addComponentColumn(arzt -> {
			Button arztLoeschen = new Button(new Icon(VaadinIcon.TRASH));
			arztLoeschen.addClickListener(click -> {
				ArztSteuerung.arztLoeschen(arzt);
				arztGrid.setItems(ArztSteuerung.getAerzte());
			});
			// wenn arztloschbar false, dann ist button disabled, wenn true, dann
			// setEnabled(true)
			arztLoeschen.setEnabled(ArztSteuerung.arztLoeschbar(arzt));
			return arztLoeschen;
		}).setHeader("Arzt entfernen");

		// Grid befüllen mit liste der ärzte, size setzen
		arztGrid.setItems(ArztSteuerung.getAerzte());
		arztGrid.setSizeFull();
		arztGrid.setEmptyStateText("Keine Ärzte gefunden.");

		// Bei Auswahl des Buttons wird event ausgeführt (Methodenaufruf von
		// arztAnlegen() s.u.)
		arztSpeichernBtn = new Button("Arzt hinzufügen", click -> {
			if (gewaehlterArzt != null) {
				arztBearbeiten();
			} else {
				arztAnlegen();
			}
		});
		arztSpeichernBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		// vaadin grid doku single selection
		// selection mode des grids auf single rows setzen( multi wäre mehrere zeilen
		// auswählbar)
		arztGrid.setSelectionMode(SelectionMode.SINGLE);// sicherstellen, dass nur eine Zeile geklickt werden kann
		// valueCHangeListener --> reagiert auf Änderung des Arztes

		arztGrid.addSelectionListener(selection -> {
			Optional<Arzt> optionalArzt = selection.getFirstSelectedItem(); // bildet alle Zustände ab
																			// (anwählen/abwählen)
			// Selection repäräsentiert null oder angewähltes Objekt
			// Optional hilft bei Arbeit mit null --> zwingt consumer von Methode mit Null
			// handling besser umzugehen
			// true -> arzt ist angewählt
			if (optionalArzt.isPresent()) {
				gewaehlterArzt = optionalArzt.get();
				// text vom button ändern sobald columg geklickt wurde
				arztSpeichernBtn.setText("Arzt speichern");

				// Felder befüllen mit Infos des ausgewählten Arztes
				vornameField.setValue(gewaehlterArzt.getVorname());
				nachnameField.setValue(gewaehlterArzt.getNachname());
				fachrichtungComboBox.setValue(gewaehlterArzt.getFachrichtung());

				// keine zeile wurde angewählt egal was davor angewählt war
			} else {
				gewaehlterArzt = null;

				// Felder leeren
				vornameField.clear();
				nachnameField.clear();
				fachrichtungComboBox.clear();
				arztSpeichernBtn.setText("Neuen Arzt anlegen");

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
	private void arztAnlegen() {
		// Infos zu Arzt aus Nutzereingabe einholen
		String vorname = vornameField.getValue();
		String nachname = nachnameField.getValue();
		Fachrichtungen fachrichtung = fachrichtungComboBox.getValue();

		ArztSteuerung.arztAnlegen(vorname, nachname, fachrichtung);

		// Um Änderungen am Grid sofort sichtbar zu machen (also neu hinzugefügten Arzt
		// sofort anzeigen)
		arztGrid.getDataProvider().refreshAll();

		// EIngabefelder wieder leeren
		vornameField.clear();
		nachnameField.clear();
		fachrichtungComboBox.clear();
	}

	// Methode um Arzt zu bearbeiten
	private void arztBearbeiten() {
		// Infos zu Arzt Änderung aus Nutzereingabe einholen
		String vorname = vornameField.getValue();
		String nachname = nachnameField.getValue();
		Fachrichtungen fachrichtung = fachrichtungComboBox.getValue();

		ArztSteuerung.arztBearbeiten(gewaehlterArzt, vorname, nachname, fachrichtung);

		// Um Änderungen am Grid sofort sichtbar zu machen (also bearbeiteten Arzt
		// sofort anzeigen)
		arztGrid.getDataProvider().refreshAll();
		// gewählte Zeile "abwählen" --> springt in selectionListener und cleared dort
		// InputFelder und setzt arzt null
		arztGrid.deselectAll();

	}
}
