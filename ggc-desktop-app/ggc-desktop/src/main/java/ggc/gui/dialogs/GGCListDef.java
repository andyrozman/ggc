package ggc.gui.dialogs;

import java.awt.*;

import com.atech.db.hibernate.HibernateSelectableObject;
import com.atech.graphics.dialogs.guilist.GUIListDefAbstract;
import com.atech.utils.ATDataAccessAbstract;

/**
 * Created by andy on 17/02/17.
 */
public abstract class GGCListDef extends GUIListDefAbstract
{

    public <E extends HibernateSelectableObject> GGCListDef(ATDataAccessAbstract dataAccess, E targetObject,
            String titleI18nKey, String listDefinitionName, String helpId, Rectangle tableBounds,
            Dimension windowDimension)
    {
        super(dataAccess, targetObject, titleI18nKey, listDefinitionName, helpId, tableBounds, windowDimension);
    }

}
