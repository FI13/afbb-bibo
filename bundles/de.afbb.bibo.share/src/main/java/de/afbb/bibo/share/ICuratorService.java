package de.afbb.bibo.share;

import java.net.ConnectException;

import de.afbb.bibo.share.model.Curator;

public interface ICuratorService {

	/**
	 * checks if a {@link Curator} exists with the given name
	 * 
	 * @param curator
	 *            curator to check
	 * @return
	 */
	boolean exists(String curatorName) throws ConnectException;;

	/**
	 * @param curator
	 */
	void create(Curator curator) throws ConnectException;

}
