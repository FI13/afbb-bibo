package de.afbb.bibo.servletclient.connection;

import java.net.ConnectException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import de.afbb.bibo.share.ICopyService;
import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.share.model.Medium;

public class CopyServiceImpl implements ICopyService {

	@Override
	public void update(final Copy copy) throws ConnectException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean exists(final String barcode) throws ConnectException {
		final Map<String, String> param = new HashMap<String, String>();
		param.put("barcode", barcode);
		final HttpResponse resp = ServerConnection.getInstance().request("/stock/existCopy", "GET", param, null);
		if (resp.getStatus() == HttpServletResponse.SC_OK) {
			return "1".equals(resp.getData());
		} else {
			throw new ConnectException("Wrong status code. Recieved was: " + resp.getStatus());
		}
	}

	@Override
	public Copy get(final String barcode) throws ConnectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Copy> getGroupedCopies(final Integer id) throws ConnectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerCopies(final Collection<Copy> copies) throws ConnectException {
		// TODO Auto-generated method stub

	}

	@Override
	public void returnCopies(final Collection<Copy> copies) throws ConnectException {
		// TODO Auto-generated method stub

	}

	@Override
	public void lendCopies(final Collection<Copy> copies, final boolean printList) throws ConnectException {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<Copy> listAll() throws ConnectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Copy> listCopies(final Medium medium) throws ConnectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(final Copy copy) throws ConnectException {
		// TODO Auto-generated method stub

	}

}
