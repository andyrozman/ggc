package ggc.gui.dialogs.stock;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import com.atech.graphics.components.JDecimalTextField;
import com.atech.graphics.dialogs.StandardDialogForObject;
import com.atech.utils.ATSwingUtils;

import ggc.core.data.defs.StockTypeBase;
import ggc.core.data.defs.StockUsageUnit;
import ggc.core.db.hibernate.StockSubTypeH;
import ggc.core.util.DataAccess;

/**
 * This dialog is in use by StockTypeListDef
 *
 * @author andy
 *
 */
// FIXME addComponent
public class StockSubTypeDialog extends StandardDialogForObject
{

    private JPanel panel;

    StockSubTypeH subTypeObject;

    private JLabel lblIdValue;
    private JComboBox comboStockType;
    private JTextField txtName;
    private JTextField txtDescription;
    private JDecimalTextField dectxtContent;
    private JTextField txtContentUnit;
    private JComboBox comboUsageType;
    private JDecimalTextField dectxtUsageMin;
    private JDecimalTextField dectxtUsageMax;
    private JCheckBox cbActive;
    // private JButton helpButton;


    public StockSubTypeDialog(JDialog dialog)
    {
        super(dialog, DataAccess.getInstance());
    }


    public StockSubTypeDialog(JDialog dialog, StockSubTypeH subTypeObject)
    {
        super(dialog, DataAccess.getInstance(), subTypeObject);
    }


    @Override
    public void loadData(Object dataObject)
    {
        this.subTypeObject = (StockSubTypeH) dataObject;

        this.lblIdValue.setText("" + this.subTypeObject.getId());
        this.comboStockType
                .setSelectedItem(StockTypeBase.getByCode((int) this.subTypeObject.getStockTypeId()).getTranslation());
        this.txtName.setText(this.subTypeObject.getName());
        this.txtDescription.setText(this.subTypeObject.getDescription());
        this.txtContentUnit.setText(subTypeObject.getPackageContentUnit());
        this.dectxtContent.setValue(subTypeObject.getPackageContent());

        this.dectxtUsageMin.setValue(subTypeObject.getUsageMin());
        this.dectxtUsageMax.setValue(subTypeObject.getUsageMax());
        this.comboUsageType.setSelectedItem(StockUsageUnit.getByCode(subTypeObject.getUsageUnit()).getTranslation());
        this.cbActive.setSelected(subTypeObject.isActive());
    }


    public boolean saveData()
    {
        if (this.subTypeObject == null)
        {
            subTypeObject = new StockSubTypeH();
        }

        List<String> listFailed = new ArrayList<String>();

        StockTypeBase stb = StockTypeBase.getByDescription((String) comboStockType.getSelectedItem());

        if (stb == StockTypeBase.None)
        {
            listFailed.add("STOCK_GROUP");
        }
        else
        {
            subTypeObject.setStockTypeId(stb.getCode());
        }

        if (checkIfTextFieldSet(txtName, "NAME", listFailed))
        {
            subTypeObject.setName(txtName.getText());
        }

        if (checkIfTextFieldSet(txtDescription, "DESCRIPTION", listFailed))
        {
            subTypeObject.setDescription(txtDescription.getText());
        }

        if (checkIfTextFieldSet(txtContentUnit, "PACKAGE_CONTENT_UNIT", listFailed))
        {
            subTypeObject.setPackageContentUnit(txtContentUnit.getText());
        }

        if (checkIfJDecimalTextFieldIsGreaterThanZero(dectxtContent, "PACKAGE_CONTENT", listFailed))
        {
            subTypeObject.setPackageContent(ATSwingUtils.getJDecimalTextValueInt(dectxtContent));
        }

        if (checkIfJDecimalTextFieldIsGreaterThanZero(dectxtUsageMin, "USAGE_MIN", listFailed))
        {
            subTypeObject.setUsageMin(ATSwingUtils.getJDecimalTextValueInt(dectxtUsageMin));
        }

        if (checkIfJDecimalTextFieldIsGreaterThanZero(dectxtUsageMax, "USAGE_MAX", listFailed))
        {
            subTypeObject.setUsageMax(ATSwingUtils.getJDecimalTextValueInt(dectxtUsageMax));
        }

        StockUsageUnit sut = StockUsageUnit.getByDescription((String) comboUsageType.getSelectedItem());

        if (sut == StockUsageUnit.None)
        {
            listFailed.add("USAGE_UNIT");
        }
        else
        {
            subTypeObject.setUsageUnit(sut.getCode());
        }

        subTypeObject.setActive(cbActive.isSelected());
        subTypeObject.setPersonId((int) dataAccess.getCurrentUserId());

        if (listFailed.size() != 0)
        {
            displayErrorWhenSavingObject(listFailed);
            return false;
        }
        else
        {
            if (subTypeObject.getId() == 0)
            {
                Long id = dataAccess.getHibernateDb().addHibernate(subTypeObject);

                System.out.println("ID: " + id);

                if ((id == null) || (id <= 0))
                {
                    System.out.println("id null");
                    return false;
                }
                else
                {
                    System.out.println("id ok");
                    subTypeObject.setId(id);
                    return true;
                }
            }
            else
            {
                return dataAccess.getHibernateDb().editHibernate(subTypeObject);
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

        // setSize(480, 400);

        ATSwingUtils.getTitleLabel(i18nControl.getMessage("STOCK_TYPE"), 0, 22, 480, 40, panel,
            ATSwingUtils.FONT_BIG_BOLD);
        this.setTitle(i18nControl.getMessage("STOCK_TYPE"));

        // Labels
        String[] labelKeys = { "ID", "STOCK_GROUP", "NAME", "DESCRIPTION", "PACKAGE_CONTENT", "STOCK_USAGE", "ACTIVE" };
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
        comboStockType = ATSwingUtils.getComboBox(StockTypeBase.getDescriptions(), 200, 105, 170, 25, panel,
            ATSwingUtils.FONT_NORMAL);

        // Name
        txtName = ATSwingUtils.getTextField("", 200, 140, 240, 25, panel, ATSwingUtils.FONT_NORMAL);

        // Description
        txtDescription = ATSwingUtils.getTextField("", 200, 175, 240, 25, panel, ATSwingUtils.FONT_NORMAL);

        // Package Content
        dectxtContent = ATSwingUtils.getNumericTextField(4, 0, 0, 200, 210, 80, 25, panel, ATSwingUtils.FONT_NORMAL);
        ATSwingUtils.getLabel(i18nControl.getMessage("UNIT_UNIT") + ":", 300, 210, 40, 25, panel,
            ATSwingUtils.FONT_NORMAL_BOLD);

        txtContentUnit = ATSwingUtils.getTextField("", 340, 210, 100, 25, panel, ATSwingUtils.FONT_NORMAL);

        // Usage Min / Max / Type
        dectxtUsageMin = ATSwingUtils.getNumericTextField(4, 0, 0, 200, 245, 40, 25, panel, ATSwingUtils.FONT_NORMAL);
        dectxtUsageMax = ATSwingUtils.getNumericTextField(4, 0, 0, 260, 245, 40, 25, panel, ATSwingUtils.FONT_NORMAL);
        comboUsageType = ATSwingUtils.getComboBox(StockUsageUnit.getDescriptions(), 330, 245, 110, 25, panel,
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
