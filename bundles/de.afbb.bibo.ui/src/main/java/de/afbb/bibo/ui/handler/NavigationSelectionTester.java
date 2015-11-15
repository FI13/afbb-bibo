package de.afbb.bibo.ui.handler;

import org.eclipse.core.expressions.PropertyTester;

import de.afbb.bibo.share.impl.NavigationTreeViewNode;

/**
 * a property tester that can enable commands based on type of selection
 * 
 * @author David Becker
 *
 */
public class NavigationSelectionTester extends PropertyTester {

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if (receiver instanceof NavigationTreeViewNode) {
			switch (((NavigationTreeViewNode) receiver).getType()) {
			case BOOK:
				return "BOOK".equals(property);//$NON-NLS-1$
			case BOOKS:
				return "BOOKS".equals(property);//$NON-NLS-1$
			case PERSON:
				return "PERSON".equals(property);//$NON-NLS-1$
			case PERSONS:
				return "PERSONS".equals(property);//$NON-NLS-1$
			case ROOT:
				return "ROOT".equals(property);//$NON-NLS-1$
			}
		}
		return false;
	}

}
