package ggc.gui.dialogs.stock.def;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

import com.atech.graphics.dialogs.guilist.*;
import com.atech.utils.ATSwingUtils;

import ggc.core.db.GGCDb;
import ggc.core.db.dto.StocktakingDTO;
import ggc.core.db.hibernate.StockH;
import ggc.core.util.DataAccess;

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

    DataAccess m_da = DataAccess.getInstance();

    // private ArrayList<DoctorH> active_list= null;

    private ArrayList<StockH> active_list = null;

    private GGCDb database;


    /**
     * Constructor 
     */
    public StockListDef()
    {
        database = m_da.getDb();
        init();
    }


    @Override
    public void doTableAction(String action)
    {
        // if (action.equals("add"))
        // {
        // StockSelectorDialog ssd = new
        // StockSelectorDialog(this.getParentDialog(), dataAccess, 1);
        // ssd.showDialog();
        // }
        // else if (action.equals("add_type"))
        // {
        // //StockTypeDialog std = new StockTypeDialog(this.getParentDialog());
        // //std.showDialog();
        // }
        // else if (action.equals("edit_type"))
        // {
        // StockSubTypeDialog sstd = new
        // StockSubTypeDialog(this.getParentDialog());
        // sstd.setVisible(true);
        // }
        // else
        if (action.equals("add_stocktaking"))
        {
            // StockAmounts sa = new StockAmounts(null, null,
            // this.getParentDialog());
            // sa.setVisible(true);
            System.out.println(this.getDefName() + " has not implemented action " + action);
        }
        else if (action.equals("edit_stocktaking"))
        {
            GUIListDialog guiListDialog = new GUIListDialog(this.getParentDialog(), new StocktakingListDef(), m_da);

            if (guiListDialog.isDataChanged())
            {
                loadData();
            }
        }
        else if (action.equals("manage_types"))
        {
            new GUIListDialog(this.getParentDialog(), new StockTypeListDef(), m_da);
        }
        else
        {
            System.out.println(this.getDefName() + " has not implemented action " + action);
        }
    }


    @Override
    public JTable getJTable()
    {
        if (this.table == null)
        {
            this.table = new JTable(new AbstractTableModel()
            {

                private static final long serialVersionUID = -9188128586566579737L;


                public int getColumnCount()
                {

                    return 4;
                }


                public int getRowCount()
                {
                    return active_list.size();
                }


                public Object getValueAt(int row, int column)
                {
                    // TODO Auto-generated method stub
                    StockH se = active_list.get(row);

                    switch (column)
                    {
                        case 0:
                            return se.getLocation(); // dh.getName();

                        case 1:
                            return se.getStocktakingId();
                            // return
                            // i18nControlAbstract.getMessage(dh.getDoctor_type().getName());
                    }

                    return null;
                }

            });

            String[] columns = { "Name", "Amount", "Calculated Till", "Location" };
            int[] cwidths = { 100, 100, 100, 180 }; // 480
            int cwidth = 0;

            TableColumnModel cm = table.getColumnModel();

            for (int i = 0; i < columns.length; i++)
            {
                cm.getColumn(i).setHeaderValue(ic.getMessage(columns[i]));

                cwidth = cwidths[i];

                if (cwidth > 0)
                {
                    cm.getColumn(i).setPreferredWidth(cwidth);
                }

            }

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

        this.setCustomDisplayHeader(true);
        this.filter_type = FILTER_NONE;
        // this.filter_enabled = true;

        // this.filter_type = GUIListDefAbstract.FILTER_COMBO_AND_TEXT;
        // this.filter_text = i18nControlAbstract.getMessage("FILTER") + ":";

        String s1[] = { ic.getMessage("STATUS_USED") + ":", ic.getMessage("DESCRIPTION") + ":" };
        this.filter_texts = s1;

        String s[] = { ic.getMessage("FILTER_ACTIVE"), ic.getMessage("FILTER_ACTIVE_1_MONTH_USED"),
                      ic.getMessage("FILTER_ACTIVE_2_MONTH_USED"), ic.getMessage("FILTER_ACTIVE_3-6_MONTH_USED"),
                      ic.getMessage("FILTER_ACTIVE_6M_MONTH_USED"), ic.getMessage("FILTER_ALL") };

        this.filter_options_combo1 = s;

        // FIXME
        this.button_defs = new ArrayList<ButtonDef>();
        this.button_defs.add(new LabelDef(this.ic.getMessage("STOCK_SUBTYPES"), LabelDef.FONT_BOLD));
        this.button_defs.add(new ButtonDef(this.ic.getMessage("MANAGE"), "manage_types", "manage_types desc",
                "table_sql_check.png"));
        // this.button_defs.add(new ButtonDef(this.ic.getMessage("EDIT"),
        // "edit_type", "STOCKS_TABLE_EDIT_DESC",
        // "table_edit.png"));
        // this.button_defs.add(new
        // ButtonDef(this.i18nControlAbstract.getMessage("VIEW_TYPE"),
        // "view", "STOCKS_TABLE_VIEW_DESC", "table_view.png"));
        this.button_defs.add(new DividerDef());
        this.button_defs.add(new LabelDef(this.ic.getMessage("STOCKTAKING"), LabelDef.FONT_BOLD));

        this.button_defs.add(new ButtonDef(this.ic.getMessage("NEW"), "new_stocktaking", "STOCKS_TABLE_VIEW_DESC",
                "table_add.png"));
        this.button_defs.add(new ButtonDef(this.ic.getMessage("EDIT"), "edit_stocktaking", "STOCKS_TABLE_VIEW_DESC",
                "table_edit.png"));
        // this.button_defs.add(new DividerDef());
        // this.button_defs.add(new ButtonDef(this.ic.getMessage("EDIT_LIST"),
        // "edit_list", "STOCKS_TABLE_VIEW_DESC",
        // "table_view.png"));

        this.def_parameters = new String[2];
        this.def_parameters[0] = "Test 1";
        this.def_parameters[1] = "Test 2";

        loadData();

    }


    public void loadData()
    {
        StocktakingDTO std = database.getLatestStocktakingDTO();

        this.active_list = new ArrayList<StockH>();

        // this.active_list = dataAccess.getDb().getStocks(-1, -1);
    }


    @Override
    public String getDefName()
    {
        return "StockListDef";
    }


    @Override
    public Rectangle getTableSize(int pos_y)
    {
        return new Rectangle(40, pos_y, 480, 250);
    }


    @Override
    public Dimension getWindowSize()
    {
        return new Dimension(700, 500);
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


    @Override
    public JPanel getCustomDisplayHeader()
    {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.blue);
        panel.setBounds(0, 0, 300, 55);

        ATSwingUtils.initLibrary();

        ATSwingUtils.getLabel("Test1:", 0, 0, 120, 25, panel);

        JLabel label_datetime = ATSwingUtils.getLabel("ccc", 130, 0, 90, 25, panel);

        ATSwingUtils.getLabel("Test2:", 0, 30, 120, 25, panel);

        JLabel label_ddd = ATSwingUtils.getLabel("ccc", 130, 30, 90, 25, panel);

        return panel;
    }


    @Override
    public void editTableRow()
    {
        // FIXME

        // Edit operation of Stocktaking element
    }

}
