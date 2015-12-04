package de.afbb.bibo.servletclient;

import de.afbb.bibo.servletclient.connection.BorrowerServiceImpl;
import de.afbb.bibo.servletclient.connection.CuratorServiceImpl;
import de.afbb.bibo.servletclient.connection.LoginServiceImpl;
import de.afbb.bibo.servletclient.connection.MediumServiceImpl;
import de.afbb.bibo.servletclient.connection.TypServiceImpl;
import de.afbb.bibo.servletclient.internal.stub.BorrowerStubService;
import de.afbb.bibo.servletclient.internal.stub.CopyStubService;
import de.afbb.bibo.servletclient.internal.stub.CuratorStubService;
import de.afbb.bibo.servletclient.internal.stub.LoginStubService;
import de.afbb.bibo.servletclient.internal.stub.MediumStubService;
import de.afbb.bibo.servletclient.internal.stub.TypStubService;
import de.afbb.bibo.share.IBorrowerService;
import de.afbb.bibo.share.ICopyService;
import de.afbb.bibo.share.ICuratorService;
import de.afbb.bibo.share.ILoginService;
import de.afbb.bibo.share.IMediumService;
import de.afbb.bibo.share.ITypService;

/**
 * finds a service that can handle the request
 *
 * @author dbecker
 */
public final class ServiceLocator {

	private ICuratorService CURATOR_SERVICE;
	private ILoginService LOGIN_SERVICE;
	private ITypService TYP_SERVICE;
	private IBorrowerService BORROWER_SERVICE;
	private IMediumService MEDIUM_SERVICE;
	private ICopyService COPY_SERVICE;

	private static final ServiceLocator INSTANCE = new ServiceLocator();

	// TODO remove
	private static final boolean useStubServices = false;

	private ServiceLocator() {
	}

	public ITypService getTypService() {
		if (TYP_SERVICE == null) {
			TYP_SERVICE = useStubServices ? new TypStubService() : new TypServiceImpl();
		}
		return TYP_SERVICE;
	}

	public static ServiceLocator getInstance() {
		return INSTANCE;
	}

	public ICuratorService getCuratorService() {
		if (CURATOR_SERVICE == null) {
			CURATOR_SERVICE = useStubServices ? new CuratorStubService() : new CuratorServiceImpl();
		}
		return CURATOR_SERVICE;
	}

	public ILoginService getLoginService() {
		if (LOGIN_SERVICE == null) {
			LOGIN_SERVICE = useStubServices ? new LoginStubService() : new LoginServiceImpl();
		}
		return LOGIN_SERVICE;
	}

	public IBorrowerService getBorrowerService() {
		if (BORROWER_SERVICE == null) {
			BORROWER_SERVICE = useStubServices ? new BorrowerStubService() : new BorrowerServiceImpl();
		}
		return BORROWER_SERVICE;
	}

	public IMediumService getMediumService() {
		if (MEDIUM_SERVICE == null) {
			MEDIUM_SERVICE = useStubServices ? new MediumStubService() : new MediumServiceImpl();
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
