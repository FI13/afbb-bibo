package de.afbb.bibo.share;

import de.afbb.bibo.share.internal.impl.AdminStubService;

public final class ServiceFactory {

	public static final IAdminService adminService = new AdminStubService();

	private ServiceFactory() {
	}

}
