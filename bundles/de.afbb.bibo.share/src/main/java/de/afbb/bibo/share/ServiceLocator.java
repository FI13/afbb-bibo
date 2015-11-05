package de.afbb.bibo.share;

import de.afbb.bibo.share.internal.stub.impl.BorrowerStubService;
import de.afbb.bibo.share.internal.stub.impl.CuratorStubService;
import de.afbb.bibo.share.internal.stub.impl.LoginStubService;
import de.afbb.bibo.share.internal.stub.impl.MediumStubService;
import de.afbb.bibo.share.internal.stub.impl.TypStubService;

/**
 * finds a service that can handle the request
 * 
 * @author dbecker
 */
public final class ServiceLocator {

	private final ICuratorService CURATOR_SERVICE = new CuratorStubService();
	private final ILoginService LOGIN_SERVICE = new LoginStubService();
	private final ITypService TYP_SERVICE = new TypStubService();
	private final IBorrowerService BORROWER_SERVICE = new BorrowerStubService();
	private final IMediumService MEDIUM_SERVICE = new MediumStubService();

	private static final ServiceLocator INSTANCE = new ServiceLocator();

	private ServiceLocator() {
	}

	public ITypService getTypService() {
		return TYP_SERVICE;
	}

	public static ServiceLocator getInstance() {
		return INSTANCE;
	}

	public ICuratorService getCuratorService() {
		return CURATOR_SERVICE;
	}

	public ILoginService getLoginService() {
		return LOGIN_SERVICE;
	}

	public IBorrowerService getBorrowerService() {
		return BORROWER_SERVICE;
	}

	public IMediumService getMediumService() {
		return MEDIUM_SERVICE;
	}
}
