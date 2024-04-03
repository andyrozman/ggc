package ggc.gui.dialogs.selector;

import ggc.core.db.hibernate.StockSubTypeH;
import ggc.core.db.hibernate.doc.DoctorH;
import ggc.core.db.hibernate.doc.DoctorTypeH;

/**
 * Created by andy on 15/02/17.
 */
public enum GGCSelectorType
{
    DoctorTypeH(DoctorTypeH.class), //
    DoctorH(DoctorH.class), //
    InventoryItemTypeH(StockSubTypeH.class), //
    ;

    Class clazz;


    GGCSelectorType(Class clazz)
    {
        this.clazz = clazz;
    }


    public Class getClazz()
    {
        return this.clazz;
    }

}
