package org.digitale.patientenakte.fo;

import org.springframework.data.jpa.repository.JpaRepository;

//<zu speichernde Objekttyp = arzt, zu speichernde typ seiner eindeutige id = long>
//Von ebene her gehören sie zu BO --> Datnebankkommunikation
public interface ArztRepository extends JpaRepository<Arzt, Long> {
	    // optional: eigene Methoden
	
		//optional: eigene queries in SQL defineiren und dann als funktion benutzen
		//überall wo ich repsoitory @AUtowired kann ich diese funktion benutzen --> wenn syntaktisch korrekt	
	/*
		@Query("SELECT A FROM ARZT A WHERE A.NACHNAME CONTAINS K")
		List<Arzt> ladeAlleAerzteMitEinemKImNachnamen();
	*/
	}


