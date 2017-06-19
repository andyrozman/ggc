package ggc.core.db.hibernate;

import java.util.List;

import com.atech.db.hibernate.HibernateSelectableObject;
import com.atech.db.hibernate.transfer.BackupRestoreObject2;
import com.atech.graphics.components.tree.CheckBoxTreeNodeInterface;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATDataAccessAbstract;

import ggc.core.util.DataAccess;

public abstract class GGCHibernateSelectableObject extends HibernateSelectableObject implements BackupRestoreObject2
{

    private static final long serialVersionUID = 6152513443224878124L;

    boolean selected = false;


    public GGCHibernateSelectableObject()
    {
        super();
    }


    public GGCHibernateSelectableObject(I18nControlAbstract i18nControl)
    {
        super(i18nControl);
    }


    public GGCHibernateSelectableObject(ATDataAccessAbstract dataAccess)
    {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void initTypeDisplayDefinition()
    {
        typeDisplayDefintion = DataAccess.getInstance().getDataDefinitionEntry(this.getClass());
    }


    /**
     * {@inheritDoc}
     */
    public String getTargetName()
    {
        checkIfInitTypeDisplayDefinitionNeeded();
        return typeDisplayDefintion.getDatabaseTableConfiguration().getBackupTargetName();
    }


    /**
     * {@inheritDoc}
     */
    public boolean isCollection()
    {
        return false;
    }


    /**
     * {@inheritDoc}
     */
    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }


    /**
     * {@inheritDoc}
     */
    public boolean isSelected()
    {
        return this.selected;
    }


    /**
     * {@inheritDoc}
     */
    public String getClassName()
    {
        return this.getClass().getSimpleName();
    }


    /**
     * {@inheritDoc}
     */
    public List<CheckBoxTreeNodeInterface> getNodeChildren()
    {
        return null;
    }


    /**
     * {@inheritDoc}
     */
    public boolean hasNodeChildren()
    {
        return false;
    }

}
