/**
 * Mars Simulation Project
 * SalvageValues.java
 * @version 2.90 2010-04-26
 * @author Scott Davis
 */
package org.mars_sim.msp.core.structure.construction;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.mars_sim.msp.core.Simulation;
import org.mars_sim.msp.core.resource.Part;
import org.mars_sim.msp.core.structure.Settlement;
import org.mars_sim.msp.core.structure.building.Building;
import org.mars_sim.msp.core.structure.building.BuildingManager;
import org.mars_sim.msp.core.structure.goods.GoodsManager;
import org.mars_sim.msp.core.structure.goods.GoodsUtil;
import org.mars_sim.msp.core.time.MarsClock;

/**
 * Calculates values for salvaging buildings at a settlement.
 */
public class SalvageValues implements Serializable {

    // Data members
    private Settlement settlement;
    private Map<Integer, Double> settlementSalvageValueCache;
    private MarsClock settlementSalvageValueCacheTime;
    
    /**
     * Constructor
     * @param settlement the settlement
     */
    SalvageValues(Settlement settlement) {
        this.settlement = settlement;
    }
    
    /**
     * Gets the overall profit for construction salvage at the settlement.
     * @return profit (VP)
     * @throws Exception if error determining profit.
     */
    public double getSettlementSalvageProfit() throws Exception {
        return getSettlementSalvageProfit(Integer.MAX_VALUE);
    }
    
    /**
     * Gets the overall profit for construction salvage at the settlement.
     * @param constructionSkill the architect's construction skill.
     * @return profit (VP)
     * @throws Exception if error determining profit.
     */
    public double getSettlementSalvageProfit(int constructionSkill) 
            throws Exception {
        
        MarsClock currentTime = Simulation.instance().getMasterClock().getMarsClock();
        if ((settlementSalvageValueCacheTime == null) || 
                (MarsClock.getTimeDiff(currentTime, settlementSalvageValueCacheTime) > 1000D)) {
            if (settlementSalvageValueCache == null) 
                settlementSalvageValueCache = new HashMap<Integer, Double>();
            settlementSalvageValueCache.clear();
            settlementSalvageValueCacheTime = (MarsClock) currentTime.clone();
        }
        
        if (!settlementSalvageValueCache.containsKey(constructionSkill)) {
            double profit = 0D;
            
            double existingSitesProfit = getAllSalvageSitesProfit(constructionSkill);
            if (existingSitesProfit > profit) profit = existingSitesProfit;
            
            double newSiteProfit = getNewSalvageSiteProfit(constructionSkill);
            if (newSiteProfit > profit) profit = newSiteProfit;
            
            settlementSalvageValueCache.put(constructionSkill, profit);
        }
        
        return settlementSalvageValueCache.get(constructionSkill);
    }
    
    /**
     * Gets the overall salvage profit of all existing construction sites at a settlement
     * that can be worked on with a given construction skill.
     * @param constructionSkill the architect's construction skill.
     * @return profit (VP)
     * @throws Exception if error determining profit.
     */
    public double getAllSalvageSitesProfit(int constructionSkill) 
            throws Exception {
        
        double result = 0D;
        
        ConstructionManager manager = settlement.getConstructionManager();
        Iterator<ConstructionSite> i = manager.getConstructionSitesNeedingSalvageMission().iterator();
        while (i.hasNext()) {
            double profit = getSalvageSiteProfit(i.next(), constructionSkill);
            if (profit > result) result = profit;
        }
        
        return result;
    }
    
    /**
     * Gets the salvage profit of an existing construction site at a settlement.
     * @param site the construction site.
     * @return profit (VP)
     * @throws Exception if error determining profit.
     */
    public double getSalvageSiteProfit(ConstructionSite site) throws Exception {
        return getSalvageSiteProfit(site, Integer.MAX_VALUE);
    }
    
