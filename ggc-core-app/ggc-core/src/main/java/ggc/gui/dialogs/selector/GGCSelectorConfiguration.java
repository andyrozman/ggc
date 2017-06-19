package ggc.gui.dialogs.selector;

import com.atech.graphics.dialogs.selector.SelectorConfiguration;

/**
 * Created by andy on 25.02.17.
 */
public enum GGCSelectorConfiguration implements SelectorConfiguration
{
    None(Object.class), //
    DoctorH(ggc.core.db.hibernate.doc.DoctorH.class, //
            "NAME,TYPE", "70,30"), //
    DoctorTypeH(ggc.core.db.hibernate.doc.DoctorTypeH.class, //
            "DOCTOR_TYPE,DTCMP_PREDEFINED", "70,30"), //
    DoctorAppointmentH(ggc.core.db.hibernate.doc.DoctorAppointmentH.class, //
            "APPOINTMENT_DATE,APPOINTMENT_TEXT,DOCTOR_NAME,DOCTOR_TYPE", "35, 35, 25, 15"), //
    InventoryItemTypeH(ggc.core.db.hibernate.inventory.InventoryItemTypeH.class, //
            "INVENTORY_GROUP,INVENTORY_NAME,INVENTORY_DESCRIPTION,ACTIVE", "20,40,30,10"), //
    InventoryH(ggc.core.db.hibernate.inventory.InventoryH.class), //
    InventoryItemH(ggc.core.db.hibernate.inventory.InventoryItemH.class, //
            "INVENTORY_ITEM_TYPE,AMOUNT,LOCATION", "50,25,25");

    Class clazz;
    String columnNames;
    String columnSizes;


    GGCSelectorConfiguration(Class clazz)
    {
        this.clazz = clazz;
    }


    GGCSelectorConfiguration(Class clazz, String columnNames, String columnSizes)
    {
        this.clazz = clazz;
        this.columnNames = columnNames;
        this.columnSizes = columnSizes;
    }


    public Class getClazz()
    {
        return this.clazz;
    }


    public String getColumnNames()
    {
        return this.columnNames;
    }


    public String getColumnSizes()
    {
        return this.columnSizes;
    }
}
