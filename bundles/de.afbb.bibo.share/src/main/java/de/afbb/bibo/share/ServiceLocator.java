package de.afbb.bibo.share;

import de.afbb.bibo.share.internal.stub.impl.BorrowerStubService;
import de.afbb.bibo.share.internal.stub.impl.CopyStubService;
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

	private ICuratorService CURATOR_SERVICE;
	private ILoginService LOGIN_SERVICE;
	private final ITypService TYP_SERVICE = new TypStubService();
	private IBorrowerService BORROWER_SERVICE;
	private IMediumService MEDIUM_SERVICE;
	private ICopyService COPY_SERVICE;

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
		if (CURATOR_SERVICE == null) {
			CURATOR_SERVICE = new CuratorStubService();
		}
		return CURATOR_SERVICE;
	}

	public ILoginService getLoginService() {
		if (LOGIN_SERVICE == null) {
			LOGIN_SERVICE = new LoginStubService();
		}
		return LOGIN_SERVICE;
	}

	public IBorrowerService getBorrowerService() {
		if (BORROWER_SERVICE == null) {
			BORROWER_SERVICE = new BorrowerStubService();
		}
		return BORROWER_SERVICE;
	}

	public IMediumService getMediumService() {
		if (MEDIUM_SERVICE == null) {
			MEDIUM_SERVICE = new MediumStubService();
		}
		return MEDIUM_SERVICE;
	}

	public ICopyService getCopyService() {
		if (COPY_SERVICE == null) {
			COPY_SERVICE = new CopyStubService();
		}
		return COPY_SERVICE;
	}
}
