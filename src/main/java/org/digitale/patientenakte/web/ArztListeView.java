package org.digitale.patientenakte.web;

import org.digitale.patientenakte.web.arztmanagement.Arzt;
import org.digitale.patientenakte.web.arztmanagement.ArztSteuerung;
import org.digitale.patientenakte.web.arztmanagement.Fachrichtungen;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
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
	private final Button arztAnlegenBtn;

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

		// Bei Auswahl des Buttons wird event ausgeführt (Methodenaufruf von
		// arztAnlegen() s.u.)
		arztAnlegenBtn = new Button("Arzt hinzufügen", event -> arztAnlegen());
		arztAnlegenBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		// Erstellung der Arzt-Übersicht
		// false lasse, um erstellen für jedes Arztattribut zu vermeiden --> besser
		// selber anlegen
		arztGrid = new Grid<>(Arzt.class, false);

		// Für jeden Arzt den Vornamen und Nachnamen ermitteln und mit Überschriften zu
		// Grid hinzufügen

		arztGrid.addColumn(arzt -> arzt.getVorname()).setHeader("Vorname");
		arztGrid.addColumn(arzt -> arzt.getNachname()).setHeader("Nachname");
		arztGrid.addColumn(arzt -> arzt.getFachrichtung().getBeschreibung()).setHeader("Fachrichtung");
		
		//Button hinzufügen. Button ist enabled, wenn arzt löschbar, sonst disabled
		arztGrid.addComponentColumn(arzt -> {
			Button arztLoeschen = new Button(new Icon(VaadinIcon.TRASH));
			arztLoeschen.addClickListener(click -> {
				ArztSteuerung.arztLoeschen(arzt);
				arztGrid.setItems(ArztSteuerung.getAerzte());
			});
			// wenn arztloschbar false, dann ist button disabled, wenn true, dann setEnabled(true)
			arztLoeschen.setEnabled(ArztSteuerung.arztLoeschbar(arzt));
			return arztLoeschen;
		}).setHeader("Arzt entfernen");

		arztGrid.setItems(ArztSteuerung.getAerzte());

		// Größe der Kpmponentenplatzierung --> andere Komponenten werden auf
		// mindest-größe gesetzt
		arztGrid.setSizeFull();

		// Platzhalter, wenn liste leer
		arztGrid.setEmptyStateText("KeineÄrzte gefunden.");

		setSizeFull();

		// layout anlegen
		VerticalLayout content = new VerticalLayout();
		HorizontalLayout arztErfassenLayout = new HorizontalLayout();
		arztErfassenLayout.add(vornameField, nachnameField, fachrichtungComboBox, arztAnlegenBtn);
		// default wäre oben alignen
		arztErfassenLayout.setAlignSelf(FlexComponent.Alignment.END, arztAnlegenBtn);
		content.add(arztErfassenLayout, arztGrid);
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

		Notification.show("Arzt erfolgreich hinzugefügt!", 3000, Notification.Position.BOTTOM_END)
				.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
	}
}
