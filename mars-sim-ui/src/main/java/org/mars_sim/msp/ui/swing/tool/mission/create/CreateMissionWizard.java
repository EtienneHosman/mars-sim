/**
 * Mars Simulation Project
 * CreateMissionWizard.java
 * @version 3.1.0 2017-02-03
 * @author Scott Davis
 */

package org.mars_sim.msp.ui.swing.tool.mission.create;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import org.mars_sim.msp.ui.swing.MainDesktopPane;
import org.mars_sim.msp.ui.swing.MarsPanelBorder;
import org.mars_sim.msp.ui.swing.ModalInternalFrame;
import org.mars_sim.msp.ui.swing.tool.mission.MissionWindow;

import com.alee.laf.button.WebButton;
import com.alee.laf.panel.WebPanel;

/**
 * A wizard for creating new missions.
 */
public class CreateMissionWizard
extends ModalInternalFrame
implements ActionListener {

	// Data members
	private int displayPanelIndex;
	
	private WebPanel infoPane;
	private WebButton prevButton;
	private WebButton nextButton;
	private WebButton finalButton;
	
	private MissionDataBean missionBean;
	private TypePanel typePanel;
	private MissionWindow missionWindow;
	
	private MainDesktopPane desktop;
	
	private List<WizardPanel> wizardPanels;

	/**
	 * Constructor.
	 * @param owner The owner frame.
	 */
	public CreateMissionWizard(MainDesktopPane desktop, MissionWindow missionWindow) {
		// Use ModalInternalFrame constructor
        super("Create Mission Wizard");
        //2016-09-24 Added missionWindow param
        this.missionWindow = missionWindow;
        this.desktop = desktop;
        
		// Set mission data bean.
		missionBean = new MissionDataBean();

		// Create info panel.
		infoPane = new WebPanel(new CardLayout());
		infoPane.setBorder(new MarsPanelBorder());

		//setContentPane(infoPane);

		add(infoPane, BorderLayout.CENTER);

		// Create wizard panels list.
		wizardPanels = new ArrayList<WizardPanel>();
		displayPanelIndex = 0;

		// Create initial set of wizard panels.
		typePanel = new TypePanel(this);
		addWizardPanel(typePanel);

        // Note: This panel is added so that next and final buttons are
        // enabled/disabled properly initially.
        addWizardPanel(new StartingSettlementPanel(this));

		// Create bottom button panel.
		WebPanel bottomButtonPane = new WebPanel(new FlowLayout(FlowLayout.CENTER));
		add(bottomButtonPane, BorderLayout.SOUTH);

		// Create previous button.
		prevButton = new WebButton("Previous");
		prevButton.addActionListener(this);
		prevButton.setEnabled(false);
		bottomButtonPane.add(prevButton);

		// Create next button.
		nextButton = new WebButton("Next");
		nextButton.addActionListener(this);
		nextButton.setEnabled(false);
		bottomButtonPane.add(nextButton);

		// Create final button.
		finalButton = new WebButton("Final");
		finalButton.addActionListener(this);
		finalButton.setEnabled(false);
		bottomButtonPane.add(finalButton);

		// Create cancel button.
		WebButton cancelButton = new WebButton("Cancel");
		cancelButton.addActionListener(
				new ActionListener() {
        			public void actionPerformed(ActionEvent e) {
        				// Dispose this dialog.
        				dispose();
        			}
				});
		bottomButtonPane.add(cancelButton);

		// Finish and display wizard.
		//pack();
        setSize(new Dimension(700, 600));
		//setLocationRelativeTo(owner);
		//setResizable(false);
		//setVisible(true);

        // Add to its own tab pane
//        if (desktop.getMainScene() != null)
//        	desktop.add(this);
//        	//desktop.getMainScene().getDesktops().get(1).add(this);
//        else 
        	desktop.add(this);
        
		Dimension desktopSize = desktop.getParent().getSize();
	    Dimension jInternalFrameSize = this.getSize();
	    int width = (desktopSize.width - jInternalFrameSize.width) / 2;
	    int height = (desktopSize.height - jInternalFrameSize.height) / 2;
	    setLocation(width, height);
	    
	    setModal(true);
	    setVisible(true);
	}

	/**
	 * Gets the current displayed wizard panel.
	 * @return wizard panel.
	 */
	private WizardPanel getCurrentWizardPanel() {
		return wizardPanels.get(displayPanelIndex);
	}

	/**
	 * Sets the final wizard panel for the mission type.
	 */
	void setFinalWizardPanels() {
		// Remove old final panels if any.
        int numPanels = wizardPanels.size();
		for (int x = 1; x < numPanels; x++) wizardPanels.remove(1);

		// Add mission type appropriate final panels.
		for (String type : MissionDataBean.MISSIONS) {
			if (missionBean.getType().equals(type)) {
	            addWizardPanel(new StudyPanel(this));
	            addWizardPanel(new LeadResearcherPanel(this));
	            addWizardPanel(new VehiclePanel(this));
	            // TODO: Change members panel to use lead researcher as member.
	            addWizardPanel(new MembersPanel(this));
	            //addWizardPanel(new BotMembersPanel(this));
	            addWizardPanel(new FieldSitePanel(this));
			}
		}
//        if (missionBean.getType().equals(MissionDataBean.AREOLOGY_FIELD_MISSION)) {
//            addWizardPanel(new StudyPanel(this));
//            addWizardPanel(new LeadResearcherPanel(this));
//            addWizardPanel(new VehiclePanel(this));
//            // TODO: Change members panel to use lead researcher as member.
//            addWizardPanel(new MembersPanel(this));
//            //addWizardPanel(new BotMembersPanel(this));
//            addWizardPanel(new FieldSitePanel(this));
//        }
//        else if (missionBean.getType().equals(MissionDataBean.BIOLOGY_FIELD_MISSION)) {
//            addWizardPanel(new StudyPanel(this));
//            addWizardPanel(new LeadResearcherPanel(this));
//            addWizardPanel(new VehiclePanel(this));
//            // TODO: Change members panel to use lead researcher as member.
//            addWizardPanel(new MembersPanel(this));
//            //addWizardPanel(new BotMembersPanel(this));
//            addWizardPanel(new FieldSitePanel(this));
//        }
//        else if (missionBean.getType().equals(MissionDataBean.METEOROLOGY_FIELD_MISSION)) {
//            addWizardPanel(new StudyPanel(this));
//            addWizardPanel(new LeadResearcherPanel(this));
//            addWizardPanel(new VehiclePanel(this));
//            // TODO: Change members panel to use lead researcher as member.
//            addWizardPanel(new MembersPanel(this));
//            //addWizardPanel(new BotMembersPanel(this));
//            addWizardPanel(new FieldSitePanel(this));
//        }
//        else if (missionBean.getType().equals(MissionDataBean.CONSTRUCTION_MISSION)) {
//            addWizardPanel(new ConstructionSettlementPanel(this));
//            addWizardPanel(new MembersPanel(this));
//            //addWizardPanel(new BotMembersPanel(this));
//            addWizardPanel(new ConstructionProjectPanel(this));
//            addWizardPanel(new ConstructionVehiclePanel(this));
//        }
//        else if (missionBean.getType().equals(MissionDataBean.EMERGENCY_SUPPLY_MISSION)) {
//            addWizardPanel(new StartingSettlementPanel(this));
//            addWizardPanel(new VehiclePanel(this));
//            addWizardPanel(new MembersPanel(this));
//            //addWizardPanel(new BotMembersPanel(this));
//            addWizardPanel(new DestinationSettlementPanel(this));
//            addWizardPanel(new EmergencySupplyPanel(this));
//        }
//		else if (missionBean.getType().equals(MissionDataBean.EXPLORATION_MISSION)) {
//            addWizardPanel(new StartingSettlementPanel(this));
//            addWizardPanel(new VehiclePanel(this));
//            addWizardPanel(new MembersPanel(this));
//            //addWizardPanel(new BotMembersPanel(this));
//            addWizardPanel(new ExplorationSitesPanel(this));
//        }
//		else if (missionBean.getType().equals(MissionDataBean.ICE_MISSION)) {
//            addWizardPanel(new StartingSettlementPanel(this));
//            addWizardPanel(new VehiclePanel(this));
//            addWizardPanel(new MembersPanel(this));
//            //addWizardPanel(new BotMembersPanel(this));
//            addWizardPanel(new ProspectingSitePanel(this));
//        }
//		else if (missionBean.getType().equals(MissionDataBean.MINING_MISSION)) {
//            addWizardPanel(new StartingSettlementPanel(this));
//            addWizardPanel(new VehiclePanel(this));
//            addWizardPanel(new MembersPanel(this));
//            //addWizardPanel(new BotMembersPanel(this));
//            addWizardPanel(new LightUtilityVehiclePanel(this));
//			addWizardPanel(new MiningSitePanel(this));
//		}
//		else if (missionBean.getType().equals(MissionDataBean.REGOLITH_MISSION)) {
//            addWizardPanel(new StartingSettlementPanel(this));
//            addWizardPanel(new VehiclePanel(this));
//            addWizardPanel(new MembersPanel(this));
//            //addWizardPanel(new BotMembersPanel(this));
//            addWizardPanel(new ProspectingSitePanel(this));
//        }
//		else if (missionBean.getType().equals(MissionDataBean.RESCUE_MISSION)) {
//            addWizardPanel(new StartingSettlementPanel(this));
//            addWizardPanel(new VehiclePanel(this));
//            addWizardPanel(new MembersPanel(this));
//            //addWizardPanel(new BotMembersPanel(this));
//            addWizardPanel(new RendezvousVehiclePanel(this));
//        }
//	    else if (missionBean.getType().equals(MissionDataBean.TRAVEL_MISSION)) {
//            addWizardPanel(new StartingSettlementPanel(this));
//            addWizardPanel(new VehiclePanel(this));
//            addWizardPanel(new MembersPanel(this));
//            //addWizardPanel(new BotMembersPanel(this));
//            addWizardPanel(new DestinationSettlementPanel(this));
//        }
//		else if (missionBean.getType().equals(MissionDataBean.TRADE_MISSION)) {
//            addWizardPanel(new StartingSettlementPanel(this));
//            addWizardPanel(new VehiclePanel(this));
//            //addWizardPanel(new MembersPanel(this));
//			addWizardPanel(new DestinationSettlementPanel(this));
//			addWizardPanel(new TradeGoodsPanel(this, false));
//			addWizardPanel(new TradeGoodsPanel(this, true));
//		}
//        else if (missionBean.getType().equals(MissionDataBean.SALVAGE_MISSION)) {
//            addWizardPanel(new SalvageSettlementPanel(this));
//            addWizardPanel(new MembersPanel(this));
//            //addWizardPanel(new BotMembersPanel(this));
//            addWizardPanel(new SalvageProjectPanel(this));
//            addWizardPanel(new SalvageVehiclePanel(this));
//        }
	}

	/**
	 * Adds a wizard panel to the list.
	 * @param newWizardPanel the wizard panel to add.
	 */
	private void addWizardPanel(WizardPanel newWizardPanel) {
		wizardPanels.add(newWizardPanel);
		infoPane.add(newWizardPanel, newWizardPanel.getPanelName());
	}

	/**
	 * Get the mission data bean.
	 * @return mission data bean.
	 */
	MissionDataBean getMissionData() {
		return missionBean;
	}

	/**
	 * Sets previous, next and final buttons to be enabled or disabled.
	 * @param nextEnabled true if next/final button is enabled.
	 */
	void setButtons(boolean nextEnabled) {

		// Enable previous button if after first panel.
		prevButton.setEnabled(displayPanelIndex > 0);

		if (nextEnabled) {
			nextButton.setEnabled(displayPanelIndex < (wizardPanels.size() - 1));
			finalButton.setEnabled(displayPanelIndex == (wizardPanels.size() - 1));
		}
		else {
			nextButton.setEnabled(false);
			finalButton.setEnabled(false);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == prevButton) buttonClickedPrev();
		else if (source == nextButton) buttonClickedNext();
		else if (source == finalButton) buttonClickedFinal();
	}

	/** Go to previous wizard panel. */
	public void buttonClickedPrev() {
		getCurrentWizardPanel().clearInfo();
		displayPanelIndex--;
		CardLayout layout = (CardLayout) infoPane.getLayout();
		layout.show(infoPane, getCurrentWizardPanel().getPanelName());
		nextButton.setEnabled(true);
		if (displayPanelIndex == 0) prevButton.setEnabled(false);
	}

	/** Go to next wizard panel. */
	public void buttonClickedNext() {
		if (getCurrentWizardPanel().commitChanges()) {
			displayPanelIndex++;
			setButtons(false);
			CardLayout layout = (CardLayout) infoPane.getLayout();
			WizardPanel currentPanel = getCurrentWizardPanel();
			currentPanel.updatePanel();
			layout.show(infoPane, currentPanel.getPanelName());
		}
	}

	public void buttonClickedFinal() {
		if (getCurrentWizardPanel().commitChanges()) {
			missionBean.createMission();
			dispose();
		}
	}
	
	
	public TypePanel getTypePanel() {
		return typePanel;
	}
	
	public MissionWindow getMissionWindow() {
		return missionWindow;
	}
	
	public MissionDataBean getMissionBean() {
		return missionBean;
	}
	
	public MainDesktopPane getDesktop() {
		return desktop;
	}
}