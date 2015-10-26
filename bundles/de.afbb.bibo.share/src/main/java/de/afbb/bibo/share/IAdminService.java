package de.afbb.bibo.share;

import java.util.List;

import de.afbb.bibo.share.model.Admin;

public interface IAdminService {

	Admin findById(Integer id);

	Admin create(Admin admin);

	List<Admin> list();

	boolean validateLogin(Admin admin, String password);

}
