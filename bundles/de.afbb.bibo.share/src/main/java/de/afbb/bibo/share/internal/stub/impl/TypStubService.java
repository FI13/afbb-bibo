package de.afbb.bibo.share.internal.stub.impl;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.afbb.bibo.share.ITypService;
import de.afbb.bibo.share.model.Typ;

public class TypStubService implements ITypService {

	List<Typ> types = new ArrayList<Typ>();

	{
		final Typ t1 = new Typ();
		t1.setId(0);
		t1.setTypname("Buch");
		t1.setIconPath("icons/16x16book2.png");
		types.add(t1);
		final Typ t2 = new Typ();
		t2.setId(1);
		t2.setTypname("CD");
		t2.setIconPath("icons/16x16cd.png");
		types.add(t2);
	}

	@Override
	public void create(final Typ type) throws ConnectException {
		types.add(type);
	}

	@Override
	public Collection<Typ> list() throws ConnectException {
		return types;
	}

	@Override
	public Typ get(final Integer id) throws ConnectException {
		try {
			return types.get(id);
		} catch (final IndexOutOfBoundsException e) {
			return null;
		}
	}

}
