package de.afbb.bibo.share.internal.stub.impl;

import java.net.ConnectException;
import java.util.Collection;

import de.afbb.bibo.share.ICopyService;
import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.share.model.Medium;

public class CopyStubService implements ICopyService {

	@Override
	public void update(Copy copy) throws ConnectException {
	}

	@Override
	public Copy get(String barcode) throws ConnectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Copy> getGrouped(Integer id) throws ConnectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerCopy(Collection<Copy> copies) throws ConnectException {
		// TODO Auto-generated method stub
		throw new ConnectException();

	}

	@Override
	public Collection<Copy> listAll() throws ConnectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Copy> listCopies(Medium medium) throws ConnectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Copy copy) throws ConnectException {
		// TODO Auto-generated method stub

	}

}
