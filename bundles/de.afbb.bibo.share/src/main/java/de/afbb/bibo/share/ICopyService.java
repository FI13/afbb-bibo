package de.afbb.bibo.share;

import java.net.ConnectException;
import java.util.Collection;

import de.afbb.bibo.share.callback.EventChangeProvider;
import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.share.model.Medium;

public interface ICopyService extends EventChangeProvider {

	/**
	 * updates the given copy
	 *
	 * @param copy
	 *            object to update
	 * @throws ConnectException
	 */
	void update(Copy copy) throws ConnectException;

	/**
	 * checks if there is a copy that the given barcode represents
	 *
	 * @param barcode
	 * @return
	 * @throws ConnectException
	 */
	boolean exists(String barcode) throws ConnectException;

	/**
	 * get the copy that is represented by the given barcode
	 *
	 * @param barcode
	 * @return
	 * @throws ConnectException
	 */
	Copy get(String barcode) throws ConnectException;

	/**
	 * gets all copies that are grouped to the given id
	 *
	 * @param id
	 *            of copy
	 * @return collection of all copies that are in the same group
	 * @throws ConnectException
	 */
	Collection<Copy> getGroupedCopies(Integer id) throws ConnectException;

	/**
	 * registers new copies and mediums as necessary. <br>
	 * copies with same {@link Copy#getGroupId()} belong to each other
	 *
	 * @param copies
	 *            collection of grouped copies.
	 * @throws ConnectException
	 */
	void registerCopies(Collection<Copy> copies) throws ConnectException;

	/**
	 * marks copies as returned
	 *
	 * @param copies
	 *            collection of copies.
	 * @throws ConnectException
	 */
	void returnCopies(Collection<Copy> copies) throws ConnectException;

	/**
	 * marks copies as lend
	 *
	 * @param copies
	 *            collection of copies.
	 * @param printList
	 *            true if a list should be printed
	 * @throws ConnectException
	 */
	void lendCopies(Collection<Copy> copies, boolean printList) throws ConnectException;

	/**
	 * gets a list of all copies of all mediums
	 *
	 * @return
	 */
	Collection<Copy> listAll() throws ConnectException;

	/**
	 * gets a list of all copies for a given medium
	 *
	 * @param medium
	 *            medium of the copies you want to receive
	 * @return
	 * @throws ConnectException
	 */
	Collection<Copy> listCopies(Medium medium) throws ConnectException;

	/**
	 * deletes the given copy
	 *
	 * @param copy
	 * @throws ConnectException
	 */
	void delete(Copy copy) throws ConnectException;
}
