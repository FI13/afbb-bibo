package de.afbb.bibo.databinding.conversion;

import java.text.DateFormat;
import java.util.Date;

import org.eclipse.core.databinding.conversion.Converter;

import de.afbb.bibo.share.internal.model.BorrowerInput;
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
	public Object convert(final Object fromObject) {
		if (fromObject instanceof Borrower) {
			return new BorrowerInput((Borrower) fromObject).getName();
		}
		if (fromObject instanceof BorrowerInput) {
			return ((BorrowerInput) fromObject).getName();
		}
		if (fromObject instanceof Curator) {
			return ((Curator) fromObject).getName();
		}
		if (fromObject instanceof Date) {
			return DateFormat.getDateInstance().format((Date) fromObject);
		}
		return "";//$NON-NLS-1$
	}

}
