package ggc.gui.dialogs.defs;

import ggc.core.db.hibernate.DoctorH;
import ggc.core.util.DataAccess;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.atech.graphics.dialogs.guilist.ButtonDef;
import com.atech.graphics.dialogs.guilist.GUIListDefAbstract;

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
 *  Filename:     HbA1cDialog2  
 *  Description:  Dialog for HbA1c, using new graph framework
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class StockListDef extends GUIListDefAbstract 
{
    
    private ArrayList<DoctorH> active_list= null;

    /**
     * Constructor 
     */
    public StockListDef()
    {
        init();
    }
    

    @Override
    public void doTableAction(String action)
    {
        System.out.println(this.getDefName() + " has not implemented action " + action);
    }

    @Override
    public JTable getJTable()
    {
        if (this.table==null)
        {
        this.table = new JTable(new AbstractTableModel()
        {

            private static final long serialVersionUID = -9188128586566579737L;

            public int getColumnCount()
            {
                // TODO Auto-generated method stub
                return 2;
            }

            public int getRowCount()
            {
                return active_list.size();
            }

            public Object getValueAt(int row, int column)
            {
                // TODO Auto-generated method stub
                DoctorH dh = (DoctorH)active_list.get(row);
                
                switch(column)
                {
                case 0:
                    return dh.getName();
                    
                case 1:
                    return ic.getMessage(dh.getDoctor_type().getName());
                }
                
                return null;
            }
            
        });
        
        
        }
        
        return this.table;
        
    }

    @Override
    public String getTitle()
    {
        return "STOCKS_LIST";
    }

   

    @Override
    public void init()
    {
        this.ic = DataAccess.getInstance().getI18nControlInstance();
        this.translation_root = "STOCKS";
        //this.filter_enabled = true;
        
        this.filter_type = GUIListDefAbstract.FILTER_COMBO_AND_TEXT;
        //this.filter_text = ic.getMessage("FILTER") + ":";
        
        String s1[] = { ic.getMessage("STATUS_USED") + ":",
                        ic.getMessage("DESCRIPTION") + ":"};
        this.filter_texts = s1;
        
        String s[] = { ic.getMessage("FILTER_ACTIVE"),
                       ic.getMessage("FILTER_ACTIVE_1_MONTH_USED"),
                       ic.getMessage("FILTER_ACTIVE_2_MONTH_USED"),
                       ic.getMessage("FILTER_ACTIVE_3-6_MONTH_USED"),
                       ic.getMessage("FILTER_ACTIVE_6M_MONTH_USED"),
                       ic.getMessage("FILTER_ALL")
                       };
        
        this.filter_options_combo1 = s;
        
        this.button_defs = new ArrayList<ButtonDef>();
        this.button_defs.add(new ButtonDef(this.ic.getMessage("ADD"), "add", "STOCKS_TABLE_ADD_DESC", "table_add.png"));
        this.button_defs.add(new ButtonDef(this.ic.getMessage("EDIT"), "edit", "STOCKS_TABLE_EDIT_DESC", "table_edit.png"));
        this.button_defs.add(new ButtonDef(this.ic.getMessage("VIEW"), "view", "STOCKS_TABLE_VIEW_DESC", "table_view.png"));
        
    }

    @Override
    public String getDefName()
    {
        return "StockListDef";
    }

    @Override
    public Rectangle getTableSize(int pos_y)
    {
        return new Rectangle(40, pos_y, 380, 250);
    }

    @Override
    public Dimension getWindowSize()
    {
        return new Dimension(600, 500);
    }


    @Override
    public void setFilterCombo(String val)
    {
        // TODO Auto-generated method stub
        System.out.println("Combo changed to: " + val);
        
    }


    @Override
    public void setFilterText(String val)
    {
        // TODO Auto-generated method stub
        System.out.println("Text Box changed to: " + val);
        
    }


    @Override
    public void setFilterCombo_2(String val)
    {
    }

    
    

    


}
