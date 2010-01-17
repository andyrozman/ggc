package ggc.gui.dialogs.defs;

import ggc.core.db.hibernate.DoctorH;
import ggc.core.util.DataAccess;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.atech.graphics.dialogs.ButtonDef;
import com.atech.graphics.dialogs.GUIListDefAbstract;

public class StockListDef extends GUIListDefAbstract 
{
    
    private ArrayList<DoctorH> active_list= null;
    //private ArrayList<DoctorH> full_list= null;
    
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
