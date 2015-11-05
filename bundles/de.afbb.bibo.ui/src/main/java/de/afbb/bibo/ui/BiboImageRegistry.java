package de.afbb.bibo.ui;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Image;

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
	public static Image getImage(final String imagePath) {
		if (!(imagePath == null || imagePath.isEmpty())) {
			if (!pathSet.contains(imagePath)) {
				imageRegistry.put(imagePath, Activator.getImageDescriptor(imagePath));
				pathSet.add(imagePath);
			}
			return imageRegistry.get(imagePath);
		}
		return null;
	}
}
