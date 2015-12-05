package de.afbb.bibo.ui.form;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.ui.BiboFormToolkit;

/**
 * displays statistic information
 *
 * @author David Becker
 *
 */
public class StatisticForm extends Composite {

	private final BiboFormToolkit toolkit;
	private Text txtCount;
	private Text txtMedian;
	private Text txtMax;
	private Text txtOldestInventoryDate;

	private final Date today = new Date();

	public StatisticForm(final Composite parent) {
		super(parent, SWT.NONE);
		toolkit = new BiboFormToolkit(Display.getCurrent());
		createUi();
	}

	private void createUi() {
		final GridLayout outerLayout = new GridLayout(1, false);
		outerLayout.marginHeight = outerLayout.marginWidth = 0;
		setLayout(outerLayout);

		final Composite content = toolkit.createComposite(this);
		final GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = layout.marginWidth = 0;
		content.setLayout(layout);

		content.setEnabled(false);

		toolkit.createLabel(content, "Anzahl Medien");
		txtCount = toolkit.createText(content, "0", SWT.RIGHT | SWT.READ_ONLY | SWT.BORDER);

		toolkit.createLabel(content, "Ausleihtage Durchschnitt");
		txtMedian = toolkit.createText(content, "0", SWT.RIGHT | SWT.READ_ONLY | SWT.BORDER);

		toolkit.createLabel(content, "Ausleihtage Maximum");
		txtMax = toolkit.createText(content, "0", SWT.RIGHT | SWT.READ_ONLY | SWT.BORDER);

		toolkit.createLabel(content, "Ältestes Inventar-Datum");
		txtOldestInventoryDate = toolkit.createText(content, "", SWT.CENTER | SWT.READ_ONLY | SWT.BORDER);

		GridDataFactory.fillDefaults().hint(80, SWT.DEFAULT).applyTo(txtCount);
		GridDataFactory.fillDefaults().hint(80, SWT.DEFAULT).applyTo(txtMedian);
		GridDataFactory.fillDefaults().hint(80, SWT.DEFAULT).applyTo(txtMax);
		GridDataFactory.fillDefaults().hint(80, SWT.DEFAULT).applyTo(txtOldestInventoryDate);
	}

	public void setInput(final Collection<Copy> input) {
		int count = 0;
		double median = 0.0f;
		long max = 0;
		Date inventoryDate = null;
		if (input != null) {
			count = input.size();

			long maxDeltaDays = 0;
			long totalDeltaDays = 0;
			double countDeltas = 0.0;

			for (final Copy copy : input) {
				if (copy != null) {
					// year 2038 problem, but who cares?
					final long deltaDays = TimeUnit.MILLISECONDS
							.toDays(today.getTime() - copy.getBorrowDate().getTime());
					if (maxDeltaDays < deltaDays) {
						maxDeltaDays = deltaDays;
					}
					totalDeltaDays += deltaDays;
					countDeltas += 1.0;

					if (inventoryDate == null || inventoryDate.compareTo(copy.getInventoryDate()) > 0) {
						inventoryDate = copy.getInventoryDate();
					}
				}
			}
			if (countDeltas > 0.0) {
				median = totalDeltaDays / countDeltas;
			}
			max = maxDeltaDays;
		}
		txtCount.setText(String.valueOf(count));
		txtMedian.setText(String.valueOf(median));
		txtMax.setText(String.valueOf(max));
		txtOldestInventoryDate.setText(inventoryDate != null ? DateFormat.getDateInstance().format(inventoryDate) : "");
	}
}