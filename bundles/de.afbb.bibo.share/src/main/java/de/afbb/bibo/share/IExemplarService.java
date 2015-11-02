package de.afbb.bibo.share;

import java.net.ConnectException;
import java.util.Collection;

import de.afbb.bibo.share.model.Copy;
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
	void registerCopy(Collection<Collection<Copy>> copy) throws ConnectException;

	/**
	 * gets a list of all copies of all mediums
	 * 
	 * @return
	 */
	Collection<Collection<Copy>> listAll();

	/**
	 * gets a list of all copies for a given medium
	 * 
	 * @return
	 */
	Collection<Collection<Copy>> listCopies(String isbn);

}
