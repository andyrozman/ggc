package ggc.gui.dialogs.defs;

import ggc.core.db.hibernate.DoctorH;
import ggc.core.util.DataAccess;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.atech.help.HelpCapable;

public class StockListDef extends GUIListDefAbstract 
{
    

    @Override
    public void doTableAction(int action)
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
    public boolean hasFilter()
    {
        return false;
    }

    @Override
    public void init()
    {
        this.ic = DataAccess.getInstance().getI18nControlInstance();
        this.translation_root = "STOCKS";
        
    }

    @Override
    public String getDefName()
    {
        return "StockListDef";
    }

    @Override
    public Rectangle getTableSize()
    {
        return new Rectangle(20, 0, 350, 350);
    }

    @Override
    public Dimension getWindowSize()
    {
        return new Dimension(600, 500);
    }


}
