package de.afbb.bibo.share;

import java.net.ConnectException;
import java.util.Collection;

import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.share.model.Medium;

public interface ICopyService {

	void update(Copy copy) throws ConnectException;

	/**
	 * gets all copies that are grouped to the given id
	 * 
	 * @param id
	 *            of copy
	 * @return collection of all copies that are in the same group
	 * @throws ConnectException
	 */
	Collection<Copy> getGrouped(Integer id) throws ConnectException;

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
	Collection<Collection<Copy>> listAll() throws ConnectException;

	/**
	 * gets a list of all copies for a given medium
	 * 
	 * @return
	 */
	Collection<Collection<Copy>> listCopies(String isbn) throws ConnectException;

	/**
	 * returns a collection of borrowers that currently have lent a copy with the given isbn
	 * 
	 * @param isbn
	 * @return
	 * @throws ConnectException
	 */
	Collection<Borrower> listLent(String isbn) throws ConnectException;

}
