package de.afbb.bibo.databinding.conversion;

import org.eclipse.core.databinding.conversion.Converter;

import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.share.model.Curator;

/**
 * one-way converter that converts objects to displayable strings
 * 
 * @author David Becker
 *
 */
public class ObjectToStringConverter extends Converter {

	public ObjectToStringConverter() {
		super(null, String.class);
	}

	@Override
	public Object convert(Object fromObject) {
		if (fromObject instanceof Borrower) {
			return ((Borrower) fromObject).getName();
		}
		if (fromObject instanceof Curator) {
			return ((Curator) fromObject).getName();
		}
		return "";//$NON-NLS-1$
	}

}
