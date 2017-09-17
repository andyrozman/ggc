package ggc.core.db.hibernate;

import com.atech.db.hibernate.HibernateBackupObject;
import com.atech.i18n.I18nControlAbstract;

import ggc.core.util.DataAccess;

/**
 * Created by andy on 17/02/17.
 */
public abstract class GGCHibernateBackupObject extends HibernateBackupObject
{

    private static final long serialVersionUID = 6152513443224878124L;


    public GGCHibernateBackupObject()
    {
        super();
    }


    public GGCHibernateBackupObject(I18nControlAbstract i18nControl)
    {
        super(i18nControl);
    }


    @Override
    protected void initTypeDisplayDefintion()
    {
        typeDisplayDefintion = DataAccess.getInstance().getDataDefinitionManager().getEntry(this.getClass());
    }

}
