package de.afbb.bibo.share;

import java.net.ConnectException;
import java.util.Collection;

import de.afbb.bibo.share.callback.EventChangeProvider;
import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.share.model.Copy;

public interface IBorrowerService extends EventChangeProvider {

	boolean exists(String firstName, String surname) throws ConnectException;

	void create(Borrower borrower) throws ConnectException;

	Borrower get(Integer id) throws ConnectException;

	void update(Borrower borrower) throws ConnectException;

	/**
	 * creates a list with all borrowers
	 *
	 * @return
	 * @throws ConnectException
	 */
	Collection<Borrower> listAll() throws ConnectException;

	/**
	 * creates a list with all borrowers that currently have copies
	 *
	 * @return
	 * @throws ConnectException
	 */
	Collection<Borrower> listOpen() throws ConnectException;

	/**
	 * lists all copies that the borrower has lent
	 *
	 * @param borrower
	 * @return
	 * @throws ConnectException
	 */
	Collection<Copy> listLent(Borrower borrower) throws ConnectException;

}
