package de.afbb.bibo.share;

import java.net.ConnectException;

import de.afbb.bibo.share.model.Typ;

public interface ITypService {

	/**
	 * creates a new instance of type
	 * 
	 * @param type
	 * @throws ConnectException
	 */
	void create(Typ type) throws ConnectException;

}
