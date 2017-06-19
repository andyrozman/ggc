package ggc.gui.dialogs.selector;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.db.hibernate.HibernateSelectableObject;
import com.atech.graphics.dialogs.DialogCreator;
import com.atech.graphics.dialogs.StandardDialogForObject;
import com.atech.graphics.dialogs.selector.SelectableInterface;
import com.atech.graphics.dialogs.selector.SelectorAbstractDialog;
import com.atech.utils.ATSwingUtils;

import ggc.core.db.hibernate.doc.DoctorH;
import ggc.core.db.hibernate.doc.DoctorTypeH;
import ggc.core.db.hibernate.inventory.InventoryItemTypeH;
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
 *  Filename:     DoctorTypeSelector
 *  Description:  Doctor Type's selector
 *
 *  Author: andyrozman {andy@atech-software.com}
 */

public class GGCSelectorDialog extends SelectorAbstractDialog
{

    private static final long serialVersionUID = 3066968241664964931L;

    private static final Logger LOG = LoggerFactory.getLogger(GGCSelectorDialog.class);
    private DataAccess dataAccess = DataAccess.getInstance();
    GGCSelectorConfiguration selectorType = null;


    public GGCSelectorDialog(JDialog parent, GGCSelectorConfiguration selectorType)
    {
        super(parent, DataAccess.getInstance(), 0, null, false);
        this.selectorType = selectorType;
        init();
        this.setVisible(true);
    }


    public GGCSelectorDialog(JFrame parent, GGCSelectorConfiguration selectorType)
    {
        super(parent, DataAccess.getInstance(), 0, null, false);
        this.selectorType = selectorType;
        init();
        this.setVisible(true);
    }


    @Override
    public void initSelectorValuesForType()
    {
        if (selectorType == GGCSelectorConfiguration.DoctorTypeH)
        {
            setSelectorObject(new DoctorTypeH());
            setSelectorName(i18nControl.getMessage("DOCTOR_TYPE_SELECTOR"));
            this.descriptions = new Hashtable<String, String>();
            this.descriptions.put("DESC_1", i18nControl.getMessage("DOCTOR_TYPE"));
            setAllowedActions(
                SelectorAbstractDialog.SELECTOR_ACTION_CANCEL | SelectorAbstractDialog.SELECTOR_ACTION_SELECT
                        | SelectorAbstractDialog.SELECTOR_ACTION_NEW | SelectorAbstractDialog.SELECTOR_ACTION_EDIT);
            this.setColumnSortingEnabled(false);
            this.setFilterType(SelectorAbstractDialog.SELECTOR_FILTER_TEXT);
            this.setHelpStringId("Doc_DocTypeSelector");
            this.setHelpEnabled(true);
            use_generic_select = true;
        }
        else if (selectorType == GGCSelectorConfiguration.DoctorH)
        {
            setSelectorObject(new DoctorH());
            setSelectorName(i18nControl.getMessage("DOCTOR_SELECTOR"));
            this.descriptions = new Hashtable<String, String>();
            this.descriptions.put("DESC_1", i18nControl.getMessage("DOCTOR_NAME"));
            setAllowedActions(
                SelectorAbstractDialog.SELECTOR_ACTION_CANCEL | SelectorAbstractDialog.SELECTOR_ACTION_SELECT
                        | SelectorAbstractDialog.SELECTOR_ACTION_NEW | SelectorAbstractDialog.SELECTOR_ACTION_EDIT);
            this.setColumnSortingEnabled(false);
            this.setFilterType(SelectorAbstractDialog.SELECTOR_FILTER_TEXT);
            this.setHelpStringId("Appointment_DoctorSelector");
            this.setHelpEnabled(true);
            use_generic_select = true;
        }
        else if (selectorType == GGCSelectorConfiguration.InventoryItemTypeH)
        {
            setSelectorObject(new InventoryItemTypeH());
            setSelectorName(i18nControl.getMessage("INVENTORY_ITEM_TYPE_SELECTOR"));

            setAllowedActions(SelectorAbstractDialog.SELECTOR_ACTION_SELECT | //
                    SelectorAbstractDialog.SELECTOR_ACTION_CANCEL | //
                    SelectorAbstractDialog.SELECTOR_ACTION_NEW | //
                    SelectorAbstractDialog.SELECTOR_ACTION_EDIT);
            this.use_generic_select = true;

            this.setColumnSortingEnabled(false);
            this.setFilterType(SelectorAbstractDialog.SELECTOR_FILTER_TEXT);
            // TODO
            // this.setHelpStringId("PumpTool_Profile_Selector"); // TODO
            // this.setHelpEnabled(false);
            setNewItemString(i18nControl.getMessage("NEW__ITEM_TYPE"));

            this.descriptions = new Hashtable<String, String>();
            this.descriptions.put("DESC_1", i18nControl.getMessage("INVENTORY_ITEM_TYPE"));
        }
        else
        {
            LOG.error("initSelectorValuesForType not implemented for type: " + selectorType.name());
        }
    }


