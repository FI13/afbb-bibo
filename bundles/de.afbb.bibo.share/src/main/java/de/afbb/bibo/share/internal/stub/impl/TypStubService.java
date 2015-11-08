package de.afbb.bibo.share.internal.stub.impl;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.afbb.bibo.share.ITypService;
import de.afbb.bibo.share.model.IconType;
import de.afbb.bibo.share.model.MediumType;

public class TypStubService implements ITypService {

	List<MediumType> types = new ArrayList<MediumType>();

	{
		final MediumType t1 = new MediumType();
		t1.setId(0);
		t1.setName("Buch");
		t1.setIcon(IconType.BOOK);
		types.add(t1);
		final MediumType t2 = new MediumType();
		t2.setId(1);
		t2.setName("CD");
		t2.setIcon(IconType.CD);
		types.add(t2);
	}

	@Override
	public void create(final MediumType type) throws ConnectException {
		types.add(type);
	}

	@Override
	public Collection<MediumType> list() throws ConnectException {
		return types;
	}

	@Override
	public MediumType get(final Integer id) throws ConnectException {
		try {
			return types.get(id);
		} catch (final IndexOutOfBoundsException e) {
			return null;
		}
	}

}
