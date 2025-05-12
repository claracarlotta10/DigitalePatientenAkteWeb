package org.digitale.patientenakte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;

@SpringBootApplication
//zum wechseln von zB Dark mode zu normal Theme --> auch eigene Themes möglich
@Theme("default")
public class PatientenAktenApplikationWeb implements AppShellConfigurator{

	public static void main(String[] args) {
		SpringApplication.run(PatientenAktenApplikationWeb.class, args);

	}

}

