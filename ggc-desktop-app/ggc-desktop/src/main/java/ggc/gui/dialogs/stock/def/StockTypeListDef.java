package ggc.gui.dialogs.stock.def;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import org.apache.commons.lang.StringUtils;

import com.atech.graphics.dialogs.guilist.ButtonDef;
import com.atech.graphics.dialogs.guilist.GUIListDefAbstract;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;

import ggc.core.data.defs.StockTypeBase;
import ggc.core.db.GGCDb;
import ggc.core.db.hibernate.StockSubTypeH;
import ggc.core.db.hibernate.doc.DoctorH;
import ggc.core.util.DataAccess;
import ggc.gui.dialogs.inventory.InventoryItemTypeDialog;

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
@Deprecated
public class StockTypeListDef extends GUIListDefAbstract
{

    // USED: 70%

    DataAccess dataAccess = DataAccess.getInstance();
    I18nControlAbstract i18nControl = dataAccess.getI18nControlInstance();

    private List<StockSubTypeH> dbList = null;
    private List<StockSubTypeH> filteredList = null;

    GGCDb database;

    AbstractTableModel model;
    StockTypeBase filterStockType = StockTypeBase.None;
    String filterText = "";


    /**
     * Constructor 
     */
    public StockTypeListDef()
    {

        super(DataAccess.getInstance(), //
                new DoctorH(), // object listed
                "STOCK_TYPE_LIST", // title
                "StockTypeListDef", // defintion name
                "Doc_DocList", // help Id
                new Rectangle(40, 0, 580, 250), // table bounds
                new Dimension(800, 500) // size
        );

        // this.database = dataAccess.getDb();
        // init();
    }


