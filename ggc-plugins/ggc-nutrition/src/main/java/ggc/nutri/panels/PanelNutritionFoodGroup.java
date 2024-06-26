package ggc.nutri.panels;

import java.awt.*;

import javax.swing.*;

import com.atech.utils.ATSwingUtils;

import ggc.nutri.data.GGCTreeRoot;
import ggc.nutri.db.datalayer.FoodGroup;
import ggc.nutri.db.datalayer.MealGroup;
import ggc.nutri.dialogs.NutritionTreeDialog;

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
 *  Filename:     PanelNutritionFoodGroup  
 *  Description:  Panel for displaying food/meal group
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class PanelNutritionFoodGroup extends GGCTreePanel
{

    private static final long serialVersionUID = -488798706263908198L;

    Font font_big, font_normal, font_normal_b;
    JLabel label, label_name, label_name_i18n, label_parent, label_title, label_name_i18n_key;
    JButton button;

    NutritionTreeDialog m_dialog = null;

    int group_type = 2;


    /**
     * Constructor
     * 
     * @param dia
     * @param type
     */
    public PanelNutritionFoodGroup(NutritionTreeDialog dia, int type)
    {
        super(true);

        this.group_type = type;

        m_dialog = dia;

        ATSwingUtils.initLibrary();

        font_big = ATSwingUtils.getFont(ATSwingUtils.FONT_BIG_BOLD);
        font_normal_b = ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL_BOLD);
        font_normal = ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL);

        createPanel();

    }


    private void createPanel()
    {
        this.setSize(520, 460);
        this.setLayout(null);

        Font fnt_14_bold = new Font("Times New Roman", Font.BOLD, 14);
        Font fnt_14 = new Font("Times New Roman", Font.PLAIN, 14);

        label_title = new JLabel(ic.getMessage("FOOD_GROUP"));
        label_title.setBounds(0, 35, 520, 40);
        label_title.setFont(font_big);
        label_title.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(label_title, null);

        // this.setDescription(ch.getDescription());
        // this.setDescription_i18n(ch.getDescription_i18n());

        label = new JLabel(ic.getMessage("GROUP_NAME") + ":");
        label.setBounds(60, 110, 100, 25);
        label.setFont(fnt_14_bold);
        this.add(label, null);

        label_name = new JLabel();
        label_name.setBounds(60, 140, 300, 25);
        label_name.setFont(fnt_14);
        this.add(label_name, null);

        label = new JLabel(ic.getMessage("TRANSLATION_KEYWORD") + ":");
        label.setBounds(60, 190, 320, 25);
        label.setFont(fnt_14_bold);
        this.add(label, null);

        label_name_i18n_key = new JLabel();
        label_name_i18n_key.setBounds(60, 220, 320, 25);
        label_name_i18n_key.setFont(fnt_14);
        this.add(label_name_i18n_key, null);

        label = new JLabel(ic.getMessage("TRANSLATED_NAME") + ":");
        label.setBounds(60, 270, 300, 25);
        label.setFont(fnt_14_bold);
        this.add(label, null);

        label_name_i18n = new JLabel();
        label_name_i18n.setBounds(60, 300, 300, 25);
        label_name_i18n.setFont(fnt_14);
        this.add(label_name_i18n, null);

        label = new JLabel(ic.getMessage("PARENT_GROUP"));
        label.setBounds(60, 350, 300, 25);
        label.setFont(fnt_14_bold);
        this.add(label, null);

        label_parent = new JLabel("ddd");
        label_parent.setBounds(60, 380, 300, 25);
        label_parent.setFont(fnt_14);
        this.add(label_parent, null);

        this.help_button = ATSwingUtils.createHelpIconByBounds(470, 10, 35, 35, this, ATSwingUtils.FONT_NORMAL, m_da);
        this.add(help_button);

        m_da.enableHelp(this);

        return;
    }


    /**
     * Set Parent
     *   
     * @see com.atech.graphics.components.EditableAbstractPanel#setParent(java.lang.Object)
     */
    @Override
    public void setParent(Object obj)
    {
    }


    /**
     * Set Data
     * 
     * @see com.atech.graphics.components.EditableAbstractPanel#setData(java.lang.Object)
     */
    @Override
    public void setData(Object obj)
    {

        if (group_type == GGCTreeRoot.TREE_USDA_NUTRITION || group_type == GGCTreeRoot.TREE_USER_NUTRITION)
        {
            FoodGroup group = (FoodGroup) obj;

            label_title.setText(ic.getMessage("FOOD_GROUP"));
            label_name.setText(group.getName());
            label_name_i18n_key.setText(group.getName_i18n());
            label_name_i18n.setText(ic.getMessage(group.getName_i18n()));

            if (group.getParentId() > 0)
            {
                // FoodGroup fg =
                // dataAccess.tree_roots.get("2").m_groups_ht.get("" +
                // group.getParentId());
                FoodGroup fg = m_da.getDbCache().tree_roots.get("2").findFoodGroup(2, group.getParentId());
                this.label_parent.setText(fg.getName());
            }
            else
            {
                this.label_parent.setText(ic.getMessage("ROOT"));
            }
        }
        else
        {
            MealGroup group = (MealGroup) obj;

            label_title.setText(ic.getMessage("MEAL_GROUP"));
            label_name.setText(group.getName());
            label_name_i18n_key.setText(group.getNameI18n());
            label_name_i18n.setText(ic.getMessage(group.getNameI18n()));

            if (group.getParentId() > 0)
            {
                MealGroup fg = m_da.getDbCache().tree_roots.get("3").findMealGroup(3, group.getParentId());
                // MealGroup fg =
                // dataAccess.tree_roots.get("3").m_meal_groups_ht.get("" +
                // group.getParentId());
                this.label_parent.setText(fg.getName());
            }
            else
            {
                this.label_parent.setText(ic.getMessage("ROOT"));
            }

        }

    }


    /**
     * Get Warning string. This method returns warning string for either add or edit.
     * If value returned is null, then no warning message box will be displayed.
     * 
     * @param _action_type type of action (ACTION_ADD, ACTION_EDIT)
     * @return String value as warning string
     */
    @Override
    public String getWarningString(int _action_type)
    {
        return null;
    }


    /**
     * Was data in this panel changed.
     * 
     * @return true if data changed, false otherwise
     */
    @Override
    public boolean hasDataChanged()
    {
        return false;
    }


    /**
     * Save data in panel
     * 
     * @return true if save was successful
     */
    @Override
    public boolean saveData()
    {
        return false;
    }


    public String getHelpId()
    {
        if (this.m_dialog.getNutritionType() == GGCTreeRoot.TREE_USDA_NUTRITION)
            return "GGC_Food_USDA_Group";
        else
            return "GGC_Food_User_Group_View";

    }

}
