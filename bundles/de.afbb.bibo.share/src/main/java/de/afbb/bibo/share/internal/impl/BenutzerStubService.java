package de.afbb.bibo.share.internal.impl;

import java.util.ArrayList;
import java.util.List;

import de.afbb.bibo.share.IBenutzerService;
import de.afbb.bibo.share.model.Curator;

public class BenutzerStubService implements IBenutzerService {

	@Override
	public Curator findById(final Integer id) {
		final Curator admin = new Curator();
		admin.setId(1);
		admin.setName("Horst");
		return admin;
	}

	@Override
	public Curator create(final Curator admin) {
		return admin;
	}

	@Override
	public List<Curator> list() {
		final List<Curator> list = new ArrayList<Curator>();
		list.add(new Curator());
		list.add(new Curator());
		list.add(new Curator());
		return list;
	}

	@Override
	public boolean validateLogin(final Curator admin) {
		if (admin == null || admin.getPassword() == null) {
			return false;
		}
		return "password123".equals(admin.getPassword());
	}

}
