package de.afbb.bibo.share.internal.impl;

import java.util.ArrayList;
import java.util.List;

import de.afbb.bibo.share.IBenutzerService;
import de.afbb.bibo.share.model.Benutzer;

public class BenutzerStubService implements IBenutzerService {

	@Override
	public Benutzer findById(final Integer id) {
		final Benutzer admin = new Benutzer();
		admin.setId(1);
		admin.setName("Horst");
		return admin;
	}

	@Override
	public Benutzer create(final Benutzer admin) {
		return admin;
	}

	@Override
	public List<Benutzer> list() {
		final List<Benutzer> list = new ArrayList<Benutzer>();
		list.add(new Benutzer());
		list.add(new Benutzer());
		list.add(new Benutzer());
		return list;
	}

	@Override
	public boolean validateLogin(final Benutzer admin) {
		if (admin == null || admin.getPassword() == null) {
			return false;
		}
		return "password123".equals(admin.getPassword());
	}

}
