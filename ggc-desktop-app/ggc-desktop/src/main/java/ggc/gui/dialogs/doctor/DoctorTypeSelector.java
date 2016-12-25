package ggc.gui.dialogs.doctor;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.*;

import com.atech.graphics.dialogs.selector.SelectableInterface;
import com.atech.graphics.dialogs.selector.SelectorAbstractDialog;
import com.atech.utils.ATSwingUtils;

import ggc.core.db.hibernate.doc.DoctorTypeH;
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

public class DoctorTypeSelector extends SelectorAbstractDialog
{

    private static final long serialVersionUID = -8393227372637587058L;

    private DataAccess dataAccess = DataAccess.getInstance();


    public DoctorTypeSelector(JDialog parent)
    {
        super(parent, DataAccess.getInstance(), 0, null, true);
    }


    public DoctorTypeSelector(JFrame parent)
    {
        super(parent, DataAccess.getInstance(), 0, null, true);
    }


    @Override
    public void initSelectorValuesForType()
    {
        setSelectorObject(new DoctorTypeH());
        setSelectorName(i18nControl.getMessage("DOCTOR_TYPE_SELECTOR"));
        this.descriptions = new Hashtable<String, String>();
        this.descriptions.put("DESC_1", i18nControl.getMessage("DOCTOR_TYPE"));
        setAllowedActions(SelectorAbstractDialog.SELECTOR_ACTION_CANCEL | SelectorAbstractDialog.SELECTOR_ACTION_SELECT
                | SelectorAbstractDialog.SELECTOR_ACTION_NEW | SelectorAbstractDialog.SELECTOR_ACTION_EDIT);
        this.setColumnSortingEnabled(false);
        this.setFilterType(SelectorAbstractDialog.SELECTOR_FILTER_TEXT);
        this.setHelpStringId("Doc_DocTypeSelector");
        this.setHelpEnabled(true);
        use_generic_select = true;
    }


    @Override
    public void getFullData()
    {
        dataAccess = DataAccess.getInstance();
        this.full = new ArrayList<SelectableInterface>();

        List<DoctorTypeH> list = dataAccess.getDb().getDoctorTypes();

        for (DoctorTypeH doctorTypeH : list)
        {
            doctorTypeH.setNameTranslated();
            this.full.add(doctorTypeH);
        }
    }


    @Override
    public void checkAndExecuteActionNew()
    {
        DoctorTypeDialog dt = new DoctorTypeDialog(this);

        if (dt.wasOperationSuccessful())
        {
            getFullData();
        }
    }


    @Override
    public void checkAndExecuteActionEdit(SelectableInterface si)
    {
        DoctorTypeH selectedObject = (DoctorTypeH) si;

        if (selectedObject.getPredefined() == 1)
        {
            dataAccess.showConfirmDialog(this, ATSwingUtils.DialogType.Error, //
                "DOCTOR_TYPE_CANT_EDIT_PREDEFINED", ATSwingUtils.ConfirmDialogType.ClosedOption);
            // JOptionPane.showConfirmDialog(this,
            // dataAccess.getI18nControlInstance().getMessage("DOCTOR_TYPE_CANT_EDIT_PREDEFINED"),
            // dataAccess.getI18nControlInstance().getMessage("ERROR"),
            // JOptionPane.CLOSED_OPTION);
        }
        else
        {
            DoctorTypeDialog dt = new DoctorTypeDialog(this, selectedObject, true);
            if (dt.wasOperationSuccessful())
            {
                getFullData();
            }
        }

    }


    @Override
    public void checkAndExecuteActionSelect()
    {

    }
}
