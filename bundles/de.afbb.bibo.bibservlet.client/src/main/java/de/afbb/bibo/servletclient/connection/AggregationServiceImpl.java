package de.afbb.bibo.servletclient.connection;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import de.afbb.bibo.share.IAggregationService;
import de.afbb.bibo.share.callback.EventListener;
import de.afbb.bibo.share.callback.IAggregatorTarget;

/**
 * service that aggregates informations
 *
 * @author David Becker
 *
 */
public class AggregationServiceImpl implements IAggregationService {

	private final Set<EventListener> listeners = new HashSet<EventListener>();

	private static final String URL_BORROWER = "/stock/countLendCopies";
	private static final String URL_MEDIUM = "/stock/getMediumInformation";

	@Override
	public void aggregateBorrowerInformation(final Integer id, final IAggregatorTarget target) {
		process(id, target, URL_BORROWER);
	}

	@Override
	public void aggregateMediumInformation(final Integer id, final IAggregatorTarget target) {
		process(id, target, URL_MEDIUM);
	}

	private void process(final Integer id, final IAggregatorTarget target, final String url) {
		try {
			final Map<String, String> param = new HashMap<String, String>();
			param.put("id", id.toString());
			HttpResponse resp;
			resp = ServerConnection.getInstance().request(url, "GET", param, null);
			if (resp.getStatus() == HttpServletResponse.SC_OK) {
				notifyListener(target, resp.getData().split("\n"));
			}
		} catch (final ConnectException e) {
			// just swallow exception
		}
	}

	@Override
	public void register(final EventListener listener) {
		listeners.add(listener);
	}

	private void notifyListener(final IAggregatorTarget target, final String[] information) {
		for (final EventListener eventListener : listeners) {
			eventListener.update(target, information);
		}
	}

}
