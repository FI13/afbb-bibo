package de.afbb.bibo.ui.provider;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import de.afbb.bibo.share.impl.NavigationTreeViewNode;
import de.afbb.bibo.ui.BiboImageRegistry;
import de.afbb.bibo.ui.ImagePath;

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
					return BiboImageRegistry.getImage(ImagePath.ICON_BOOK2_16);
				case BOOKS:
					return BiboImageRegistry.getImage(ImagePath.ICON_BOOK_16);
				case PERSON:
					return BiboImageRegistry.getImage(ImagePath.ICON_PUPIL_16);
				case PERSONS:
					return BiboImageRegistry.getImage(ImagePath.ICON_USER_16);
				case ROOT:
					return BiboImageRegistry.getImage(ImagePath.ICON_USER_16);
				default:
					throw new IllegalStateException("Illegal NavigationTreeViewNode State");
			}

		} else {
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}
}