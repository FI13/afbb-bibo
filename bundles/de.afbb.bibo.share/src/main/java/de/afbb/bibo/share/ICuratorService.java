package de.afbb.bibo.share;

import de.afbb.bibo.share.model.Curator;

public interface ICuratorService {

	/**
	 * checks if a {@link Curator} exists with the given name
	 * 
	 * @param curator
	 *            curator to check
	 * @return
	 */
	boolean exists(Curator curator);

}
