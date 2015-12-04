package de.afbb.bibo.share;

import java.net.ConnectException;
import java.util.Collection;

import de.afbb.bibo.share.callback.EventChangeProvider;
import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.share.model.Medium;

public interface IMediumService extends EventChangeProvider {

	/**
	 * updates the given medium
	 *
	 * @param medium
	 *            object to update
	 * @throws ConnectException
	 */
	void update(Medium medium) throws ConnectException;

	/**
	 * creates the given medium and populates the ID field
	 *
	 * @param medium
	 * @throws ConnectException
	 */
	void create(Medium medium) throws ConnectException;

	/**
	 * tries to read the medium from the database with the given ID
	 *
	 * @param id
	 *            database id to find medium by
	 * @throws ConnectException
	 * @return
	 */
	Medium getMedium(int id) throws ConnectException;

	/**
	 * tries to read the medium information for given ISBN number (database
	 * first -> if not found Amazon API)
	 *
	 * @param isbn
	 *            ISBN number to find medium by
	 * @return
	 */
	Medium getMedium(String isbn) throws ConnectException;

	/**
	 * returns a collection of borrowers that currently have lent this medium
	 * specified by ISBN
	 *
	 * @param isbn
	 * @return
	 * @throws ConnectException
	 */
	Collection<Borrower> listLent(String isbn) throws ConnectException;

	/**
	 * list all media items
	 *
	 * @return
	 * @throws ConnectException
	 */
	Collection<Medium> list() throws ConnectException;

	/**
	 * deletes the given medium
	 *
	 * @param medium
	 * @throws ConnectException
	 */
	void delete(Medium medium) throws ConnectException;
}
