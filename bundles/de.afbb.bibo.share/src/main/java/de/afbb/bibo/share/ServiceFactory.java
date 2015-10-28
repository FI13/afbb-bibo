package de.afbb.bibo.share;

import de.afbb.bibo.share.internal.impl.BenutzerStubService;

public final class ServiceFactory {

	public static final IBenutzerService adminService = new BenutzerStubService();

	private ServiceFactory() {
	}

}