    @Override
    public void doTableAction(String action)
    {
        if (action.equals("add_stocktype"))
        {
            InventoryItemTypeDialog dialog = new InventoryItemTypeDialog(this.getParentDialog());

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
                    ATSwingUtils.showMessageDialog(this.getParentDialog(), ATSwingUtils.DialogType.Error,
                        "STOCK_SUB_TYPE_IN_USE", i18nControl);
                }
                else
                {
                    database.deleteHibernate(stockType);
                    loadData();
                }

            }
        }
        else
        {
            System.out.println(this.getDefName() + " has not implemented action " + action);
        }
    }


    @Override
    public boolean doCustomTableAction(String action)
    {
        return false;
    }


    // private void refreshDataFromDb()
    // {
    // this.dbList = database.getStockTypes();
    // }

    @Override
    public void filterData()
    {
        if (this.filteredList == null)
        {
            this.filteredList = new ArrayList<StockSubTypeH>();
        }
        else
        {
            this.filteredList.clear();
        }

        List<StockSubTypeH> found = new ArrayList<StockSubTypeH>();

        if (this.filterStockType != StockTypeBase.None)
        {
            for (StockSubTypeH entry : this.dbList)
            {
                if (entry.getStockType() == this.filterStockType)
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

        if (model != null)
        {
            model.fireTableDataChanged();
        }
    }

    // @Override
    // public JTable getJTable()
    // {
    //
    // if (this.table == null)
    // {
    //
    // this.table = new JTable(model = new AbstractTableModel()
    // {
    //
    // private static final long serialVersionUID = -9188128586566579737L;
    //
    //
    // public int getColumnCount()
    // {
    //
    // return 5;
    // }
    //
    //
    // public int getRowCount()
    // {
    // return filteredList.size();
    // }
    //
    //
    // public Object getValueAt(int row, int column)
    // {
    // StockSubTypeH sst = filteredList.get(row);
    //
    // switch (column)
    // {
    // case 0:
    // return StockTypeBase.getByCode((int)
    // sst.getStockTypeId()).getTranslation();
    //
    // case 1:
    // return sst.getName();
    //
    // case 2:
    // return sst.getDescription();
    //
    // case 3:
    // return sst.getPackageContent() + " " +
    // i18nControl.getMessage(sst.getPackageContentUnit());
    //
    // case 4:
    // return sst.getUsageDescription();
    //
    // }
    //
    // return null;
    // }
    //
    // });
    //
    // // FIXME
    // String[] columns = { "STOCK_GROUP", "NAME", "DESCRIPTION",
    // "PACKAGE_CONTENT_SHORT", "STOCK_USAGE" };
    // int[] cwidths = { 100, 120, 160, 100, 100 }; // 480
    // int cwidth = 0;
    //
    // TableColumnModel cm = table.getColumnModel();
    //
    // for (int i = 0; i < columns.length; i++)
    // {
    // cm.getColumn(i).setHeaderValue(this.i18nControl.getMessage(columns[i]));
    //
    // cwidth = cwidths[i];
    //
    // if (cwidth > 0)
    // {
    // cm.getColumn(i).setPreferredWidth(cwidth);
    // }
    // }
    //
    // //
    // // stock_type_id bigint,
    // // name character varying(512),
    // // description character varying(2000),
    // // content_pkg bigint,
    // // usage_type integer,
    // // usage_min integer,
    // // usage_max integer,
    // // active boolean,
    // // extended text,
    // // comment character varying(2000),
    //
    // }
    //
    // return this.table;
    //
    // }


    // @Override
    // public String getTitle()
    // {
    // return i18nControl.getMessage("STOCK_TYPE_LIST");
    // }

    @Override
    public void init()
    {
        this.i18nControl = DataAccess.getInstance().getI18nControlInstance();
        this.translationRoot = "STOCKTYPE";

        // this.setCustomDisplayHeaderEnabled(true);
        // this.filter_type = FILTER_NONE;
        // this.filter_enabled = true;

        this.filterType = GUIListDefAbstract.GuiListFilterType.ComboAndText;
        // this.filter_text = i18nControlAbstract.getMessage("FILTER") + ":";

        String s1[] = { this.i18nControl.getMessage("STOCK_GROUP") + ":", this.i18nControl.getMessage("NAME") + ":" };
        this.filterDescriptionTexts = s1;

        // String s[] = { i18nControl.getMessage("FILTER_ACTIVE"),
        // i18nControl.getMessage("FILTER_ACTIVE_1_MONTH_USED"),
        // i18nControl.getMessage("FILTER_ACTIVE_2_MONTH_USED"),
        // i18nControl.getMessage("FILTER_ACTIVE_3-6_MONTH_USED"),
        // i18nControl.getMessage("FILTER_ACTIVE_6M_MONTH_USED"),
        // i18nControl.getMessage("FILTER_ALL") };

        this.filterOptionsCombo1 = StockTypeBase.getDescriptions();

        this.buttonDefintions = new ArrayList<ButtonDef>();
        // this.button_defs.add(new
        // LabelDef(this.i18nControl.getMessage("STOCK_SUBTYPES"),
        // LabelDef.FONT_BOLD));
        // this.button_defs.add(new
        // ButtonDef(this.i18nControl.getMessage("MANAGE"), "manage_type",
        // "STOCKS_TABLE_ADD_DESC",
        // "table_sql_check.png"));
        // this.button_defs.add(new
        // ButtonDef(this.i18nControl.getMessage("EDIT"), "edit_type",
        // "STOCKS_TABLE_EDIT_DESC",
        // "table_edit.png"));
        // this.button_defs.add(new
        // ButtonDef(this.i18nControlAbstract.getMessage("VIEW_TYPE"),
        // "view", "STOCKS_TABLE_VIEW_DESC", "table_view.png"));
        // this.button_defs.add(new DividerDef());
        // this.button_defs.add(new
        // LabelDef(this.i18nControl.getMessage("STOCKTAKING"),
        // LabelDef.FONT_BOLD));

        this.buttonDefintions.add(new ButtonDef(this.i18nControl.getMessage("ADD"), "add_stocktype",
                "STOCKS_TABLE_VIEW_DESC", "table_add.png"));
        this.buttonDefintions.add(new ButtonDef(this.i18nControl.getMessage("EDIT"), "edit_stocktype",
                "STOCKS_TABLE_VIEW_DESC", "table_edit.png"));
        this.buttonDefintions.add(new ButtonDef(this.i18nControl.getMessage("DELETE"), "delete_stocktype",
                "STOCKS_TABLE_VIEW_DESC", "table_delete.png"));
        // this.button_defs.add(new DividerDef());
        // this.button_defs.add(new
        // ButtonDef(this.i18nControl.getMessage("EDIT_LIST"), "edit_list",
        // "STOCKS_TABLE_VIEW_DESC",
        // "table_view.png"));

        this.defaultParameters = new String[2];
        this.defaultParameters[0] = "Test 1";
        this.defaultParameters[1] = "Test 2";

        // loadData();

    }

    // public void loadData()
    // {
    // refreshDataFromDb();
    // refreshFilter();
    // }

    // @Override
    // public String getDefName()
    // {
    // return "StockTypeListDef";
    // }
    //
    //
    // @Override
    // public Rectangle getTableSize(int pos_y)
    // {
    // return new Rectangle(40, pos_y, 580, 250);
    // }
    //
    //
    // @Override
    // public Dimension getWindowSize()
    // {
    // return new Dimension(800, 500);
    // }


    // @Override
    // public void setFilterCombo(String val)
    // {
    // filterStockType = StockTypeBase.getByDescription(val);
    // this.refreshFilter();
    // }
    //
    //
    // @Override
    // public void setFilterText(String val)
    // {
    // filterText = val;
    // this.refreshFilter();
    // }
    //
    //
    // @Override
    // public void setFilterCombo2(String val)
    // {
    // }

    public JPanel initCustomDisplayHeader()
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

    // @Override
    // public void editTableRow()
    // {
    // int index = this.getParentDialog().getSelectedObjectIndexFromTable();
    //
    // if (index > -1)
    // {
    // StockSubTypeH stockType = this.filteredList.get(index);
    //
    // InventoryItemTypeDialog dialog = new
    // InventoryItemTypeDialog(this.getParentDialog(), stockType);
    //
    // if (dialog.wasOperationSuccessful())
    // {
    // loadData();
    // }
    // }
    // }

}
