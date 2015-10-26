package de.afbb.bibo.share.internal.impl;

import java.util.ArrayList;
import java.util.List;

import de.afbb.bibo.share.IAdminService;
import de.afbb.bibo.share.model.Admin;

public class AdminStubService implements IAdminService {

	@Override
	public Admin findById(final Integer id) {
		final Admin admin = new Admin();
		admin.setId(1);
		admin.setName("Horst");
		return admin;
	}

	@Override
	public Admin create(final Admin admin) {
		return admin;
	}

	@Override
	public List<Admin> list() {
		final List<Admin> list = new ArrayList<Admin>();
		list.add(new Admin());
		list.add(new Admin());
		list.add(new Admin());
		return list;
	}

}
