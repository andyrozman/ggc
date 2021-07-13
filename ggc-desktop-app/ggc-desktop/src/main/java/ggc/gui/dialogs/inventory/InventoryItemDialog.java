package ggc.gui.dialogs.inventory;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.graphics.components.JDecimalTextField;
import com.atech.graphics.dialogs.StandardDialogForObject;
import com.atech.utils.ATSwingUtils;
import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

import ggc.core.db.hibernate.inventory.InventoryItemH;
import ggc.core.db.hibernate.inventory.InventoryItemTypeH;
import ggc.core.util.DataAccess;
import ggc.gui.dialogs.selector.GGCSelectorConfiguration;
import ggc.gui.dialogs.selector.GGCSelectorDialog;

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
 *  Filename:     zzz  
 *  Description:  zzz
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

// fix this
// for defining stock entry

// REUSED

/**
 * This dialog is in use by StocktakingListDef and StockListDef
 *
 * @author andy
 *
 */
// FIXME addComponent
@Deprecated
public class InventoryItemDialog extends StandardDialogForObject
{

    private static final long serialVersionUID = -5936151592947261378L;

    private static final Logger LOG = LoggerFactory.getLogger(InventoryItemDialog.class);

    // TO DO
    // object gui
    // load
    // save

    // edit value
    // edit stock

    // s boolean editValue;

    // // OLD
    // private DataAccess dataAccess = DataAccess.getInstance();
    // private I18nControlAbstract i18nControl = dataAccess.getI18nControlInstance();
    // private GGCProperties props = dataAccess.getSettings();
    //
    // private boolean m_actionDone = false;

    // boolean in_process;
    // boolean debug = false;

    // private boolean m_add_action = true;
    // private Container m_parent = null;
    // private JLabel lblNewLabel;
    // private JLabel lblNewLabel_1;
    // private JLabel lblNewLabel_2;
    // private JLabel lblAmount;
    // private JComboBox comboBox;
    // private JLabel lblEntryType;
    // private JButton btnOk;
    // private JButton btnCancel;
    // private JFormattedTextField formattedTextField;
    // private JLabel lblStockType;
    // private JLabel lblStockSubType;
    // private JButton btnSelectstocktype;

    // private InventoryItemTypeH inventoryItemType;
    // private long inventoryId;
    // private long amount;
    // private String location;
    // private Long itemChanged;
    // private Long itemUsedTillMin;
    // private Long itemUsedTillMax;

    // private JButton btnHelp;

    // NEW
    private InventoryItemTypeH inventoryItemType;

    long inventoryId;
    InventoryItemH inventoryItem;
    private JLabel lblItemType1, lblItemType2, lblChanged;
    private JDecimalTextField decTxtAmount;
    private JTextField txtLocation;


    public InventoryItemDialog(JDialog dialog, long inventoryId)
    {
        super(dialog, DataAccess.getInstance(), false);

        this.inventoryId = inventoryId;
        init();
    }


    public InventoryItemDialog(JDialog dialog, InventoryItemH inventoryItemH, boolean editValue)
    {
        super(dialog, DataAccess.getInstance(), inventoryItemH, false);

        this.editValue = editValue;
        this.inventoryId = inventoryItemH.getInventoryId();

        init(inventoryItemH);
    }


    @Override
    public void loadData(Object dataObject)
    {
        this.inventoryItem = (InventoryItemH) dataObject;

        this.inventoryItemType = this.inventoryItem.getInventoryItemType();
        this.decTxtAmount.setValue(this.inventoryItem.getAmount());
        this.lblChanged.setText(
            ATechDate.getDateTimeString(ATechDateType.DateAndTimeSec, this.inventoryItem.getItemChanged()));
        this.txtLocation.setText(this.inventoryItem.getLocation());
    }


