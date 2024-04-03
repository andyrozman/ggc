package ggc.core.db;

import com.atech.db.hibernate.DataTransformer;
import com.atech.db.hibernate.HibernateObject;

import ggc.core.db.hibernate.doc.DoctorH;
import ggc.core.db.hibernate.doc.DoctorTypeH;

/**
 * Created by andy on 16/02/17.
 */
public class GGCDataTransformer implements DataTransformer
{

    public <E extends HibernateObject> boolean isTransformationRequired(Class<E> clazz)
    {
        return (clazz == DoctorTypeH.class || clazz == DoctorH.class);
    }


    public <E extends HibernateObject> void transformData(E dataObject, Class<E> clazz)
    {
        if (clazz == DoctorTypeH.class)
        {
            DoctorTypeH typedDataObject = (DoctorTypeH) dataObject;
            typedDataObject.setNameTranslated();
        }
        else if (clazz == DoctorH.class)
        {
            DoctorH doctor = (DoctorH) dataObject;
            doctor.getDoctorType().setNameTranslated();
        }
    }

}
