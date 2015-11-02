package de.afbb.bibo.share;

import java.net.ConnectException;
import java.util.Collection;

import de.afbb.bibo.share.model.Typ;

public interface ITypService {

	/**
	 * creates a new instance of type
	 * 
	 * @param type
	 * @throws ConnectException
	 */
	void create(Typ type) throws ConnectException;

	/**
	 * lists all types
	 * 
	 * @return
	 * @throws ConnectException
	 */
	Collection<Typ> list() throws ConnectException;

	Typ get(Integer id) throws ConnectException;

}