    @Override
    public boolean saveData()
    {
        if (this.inventoryItem == null)
        {
            inventoryItem = new InventoryItemH();
        }

        java.util.List<String> listFailed = new ArrayList<String>();

        if (this.inventoryItemType == null)
        {
            listFailed.add("INVENTORY_ITEM_TYPE");
        }
        else
        {
            this.inventoryItem.setInventoryItemType(inventoryItemType);
        }

        this.inventoryItem.setInventoryId(this.inventoryId);

        if (checkIfJDecimalTextFieldIsGreaterThanZero(decTxtAmount, "AMOUNT", listFailed))
        {
            inventoryItem.setAmount(dataAccess.getLongValue(decTxtAmount.getValue()));
        }

        if (checkIfTextFieldSet(txtLocation, "LOCATION", listFailed))
        {
            inventoryItem.setLocation(txtLocation.getText());
        }

        inventoryItem
                .setItemChanged(ATechDate.getATDateTimeFromGC(new GregorianCalendar(), ATechDateType.DateAndTimeSec));
        inventoryItem.setPersonId((int) dataAccess.getCurrentUserId());

        calculateItemUsageDates(inventoryItem);

        if (listFailed.size() != 0)
        {
            displayErrorWhenSavingObject(listFailed);
            return false;
        }
        else
        {
            if (inventoryItem.getId() == 0)
            {
                Long id = dataAccess.getHibernateDb().addHibernate(inventoryItem);

                // System.out.println("ID: " + id);

                if ((id == null) || (id <= 0))
                {
                    // System.out.println("id null");
                    return false;
                }
                else
                {
                    // System.out.println("id ok");
                    inventoryItem.setId(id);
                    return true;
                }
            }
            else
            {
                return dataAccess.getHibernateDb().editHibernate(inventoryItem);
            }
        }

    }


    private void calculateItemUsageDates(InventoryItemH inventoryItem)
    {

    }


    public void initGUI()
    {
        int width = 530;
        int height = 390;

        this.setBounds(0, 0, width, height);

        this.dataAccess.addComponent(this);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, width, height);
        panel.setLayout(null);

        getContentPane().setLayout(null);

        this.getContentPane().add(panel);

        ATSwingUtils.initLibrary();

        String title = i18nControl.getMessage(editValue ? "EDIT_INVENTORY_ITEM" : "ADD_INVENTORY_ITEM");

        int pos_y = 25;

        ATSwingUtils.getTitleLabel(title, 0, pos_y, width, 35, panel, ATSwingUtils.FONT_BIG_BOLD);
        this.setTitle(title);

        ATSwingUtils.getLabel(i18nControl.getMessage("INVENTORY_ITEM_TYPE") + ":", //
            40, pos_y + 50, 300, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD);

        this.lblItemType1 = ATSwingUtils.getLabel("1234567890a1234567890b1234567890c1234567890d", //
            40, pos_y + 75, 350, 25, panel, ATSwingUtils.FONT_NORMAL);
        this.lblItemType2 = ATSwingUtils.getLabel("xxx", //
            40, pos_y + 100, 350, 25, panel, ATSwingUtils.FONT_NORMAL);

        ATSwingUtils.getButton("", 435, pos_y + 60, 50, 50, panel, ATSwingUtils.FONT_NORMAL, "data_find.png",
            "select_inventory_type", this, dataAccess, new int[] { 35, 35 });

        displayInventoryItemType();

        ATSwingUtils.getLabel(i18nControl.getMessage("AMOUNT") + ":", //
            40, pos_y + 140, 120, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD);

        this.decTxtAmount = ATSwingUtils.getNumericTextField(0, 1.0d, //
            180, pos_y + 140, 80, 25, //
            panel, ATSwingUtils.FONT_NORMAL);

        ATSwingUtils.getLabel(i18nControl.getMessage("LOCATION") + ":", //
            40, pos_y + 180, 120, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD);

        this.txtLocation = ATSwingUtils.getTextField("", //
            180, pos_y + 180, 200, 25, panel);

        ATSwingUtils.getLabel(i18nControl.getMessage("LAST_CHANGE") + ":", //
            40, pos_y + 220, 120, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD);

        this.lblChanged = ATSwingUtils.getLabel(i18nControl.getMessage("NEW_ENTRY"), //
            180, pos_y + 220, 150, 25, panel, ATSwingUtils.FONT_NORMAL);

        // buttons
        ATSwingUtils.getButton("   " + i18nControl.getMessage("OK"), 60, pos_y + 280, 125, 25, panel,
            ATSwingUtils.FONT_NORMAL, "ok.png", "ok", this, dataAccess);

        ATSwingUtils.getButton("   " + i18nControl.getMessage("CANCEL"), 200, pos_y + 280, 125, 25, panel,
            ATSwingUtils.FONT_NORMAL, "cancel.png", "cancel", this, dataAccess);

        this.btnHelp = ATSwingUtils.createHelpButtonByBounds(340, pos_y + 280, 125, 25, this, ATSwingUtils.FONT_NORMAL,
            dataAccess);
        panel.add(this.btnHelp);

        ATSwingUtils.centerJDialog(this, this.parent);

