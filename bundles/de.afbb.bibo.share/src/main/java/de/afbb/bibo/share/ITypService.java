package de.afbb.bibo.share;

import java.net.ConnectException;
import java.util.Collection;

import de.afbb.bibo.share.model.MediumType;

/**
 * service interface for a service that can read & create {@link MediumType}s
 *
 * @author David Becker
 *
 */
public interface ITypService {

	/**
	 * creates a new instance of type
	 *
	 * @param type
	 * @throws ConnectException
	 */
	void create(MediumType type) throws ConnectException;

	/**
	 * lists all types
	 *
	 * @return
	 * @throws ConnectException
	 */
	Collection<MediumType> list() throws ConnectException;

	MediumType get(Integer id) throws ConnectException;

}
