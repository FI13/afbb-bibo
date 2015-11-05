package de.afbb.bibo.ui.provider;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import de.afbb.bibo.share.impl.NavigationTreeViewNode;
import de.afbb.bibo.ui.BiboImageRegistry;
import de.afbb.bibo.ui.IconSize;
import de.afbb.bibo.ui.IconType;

public class NavigationTreeViewLabelProvider extends LabelProvider {

	public NavigationTreeViewLabelProvider() {
		super();
	}

	@Override
	public String getText(final Object obj) {
		if (obj instanceof NavigationTreeViewNode) {
			return ((NavigationTreeViewNode) obj).getTitle();
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
}