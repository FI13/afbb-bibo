package de.afbb.bibo.share;

import java.net.ConnectException;
import java.util.Collection;

import de.afbb.bibo.share.model.Exemplar;
import de.afbb.bibo.share.model.Medium;

public interface IExemplarService {

	/**
	 * tries to read the medium information for given isbn number (database first -> if not found amazon API)
	 * 
	 * @param isbn
	 * @return
	 */
	Medium getMedium(String isbn) throws ConnectException;

	/**
	 * registers new copies and mediums as necessary
	 * 
	 * @param copy
	 *            collection of grouped copies.
	 * @throws ConnectException
	 */
	void registerCopy(Collection<Collection<Exemplar>> copy) throws ConnectException;

	/**
	 * gets a list of all copies of all mediums
	 * 
	 * @return
	 */
	Collection<Collection<Exemplar>> listAll();

	/**
	 * gets a list of all copies for a given medium
	 * 
	 * @return
	 */
	Collection<Collection<Exemplar>> listCopies(String isbn);

}
