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

	void invalidate(NavigationTreeNodeType type);

}
