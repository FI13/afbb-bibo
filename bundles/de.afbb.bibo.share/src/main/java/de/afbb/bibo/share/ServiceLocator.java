package de.afbb.bibo.share;

import de.afbb.bibo.share.internal.impl.BenutzerStubService;
import de.afbb.bibo.share.internal.impl.LoginStubService;
import de.afbb.bibo.share.internal.impl.TypStubService;

/**
 * finds a service that can handle the request
 * 
 * @author dbecker
 */
public final class ServiceLocator {

	private final IBenutzerService adminService = new BenutzerStubService();
	private final ILoginService LOGIN_SERVICE = new LoginStubService();
	private final ITypService TYP_SERVICE = new TypStubService();

	private static final ServiceLocator instance = new ServiceLocator();

	private ServiceLocator() {
	}

	public ITypService getTypService() {
		return TYP_SERVICE;
	}

	public static ServiceLocator getInstance() {
		return instance;
	}

	public IBenutzerService getAdminservice() {
		return adminService;
	}

	public ILoginService getLoginService() {
		return LOGIN_SERVICE;
	}

}
