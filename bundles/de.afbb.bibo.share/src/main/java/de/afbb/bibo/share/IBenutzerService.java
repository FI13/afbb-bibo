package de.afbb.bibo.share;

import java.util.List;

import de.afbb.bibo.share.model.Benutzer;

public interface IBenutzerService {

	Benutzer findById(Integer id);

	Benutzer create(Benutzer admin);

	List<Benutzer> list();

	boolean validateLogin(Benutzer admin);

}
