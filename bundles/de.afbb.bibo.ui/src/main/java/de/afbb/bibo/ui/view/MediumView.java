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

import de.afbb.bibo.servletclient.ServiceLocator;
import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.share.model.Medium;
import de.afbb.bibo.ui.form.CopyXviewerForm;
import de.afbb.bibo.ui.form.MediumForm;
import de.afbb.bibo.ui.form.MediumStatisticForm;

public class MediumView extends AbstractView<Medium> {

	public static final String ID = "de.afbb.bibo.ui.medium";//$NON-NLS-1$
	private static final int MAX_TITLE_LENGTH = 30;
	private static final String MORE = "...";//$NON-NLS-1$
	private static final String MEDIUM_SHOW = "medium.show";//$NON-NLS-1$

	private CopyXviewerForm xViewer;
	private MediumStatisticForm statisticForm;
	private MediumForm informationForm;
	private Job job;

	@Override
	protected Composite initUi(final Composite parent) throws ConnectException {
		final Composite content = toolkit.createComposite(parent, SWT.NONE);
		content.setLayout(new GridLayout(2, false));

		final Group mediumGroup = toolkit.createGroup(content, "Allgemein");
		informationForm = new MediumForm(mediumGroup, input, bindingContext, toolkit);

		final Group statisticGroup = toolkit.createGroup(content, "Statistik");
		statisticForm = new MediumStatisticForm(statisticGroup);

		final Group copyGroup = toolkit.createGroup(content, "Exemplare");
		xViewer = new CopyXviewerForm(copyGroup, MEDIUM_SHOW);

		GridDataFactory.fillDefaults().grab(true, false).applyTo(mediumGroup);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(informationForm);
		GridDataFactory.fillDefaults().applyTo(statisticGroup);
		GridDataFactory.fillDefaults().applyTo(statisticForm);
		GridDataFactory.fillDefaults().grab(true, true).span(2, 1).applyTo(copyGroup);

		return content;
	}

	@Override
	protected void initBinding() throws ConnectException {
		bindingContext.bindValue(BeansObservables.observeValue(informationForm, INPUT), inputObservable,
				new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER), null);
	}

	@Override
	public void setFocus() {
		if (informationForm != null && !informationForm.isDisposed()) {
			informationForm.setFocus();
		}
	}

	@Override
	protected String computePartName(final Medium input) {
		String partName = input != null ? input.getTitle() : null;
		// truncate part name when length over 30
		if (partName != null && partName.length() > MAX_TITLE_LENGTH) {
			partName = partName.substring(0, MAX_TITLE_LENGTH - MORE.length()) + MORE;
			System.err.println(partName.length());
		}
		return partName;
	}

	@Override
	protected Medium cloneInput(final Medium input) {
		return (Medium) input.clone();
	}

	@Override
	public void setInput(final Medium editorInput) {
		super.setInput(editorInput);
		setXViewerInput();
	}

	/**
	 * load the list of copies in a background thread to not block the user
	 * interface if it takes to long
	 */
	private void setXViewerInput() {
		// shouldn't happen - just to be sure
		if (job != null) {
			job.cancel();
		}
		job = new Job("Lade Medien") {

			@Override
			protected IStatus run(final IProgressMonitor monitor) {
				try {
					final Collection<Copy> listLent = ServiceLocator.getInstance().getCopyService().listCopies(input);
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

	@Override
	public void doSave(final IProgressMonitor monitor) {
		super.doSave(monitor);
		final Job saveJob = new Job("Speichere Änderungen") {

			@Override
			protected IStatus run(final IProgressMonitor monitor) {
				try {
					ServiceLocator.getInstance().getMediumService().update(input);
				} catch (final ConnectException e) {
					handle(e);
				}
				return Status.OK_STATUS;
			}

		};
		saveJob.schedule();
		closeView();
	}

}