        // FIXME
        // dataAccess.enableHelp(this);

        // private JLabel lblItemType1, lblItemType2, lblChanged;
        // private JDecimalTextField decTxtAmount;
        // private JTextField txtLocation;

        // this.lblNewLabel_2 = new JLabel("Stock Type");
        // this.lblNewLabel_2.setFont(new Font("SansSerif", Font.BOLD, 12));
        // this.lblNewLabel_2.setBounds(40, 73, 93, 14);
        // // panel.add(this.lblNewLabel_2);
        //
        // this.lblNewLabel_1 = new JLabel("Date Time");
        // this.lblNewLabel_1.setFont(new Font("SansSerif", Font.BOLD, 12));
        // this.lblNewLabel_1.setBounds(40, 148, 91, 14);
        // // panel.add(this.lblNewLabel_1);
        //
        // this.lblAmount = new JLabel("Amount:");
        // this.lblAmount.setFont(new Font("SansSerif", Font.BOLD, 12));
        // this.lblAmount.setBounds(40, 202, 91, 14);
        // // panel.add(this.lblAmount);
        //
        // this.comboBox = new JComboBox();
        // this.comboBox.setBounds(156, 231, 106, 20);
        // // panel.add(this.comboBox);
        //
        // this.lblEntryType = new JLabel("Entry type:");
        // this.lblEntryType.setFont(new Font("SansSerif", Font.BOLD, 12));
        // this.lblEntryType.setBounds(40, 233, 91, 14);
        // // panel.add(this.lblEntryType);
        //
        // this.btnOk = new JButton("OK");
        // this.btnOk.setBounds(74, 280, 89, 23);
        // panel.add(this.btnOk);
        //
        // this.btnCancel = new JButton("Cancel");
        // this.btnCancel.setBounds(173, 280, 89, 23);
        // panel.add(this.btnCancel);
        //
        // this.formattedTextField = new JFormattedTextField();
        // this.formattedTextField.setBounds(156, 200, 103, 20);
        // // panel.add(this.formattedTextField);
        //
        // this.lblStockType = new JLabel("Stock Type");
        // this.lblStockType.setFont(new Font("SansSerif", Font.PLAIN, 12));
        // this.lblStockType.setBounds(156, 73, 144, 14);
        // // panel.add(this.lblStockType);
        //
        // this.lblStockSubType = new JLabel("stock sub type");
        // this.lblStockSubType.setFont(new Font("SansSerif", Font.PLAIN, 12));
        // this.lblStockSubType.setBounds(156, 109, 140, 14);
        // // panel.add(this.lblStockSubType);
        //
        // this.btnHelp = new JButton("Help");
        // this.btnHelp.setBounds(272, 280, 89, 23);
        // panel.add(this.btnHelp);

    }


    // FIXME
    private void displayInventoryItemType()
    {
        if (inventoryItemType == null)
        {
            this.lblItemType1.setText("");
            this.lblItemType1.setToolTipText("");
            this.lblItemType2.setText("");
            this.lblItemType2.setToolTipText("");
        }
        else
        {
            this.lblItemType1.setText(
                inventoryItemType.getDisplayString(InventoryItemTypeH.InventoryItemTypeDisplay.Line1, i18nControl));
            this.lblItemType1.setToolTipText(inventoryItemType
                    .getDisplayString(InventoryItemTypeH.InventoryItemTypeDisplay.Line1_Tooltip, i18nControl));
            this.lblItemType2.setText(
                inventoryItemType.getDisplayString(InventoryItemTypeH.InventoryItemTypeDisplay.Line2, i18nControl));
            this.lblItemType2.setToolTipText(inventoryItemType
                    .getDisplayString(InventoryItemTypeH.InventoryItemTypeDisplay.Line2_Tooltip, i18nControl));
        }
    }


    public void customActionPerformed(String actionCommand)
    {
        if (actionCommand.equals("select_inventory_type"))
        {
            GGCSelectorDialog ssd = new GGCSelectorDialog(this, GGCSelectorConfiguration.InventoryItemTypeH);

            if (ssd.wasAction())
            {
                inventoryItemType = (InventoryItemTypeH) ssd.getSelectedItem();
                displayInventoryItemType();
            }
        }
        else
        {
            LOG.warn("Action (" + actionCommand + ") is not supported by " + this.getClass().getSimpleName()
                    + ". If you wish to change this you need to override customActionPerformed method.");
        }
    }


    @Override
    public String getHelpId()
    {
        // TODO
        return null;
    }

}
