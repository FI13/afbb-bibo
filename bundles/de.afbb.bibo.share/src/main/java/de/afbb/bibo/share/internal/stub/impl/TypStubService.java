package de.afbb.bibo.share.internal.stub.impl;

import java.net.ConnectException;

import de.afbb.bibo.share.ITypService;
import de.afbb.bibo.share.model.Typ;

public class TypStubService implements ITypService {

	@Override
	public void create(final Typ type) throws ConnectException {
		// nothing to do for stub
	}

}
