package de.afbb.bibo.share;

import java.net.ConnectException;
import java.util.Collection;

import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.share.model.Copy;

public interface IBorrowerService {

	boolean exists(String firstName, String surname) throws ConnectException;

	void create(Borrower borrower) throws ConnectException;

	void update(Borrower Borrower) throws ConnectException;

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
	 * @param Borrower
	 * @return
	 * @throws ConnectException
	 */
	Collection<Copy> listLent(Borrower Borrower) throws ConnectException;

}
