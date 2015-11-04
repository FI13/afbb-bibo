package de.afbb.bibo.ui.view;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import de.afbb.bibo.share.impl.NavigationTreeViewNode;
import de.afbb.bibo.ui.Activator;
import de.afbb.bibo.ui.ImagePath;

public class NavigationTreeViewLabelProvider extends LabelProvider {

	ImageRegistry imageRegistry = JFaceResources.getImageRegistry();

	public NavigationTreeViewLabelProvider() {
		super();
		imageRegistry.put(ImagePath.ICON_BOOK_16, Activator.getImageDescriptor(ImagePath.ICON_BOOK_16));
		imageRegistry.put(ImagePath.ICON_BOOK2_16, Activator.getImageDescriptor(ImagePath.ICON_BOOK2_16));
		imageRegistry.put(ImagePath.ICON_PUPIL_16, Activator.getImageDescriptor(ImagePath.ICON_PUPIL_16));
		imageRegistry.put(ImagePath.ICON_TEACHER_16, Activator.getImageDescriptor(ImagePath.ICON_TEACHER_16));
		imageRegistry.put(ImagePath.ICON_USER_16, Activator.getImageDescriptor(ImagePath.ICON_USER_16));
		imageRegistry.put(ImagePath.ICON_CD_16, Activator.getImageDescriptor(ImagePath.ICON_CD_16));
	}

	@Override
	public String getText(final Object obj) {
		return obj.toString();
	}

	@Override
	public Image getImage(final Object obj) {
		if (obj instanceof NavigationTreeViewNode && ((NavigationTreeViewNode) obj).hasType()) {
			switch (((NavigationTreeViewNode) obj).getType()) {
				case BOOK:
					return imageRegistry.get(ImagePath.ICON_BOOK_16);
				case BOOKS:
					return imageRegistry.get(ImagePath.ICON_BOOK2_16);
				case PERSON:
					return imageRegistry.get(ImagePath.ICON_PUPIL_16);
				case PERSONS:
					return imageRegistry.get(ImagePath.ICON_USER_16);
				default:
					throw new IllegalStateException("Illegal NavigationTreeViewNode State");
			}

		} else {
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}
}