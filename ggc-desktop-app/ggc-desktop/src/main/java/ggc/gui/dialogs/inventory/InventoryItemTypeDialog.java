package ggc.gui.dialogs.inventory;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import com.atech.db.hibernate.HibernateObject;
import com.atech.graphics.components.JDecimalTextField;
import com.atech.graphics.dialogs.StandardDialogForObject;
import com.atech.utils.ATSwingUtils;

import ggc.core.data.defs.InventoryGroupType;
import ggc.core.data.defs.InventoryItemUnit;
import ggc.core.data.defs.InventoryItemUsageUnit;
import ggc.core.db.hibernate.inventory.InventoryItemTypeH;
import ggc.core.util.DataAccess;

/**
 * This dialog is in use by StockTypeListDef
 *
 * @author andy
 *
 */
// FIXME help
@Deprecated
public class InventoryItemTypeDialog extends StandardDialogForObject
{

    private static final long serialVersionUID = 1295578178604220051L;

    private JPanel panel;

    InventoryItemTypeH inventoryItemType;

    private JLabel lblIdValue;
    private JComboBox comboStockType;
    private JTextField txtName;
    private JTextField txtDescription;
    private JDecimalTextField dectxtContent;
    private JComboBox comboUsageType;
    private JComboBox comboUnitType;
    private JDecimalTextField decTxtUsageMin;
    private JDecimalTextField decTxtUsageMax;
    private JCheckBox cbActive;


    public InventoryItemTypeDialog(JDialog dialog)
    {
        super(dialog, DataAccess.getInstance());
    }


    public InventoryItemTypeDialog(JDialog dialog, HibernateObject inventoryItemType)
    {
        super(dialog, DataAccess.getInstance(), inventoryItemType);
    }


    @Override
    public void loadData(Object dataObject)
    {
        this.inventoryItemType = (InventoryItemTypeH) dataObject;

        this.lblIdValue.setText("" + this.inventoryItemType.getId());
        this.comboStockType.setSelectedItem(
            InventoryGroupType.getByCode((int) this.inventoryItemType.getInventoryGroupId()).getTranslation());
        this.txtName.setText(this.inventoryItemType.getName());
        this.txtDescription.setText(this.inventoryItemType.getDescription());

        this.comboUnitType.setSelectedItem(
            InventoryItemUnit.getByI18nKey(this.inventoryItemType.getPackageContentUnit()).getTranslation());

        // this.txtContentUnit.setText(inventoryItemType.getPackageContentUnit());
        this.dectxtContent.setValue(inventoryItemType.getPackageContent());

        this.decTxtUsageMin.setValue(inventoryItemType.getUsageMin());
        this.decTxtUsageMax.setValue(inventoryItemType.getUsageMax());
        this.comboUsageType
                .setSelectedItem(InventoryItemUsageUnit.getByCode(inventoryItemType.getUsageUnit()).getTranslation());
        this.cbActive.setSelected(inventoryItemType.isActive());
    }


    public boolean saveData()
    {
        if (this.inventoryItemType == null)
        {
            inventoryItemType = new InventoryItemTypeH();
        }

        List<String> listFailed = new ArrayList<String>();

        InventoryGroupType stb = InventoryGroupType.getByDescription((String) comboStockType.getSelectedItem());

        if (stb == InventoryGroupType.None)
        {
            listFailed.add("INVENTORY_GROUP");
        }
        else
        {
            inventoryItemType.setInventoryGroupId(stb.getCode());
        }

        if (checkIfTextFieldSet(txtName, "NAME", listFailed))
        {
            inventoryItemType.setName(txtName.getText());
        }

        if (checkIfTextFieldSet(txtDescription, "DESCRIPTION", listFailed))
        {
            inventoryItemType.setDescription(txtDescription.getText());
        }

        InventoryItemUnit unit = InventoryItemUnit.getByDescription((String) comboUnitType.getSelectedItem());

        if (unit == InventoryItemUnit.None)
        {
            listFailed.add("PACKAGE_CONTENT_UNIT");
        }
        else
        {
            inventoryItemType.setPackageContentUnit(unit.getI18nKey());
        }

        if (checkIfJDecimalTextFieldIsGreaterThanZero(dectxtContent, "PACKAGE_CONTENT", listFailed))
        {
            inventoryItemType.setPackageContent(ATSwingUtils.getJDecimalTextValueInt(dectxtContent));
        }

        if (checkIfJDecimalTextFieldIsGreaterThanZero(decTxtUsageMin, "USAGE_MIN", listFailed))
        {
            inventoryItemType.setUsageMin(ATSwingUtils.getJDecimalTextValueInt(decTxtUsageMin));
        }

        if (checkIfJDecimalTextFieldIsGreaterThanZero(decTxtUsageMax, "USAGE_MAX", listFailed))
        {
            inventoryItemType.setUsageMax(ATSwingUtils.getJDecimalTextValueInt(decTxtUsageMax));
        }

        InventoryItemUsageUnit sut = InventoryItemUsageUnit.getByDescription((String) comboUsageType.getSelectedItem());

        if (sut == InventoryItemUsageUnit.None)
        {
            listFailed.add("USAGE_UNIT");
        }
        else
        {
            inventoryItemType.setUsageUnit(sut.getCode());
        }

        inventoryItemType.setActive(cbActive.isSelected());
        inventoryItemType.setPersonId((int) dataAccess.getCurrentUserId());

        if (listFailed.size() != 0)
        {
            displayErrorWhenSavingObject(listFailed);
            return false;
        }
        else
        {
            if (inventoryItemType.getId() == 0)
            {
                Long id = dataAccess.getHibernateDb().addHibernate(inventoryItemType);

                // System.out.println("ID: " + id);

                if ((id == null) || (id <= 0))
                {
                    // System.out.println("id null");
                    return false;
                }
                else
                {
                    // System.out.println("id ok");
                    inventoryItemType.setId(id);
                    return true;
                }
            }
            else
            {
                return dataAccess.getHibernateDb().editHibernate(inventoryItemType);
            }
        }
    }