    @Override
    public void getFullData()
    {
        dataAccess = DataAccess.getInstance();
        this.full = new ArrayList<SelectableInterface>();

        if (selectorType == GGCSelectorConfiguration.DoctorTypeH || //
                selectorType == GGCSelectorConfiguration.DoctorH || //
                selectorType == GGCSelectorConfiguration.InventoryItemTypeH)
        {
            this.full.addAll(dataAccess.getDb().getAllTypedHibernateData(selectorType.getClazz()));
        }
        else
        {
            LOG.error("getFullData not implemented for type: " + selectorType.name());
        }

    }


    @Override
    public void checkAndExecuteActionNew()
    {
        boolean readFullData = false;

        if (selectorType == GGCSelectorConfiguration.DoctorTypeH || //
                selectorType == GGCSelectorConfiguration.DoctorH || //
                selectorType == GGCSelectorConfiguration.InventoryItemTypeH)
        {
            StandardDialogForObject dt = createDialog(selectorType.getClazz());

            if (dt != null)
            {
                readFullData = dt.wasOperationSuccessful();
            }
            else
            {
                LOG.error("Problem creating dialog for adding: " + selectorType.getClazz());
            }
        }
        else
        {
            LOG.error("checkAndExecuteActionNew not implemented for type: " + selectorType.name());
        }

        // if (selectorType == GGCSelectorType.DoctorTypeH)
        // {
        // StandardDialogForObject dt = new DoctorTypeDialog(this);
        //
        // if (dt != null)
        // {
        // readFullData = dt.wasOperationSuccessful();
        // }
        // else
        // {
        // LOG.error("Problem creating dialog for adding: " +
        // selectorType.getClazz());
        // }
        // }
        // else if (selectorType == GGCSelectorType.DoctorH)
        // {
        // StandardDialogForObject dt = new DoctorDialog(this);
        // readFullData = dt.wasOperationSuccessful();
        // }
        // else
        // {
        // LOG.error("checkAndExecuteActionNew not implemented for type: " +
        // selectorType.name());
        // }

        if (readFullData)
        {
            getFullData();
        }
    }


    @Override
    public void checkAndExecuteActionEdit(SelectableInterface si)
    {
        boolean readFullData = false;

        if (selectorType == GGCSelectorConfiguration.DoctorTypeH)
        {
            DoctorTypeH selectedObject = (DoctorTypeH) si;

            if (selectedObject.getPredefined() == 1)
            {
                dataAccess.showConfirmDialog(this, ATSwingUtils.DialogType.Error, //
                    "DOCTOR_TYPE_CANT_EDIT_PREDEFINED", ATSwingUtils.ConfirmDialogType.ClosedOption);
            }
            else
            {
                StandardDialogForObject dt = createDialog(selectorType.getClazz(), selectedObject, true);
                readFullData = dt.wasOperationSuccessful();
            }
        }
        else if (selectorType == GGCSelectorConfiguration.DoctorH || //
                selectorType == GGCSelectorConfiguration.InventoryItemTypeH

        ) // standard method
        {
            StandardDialogForObject dt = createDialog(selectorType.getClazz(), si, true);
            readFullData = dt.wasOperationSuccessful();
        }
        else
        {
            LOG.error("checkAndExecuteActionEdit not implemented for type: " + selectorType.name());
        }

        if (readFullData)
        {
            getFullData();
        }

    }


    @Override
    public void checkAndExecuteActionSelect()
    {
        LOG.error("checkAndExecuteActionSelect not implemented for type: " + selectorType.name());
    }


    public StandardDialogForObject createDialog(Class clazz)
    {

        DialogCreator dialogCreator = getDialogCreator(clazz);

        if (dialogCreator != null)
        {
            return dialogCreator.createDialog(clazz, this);
        }

        return null;
    }


    public StandardDialogForObject createDialog(Class clazz, SelectableInterface selectableObject, boolean edit)
    {
        DialogCreator dialogCreator = getDialogCreator(clazz);

        if (dialogCreator != null)
        {
            return dialogCreator.createDialog(clazz, this, (HibernateSelectableObject) selectableObject, edit);
        }

        return null;
    }


    public DialogCreator getDialogCreator(Class clazz)
    {
        for (DialogCreator creator : dataAccess.getDialogCreators())
        {
            if (creator.isApplicable(clazz))
                return creator;
        }

        return null;
    }

}
