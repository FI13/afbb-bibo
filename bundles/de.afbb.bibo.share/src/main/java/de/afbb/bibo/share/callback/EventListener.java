package de.afbb.bibo.share.callback;

import de.afbb.bibo.share.model.NavigationTreeNodeType;

/**
 * interface for listeners that want to be informed when a particular piece of
 * information has changed
 *
 * @author David Becker
 *
 */
public interface EventListener {

	/**
	 * marks the given tree node as out of date
	 *
	 * @param type
	 */
	void invalidate(NavigationTreeNodeType type);

	/**
	 * callback method to update a specific part of the tree
	 *
	 * @param target
	 *            item to update
	 * @param information
	 *            informations that where gathered by the service to display
	 */
	void update(IAggregatorTarget target, String[] information);

}
