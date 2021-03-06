/**
 * Mars Simulation Project
 * GuideWindow.java
 * @version 3.1.0 2017-01-21
 * @author Lars Naesbye Christensen
 */

package org.mars_sim.msp.ui.swing.tool.guide;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.mars_sim.msp.core.Msg;
import org.mars_sim.msp.ui.swing.HTMLContentPane;
import org.mars_sim.msp.ui.swing.MainDesktopPane;
import org.mars_sim.msp.ui.swing.MarsPanelBorder;
import org.mars_sim.msp.ui.swing.toolWindow.ToolWindow;

import com.alee.laf.button.WebButton;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.viewport.WebViewport;

/**
 * The GuideWindow is a tool window that displays the built-in User Guide, About
 * Box and Tutorial.
 */
public class GuideWindow extends ToolWindow implements ActionListener,
		HyperlinkListener, ComponentListener {
	/** Default serial id. */
	private static final long serialVersionUID = 1L;

	/** Default logger. */
//	private static Logger logger = Logger.getLogger(GuideWindow.class.getName());

	/** Tool name. */
	public static final String NAME = Msg.getString("GuideWindow.title"); //$NON-NLS-1$

	/** Data members. */
	private List<URL> history = new ArrayList<>();

	private int historyIndex;

	/** our HTML content pane. */
	private HTMLContentPane htmlPane;
	/** The view port for the text pane. */
	private WebViewport viewPort;
	// private URL guideURL = GuideWindow.class.getClassLoader().getResource("docs"
	// + File.separator +
	// "help" + File.separator + "userguide.html");
	/** [landrus, 27.11.09]: load the url in the constructor. */
	private URL guideURL;
//	private URL aboutURL, tutorialURL, shortcutsURL;
//	private String discussionURLstring, wikiURLstring, projectsiteURLstring;

//	private WebButton shortcutsButton = new WebButton(Msg.getString("GuideWindow.button.shortcuts")); //$NON-NLS-1$
//	private WebButton aboutButton = new WebButton(Msg.getString("GuideWindow.button.about")); //$NON-NLS-1$
//	private WebButton tutorialButton = new WebButton(Msg.getString("GuideWindow.button.tutorial")); //$NON-NLS-1$
//	private WebButton userguideButton = new WebButton(Msg.getString("GuideWindow.button.userguide")); //$NON-NLS-1$
//	private WebButton projectsiteButton = new WebButton(Msg.getString("GuideWindow.button.projectsite")); //$NON-NLS-1$
//	private WebButton wikiButton = new WebButton(Msg.getString("GuideWindow.button.wiki")); //$NON-NLS-1$

	private WebButton homeButton = new WebButton(Msg.getString("GuideWindow.button.home")); //$NON-NLS-1$
	private WebButton backButton = new WebButton(Msg.getString("GuideWindow.button.back")); //$NON-NLS-1$
	private WebButton forwardButton = new WebButton(Msg.getString("GuideWindow.button.forward")); //$NON-NLS-1$

//	private BrowserJFX browser;
//	private WebPanel browserPanel;

	/**
	 * Constructor.
	 * 
	 * @param desktop
	 *            the desktop pane
	 */
	public GuideWindow(MainDesktopPane desktop) {
		super(NAME, desktop);

		/* [landrus, 27.11.09]: use classloader compliant paths */
		guideURL = getClass().getResource(Msg.getString("doc.guide")); //$NON-NLS-1$
//		shortcutsURL = getClass().getResource(Msg.getString("doc.shortcuts")); //$NON-NLS-1$
//		aboutURL = getClass().getResource(Msg.getString("doc.about")); //$NON-NLS-1$
//		tutorialURL = getClass().getResource(Msg.getString("doc.tutorial")); //$NON-NLS-1$

//		projectsiteURLstring = Msg.getString("url.projectSite"); //$NON-NLS-1$
//		// discussionURLstring = Msg.getString("url.discussion"); //$NON-NLS-1$
//		wikiURLstring = Msg.getString("url.wiki"); //$NON-NLS-1$

		homeButton.setToolTipText(Msg.getString("GuideWindow.tooltip.home")); //$NON-NLS-1$
		homeButton.addActionListener(this);

		backButton.setToolTipText(Msg.getString("GuideWindow.tooltip.back")); //$NON-NLS-1$
		backButton.addActionListener(this);

		forwardButton.setToolTipText(Msg.getString("GuideWindow.tooltip.forward")); //$NON-NLS-1$
		forwardButton.addActionListener(this);

		// Create the main panel
		WebPanel mainPane = new WebPanel(new BorderLayout());
		mainPane.setBorder(new MarsPanelBorder());
		setContentPane(mainPane);

//		shortcutsButton.setToolTipText(Msg.getString("GuideWindow.tooltip.shortcuts")); //$NON-NLS-1$
//		shortcutsButton.addActionListener(this);
//
//		userguideButton.setToolTipText(Msg.getString("GuideWindow.tooltip.userguide")); //$NON-NLS-1$
//		userguideButton.addActionListener(this);
//
//		aboutButton.setToolTipText(Msg.getString("GuideWindow.tooltip.about")); //$NON-NLS-1$
//		aboutButton.addActionListener(this);
//
//		tutorialButton.setToolTipText(Msg.getString("GuideWindow.tooltip.tutorial")); //$NON-NLS-1$
//		tutorialButton.addActionListener(this);
//
//		projectsiteButton.setToolTipText(Msg.getString("GuideWindow.tooltip.projectsite")); //$NON-NLS-1$
//		projectsiteButton.addActionListener(this);

		// discussionButton.setToolTipText(Msg.getString("GuideWindow.tooltip.discussion")); //$NON-NLS-1$
		// discussionButton.addActionListener(this);

//		wikiButton.setToolTipText(Msg.getString("GuideWindow.tooltip.wiki")); //$NON-NLS-1$
//		wikiButton.addActionListener(this);

		// A toolbar to hold all our buttons
		WebPanel toolPanel = new WebPanel(new FlowLayout());
		toolPanel.add(homeButton);
		toolPanel.add(backButton);
		toolPanel.add(forwardButton);
//		toolPanel.add(aboutButton);
//		toolPanel.add(tutorialButton);
//		toolPanel.add(userguideButton);
//		toolPanel.add(shortcutsButton);
//		toolPanel.add(projectsiteButton);
//		toolPanel.add(wikiButton);
		// toolPanel.add(discussionButton);
		mainPane.add(toolPanel, BorderLayout.NORTH);
			
//		browser = desktop.getBrowserJFX();
//		browserPanel = browser.getPanel();// .init();
//		mainPane.add(browserPanel, BorderLayout.CENTER);

		htmlPane = new HTMLContentPane();
		htmlPane.addHyperlinkListener(this);
		htmlPane.goToURL(guideURL);

		htmlPane.setBackground(Color.lightGray);
		htmlPane.setBorder(new EmptyBorder(2, 2, 2, 2));

		WebScrollPane scrollPane = new WebScrollPane(htmlPane);
		scrollPane.setBorder(new MarsPanelBorder());
		viewPort = (WebViewport) scrollPane.getViewport();
		viewPort.addComponentListener(this);
		viewPort.setScrollMode(WebViewport.BACKINGSTORE_SCROLL_MODE);
		
		mainPane.add(scrollPane);
		
		updateButtons();
		
		setResizable(true);
		setMaximizable(true);
		setVisible(true);

		setMinimumSize(new Dimension(800, 600));
		setSize(new Dimension(1024, 600));

//		if (desktop.getMainScene() != null) {
//			setClosable(false);
//		} else {
			Dimension desktopSize = desktop.getSize();
			Dimension jInternalFrameSize = getSize();
			int width = (desktopSize.width - jInternalFrameSize.width) / 2;
			int height = (desktopSize.height - jInternalFrameSize.height) / 2;
			setLocation(width, height);
//		}

		// Pack window.
		// pack(); // this will shrink the window to one line tall in swing mode
	}

	/**
	 * Set a display URL .
	 */
	public void setURL(String fileloc) {
		htmlPane.goToURL(getClass().getResource(fileloc));
	}
	
//	/** 
//	 * Set a display URL. Added displaying the hyperlink's path and html filename.
//	 * @param fileloc
//	 * 
//	 */
//	public void setURL(String fileloc) {
//		// goToURL(getClass().getResource(fileloc));
//		// browser.getStatusBarLabel().setText(fileloc);
//		String fullLink = getClass().getResource(fileloc).toExternalForm();
////		Platform.runLater(() -> {
////			browser.setTextInputCache(fullLink);
////			browser.checkInputURLType(fullLink);// , BrowserJFX.REMOTE_HTML);
////			browser.showFormattedURL();
////			browser.fireButtonGo(fullLink);
////		});
//	}
//
//	/** Gets the full URL string for internal html files. */
//	// Added displaying the hyperlink's path and html filename.
//	public String getFullURL(String fileloc) {
//		return getClass().getResource(fileloc).toExternalForm();
//	}

//	/** Implementing ActionListener method. */
//	@SuppressWarnings("restriction")
//	@Override
//	public void actionPerformed(ActionEvent event) {
//		Object source = event.getSource();
//		if (source == this.userguideButton) {
//			String input = guideURL.toExternalForm();
////			Platform.runLater(() -> {
////				browser.setTextInputCache(input);
////				browser.checkInputURLType(input);
////				browser.showFormattedURL();
////			});
//		}
//
//		else if (source == this.shortcutsButton) {
//			String input = shortcutsURL.toExternalForm();
////			Platform.runLater(() -> {
////				browser.setTextInputCache(input);
////				browser.checkInputURLType(input);
////				browser.showFormattedURL();
////			});
//		}
//
//		else if (source == this.aboutButton) {
//			String input = aboutURL.toExternalForm();
////			Platform.runLater(() -> {
////				browser.setTextInputCache(input);
////				browser.checkInputURLType(input);
////				browser.showFormattedURL();
////			});
//		}
//
//		else if (source == this.tutorialButton) {
//			String input = tutorialURL.toExternalForm();
////			Platform.runLater(() -> {
////				browser.setTextInputCache(input);
////				browser.checkInputURLType(input);
////				browser.showFormattedURL();
////			});
//		}
//
//		else if (source == this.projectsiteButton || source == this.wikiButton) {
////			Platform.runLater(() -> {
////				browser.setTextInputCache(projectsiteURLstring);
////				browser.checkInputURLType(projectsiteURLstring);
////				browser.showFormattedURL();
////			});
//		}
//	}

	/** Implementing ActionListener method. */
	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source == this.homeButton) {
			htmlPane.goToURL(guideURL);
			updateButtons();
		} else if (source == this.backButton) {
			htmlPane.back();
			updateButtons();
		} else if (source == this.forwardButton) {
			htmlPane.forward();
			updateButtons();
		}
	}
	
	/**
	 * Implement ComponentListener interface. Make sure the text is scrolled to the
	 * top. Need to find a better way to do this
	 * 
	 * @author Scott
	 */
	@Override
	public void componentResized(ComponentEvent e) {
		viewPort.setViewPosition(new Point(0, 0));
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentShown(ComponentEvent e) {
	}

	public void componentHidden(ComponentEvent e) {
	}

	/**
	 * Handles a click on a link.
	 * @param event the HyperlinkEvent
	 */
	@Override
	public void hyperlinkUpdate(HyperlinkEvent event) {
		if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			htmlPane.goToURL(event.getURL());
			updateButtons();
		}
	}
	
	/**
	 * Updates navigation buttons.
	 */
	public void updateButtons() {
		homeButton.setEnabled(true);
		backButton.setEnabled(!htmlPane.isFirst());
		forwardButton.setEnabled(!htmlPane.isLast());
	}
	
	/** Prepare tool window for deletion. */
	@Override
	public void destroy() {
	}

}