package de.afbb.bibo.servletclient.internal.stub;

import java.net.ConnectException;

import de.afbb.bibo.crypto.CryptoUtil;
import de.afbb.bibo.share.ICuratorService;
import de.afbb.bibo.share.model.Curator;

public class CuratorStubService implements ICuratorService {

	Curator curator = new Curator(1, "Hugo", "salt", CryptoUtil.hashPassword("password123", "salt"));

	@Override
	public boolean exists(final String curatorName) throws ConnectException {
		return curator.getName().equals(curatorName);
	}

	@Override
	public void create(final Curator curator) throws ConnectException {
	}

	@Override
	public void update(final Curator curator) throws ConnectException {
		this.curator = curator;
	}

	@Override
	public Curator get(final Integer id) throws ConnectException {
		// TODO Auto-generated method stub
		return null;
	}

}