    public void initGUI()
    {
        ATSwingUtils.initLibrary();

        getContentPane().setLayout(null);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, 500, 400);

        this.setBounds(0, 0, 500, 400);

        this.getContentPane().add(panel, null);

        ATSwingUtils.centerJDialog(this, this.parent);

        ATSwingUtils.getTitleLabel(i18nControl.getMessage("INVENTORY_ITEM_TYPE"), 0, 22, 480, 40, panel,
            ATSwingUtils.FONT_BIG_BOLD);
        this.setTitle(i18nControl.getMessage("INVENTORY_ITEM_TYPE"));

        // Labels
        String[] labelKeys = { "ID", "INVENTORY_GROUP", "NAME", "DESCRIPTION", "PACKAGE_CONTENT", "INVENTORY_USAGE",
                               "ACTIVE" };
        int pos_y = 70;

        for (String labelKey : labelKeys)
        {
            ATSwingUtils.getLabel(this.i18nControl.getMessage(labelKey) + ":", 40, pos_y, 150, 25, panel,
                ATSwingUtils.FONT_NORMAL_BOLD);
            pos_y += 35;
        }

        // ID
        this.lblIdValue = ATSwingUtils.getLabel("-", 200, 70, 46, 25, panel, ATSwingUtils.FONT_NORMAL);

        // Base Type
        comboStockType = ATSwingUtils.getComboBox(InventoryGroupType.getDescriptions(), 200, 105, 170, 25, panel,
            ATSwingUtils.FONT_NORMAL);

        // Name
        txtName = ATSwingUtils.getTextField("", 200, 140, 240, 25, panel, ATSwingUtils.FONT_NORMAL);

        // Description
        txtDescription = ATSwingUtils.getTextField("", 200, 175, 240, 25, panel, ATSwingUtils.FONT_NORMAL);

        // Package Content
        dectxtContent = ATSwingUtils.getNumericTextField(0, 0, 200, 210, 60, 25, panel, ATSwingUtils.FONT_NORMAL);
        ATSwingUtils.getLabel(i18nControl.getMessage("UNIT_DESC") + ":", 270, 210, 40, 25, panel,
            ATSwingUtils.FONT_NORMAL_BOLD);

        comboUnitType = ATSwingUtils.getComboBox(InventoryItemUnit.getDescriptions(), 340, 210, 100, 25, panel,
            ATSwingUtils.FONT_NORMAL);

        // Usage Min / Max / Type
        decTxtUsageMin = ATSwingUtils.getNumericTextField(0, 0, 200, 245, 40, 25, panel, ATSwingUtils.FONT_NORMAL);
        decTxtUsageMax = ATSwingUtils.getNumericTextField(0, 0, 260, 245, 40, 25, panel, ATSwingUtils.FONT_NORMAL);
        comboUsageType = ATSwingUtils.getComboBox(InventoryItemUsageUnit.getDescriptions(), 330, 245, 110, 25, panel,
            ATSwingUtils.FONT_NORMAL);
        ATSwingUtils.getLabel("-", 240, 245, 150, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD);
        ATSwingUtils.getLabel("/", 310, 245, 150, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD);

        // Active
        cbActive = ATSwingUtils.getCheckBox("", 200, 280, 500, 25, panel, ATSwingUtils.FONT_NORMAL);
        cbActive.setSelected(true);

        // buttons
        ATSwingUtils.getButton("   " + i18nControl.getMessage("OK"), 50, 320, 125, 25, panel, ATSwingUtils.FONT_NORMAL,
            "ok.png", "ok", this, dataAccess);

        ATSwingUtils.getButton("   " + i18nControl.getMessage("CANCEL"), 180, 320, 125, 25, panel,
            ATSwingUtils.FONT_NORMAL, "cancel.png", "cancel", this, dataAccess);

        this.btnHelp = ATSwingUtils.createHelpButtonByBounds(310, 320, 125, 25, this, ATSwingUtils.FONT_NORMAL,
            dataAccess);
        panel.add(this.btnHelp);

        // dataAccess.enableHelp(this);

    }


    @Override
    public String getHelpId()
    {
        // FIxME
        return "StockSubType";
    }

}
