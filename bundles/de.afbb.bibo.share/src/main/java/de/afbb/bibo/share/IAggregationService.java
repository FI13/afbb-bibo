package de.afbb.bibo.share;

import de.afbb.bibo.share.callback.EventChangeProvider;
import de.afbb.bibo.share.callback.IAggregatorTarget;

/**
 * Service Interface for a service that aggregates information about given
 * entities
 *
 * @author David Becker
 *
 */
public interface IAggregationService extends EventChangeProvider {

	void aggregateBorrowerInformation(Integer id, IAggregatorTarget target);

	void aggregateMediumInformation(Integer id, IAggregatorTarget target);

}
