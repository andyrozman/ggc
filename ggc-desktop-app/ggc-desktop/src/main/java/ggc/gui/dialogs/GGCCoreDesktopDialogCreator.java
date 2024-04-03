package ggc.gui.dialogs;

import javax.swing.*;

import com.atech.db.hibernate.HibernateObject;
import com.atech.graphics.dialogs.DialogCreator;
import com.atech.graphics.dialogs.StandardDialogForObject;

import ggc.core.db.hibernate.doc.DoctorAppointmentH;
import ggc.core.db.hibernate.doc.DoctorH;
import ggc.core.db.hibernate.doc.DoctorTypeH;
import ggc.core.db.hibernate.inventory.InventoryItemTypeH;
import ggc.gui.dialogs.doctor.DoctorDialog;
import ggc.gui.dialogs.doctor.DoctorTypeDialog;
import ggc.gui.dialogs.doctor.appointment.AppointmentDialog;
import ggc.gui.dialogs.inventory.InventoryItemTypeDialog;

public class GGCCoreDesktopDialogCreator implements DialogCreator
{

    public <E extends HibernateObject> boolean isApplicable(Class<E> clazz)
    {
        return (clazz == DoctorTypeH.class || //
                clazz == DoctorH.class || //
                clazz == InventoryItemTypeH.class || //
                clazz == DoctorAppointmentH.class);
    }


    public <E extends HibernateObject> StandardDialogForObject createDialog(Class<E> clazz, JDialog parentDialog)
    {
        StandardDialogForObject dialog = null;

        if (clazz == DoctorTypeH.class)
        {
            dialog = new DoctorTypeDialog(parentDialog);
        }
        else if (clazz == DoctorH.class)
        {
            dialog = new DoctorDialog(parentDialog);
        }
        else if (clazz == DoctorAppointmentH.class)
        {
            dialog = new AppointmentDialog(parentDialog);
        }
        else if (clazz == InventoryItemTypeH.class)
        {
            dialog = new InventoryItemTypeDialog(parentDialog);
        }

        return dialog;
    }


    public <E extends HibernateObject> StandardDialogForObject createDialog(Class<E> clazz, JDialog parentDialog,
            E selectableObject, boolean isEdit)
    {
        StandardDialogForObject dialog = null;

        if (clazz == DoctorTypeH.class)
        {
            dialog = new DoctorTypeDialog(parentDialog, selectableObject, isEdit);
        }
        else if (clazz == DoctorH.class)
        {
            dialog = new DoctorDialog(parentDialog, selectableObject, isEdit);
        }
        else if (clazz == DoctorAppointmentH.class)
        {
            dialog = new AppointmentDialog(parentDialog, selectableObject, isEdit);
        }
        else if (clazz == InventoryItemTypeH.class)
        {
            dialog = new InventoryItemTypeDialog(parentDialog, selectableObject);
        }

        return dialog;
    }
}
