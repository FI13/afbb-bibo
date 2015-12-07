package de.afbb.bibo.ui.form;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Date;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.share.model.IconType;
import de.afbb.bibo.ui.BiboFormToolkit;
import de.afbb.bibo.ui.BiboImageRegistry;
import de.afbb.bibo.ui.IconSize;

/**
 * displays statistic information
 *
 * @author David Becker
 *
 */
public class MediumStatisticForm extends Composite {

	private static final String NEW = "NEU";//$NON-NLS-1$
	private final BiboFormToolkit toolkit;
	private Text txtCount;
	private Text txtAvailableAbsolut;
	private Text txtAvailableRelative;
	private Text txtLendAbsolut;
	private Text txtLendRelative;
	private Text txtDamagedAbsolut;
	private Text txtDamagedRelative;
	private Text txtOldestInventoryDate;

	public MediumStatisticForm(final Composite parent) {
		super(parent, SWT.NONE);
		toolkit = new BiboFormToolkit(Display.getCurrent());
		createUi();
	}

	private void createUi() {
		final GridLayout outerLayout = new GridLayout(1, false);
		outerLayout.marginHeight = outerLayout.marginWidth = 0;
		setLayout(outerLayout);

		final Composite content = toolkit.createComposite(this);
		final GridLayout layout = new GridLayout(4, false);
		layout.marginHeight = layout.marginWidth = 0;
		content.setLayout(layout);

		content.setEnabled(false);

		toolkit.createLabel(content, null);
		toolkit.createLabel(content, "Absolut");
		toolkit.createLabel(content, "Relativ");
		toolkit.createLabel(content, null);

		toolkit.createLabel(content, "Anzahl Medien");
		txtCount = toolkit.createText(content, "0", SWT.RIGHT | SWT.READ_ONLY | SWT.BORDER);
		GridDataFactory.swtDefaults().span(2, 1).applyTo(toolkit.createLabel(content, null));

		toolkit.createLabel(content, "Verfügbare Medien");
		txtAvailableAbsolut = toolkit.createText(content, "0", SWT.RIGHT | SWT.READ_ONLY | SWT.BORDER);
		txtAvailableRelative = toolkit.createText(content, "0.0", SWT.RIGHT | SWT.READ_ONLY | SWT.BORDER);
		toolkit.createLabel(content, null)
				.setImage(BiboImageRegistry.getImage(IconType.BOOK_AVAILABLE, IconSize.small));

		toolkit.createLabel(content, "Ausgeliehene Medien");
		txtLendAbsolut = toolkit.createText(content, "0", SWT.RIGHT | SWT.READ_ONLY | SWT.BORDER);
		txtLendRelative = toolkit.createText(content, "0.0", SWT.RIGHT | SWT.READ_ONLY | SWT.BORDER);
		toolkit.createLabel(content, null).setImage(BiboImageRegistry.getImage(IconType.BOOK_LENT, IconSize.small));

		toolkit.createLabel(content, "Beschädigte Medien");
		txtDamagedAbsolut = toolkit.createText(content, "0", SWT.RIGHT | SWT.READ_ONLY | SWT.BORDER);
		txtDamagedRelative = toolkit.createText(content, "0.0", SWT.RIGHT | SWT.READ_ONLY | SWT.BORDER);
		toolkit.createLabel(content, null).setImage(BiboImageRegistry.getImage(IconType.BOOK_DAMAGED, IconSize.small));

		toolkit.createLabel(content, "Ältestes Inventar-Datum");
		txtOldestInventoryDate = toolkit.createText(content, "", SWT.CENTER | SWT.READ_ONLY | SWT.BORDER);

		GridDataFactory.fillDefaults().hint(80, SWT.DEFAULT).applyTo(txtCount);
		GridDataFactory.fillDefaults().hint(80, SWT.DEFAULT).applyTo(txtAvailableAbsolut);
		GridDataFactory.fillDefaults().hint(40, SWT.DEFAULT).applyTo(txtAvailableRelative);
		GridDataFactory.fillDefaults().hint(80, SWT.DEFAULT).applyTo(txtLendAbsolut);
		GridDataFactory.fillDefaults().hint(40, SWT.DEFAULT).applyTo(txtLendRelative);
		GridDataFactory.fillDefaults().hint(80, SWT.DEFAULT).applyTo(txtDamagedAbsolut);
		GridDataFactory.fillDefaults().hint(40, SWT.DEFAULT).applyTo(txtDamagedRelative);
		GridDataFactory.fillDefaults().hint(80, SWT.DEFAULT).applyTo(txtOldestInventoryDate);
	}

	public void setInput(final Collection<Copy> input) {
		double count = 0.0;
		int countAvailable = 0;
		int countDamaged = 0;
		Date inventoryDate = null;
		if (input != null) {
			for (final Copy copy : input) {
				if (copy != null) {
					if (copy.getBorrowDate() == null || copy.getLastBorrowDate() != null
							&& copy.getBorrowDate().compareTo(copy.getLastBorrowDate()) <= 0) {
						countAvailable++;
					}

					if (copy.getCondition() != null && !copy.getCondition().isEmpty()
							&& !NEW.equals(copy.getCondition().trim().toUpperCase())) {
						countDamaged++;
					}
					if (inventoryDate == null || inventoryDate.compareTo(copy.getInventoryDate()) > 0) {
						inventoryDate = copy.getInventoryDate();
					}
					count++;
				}
			}
		}
		final int lend = (int) (count - countAvailable);
		txtCount.setText(String.valueOf((int) count));
		txtAvailableAbsolut.setText(String.valueOf(countAvailable));
		txtAvailableRelative
				.setText(NumberFormat.getPercentInstance().format(count > 0 ? countAvailable / count : count));
		txtLendAbsolut.setText(String.valueOf(lend));
		txtLendRelative.setText(NumberFormat.getPercentInstance().format(count > 0 ? lend / count : count));
		txtDamagedAbsolut.setText(String.valueOf(countDamaged));
		txtDamagedRelative.setText(NumberFormat.getPercentInstance().format(count > 0 ? countDamaged / count : count));
		txtOldestInventoryDate.setText(inventoryDate != null ? DateFormat.getDateInstance().format(inventoryDate) : "");
	}
}