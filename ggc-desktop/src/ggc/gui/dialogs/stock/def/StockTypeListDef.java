package ggc.gui.dialogs.stock.def;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;
import ggc.core.data.defs.StockTypeBase;
import ggc.core.db.GGCDb;
import ggc.core.db.hibernate.StockSubTypeH;
import ggc.core.util.DataAccess;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

import com.atech.graphics.dialogs.guilist.ButtonDef;
import com.atech.graphics.dialogs.guilist.GUIListDefAbstract;
import ggc.gui.dialogs.stock.StockSubTypeDialog;
import org.apache.commons.lang.StringUtils;

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

public class StockTypeListDef extends GUIListDefAbstract
{
    DataAccess dataAccess = DataAccess.getInstance();
    I18nControlAbstract i18nControl = dataAccess.getI18nControlInstance();

    private List<StockSubTypeH> dbList = null;
    private List<StockSubTypeH> filteredList = null;

    GGCDb database;

    AbstractTableModel model;
    StockTypeBase filterType = StockTypeBase.None;
    String filterText = "";


    /**
     * Constructor 
     */
    public StockTypeListDef()
    {
        this.database = dataAccess.getDb();
        init();
    }

    @Override
    public void doTableAction(String action)
    {
        if (action.equals("add_stocktype"))
        {
            StockSubTypeDialog dialog = new StockSubTypeDialog(this.getParentDialog());

            if (dialog.wasOperationSuccessful())
            {
                System.out.println("Succcess in list");
                loadData();


            }
            else
            {
                System.out.println("NOT Succcess");
            }

        }
        else if (action.equals("edit_stocktype"))
        {
            this.editTableRow();
        }
        else if (action.equals("delete_stocktype"))
        {
            int index = this.getParentDialog().getSelectedObjectIndexFromTable();

            if (index > -1)
            {
                StockSubTypeH stockType = this.filteredList.get(index);


                if (database.isStockSubTypeUsed(stockType))
                {
                    System.out.println("USED");
                }
                else
                {
                    System.out.println("NOT USED");
//                    loadData();
                }

            }
        }
        else
        {
            System.out.println(this.getDefName() + " has not implemented action " + action);
        }
    }


    private void refreshDataFromDb()
    {
        this.dbList = database.getStockTypes();
    }


    private void refreshFilter()
    {
        if (this.filteredList==null)
        {
            this.filteredList = new ArrayList<StockSubTypeH>();
        }
        else
        {
            this.filteredList.clear();
        }

        List<StockSubTypeH> found = new ArrayList<StockSubTypeH>();

        if (this.filterType!=StockTypeBase.None)
        {
            for (StockSubTypeH entry : this.dbList)
            {
                if (entry.getStockType() == this.filterType)
                {
                    found.add(entry);
                }
            }
        }
        else
        {
            found.addAll(this.dbList);
        }


        if (StringUtils.isNotBlank(this.filterText))
        {
            this.filterText = this.filterText.trim();


            for (StockSubTypeH entry : found)
            {
                if (entry.getName().contains(this.filterText))
                {
                    this.filteredList.add(entry);
                }
            }
        }
        else
        {
            this.filteredList.addAll(found);
        }

        if (model!=null)
        {
            model.fireTableDataChanged();
        }
    }



    @Override
    public JTable getJTable()
    {

        if (this.table == null)
        {


            this.table = new JTable(model = new AbstractTableModel()
            {

                private static final long serialVersionUID = -9188128586566579737L;

                public int getColumnCount()
                {

                    return 5;
                }

                public int getRowCount()
                {
                    return filteredList.size();
                }

                public Object getValueAt(int row, int column)
                {
                    StockSubTypeH sst = filteredList.get(row);

                    switch (column)
                    {
                        case 0:
                            return StockTypeBase.getByCode((int)sst.getStockTypeId()).getTranslation();

                        case 1:
                            return sst.getName();

                        case 2:
                            return sst.getDescription();

                        case 3:
                            return sst.getPackageContent() + " " + sst.getPackageContentUnit();

                        case 4:
                            return sst.getUsageDescription();

                    }

                    return null;
                }

            });

            String[] columns = { "Base Type", "Name", "Description", "Pkg Content", "Usage"};
            int[] cwidths = { 100, 100, 100, 100, 180 }; // 480
            int cwidth = 0;

            TableColumnModel cm = table.getColumnModel();

            for(int i = 0; i<columns.length; i++)
            {
                cm.getColumn(i).setHeaderValue(ic.getMessage(columns[i]));

                cwidth = cwidths[i];

                if (cwidth > 0)
                {
                    cm.getColumn(i).setPreferredWidth(cwidth);
                }
            }

//
//            stock_type_id bigint,
//            name character varying(512),
//                description character varying(2000),
//                content_pkg bigint,
//                usage_type integer,
//                usage_min integer,
//                usage_max integer,
//                active boolean,
//            extended text,
//            comment character varying(2000),


        }

        return this.table;

    }

