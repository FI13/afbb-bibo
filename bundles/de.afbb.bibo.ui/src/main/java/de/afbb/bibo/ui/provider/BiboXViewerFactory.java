package de.afbb.bibo.ui.provider;

import org.eclipse.nebula.widgets.xviewer.XViewerFactory;

public class BiboXViewerFactory extends XViewerFactory {

	public BiboXViewerFactory(final String namespace) {
		super(namespace);
	}

	@Override
	public boolean isAdmin() {
		return false;
	}

	@Override
	public boolean isFilterUiAvailable() {
		return false;
	}

	@Override
	public boolean isLoadedStatusLabelAvailable() {
		return false;
	}

	@Override
	public boolean isSearchUiAvailable() {
		return false;
	}
}
