package org.digitale.patientenakte.web;

import org.digitale.patientenakte.web.arztmanagement.Termin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TerminRepository extends JpaRepository<Termin, Long>{

}
