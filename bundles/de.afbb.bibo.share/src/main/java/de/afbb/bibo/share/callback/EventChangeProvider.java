package de.afbb.bibo.share.callback;

/**
 * interface for classes that can inform listeners of events
 *
 * @author David Becker
 *
 */
public interface EventChangeProvider {

	/**
	 * adds this listener to the list of informed listeners
	 *
	 * @param listener
	 *            to notify
	 */
	void register(EventListener listener);

}
