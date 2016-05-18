package ggc.nutri.dialogs;

import java.awt.event.ItemEvent;

import javax.swing.*;

import com.atech.graphics.dialogs.selector.SelectableInterface;
import com.atech.graphics.dialogs.selector.SelectorAbstractDialog;

import ggc.nutri.db.datalayer.NutritionDefinition;
import ggc.nutri.db.datalayer.NutritionHomeWeightType;
import ggc.nutri.util.DataAccessNutri;

/**
 *  Application:   GGC - GNU Gluco Control
 *
 *  See AUTHORS for copyright information.
 * 
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 * 
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 *  Filename:     FoodPartSelectorDialog
 *  Description:  Selector component for Nutrition and HomeWeight parts...
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class FoodPartSelectorDialog extends SelectorAbstractDialog
{

    private static final long serialVersionUID = 7262008735572008874L;
    private DataAccessNutri m_da = null;

    /**
     * Selector: Nutrition
     */
    public static final int SELECTOR_NUTRITION = 1;

    /**
     * Selector: Home Weight
     */
    public static final int SELECTOR_HOME_WEIGHT = 2;


    /**
     * Constructor
     * 
     * @param parent 
     * @param da
     * @param type
     * @param except
     */
    public FoodPartSelectorDialog(JDialog parent, DataAccessNutri da, int type, String except)
    {
        super(parent, da, type, except, true);
        this.showDialog();
    }


    /**
     * Constructor
     * 
     * @param parent 
     * @param da
     * @param type
     * @param except
     */
    public FoodPartSelectorDialog(JFrame parent, DataAccessNutri da, int type, String except)
    {
        super(parent, da, type, except, true);
        this.showDialog();
    }


    /**
     * Init Selector Values For Type
     */
    @Override
    public void initSelectorValuesForType()
    {
        if (this.getSelectorType() == FoodPartSelectorDialog.SELECTOR_NUTRITION)
        {
            setSelectorObject(new NutritionDefinition());
            setSelectorName(i18nControl.getMessage("SELECTOR_NUTRITION_DEFINITION"));
            setHelpStringId("GGC_Food_User_Select_Nutrition");
            setNewItemString(i18nControl.getMessage("NEW_NUTR_DEF"));
            setAllowedActions(SelectorAbstractDialog.SELECTOR_ACTION_CANCEL_AND_SELECT);
            this.getDescriptions().put("DESC_1", i18nControl.getMessage("NUTRITION_NAME"));
            setFilterType(SelectorAbstractDialog.SELECTOR_FILTER_TEXT);
            this.setHelpEnabled(true);
        }
        else if (this.getSelectorType() == FoodPartSelectorDialog.SELECTOR_HOME_WEIGHT)
        {
            setSelectorObject(new NutritionHomeWeightType());
            setSelectorName(i18nControl.getMessage("SELECTOR_HOME_WEIGHT"));
            setHelpStringId("GGC_Food_User_Select_HomeWeight");
            setNewItemString(i18nControl.getMessage("NEW_HOME_WEIGHT"));
            setAllowedActions(SelectorAbstractDialog.SELECTOR_ACTION_CANCEL_AND_SELECT);
            this.getDescriptions().put("DESC_1", i18nControl.getMessage("HOME_WEIGHT_NAME"));
            setFilterType(SelectorAbstractDialog.SELECTOR_FILTER_TEXT);
            this.setHelpEnabled(true);
        }

    }


    /**
     * Get Full Data
     * @see com.atech.graphics.dialogs.selector.SelectorAbstractDialog#getFullData()
     */
    @Override
    public void getFullData()
    {
        this.m_da = DataAccessNutri.getInstance();

        if (this.getSelectorType() == FoodPartSelectorDialog.SELECTOR_NUTRITION)
        {
            this.full = this.m_da.getNutriDb().getNutritionDefinitions();
        }
        else if (this.getSelectorType() == FoodPartSelectorDialog.SELECTOR_HOME_WEIGHT)
        {
            this.full = this.m_da.getNutriDb().getNutritionHomeWeights();
        }
        else
        {
            System.out.println("getFullData(): type not handled (" + this.getSelectorType() + ")");
        }
    }


    /**
     * Check and Execute Action: Edit
     */
    @Override
    public void checkAndExecuteActionEdit(SelectableInterface si)
    {
        System.out.println("checkAndExecuteActionEdit()");
    }


    /**
     * Check and Execute Action: New
     */
    @Override
    public void checkAndExecuteActionNew()
    {
        System.out.println("checkAndExecuteActionNew()");
    }


    /**
     * Check and Execute Action: Select
     */
    @Override
    public void checkAndExecuteActionSelect()
    {
        if (table != null)
        {
            if (table.getSelectedRow() == -1)
                return;

            this.selected_object = list.get(table.getSelectedRow());
            this.dispose();
        }

    }


    /**
     * Item State Changed
     * 
     * @see com.atech.graphics.dialogs.selector.SelectorAbstractDialog#itemStateChanged(java.awt.event.ItemEvent)
     */
    @Override
    public void itemStateChanged(ItemEvent e)
    {
    }

}
