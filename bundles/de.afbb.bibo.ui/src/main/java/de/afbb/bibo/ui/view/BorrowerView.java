package de.afbb.bibo.ui.view;

import java.net.ConnectException;
import java.util.Collection;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;

import de.afbb.bibo.share.ServiceLocator;
import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.ui.form.BorrowerForm;
import de.afbb.bibo.ui.form.CopyXviewerForm;
import de.afbb.bibo.ui.form.StatisticForm;

/**
 * this view adds or edits a person who can borrow books
 *
 * @author philippwiddra
 * @author David Becker
 */
public class BorrowerView extends AbstractView<Borrower> {

	public static final String ID = "de.afbb.bibo.ui.borrower";//$NON-NLS-1$
	private static final String BORROWER_SHOW = "borrower.show";//$NON-NLS-1$

	private BorrowerForm borrowerForm;
	private CopyXviewerForm xViewer;
	private Job job;
	private StatisticForm statisticForm;

	@Override
	protected String computePartName(final Borrower input) {
		return input != null ? input.getName() : null;
	}

	@Override
	protected Borrower cloneInput(final Borrower input) {
		return (Borrower) input.clone();
	}

	@Override
	public void setFocus() {
		borrowerForm.setFocus();
	}

	// @Override
	// public void doSave(final IProgressMonitor monitor) {
	//
	// }
	//
	// @Override
	// public boolean isDirty() {
	// return !input.equals(inputCache);
	// }

	@Override
	protected Composite initUi(final Composite parent) throws ConnectException {
		final Composite content = toolkit.createComposite(parent, SWT.NONE);
		content.setLayout(new GridLayout(2, false));

		final Group borrowerGroup = toolkit.createGroup(content, "Allgemein");
		borrowerForm = new BorrowerForm(borrowerGroup, input, bindingContext, toolkit);

		final Group statisticGroup = toolkit.createGroup(content, "Statistik");
		statisticForm = new StatisticForm(statisticGroup);

		final Group lendGroup = toolkit.createGroup(content, "Aktuell ausgeliehene Medien");
		xViewer = new CopyXviewerForm(lendGroup, BORROWER_SHOW);

		GridDataFactory.fillDefaults().grab(true, true).applyTo(content);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(borrowerForm);
		GridDataFactory.fillDefaults().applyTo(statisticGroup);
		GridDataFactory.fillDefaults().hint(200, SWT.DEFAULT).grab(true, false).applyTo(borrowerGroup);
		GridDataFactory.fillDefaults().grab(true, true).span(2, 1).applyTo(lendGroup);

		return content;
	}

	@Override
	protected void initBinding() {
		// one-way binding to update the input in sub-form
		bindingContext.bindValue(BeansObservables.observeValue(borrowerForm, INPUT),
				BeansObservables.observeValue(this, INPUT), new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER),
				null);
	}

	// @Override
	// protected void setInput(final IEditorInput editorInput) {
	// super.setInput(editorInput);
	// setXViewerInput();
	// }

	/**
	 * load the list of copies in a background thread to not block the user
	 * interface if it takes to long
	 */
	private void setXViewerInput() {
		// shouldn't happen - just to be sure
		if (job != null) {
			job.cancel();
		}
		job = new Job("Lade ausgeliehene Medien") {

			@Override
			protected IStatus run(final IProgressMonitor monitor) {
				try {
					final Collection<Copy> listLent = ServiceLocator.getInstance().getBorrowerService().listLent(input);
					Display.getDefault().asyncExec(new Runnable() {

						@Override
						public void run() {
							if (xViewer != null && xViewer.getTree() != null && !xViewer.getTree().isDisposed()) {
								xViewer.setInput(listLent);
							}
							if (statisticForm != null && !statisticForm.isDisposed()) {
								statisticForm.setInput(listLent);
							}
						}
					});
				} catch (final ConnectException e) {
					handle(e);
				}
				return Status.OK_STATUS;
			}
		};
		job.schedule();
	}

}