    @Override
    public String getTitle()
    {
        return i18nControl.getMessage("STOCK_TYPE_LIST");
    }

    @Override
    public void init()
    {
        this.ic = DataAccess.getInstance().getI18nControlInstance();
        this.translation_root = "STOCKTYPE";

        //this.setCustomDisplayHeader(true);
        //this.filter_type = FILTER_NONE;
        // this.filter_enabled = true;

        this.filter_type = GUIListDefAbstract.FILTER_COMBO_AND_TEXT;
        // this.filter_text = i18nControlAbstract.getMessage("FILTER") + ":";

        String s1[] = { ic.getMessage("STOCK_GROUP") + ":", ic.getMessage("NAME") + ":" };
        this.filter_texts = s1;

//        String s[] = { ic.getMessage("FILTER_ACTIVE"), ic.getMessage("FILTER_ACTIVE_1_MONTH_USED"),
//                ic.getMessage("FILTER_ACTIVE_2_MONTH_USED"), ic.getMessage("FILTER_ACTIVE_3-6_MONTH_USED"),
//                ic.getMessage("FILTER_ACTIVE_6M_MONTH_USED"), ic.getMessage("FILTER_ALL") };

        this.filter_options_combo1 = StockTypeBase.getDescriptions();

        this.button_defs = new ArrayList<ButtonDef>();
        //this.button_defs.add(new LabelDef(this.ic.getMessage("STOCK_SUBTYPES"), LabelDef.FONT_BOLD));
        //this.button_defs.add(new ButtonDef(this.ic.getMessage("MANAGE"), "manage_type", "STOCKS_TABLE_ADD_DESC",
        //        "table_sql_check.png"));
//        this.button_defs.add(new ButtonDef(this.ic.getMessage("EDIT"), "edit_type", "STOCKS_TABLE_EDIT_DESC",
//                "table_edit.png"));
        // this.button_defs.add(new ButtonDef(this.i18nControlAbstract.getMessage("VIEW_TYPE"),
        // "view", "STOCKS_TABLE_VIEW_DESC", "table_view.png"));
        //this.button_defs.add(new DividerDef());
        //this.button_defs.add(new LabelDef(this.ic.getMessage("STOCKTAKING"), LabelDef.FONT_BOLD));

        this.button_defs.add(new ButtonDef(this.ic.getMessage("ADD"), "add_stocktype", "STOCKS_TABLE_VIEW_DESC",
                "table_add.png"));
        this.button_defs.add(new ButtonDef(this.ic.getMessage("EDIT"), "edit_stocktype", "STOCKS_TABLE_VIEW_DESC",
                "table_edit.png"));
        this.button_defs.add(new ButtonDef(this.ic.getMessage("DELETE"), "delete_stocktype", "STOCKS_TABLE_VIEW_DESC",
                "table_delete.png"));
//        this.button_defs.add(new DividerDef());
//        this.button_defs.add(new ButtonDef(this.ic.getMessage("EDIT_LIST"), "edit_list", "STOCKS_TABLE_VIEW_DESC",
//                "table_view.png"));

        this.def_parameters = new String[2];
        this.def_parameters[0] = "Test 1";
        this.def_parameters[1] = "Test 2";

        loadData();

    }

    public void loadData()
    {
        refreshDataFromDb();
        refreshFilter();

    }

    @Override
    public String getDefName()
    {
        return "StockTypeListDef";
    }

    @Override
    public Rectangle getTableSize(int pos_y)
    {
        return new Rectangle(40, pos_y, 580, 250);
    }

    @Override
    public Dimension getWindowSize()
    {
        return new Dimension(800, 500);
    }



    @Override
    public void setFilterCombo(String val)
    {
        filterType = StockTypeBase.getByDescription(val);
        this.refreshFilter();
    }



    @Override
    public void setFilterText(String val)
    {
        filterText = val;
        this.refreshFilter();
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
        int index = this.getParentDialog().getSelectedObjectIndexFromTable();

        if (index > -1)
        {
            StockSubTypeH stockType = this.filteredList.get(index);

            StockSubTypeDialog dialog = new StockSubTypeDialog(this.getParentDialog(), stockType);

            if (dialog.wasOperationSuccessful())
            {
                loadData();
            }
        }
    }


}
