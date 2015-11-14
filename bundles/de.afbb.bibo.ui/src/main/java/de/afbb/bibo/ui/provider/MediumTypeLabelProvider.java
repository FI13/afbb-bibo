package de.afbb.bibo.ui.provider;

import org.eclipse.jface.viewers.LabelProvider;
import de.afbb.bibo.share.model.MediumType;

/**
 * a label provider to display information from {@link MediumType}
 * 
 * @author David Becker
 *
 */
public class MediumTypeLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof MediumType) {
			return ((MediumType) element).getName();
		}
		return null;
	}

}
