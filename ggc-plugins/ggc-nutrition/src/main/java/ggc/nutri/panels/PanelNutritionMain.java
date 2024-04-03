package ggc.nutri.panels;

import com.atech.utils.ATSwingUtils;
import ggc.nutri.dialogs.NutritionTreeDialog;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.atech.help.HelpCapable;
import com.atech.utils.ATDataAccessAbstract;

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
 *  Filename:     PanelNutritionMain  
 *  Description:  Main Nutrition Panel
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class PanelNutritionMain extends GGCTreePanel implements HelpCapable
{

    private static final long serialVersionUID = -8242508108941080163L;

    Font font_big, font_normal, font_normal_b;
    JLabel label;
    JButton button, help_button;

    NutritionTreeDialog m_dialog = null;

    String[] nutrition_db = { "", "USDA_NUTRITION_DATABASE", "USER_NUTRITION_DATABASE", "MEALS_DATABASE" };

    String[] help_ids = { "", "GGC_Food_USDA_Main", "GGC_Food_User_Main", "GGC_Food_Meal_Main" };

    /**
     * Constructor
     * 
     * @param dia
     */
    public PanelNutritionMain(NutritionTreeDialog dia)
    {
        super(true);

        m_dialog = dia;

        ATSwingUtils.initLibrary();

        font_big = ATSwingUtils.getFont(ATSwingUtils.FONT_BIG_BOLD);
        font_normal_b = ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL_BOLD);
        font_normal = ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL);

        createPanel();

    }

    private void createPanel()
    {
        this.setSize(550, 520);
        this.setLayout(null);

        Font fnt_18 = new Font("Times New Roman", Font.PLAIN, 14);

        String nut_db = nutrition_db[this.m_dialog.getNutritionType()];

        label = new JLabel(ic.getMessage(nut_db));
        label.setBounds(0, 35, 520, 40);
        label.setFont(font_big);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(label, null);

        label = new JLabel(ic.getMessage(nut_db + "_DESC"));
        label.setBounds(40, 120, 400, 250);
        label.setVerticalAlignment(SwingConstants.TOP);
        label.setFont(fnt_18);
        this.add(label, null);

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

    @Override
    public Component getComponent()
    {
        return this.m_dialog;
    }

    @Override
    public JButton getHelpButton()
    {
        return this.help_button;
    }

    public String getHelpId()
    {
        return this.help_ids[this.m_dialog.getNutritionType()];
    }

}