    /**
     * Gets the salvage profit of an existing construction site at a settlement.
     * @param site the construction site.
     * @param constructionSkill the architect's construction skill.
     * @return profit (VP)
     * @throws Exception if error determining profit.
     */
    public double getSalvageSiteProfit(ConstructionSite site, int constructionSkill) 
            throws Exception {
        
        double result = 0D;
        
        if (!site.isUndergoingSalvage()) {
            ConstructionStage stage = site.getCurrentConstructionStage();
            ConstructionStageInfo prerequisiteStage = ConstructionUtil.getPrerequisiteStage(stage.getInfo());
            
            if (site.hasUnfinishedStage()) {
                if (stage.getInfo().getArchitectConstructionSkill() <= constructionSkill) {
                    result = getSalvageStageValue(prerequisiteStage);
                    
                    // Determine estimated salvaged parts value.
                    result += getEstimatedSalvagedPartsValue(stage.getInfo(), constructionSkill, 1D);
                }
            }
            else {
                double currentValue = getSalvageStageValue(stage.getInfo());
                double preValue = getSalvageStageValue(prerequisiteStage);
                
                // Determine estimated construction parts returned.
                double partsValue = getEstimatedSalvagedPartsValue(stage.getInfo(), constructionSkill, 1D);
                
                result = preValue - currentValue + partsValue;
            }
        }
        
        return result;
    }
    
    /**
     * Gets the profit of creating a new salvage construction site at a settlement.
     * @param constructionSkill the architect's construction skill.
     * @return profit (VP)
     * @throws Exception if error determining profit.
     */
    public double getNewSalvageSiteProfit(int constructionSkill) 
            throws Exception {
        
        double result = 0D;
        
        BuildingManager buildingManager = settlement.getBuildingManager();
        Iterator<Building> i = buildingManager.getBuildings().iterator();
        while (i.hasNext()) {
            double salvageProfit = getNewBuildingSalvageProfit(i.next(), constructionSkill);
            if (salvageProfit > result) result = salvageProfit;
        }
        
        return result;
    }
    
    /**
     * Gets the profit from salvaging a given existing building.
     * @param building the building.
     * @param constructionSkill the architect's construction skill.
     * @return salvage profit (VP).
     * @throws Exception if error determining salvage profit.
     */
    public double getNewBuildingSalvageProfit(Building building, int constructionSkill)
            throws Exception {
        double result = 0D;
        
        // Get current value of building.
        BuildingManager buildingManager = settlement.getBuildingManager();
        double buildingValue = buildingManager.getBuildingValue(building);
        
        // Get value of prerequisite frame stage.
        double frameStageValue = 0D;
        ConstructionStageInfo buildingStage = ConstructionUtil.getConstructionStageInfo(building.getName());
        if ((buildingStage != null) && buildingStage.isSalvagable() && 
                (constructionSkill >= buildingStage.getArchitectConstructionSkill())) {
            ConstructionStageInfo frameStage = ConstructionUtil.getPrerequisiteStage(buildingStage);
            frameStageValue = getSalvageStageValue(frameStage);
            
            // Determine value of estimated salvaged parts.
            double buildingWear = building.getMalfunctionManager().getWearCondition();
            double partsValue = getEstimatedSalvagedPartsValue(buildingStage, constructionSkill, (buildingWear / 100D));
            
            double salvageProfit = frameStageValue - buildingValue + partsValue;
            result = salvageProfit;
        }
        
        return result;
    }
    
    /**
     * Gets the value of a construction stage info.
     * @param stageInfo the construction stage info.
     * @return value of the stage (VP).
     * @throws Exception if error determining value.
     */
    private double getSalvageStageValue(ConstructionStageInfo stageInfo) throws Exception {
        // Use construction stage value.
        return settlement.getConstructionManager().getConstructionValues().getConstructionStageValue(stageInfo);
    }
    
    /**
     * Gets the estimated value of salvaged parts.
     * @param stageInfo the stage info.
     * @param constructionSkill the architect's construction skill.
     * @param wearCondition the wear factor for the stage (0.0 - 1.0).
     * @return value of salvaged parts.
     * @throws Exception if error determining value.
     */
    private double getEstimatedSalvagedPartsValue(ConstructionStageInfo stageInfo, int constructionSkill, 
            double wearConditionModifier) throws Exception {
        double result = 0D;
        
        GoodsManager goodsManager = settlement.getGoodsManager();
        
        Iterator<Part> i = stageInfo.getParts().keySet().iterator();
        while (i.hasNext()) {
            Part part = i.next();
            int number = stageInfo.getParts().get(part);
            double partValue = goodsManager.getGoodValuePerItem(GoodsUtil.getResourceGood(part));
            result += number * partValue;
        }
        
        // Modify total parts good value by wear condition and salvager skill.
        double valueModifier = .25D + (wearConditionModifier * .25D) + ((double) constructionSkill * .05D);
        result *= valueModifier;
        
        return result;
    }
}