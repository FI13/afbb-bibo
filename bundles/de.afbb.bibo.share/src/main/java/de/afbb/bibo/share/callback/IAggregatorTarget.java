package de.afbb.bibo.share.callback;

/**
 * interface for a class that wants to get information from an aggregation
 * service
 *
 * @author David Becker
 */
public interface IAggregatorTarget {

	void setInformation(String information);

}
