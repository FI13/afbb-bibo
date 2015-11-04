/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.afbb.bibo.servletclient.connection;

import java.net.ConnectException;

import de.afbb.bibo.servletclient.interfaces.ICuratorService;
import de.afbb.bibo.share.model.Curator;

/**
 * @author fi13.pendrulat
 */
public class CuratorServiceImpl implements ICuratorService {

	@Override
	public boolean exists(final String curatorName) throws ConnectException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void create(final Curator curator) throws ConnectException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void update(final Curator curator) throws ConnectException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	}

}
