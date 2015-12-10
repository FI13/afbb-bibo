package de.afbb.bibo.ui.view;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import de.afbb.bibo.servletclient.ServiceLocator;
import de.afbb.bibo.share.SessionHolder;
import de.afbb.bibo.share.model.Curator;

/**
 * this view greets the user and gives hints to use the software
 *
 * @author dbecker
 */
public class WelcomeView extends AbstractView<Curator> {

	public static final String ID = "de.afbb.bibo.ui.view.welcome";//$NON-NLS-1$

	private Browser browser;

	private Button btnShow;

	@Override
	public Composite initUi(final Composite parent) {
		final Composite content = toolkit.createComposite(parent);
		final GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		content.setLayout(layout);

		browser = new Browser(content, SWT.NONE);
		browser.setLayoutData(new GridData(GridData.FILL_BOTH));
		browser.setText(readPage());

		btnShow = toolkit.createButton(content, "Diese Ansicht beim Start anzeigen", SWT.CHECK);
		setCheckBoxt();
		btnShow.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				try {
					ServiceLocator.getInstance().getCuratorService()
							.toggleWelcome(SessionHolder.getInstance().getCurator().getId());
					setCheckBoxt();
				} catch (final ConnectException exception) {
					handle(exception);
				}
			}
		});
		return content;
	}

	private void setCheckBoxt() {
		btnShow.setSelection(SessionHolder.getInstance().getCurator().isShowWelcome());
	}

	@Override
	protected void createValidationComposite(final Composite parent) {
		// no need for this here
	}

	@Override
	protected void initBinding() {
		// nothing to do
	}

	@Override
	public void setFocus() {
		browser.setFocus();
	}

	private static String readPage() {
		final InputStream pageStream = WelcomeView.class.getClassLoader().getResourceAsStream("welcome.html");//$NON-NLS-1$
		final char[] buffer = new char[1024];
		final StringBuilder out = new StringBuilder();
		try (Reader in = new InputStreamReader(pageStream, "UTF-8")) {//$NON-NLS-1$
			for (;;) {
				final int rsz = in.read(buffer, 0, buffer.length);
				if (rsz < 0) {
					break;
				}
				out.append(buffer, 0, rsz);
			}
		} catch (final UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return out.toString();
	}
}
