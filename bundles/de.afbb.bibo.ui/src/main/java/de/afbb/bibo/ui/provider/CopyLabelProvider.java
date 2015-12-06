package de.afbb.bibo.ui.provider;

import java.text.DateFormat;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.nebula.widgets.xviewer.XViewer;
import org.eclipse.nebula.widgets.xviewer.XViewerColumn;
import org.eclipse.nebula.widgets.xviewer.XViewerLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.share.model.IconType;
import de.afbb.bibo.ui.BiboImageRegistry;
import de.afbb.bibo.ui.IconSize;

/**
 * label provider for an {@link XViewer} that displays instances of {@link Copy}
 *
 * @author dbecker
 */
public class CopyLabelProvider extends XViewerLabelProvider {

	private final ITreeContentProvider contentProvider;

	/**
	 * regular Constructor. doesn't provide highlighting of parent elements
	 *
	 * @param viewer
	 *            the {@link XViewer} that is used with this label provider
	 */
	public CopyLabelProvider(final XViewer viewer) {
		this(viewer, null);
	}

	/**
	 * Constructor.
	 *
	 * @param viewer
	 *            the {@link XViewer} that is used with this label provider
	 * @param contentProvider
	 *            the content provider for viewer. can be <code>null</code>
	 */
	public CopyLabelProvider(final XViewer viewer, final ITreeContentProvider contentProvider) {
		super(viewer);
		this.contentProvider = contentProvider;
	}

	@Override
	public void addListener(final ILabelProviderListener listener) {
	}

	@Override
	public void removeListener(final ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(final Object element, final String property) {
		return false;
	}

	@Override
	public Image getColumnImage(final Object element, final XViewerColumn xCol, final int columnIndex)
			throws Exception {
		if (element instanceof Copy) {
			final Copy copy = (Copy) element;
			if (columnIndex == 0 && copy.getMedium() != null && copy.getMedium().getType() != null) {
				return BiboImageRegistry.getImage(copy.getMedium().getType().getIcon(), IconSize.small);
			} else if (columnIndex == 9 && copy.getMedium().getMediumId() > 0) {
				return BiboImageRegistry.getImage(isAvailable(copy) ? IconType.BOOK_AVAILABLE : IconType.BOOK_LENT,
						IconSize.small);
			} else if (columnIndex == 10 && copy.getMedium().getMediumId() > 0) {
				return BiboImageRegistry.getImage(isDamaged(copy) ? IconType.BOOK_DAMAGED : IconType.BOOK,
						IconSize.small);
			}
		}
		return null;
	}

	@Override
	public String getColumnText(final Object element, final XViewerColumn xCol, final int columnIndex)
			throws Exception {
		final Copy copy = (Copy) element;
		String value = "";//$NON-NLS-1$
		if (columnIndex == 0 && copy.getMedium() != null && copy.getMedium().getType() != null) {
			value = copy.getMedium().getType().getName();
		} else if (columnIndex == 1 && copy.getBarcode() != null) {
			value = copy.getBarcode();
		} else if (columnIndex == 2 && copy.getMedium() != null && copy.getMedium().getIsbn() != null) {
			value = copy.getMedium().getIsbn();
		} else if (columnIndex == 3 && copy.getMedium() != null && copy.getMedium().getTitle() != null) {
			value = copy.getMedium().getTitle();
		} else if (columnIndex == 4 && copy.getMedium() != null && copy.getMedium().getAuthor() != null) {
			value = copy.getMedium().getAuthor();
		} else if (columnIndex == 5 && copy.getMedium() != null && copy.getMedium().getPublisher() != null) {
			value = copy.getMedium().getPublisher();
		} else if (columnIndex == 6 && copy.getMedium() != null && copy.getMedium().getLanguage() != null) {
			value = copy.getMedium().getLanguage();
		} else if (columnIndex == 7 && copy.getEdition() != null) {
			value = copy.getEdition();
		} else if (columnIndex == 8 && copy.getInventoryDate() != null) {
			value = DateFormat.getDateInstance().format(copy.getInventoryDate());
		} else if (columnIndex == 9 && copy.getMedium().getMediumId() > 0) {
			value = isAvailable(copy) ? "Ja" : "Nein";
		} else if (columnIndex == 10 && copy.getMedium().getMediumId() > 0) {
			value = isDamaged(copy) ? "Ja" : "Nein";
		} else if (columnIndex == 11 && copy.getBorrowDate() != null) {
			value = DateFormat.getDateInstance().format(copy.getBorrowDate());
		} else if (columnIndex == 12 && copy.getCurator() != null) {
			value = copy.getCurator().getName();
		} else if (columnIndex == 13 && copy.getBorrower() != null) {
			value = copy.getBorrower().getName();
		} else if (columnIndex == 14 && copy.getLastBorrowDate() != null) {
			value = DateFormat.getDateInstance().format(copy.getLastBorrowDate());
		} else if (columnIndex == 15 && copy.getLastCurator() != null) {
			value = copy.getLastCurator().getName();
		} else if (columnIndex == 16 && copy.getLastBorrower() != null) {
			value = copy.getLastBorrower().getName();
		}
		return value;
	}

	@Override
	public Color getBackground(final Object element, final int columnIndex) {
		// darken the background for children elements
		if (contentProvider != null && contentProvider.getParent(element) != null) {
			return Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW);
		}
		return null;
	}

	private static boolean isAvailable(final Copy copy) {
		return copy.getBorrowDate() == null
				|| copy.getLastBorrowDate() != null && copy.getBorrowDate().compareTo(copy.getLastBorrowDate()) <= 0;
	}

	private static boolean isDamaged(final Copy copy) {
		return copy.getCondition() != null && !copy.getCondition().isEmpty();
	}

}
