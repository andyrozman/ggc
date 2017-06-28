package ggc.gui.dialogs.inventory.def;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import com.atech.db.hibernate.HibernateSelectableObject;
import com.atech.graphics.dialogs.guilist.ButtonDef;
import com.atech.graphics.dialogs.guilist.DividerDef;
import com.atech.graphics.dialogs.guilist.GUIListDefAbstract;
import com.atech.utils.ATSwingUtils;

import ggc.core.db.GGCDb;
import ggc.core.db.hibernate.inventory.InventoryH;
import ggc.core.db.hibernate.inventory.InventoryItemH;
import ggc.core.util.DataAccess;
import ggc.gui.dialogs.inventory.InventoryItemDialog;

/**
 * Created by andy on 02.03.15.
 */
public class InventoryListDef extends GUIListDefAbstract
{

    DataAccess dataAccessLocal = DataAccess.getInstance();
    // I18nControlAbstract i18nControl = dataAccess.getI18nControlInstance();

    // private List<StockSubTypeH> activeList = null;
    // private List<StockSubTypeH> filteredList = null;

    // StocktakingDTO dto = null;

    GGCDb databaseLocal;

    long inventoryId;
    InventoryH inventory;


    /**
     * Constructor
     */
    public InventoryListDef(long inventoryId)
    {
        super(DataAccess.getInstance(), //
                new InventoryItemH(), // object listed
                "STOCKTAKING_LIST", // title
                "StocktakingListDef", // defintion name
                "Doc_DocList", // help Id FIXME
                new Rectangle(40, 0, 580, 250), // table bounds
                new Dimension(800, 450) // size
        );

        this.inventoryId = inventoryId;
        this.databaseLocal = dataAccessLocal.getDb();

        loadData();
    }


    protected void loadData()
    {
        this.inventory = databaseLocal.getInventory(this.inventoryId);
        this.fullList = this.inventory.getItemList();
        this.activeList = this.inventory.getItemList();

        refreshTable();
    }


    protected void reloadData()
    {
        this.loadData();
    }


    @Override
    public boolean doCustomTableAction(String action)
    {
        if (action.equals("add_stock"))
        {
            InventoryItemDialog dialog = new InventoryItemDialog(this.getParentDialog(), inventoryId);

            if (dialog.wasOperationSuccessful())
            {
                this.reloadData();
            }

            return true;
        }
        else if (action.equals("edit_stock"))
        {
            InventoryItemDialog dialog = new InventoryItemDialog(this.getParentDialog(), inventoryId);

            if (dialog.wasOperationSuccessful())
            {
                this.reloadData();
            }

            return true;
        }

        return false;
    }


    @Override
    public void init()
    {
        this.databaseLocal = DataAccess.getInstance().getDb();

        this.i18nControl = DataAccess.getInstance().getI18nControlInstance();
        this.translationRoot = "STOCK";

        // this.setCustomDisplayHeaderEnabled(true);
        // this.filter_type = FILTER_NONE;
        // this.filter_enabled = true;

        this.filterType = GuiListFilterType.None; // .FILTER_COMBO_AND_TEXT;
        // this.filter_text = i18nControlAbstract.getMessage("FILTER") + ":";

        // String s1[] = { i18nControl.getMessage("STATUS_USED") + ":",
        // i18nControl.getMessage("DESCRIPTION") + ":" };
        // this.filter_texts = s1;
        //
        // String s[] = { i18nControl.getMessage("FILTER_ACTIVE"),
        // i18nControl.getMessage("FILTER_ACTIVE_1_MONTH_USED"),
        // i18nControl.getMessage("FILTER_ACTIVE_2_MONTH_USED"),
        // i18nControl.getMessage("FILTER_ACTIVE_3-6_MONTH_USED"),
        // i18nControl.getMessage("FILTER_ACTIVE_6M_MONTH_USED"),
        // i18nControl.getMessage("FILTER_ALL") };
        //
        // this.filter_options_combo1 = s;

        this.buttonDefintions = new ArrayList<ButtonDef>();
        // this.button_defs.add(new
        // LabelDef(this.i18nControl.getMessage("IMPORTS"),
        // LabelDef.FONT_BOLD));

        this.buttonDefintions.add(new ButtonDef(this.i18nControl.getMessage("IMPORT"), "import",
                "STOCKS_TABLE_ADD_DESC", "table_sql_check.png"));

        this.buttonDefintions.add(new DividerDef());

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

        this.buttonDefintions.add(
            new ButtonDef(this.i18nControl.getMessage("ADD"), "add_object", "STOCKS_TABLE_VIEW_DESC", "table_add.png"));
        this.buttonDefintions.add(new ButtonDef(this.i18nControl.getMessage("EDIT"), "edit_object",
                "STOCKS_TABLE_VIEW_DESC", "table_edit.png"));
        this.buttonDefintions.add(new ButtonDef(this.i18nControl.getMessage("DELETE"), "delete_stock",
                "STOCKS_TABLE_VIEW_DESC", "table_delete.png"));

        this.buttonDefintions.add(new DividerDef());

        this.buttonDefintions.add(new ButtonDef(this.i18nControl.getMessage("EDIT_VALUE"), "edit_stock_value",
                "STOCKS_TABLE_VIEW_DESC", "table_edit.png"));

        // this.button_defs.add(new DividerDef());
        // this.button_defs.add(new
        // ButtonDef(this.i18nControl.getMessage("EDIT_LIST"), "edit_list",
        // "STOCKS_TABLE_VIEW_DESC",
        // "table_view.png"));

        // loadData();

    }


    // public void loadData()
    // {
    // refreshDataFromDb();
    // }

    @Override
    protected void filterData()
    {
        // java.util.List<InventoryItemH> list = new
        // ArrayList<InventoryItemH>();
        //
        // for (HibernateSelectableObject object : this.fullList)
        // {
        // InventoryItemH item = (InventoryItemH) object;
        // list.add(item);
        // }
        //
        // this.activeList = list;
    }


    @Override
    public void addTableRow()
    {
        InventoryItemDialog dialog = new InventoryItemDialog(this.getParentDialog(), inventoryId);

        if (dialog.wasOperationSuccessful())
        {
            this.reloadData();
        }
    }


    @Override
    public void editTableRow()
    {
        int index = this.getParentDialog().getSelectedObjectIndexFromTable();

        if (index > -1)
        {
            HibernateSelectableObject editableObject = this.activeList.get(index);

            InventoryItemDialog dialog = new InventoryItemDialog(this.getParentDialog(),
                    (InventoryItemH) editableObject, true);

            if (dialog.wasOperationSuccessful())
            {
                reloadData();
            }
        }
    }


    public JPanel getCustomDisplayHeaderEnabled()
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

}
