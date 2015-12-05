package de.afbb.bibo.ui.provider;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import de.afbb.bibo.aggregation.NavigationTreeViewNode;
import de.afbb.bibo.share.model.IconType;
import de.afbb.bibo.ui.BiboImageRegistry;
import de.afbb.bibo.ui.IconSize;

public class NavigationViewLabelProvider extends ColumnLabelProvider {

	public NavigationViewLabelProvider() {
		super();
	}

	@Override
	public String getText(final Object obj) {
		if (obj instanceof NavigationTreeViewNode) {
			final NavigationTreeViewNode navigationTreeViewNode = (NavigationTreeViewNode) obj;
			final StringBuilder text = new StringBuilder(navigationTreeViewNode.getTitle());
			if (navigationTreeViewNode.getInformation() != null) {
				text.append("  ");
				text.append(navigationTreeViewNode.getInformationText());
			}
			return text.toString();
		} else {
			return obj.toString();
		}
	}

	@Override
	public Image getImage(final Object obj) {
		if (obj instanceof NavigationTreeViewNode && ((NavigationTreeViewNode) obj).hasType()) {
			switch (((NavigationTreeViewNode) obj).getType()) {
			case BOOK:
				return BiboImageRegistry.getImage(IconType.BOOK, IconSize.small);
			case BOOKS:
				return BiboImageRegistry.getImage(IconType.BOOK_GROUP, IconSize.small);
			case PERSON:
				return BiboImageRegistry.getImage(IconType.PUPIL, IconSize.small);
			case PERSONS:
				return BiboImageRegistry.getImage(IconType.USER, IconSize.small);
			case ROOT:
				return BiboImageRegistry.getImage(IconType.USER, IconSize.small);
			default:
				throw new IllegalStateException("Illegal NavigationTreeViewNode State");
			}

		} else {
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	@Override
	public String getToolTipText(final Object element) {
		if (element instanceof NavigationTreeViewNode) {
			final NavigationTreeViewNode navigationTreeViewNode = (NavigationTreeViewNode) element;
			return navigationTreeViewNode.getTooltipText();
		}
		return null;
	}
}