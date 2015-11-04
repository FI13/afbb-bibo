/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.afbb.bibo.servletclient.interfaces;

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

	void create(Curator curator) throws ConnectException;

	void update(Curator curator) throws ConnectException;

}
