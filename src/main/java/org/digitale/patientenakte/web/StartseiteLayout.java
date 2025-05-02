package org.digitale.patientenakte.web;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Layout
//the root layout can be defined using the @Layout annotation, which tells the router to render all routes or views inside of it
//component for the root layout of a Vaadin application. It provides predefined areas for the navigation drawer, the header, and the view’s content
public class StartseiteLayout extends AppLayout {

	private static final long serialVersionUID = 1L;

	StartseiteLayout() {
		// The drawer can switch between a fixed area next to the view’s content and an
		// expandable panel, toggled via the drawer toggle.
		// It typically contains the application’s primary navigation, such as a Side
		// Navigation component.
		// Toggle anlegen und zu navbar oben hinzufügen
		DrawerToggle toggle = new DrawerToggle();
		addToNavbar(toggle);

		// Permanente H1 überschrift und styling der überschrift nach vaadin bsp.
		H1 ueberschrift = new H1("Digitale Patientenakte");
		ueberschrift.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");

		// hinzufügen zur permanenten navigation bar (horizontal, bereitgestellt von
		// AppLayout)
		addToNavbar(ueberschrift);

		// Statische Navigation an der seite
		SideNav navigation = new SideNav();
		navigation.addItem(new SideNavItem("Arzt Liste", ArztListeView.class));
		navigation.addItem(new SideNavItem("Termine", TerminListeView.class));

		// Navigation in scrollbaren Bereich einbetten
		Scroller scroller = new Scroller(navigation);
		scroller.addClassName(LumoUtility.Padding.SMALL);
		// hinzufügen zu drawer
		addToDrawer(scroller);

		setPrimarySection(Section.DRAWER);

	}

}
