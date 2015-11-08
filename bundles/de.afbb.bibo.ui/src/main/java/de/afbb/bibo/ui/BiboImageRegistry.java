package de.afbb.bibo.ui;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Image;

import de.afbb.bibo.share.model.IconType;

/**
 * singleton to manage images
 * 
 * @author dbecker
 */
public final class BiboImageRegistry {

	private static final ImageRegistry imageRegistry = JFaceResources.getImageRegistry();
	private static final Set<String> pathSet = new HashSet<String>();

	private BiboImageRegistry() {
	}

	/**
	 * gets an image from the image registry. <br>
	 * maps an image if it is not already mapped
	 * 
	 * @param imagePath
	 * @return
	 */
	public static Image getImage(final IconType iconType, final IconSize size) {
		final String imagePath = getPath(iconType, size);
		if (!(imagePath == null || imagePath.isEmpty())) {
			if (!pathSet.contains(imagePath)) {
				imageRegistry.put(imagePath, Activator.getImageDescriptor(imagePath));
				pathSet.add(imagePath);
			}
			return imageRegistry.get(imagePath);
		}
		return null;
	}

	private static String getPath(final IconType iconType, final IconSize iconSize) {
		if (iconType == null || iconSize == null || IconType.NONE.equals(iconType)) {
			return null;
		}

		String path = "icons/";

		switch (iconSize) {
			case small:
				path += "16x16";
				break;
			case medium:
				path += "32x32";
				break;
			case large:
				path += "48x48";
				break;
			case huge:
				path += "64x64";
				break;
		}

		switch (iconType) {
			case LOGIN:
				path += "login";
				break;
			case USER_MANAGE:
				path += "benutzerverwaltung";
				break;
			case USER_MANAGE_ADD:
				path += "add_benutzerverwaltung2";
				break;
			case USER:
				path += "user";
				break;
			case PUPIL:
				path += "schueler";
				break;
			case PUPIL_ADD:
				path += "add_schueler";
				break;
			case TEACHER:
				path += "teacher";
				break;
			case TEACHER_ADD:
				path += "add_teacher";
				break;
			case BOOK_GROUP:
				path += "book";
				break;
			case BOOK_GROUP_ADD:
				path += "add_book";
				break;
			case BOOK:
				path += "book2";
				break;
			case BOOK_ADD:
				path += "add_book2";
				break;
			case BOOK_AVAILABLE:
				path += "book2-verfügbar";
				break;
			case BOOK_DAMAGED:
				path += "book2-beschädigt";
				break;
			case BOOK_LENT:
				path += "book2-entliehen";
				break;
			case CD:
				path += "cd";
				break;
			case CD_ADD:
				path += "add_cd";
				break;
			case ARROW_UP:
				path += "arrow-up";
				break;
			case ARROW_DOWN:
				path += "arrow-down";
				break;
			case PLUS:
				path += "plus";
				break;
			case MINUS:
				path += "minus";
				break;
			case SAVE:
				path += "Save";
				break;
			case LOGO:
				path += "logo";
				break;
			case MEDIA:
				path += "medien";
				break;
			case HELP:
				path += "Help";
				break;
			default:
				break;
		}

		path += ".png";

		return path;
	}
}
