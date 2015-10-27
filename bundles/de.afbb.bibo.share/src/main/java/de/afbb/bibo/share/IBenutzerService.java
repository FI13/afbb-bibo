package de.afbb.bibo.share;

import java.util.List;

import de.afbb.bibo.share.model.Curator;

public interface IBenutzerService {

	Curator findById(Integer id);

	Curator create(Curator admin);

	List<Curator> list();

	boolean validateLogin(Curator admin);

}
