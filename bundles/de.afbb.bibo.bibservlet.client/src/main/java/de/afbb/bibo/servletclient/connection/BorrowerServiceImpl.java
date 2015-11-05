package de.afbb.bibo.servletclient.connection;

import java.net.ConnectException;
import java.util.Collection;

import de.afbb.bibo.share.IBorrowerService;
import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.share.model.Copy;

public class BorrowerServiceImpl implements IBorrowerService{
	

	@Override
	public boolean exists(String firstName, String surname) throws ConnectException {
		ServerConnection.getInstance().request("/user/getBorrower", "GET", null, null);
		return false;
	}

	@Override
	public void create(Borrower borrower) throws ConnectException {
		HttpResponse resp = ServerConnection.getInstance().request("/user/createBorrower", "POST", null, null);
	}

	@Override
	public void update(Borrower Borrower) throws ConnectException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<Borrower> listAll() throws ConnectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Borrower> listOpen() throws ConnectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Copy> listLent(Borrower Borrower) throws ConnectException {
		// TODO Auto-generated method stub
		return null;
	}

}
