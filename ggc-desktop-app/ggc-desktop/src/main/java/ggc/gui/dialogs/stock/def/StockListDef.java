package ggc.gui.dialogs.stock.def;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import com.atech.graphics.dialogs.guilist.*;
import com.atech.utils.ATSwingUtils;

import ggc.core.db.GGCDb;
import ggc.core.db.dto.StocktakingDTO;
import ggc.core.db.hibernate.inventory.InventoryH;
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
@Deprecated
public class StockListDef extends GUIListDefAbstract
{

    // USED: 60%

    DataAccess dataAccessLocal = DataAccess.getInstance();

    // private ArrayList<DoctorH> active_list= null;

    // private ArrayList<StockH> active_list = null;

    private GGCDb databaseLocal;


    /**
     * Constructor 
     */
    public StockListDef()
    {
        super(DataAccess.getInstance(), //
                new InventoryH(), // object listed
                "STOCKS_LIST", // title
                "StockListDef", // defintion name
                "Doc_DocList", // help Id FIXME
                new Rectangle(40, 0, 480, 250), // table bounds
                new Dimension(700, 500) // size
        );

        databaseLocal = dataAccessLocal.getDb();
    }


    public void doTableAction_999(String action)
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
        // InventoryItemTypeDialog sstd = new
        // InventoryItemTypeDialog(this.getParentDialog());
        // sstd.setVisible(true);
        // }
        // else
        if (action.equals("new_stocktaking"))
        {
            System.out.println("This is for testing only !!!!");

            // InventoryItemDialog sd = new
            // InventoryItemDialog(this.getParentDialog());

            // StockAmounts sa = new StockAmounts(null, null,
            // this.getParentDialog());
            // sa.setVisible(true);
            System.out.println(this.getDefName() + " has not implemented action " + action);
        }
        else if (action.equals("edit_stocktaking"))
        {
            GUIListDialog guiListDialog = new GUIListDialog(this.getParentDialog(), new StocktakingListDef(),
                    dataAccessLocal);

            if (guiListDialog.isDataChanged())
            {
                loadData();
            }
        }
        else if (action.equals("manage_types"))
        {
            new GUIListDialog(this.getParentDialog(), new StockTypeListDef(), dataAccessLocal);
        }
        else
        {
            System.out.println(this.getDefName() + " has not implemented action " + action);
        }
    }


    // @Override
    // public JTable getJTable()
    // {
    // if (this.table == null)
    // {
    // this.table = new JTable(new AbstractTableModel()
    // {
    //
    // private static final long serialVersionUID = -9188128586566579737L;
    //
    //
    // public int getColumnCount()
    // {
    //
    // return 4;
    // }
    //
    //
    // public int getRowCount()
    // {
    // return active_list.size();
    // }
    //
    //
    // public Object getValueAt(int row, int column)
    // {
    // // TODO Auto-generated method stub
    // StockH se = active_list.get(row);
    //
    // switch (column)
    // {
    // case 0:
    // return se.getLocation(); // dh.getName();
    //
    // case 1:
    // return se.getStocktakingId();
    // // return
    // // i18nControlAbstract.getMessage(dh.getDoctorType().getName());
    // }
    //
    // return null;
    // }
    //
    // });
    //
    // String[] columns = { "Name", "Amount", "Calculated Till", "Location" };
    // int[] cwidths = { 100, 100, 100, 180 }; // 480
    // int cwidth = 0;
    //
    // TableColumnModel cm = table.getColumnModel();
    //
    // for (int i = 0; i < columns.length; i++)
    // {
    // cm.getColumn(i).setHeaderValue(i18nControl.getMessage(columns[i]));
    //
    // cwidth = cwidths[i];
    //
    // if (cwidth > 0)
    // {
    // cm.getColumn(i).setPreferredWidth(cwidth);
    // }
    //
    // }
    //
    // }
    //
    // return this.table;
    //
    // }

    @Override
    public boolean doCustomTableAction(String action)
    {
        return false;
    }


    @Override
    public void init()
    {
        this.i18nControl = DataAccess.getInstance().getI18nControlInstance();
        this.translationRoot = "STOCKS";

        // this.setCustomDisplayHeaderEnabled(true);
        this.filterType = GuiListFilterType.Combo;
        // this.filter_enabled = true;

        // this.filter_type = GUIListDefAbstract.FILTER_COMBO_AND_TEXT;
        // this.filter_text = i18nControlAbstract.getMessage("FILTER") + ":";

        String s1[] = { i18nControl.getMessage("STATUS_USED") + ":", i18nControl.getMessage("DESCRIPTION") + ":" };
        this.filterDescriptionTexts = s1;

        String s[] = { i18nControl.getMessage("FILTER_ACTIVE"), i18nControl.getMessage("FILTER_ACTIVE_1_MONTH_USED"),
                       i18nControl.getMessage("FILTER_ACTIVE_2_MONTH_USED"),
                       i18nControl.getMessage("FILTER_ACTIVE_3-6_MONTH_USED"),
                       i18nControl.getMessage("FILTER_ACTIVE_6M_MONTH_USED"), i18nControl.getMessage("FILTER_ALL") };

        this.filterOptionsCombo1 = s;

        // FIXME
        this.buttonDefintions = new ArrayList<ButtonDef>();
        this.buttonDefintions.add(new LabelDef(this.i18nControl.getMessage("STOCK_SUBTYPES"), LabelDef.FONT_BOLD));
        this.buttonDefintions.add(new ButtonDef(this.i18nControl.getMessage("MANAGE"), "manage_types",
                "manage_types desc", "table_sql_check.png"));
        // this.button_defs.add(new
        // ButtonDef(this.i18nControl.getMessage("EDIT"),
        // "edit_type", "STOCKS_TABLE_EDIT_DESC",
        // "table_edit.png"));
        // this.button_defs.add(new
        // ButtonDef(this.i18nControlAbstract.getMessage("VIEW_TYPE"),
        // "view", "STOCKS_TABLE_VIEW_DESC", "table_view.png"));
        this.buttonDefintions.add(new DividerDef());
        this.buttonDefintions.add(new LabelDef(this.i18nControl.getMessage("STOCKTAKING"), LabelDef.FONT_BOLD));

        this.buttonDefintions.add(new ButtonDef(this.i18nControl.getMessage("NEW"), "new_stocktaking",
                "STOCKS_TABLE_VIEW_DESC", "table_add.png"));
        this.buttonDefintions.add(new ButtonDef(this.i18nControl.getMessage("EDIT"), "edit_stocktaking",
                "STOCKS_TABLE_VIEW_DESC", "table_edit.png"));
        // this.button_defs.add(new DividerDef());
        // this.button_defs.add(new
        // ButtonDef(this.i18nControl.getMessage("EDIT_LIST"),
        // "edit_list", "STOCKS_TABLE_VIEW_DESC",
        // "table_view.png"));

        this.defaultParameters = new String[2];
        this.defaultParameters[0] = "Test 1";
        this.defaultParameters[1] = "Test 2";

        loadData();

    }


    public void loadData()
    {
        StocktakingDTO std = databaseLocal.getLatestStocktakingDTO();

        // this.active_list = new ArrayList<StockH>();

        // this.active_list = dataAccess.getDb().getStocks(-1, -1);
    }


    @Override
    protected void filterData()
    {

    }


    // @Override
    // public void setFilterCombo(String val)
    // {
    // // TODO Auto-generated method stub
    // System.out.println("Combo changed to: " + val);
    //
    // }
    //
    //
    // @Override
    // public void setFilterText(String val)
    // {
    // // TODO Auto-generated method stub
    // System.out.println("Text Box changed to: " + val);
    //
    // }
    //
    //
    // @Override
    // public void setFilterCombo2(String val)
    // {
    // }

    @Override
    public void initCustomDisplayPanel()
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

        this.setCustomDisplayHeader(panel);
    }

}
