package ggc.gui.dialogs.stock;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.*;

import com.atech.graphics.dialogs.selector.SelectableInterface;
import com.atech.graphics.dialogs.selector.SelectorAbstractDialog;
import com.atech.utils.ATDataAccessAbstract;

import ggc.core.db.datalayer.StockBaseType;
import ggc.core.util.DataAccess;

// DEPRECATED ??

public class StockSelectorDialog extends SelectorAbstractDialog
{

    DataAccess da_local = null;
    int m_data_type = 0;


    public StockSelectorDialog(JDialog parent, ATDataAccessAbstract da, int data_type)
    {
        super(parent, da, 1, false);
        da_local = (DataAccess) da;

        this.m_data_type = data_type;
        // this.initSelectorValuesForType();
        this.init();
    }


    @Override
    public void initSelectorValuesForType()
    {
        if (this.m_data_type == 1)
        {

            setSelectorObject(new StockBaseType());
            setSelectorName(i18nControl.getMessage("STOCK_BASE_TYPE_SELECTOR"));

            setAllowedActions(
                SelectorAbstractDialog.SELECTOR_ACTION_SELECT | SelectorAbstractDialog.SELECTOR_ACTION_CANCEL
                        | SelectorAbstractDialog.SELECTOR_ACTION_NEW | SelectorAbstractDialog.SELECTOR_ACTION_EDIT);
            this.use_generic_select = true;

            // this.se
            this.setColumnSortingEnabled(false);
            this.setFilterType(SelectorAbstractDialog.SELECTOR_FILTER_TEXT);
            this.setHelpStringId("PumpTool_Profile_Selector");
            this.setHelpEnabled(false);
            setNewItemString(i18nControl.getMessage("NEW__STOCK_TYPE"));

            this.descriptions = new Hashtable<String, String>();
            this.descriptions.put("DESC_1", i18nControl.getMessage("STOCK_BASE_TYPE"));

        }
        else
        {
            System.out.println("Unknown dialog type");
        }
    }


    @Override
    public void getFullData()
    {
        this.full = new ArrayList<SelectableInterface>();

        if (this.m_data_type == 1)
        {
            // this.full.addAll(da_local.getDb().getStockBaseTypes());
        }

    }


    @Override
    public void checkAndExecuteActionNew()
    {
        if (this.m_data_type == 1)
        {
            // StockTypeDialog std = new StockTypeDialog(this);
            // std.setVisible(true);
            //
            // if (std.actionSuccessful())
            // {
            // this.getFullData();
            // this.filterEntries();
            // }
        }
        // TODO Auto-generated method stub

    }


    @Override
    public void checkAndExecuteActionEdit(SelectableInterface si)
    {
        if (this.m_data_type == 1)
        {
            // StockTypeDialog std = new StockTypeDialog(this, (StockBaseType)
            // si);
            // std.setVisible(true);
            //
            // if (std.actionSuccessful())
            // {
            // // this.getFullData();
            // this.filterEntries();
            // }
        }

    }


    @Override
    public void checkAndExecuteActionSelect()
    {
    }

}
