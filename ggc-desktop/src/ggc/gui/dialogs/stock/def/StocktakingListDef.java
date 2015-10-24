package ggc.gui.dialogs.stock.def;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

import com.atech.graphics.dialogs.guilist.ButtonDef;
import com.atech.graphics.dialogs.guilist.DividerDef;
import com.atech.graphics.dialogs.guilist.GUIListDefAbstract;
import com.atech.i18n.I18nControlAbstract;

import ggc.core.db.GGCDb;
import ggc.core.db.dto.StockDTO;
import ggc.core.db.dto.StocktakingDTO;
import ggc.core.util.DataAccess;

/**
 * Created by andy on 02.03.15.
 */
public class StocktakingListDef extends GUIListDefAbstract
{

    DataAccess dataAccess = DataAccess.getInstance();
    I18nControlAbstract i18nControl = dataAccess.getI18nControlInstance();

    // private List<StockSubTypeH> activeList = null;
    // private List<StockSubTypeH> filteredList = null;

    StocktakingDTO dto = null;

    GGCDb database;


    /**
     * Constructor
     */
    public StocktakingListDef()
    {
        this.database = dataAccess.getDb();
        init();
    }


    @Override
    public void doTableAction(String action)
    {
        // if (action.equals("add_stocktype"))
        // {
        // StockSubTypeDialog dialog = new
        // StockSubTypeDialog(this.getParentDialog());
        //
        // if (dialog.wasOperationSuccessful())
        // {
        // loadData();
        // table.invalidate();
        // }
        //
        // }
        // else if (action.equals("edit_stocktype"))
        // {
        // this.editTableRow();
        // }
        // else if (action.equals("delete_stocktype"))
        // {
        //
        // }
        // else
        {
            System.out.println(this.getDefName() + " has not implemented action " + action);
        }

    }


    private void refreshDataFromDb()
    {
        // FIXME read from Db

        dto = new StocktakingDTO();

        // this.activeList = database.getStockTypes();
    }


    @Override
    public JTable getJTable()
    {
        if (this.table == null)
        {
            this.table = new JTable(new AbstractTableModel()
            {

                private static final long serialVersionUID = 7899186427378309394L;


                public int getColumnCount()
                {
                    return 5;
                }


                public int getRowCount()
                {
                    return dto.getEntries().size();
                }


                public Object getValueAt(int row, int column)
                {
                    StockDTO std = dto.getEntries().get(row);

                    switch (column)
                    {
                        case 0:
                            return std.getStockSubtype().getStockType().getTranslation();

                        case 1:
                            return std.getStockSubtype().getName();

                        case 2:
                            return std.getAmount();

                        case 3:
                            return std.getLocation();

                        case 4:
                            return i18nControl.getMessage(std.hasChanges());

                        // private long stockSubtypeId;
                        // private long amount;
                        // private String location;

                        // case 0:
                        // return std.
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
                        // sst.getPackageContentUnit();
                        //
                        // case 4:
                        // return sst.getUsageDescription();

                        default:
                            return "";

                    }

                }

            });

            String[] columns = { "Base Type", "Type", "Amount", "Location", "Has Changes" };
            int[] cwidths = { 100, 100, 100, 100, 180 }; // 480
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

            //
            // stock_type_id bigint,
            // name character varying(512),
            // description character varying(2000),
            // content_pkg bigint,
            // usage_type integer,
            // usage_min integer,
            // usage_max integer,
            // active boolean,
            // extended text,
            // comment character varying(2000),

        }

        return this.table;

    }


    @Override
    public String getTitle()
    {
        return "STOCKTAKING_LIST";
    }


    @Override
    public void init()
    {
        this.ic = DataAccess.getInstance().getI18nControlInstance();
        this.translation_root = "STOCK";

        // this.setCustomDisplayHeader(true);
        // this.filter_type = FILTER_NONE;
        // this.filter_enabled = true;

        this.filter_type = GUIListDefAbstract.FILTER_NONE; // .FILTER_COMBO_AND_TEXT;
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

        this.button_defs = new ArrayList<ButtonDef>();
        // this.button_defs.add(new
        // LabelDef(this.i18nControl.getMessage("IMPORTS"),
        // LabelDef.FONT_BOLD));

        this.button_defs.add(
            new ButtonDef(this.ic.getMessage("IMPORT"), "import", "STOCKS_TABLE_ADD_DESC", "table_sql_check.png"));

        this.button_defs.add(new DividerDef());

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

        this.button_defs
                .add(new ButtonDef(this.ic.getMessage("ADD"), "add_stock", "STOCKS_TABLE_VIEW_DESC", "table_add.png"));
        this.button_defs.add(
            new ButtonDef(this.ic.getMessage("EDIT"), "edit_stock", "STOCKS_TABLE_VIEW_DESC", "table_edit.png"));
        this.button_defs.add(
            new ButtonDef(this.ic.getMessage("DELETE"), "delete_stock", "STOCKS_TABLE_VIEW_DESC", "table_delete.png"));

        this.button_defs.add(new DividerDef());

        this.button_defs.add(new ButtonDef(this.ic.getMessage("EDIT_VALUE"), "edit_stock_value",
                "STOCKS_TABLE_VIEW_DESC", "table_edit.png"));

        // this.button_defs.add(new DividerDef());
        // this.button_defs.add(new
        // ButtonDef(this.i18nControl.getMessage("EDIT_LIST"), "edit_list",
        // "STOCKS_TABLE_VIEW_DESC",
        // "table_view.png"));

        loadData();

    }


    public void loadData()
    {
        refreshDataFromDb();
    }


    @Override
    public String getDefName()
    {
        return "StocktakingListDef";
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
    }


    @Override
    public void setFilterText(String val)
    {
    }


    @Override
    public void setFilterCombo_2(String val)
    {
    }


    // @Override
    // public JPanel getCustomDisplayHeader()
    // {
    // JPanel panel = new JPanel();
    // panel.setLayout(null);
    // panel.setBackground(Color.blue);
    // panel.setBounds(0, 0, 300, 55);
    //
    // ATSwingUtils.initLibrary();
    //
    // ATSwingUtils.getLabel("Test1:", 0, 0, 120, 25, panel);
    //
    // JLabel label_datetime = ATSwingUtils.getLabel("ccc", 130, 0, 90, 25,
    // panel);
    //
    // ATSwingUtils.getLabel("Test2:", 0, 30, 120, 25, panel);
    //
    // JLabel label_ddd = ATSwingUtils.getLabel("ccc", 130, 30, 90, 25, panel);
    //
    // return panel;
    // }

    @Override
    public void editTableRow()
    {
        // int index = this.getParentDialog().getSelectedObjectIndexFromTable();
        //
        // if (index > -1)
        // {
        // StockSubTypeH stockType = this.filteredList.get(index);
        //
        // StockSubTypeDialog dialog = new
        // StockSubTypeDialog(this.getParentDialog(), stockType);
        //
        // if (dialog.wasOperationSuccessful())
        // {
        // loadData();
        // }
        // }
    }

}
