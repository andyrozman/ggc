package ggc.core.nutrition.panels;

import ggc.core.nutrition.NutritionTreeDialog;
import ggc.core.util.DataAccess;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

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

public class PanelNutritionMain extends GGCTreePanel
{

    private static final long serialVersionUID = -8242508108941080163L;

    Font font_big, font_normal, font_normal_b;
    JLabel label;
    JButton button;

    NutritionTreeDialog m_dialog = null;

    String[] nutrition_db = {
	    "",
	    "USDA_NUTRITION_DATABASE",
	    "USER_NUTRITION_DATABASE",
	    "MEALS_DATABASE"
    };
    

    /**
     * Constructor
     * 
     * @param dia
     */
    public PanelNutritionMain(NutritionTreeDialog dia)
    {
        super(false, dia.ic);

        m_dialog = dia;

        font_big = m_da.getFont(DataAccess.FONT_BIG_BOLD);
        font_normal_b = m_da.getFont(DataAccess.FONT_NORMAL_BOLD);
        font_normal = m_da.getFont(DataAccess.FONT_NORMAL);

        createPanel();

    }



    private void createPanel()
    {
        this.setSize(460, 520);
        this.setLayout(null);

        Font fnt_18 = new Font("Times New Roman", Font.PLAIN, 14);

        String nut_db = nutrition_db[this.m_dialog.getType()];
        
        label = new JLabel(ic.getMessage(nut_db));
        label.setBounds(0, 35, 520, 40);
        label.setFont(font_big); 
        label.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(label, null);


        label = new JLabel(ic.getMessage(nut_db + "_DESC"));
        label.setBounds(40, 120, 400, 250);
        label.setVerticalAlignment(JLabel.TOP);
        label.setFont(fnt_18); 
        this.add(label, null);

        return;
    }

    /**
     * Set Parent
     * 
     * @see com.atech.graphics.components.EditableAbstractPanel#setParent(java.lang.Object)
     */
    public void setParent(Object obj)
    {
    }


    /**
     * Set Data
     * 
     * @see com.atech.graphics.components.EditableAbstractPanel#setData(java.lang.Object)
     */
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
    public String getWarningString(int _action_type)
    {
        return null;
    }



    /**
     * Was data in this panel changed.
     * 
     * @return true if data changed, false otherwise
     */
    public boolean hasDataChanged()
    {
	return false;
    }



    /**
     * Save data in panel
     * 
     * @return true if save was successful
     */
    public boolean saveData()
    {
	return false;
    }
    
    

}
    
    

