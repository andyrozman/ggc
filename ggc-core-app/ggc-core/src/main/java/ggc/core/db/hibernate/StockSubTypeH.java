package ggc.core.db.hibernate;

import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.Criteria;
import org.hibernate.Session;

import com.atech.db.hibernate.HibernateBackupSelectableObject;
import com.atech.graphics.dialogs.selector.SelectableInterface;

import ggc.core.data.defs.InventoryItemUsageUnit;
import ggc.core.data.defs.StockTypeBase;

public class StockSubTypeH extends HibernateBackupSelectableObject
{

    private static final long serialVersionUID = -7076410510425754609L;

    private long id;
    private long stockTypeId;
    private String name;
    private String description;
    private long packageContent;
    private String packageContentUnit;
    private int usageUnit;
    private int usageMin;
    private int usageMax;
    private boolean active;
    private int personId;
    private String extended;
    private String comment;


    /** default constructor */
    public StockSubTypeH()
    {
    }


    /**
     * Get Id
     * 
     * @return
     */
    public long getId()
    {
        return this.id;
    }


    /**
     * Set Id
     * 
     * @param id
     */
    public void setId(long id)
    {
        this.id = id;
    }


    /**
     * Get Name
     * 
     * @return name
     */
    public String getName()
    {
        return this.name;
    }


    /**
     * Set Name
     * 
     * @param name as string
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Get Description
     * 
     * @return description parameter
     */
    public String getDescription()
    {
        return this.description;
    }


    /**
     * Set Description
     * 
     * @param description parameter
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    public long getStockTypeId()
    {
        return stockTypeId;
    }


    public StockTypeBase getStockType()
    {
        return StockTypeBase.getByCode((int) this.stockTypeId);
    }


    public void setStockTypeId(long stockTypeId)
    {
        this.stockTypeId = stockTypeId;
    }


    public long getPackageContent()
    {
        return packageContent;
    }


    public void setPackageContent(long packageContent)
    {
        this.packageContent = packageContent;
    }


    public String getPackageContentUnit()
    {
        return packageContentUnit;
    }


    public void setPackageContentUnit(String packageContentUnit)
    {
        this.packageContentUnit = packageContentUnit;
    }


    public int getUsageMin()
    {
        return usageMin;
    }


    public void setUsageMin(int usageMin)
    {
        this.usageMin = usageMin;
    }


    public int getUsageMax()
    {
        return usageMax;
    }


    public void setUsageMax(int usageMax)
    {
        this.usageMax = usageMax;
    }


    public boolean isActive()
    {
        return active;
    }


    public void setActive(boolean active)
    {
        this.active = active;
    }


    public int getPersonId()
    {
        return personId;
    }


    public void setPersonId(int personId)
    {
        this.personId = personId;
    }


    public String getExtended()
    {
        return extended;
    }


    public void setExtended(String extended)
    {
        this.extended = extended;
    }


    public String getUsageDescription()
    {
        return "" + this.usageMin + " - " + this.usageMax + " / "
                + InventoryItemUsageUnit.getByCode(this.getUsageUnit()).getTranslation();
    }


    /**
     * Get Comment
     * 
     * @return
     */
    public String getComment()
    {
        return this.comment;
    }


    /**
     * Set Comment
     * 
     * @param comment
     */
    public void setComment(String comment)
    {
        this.comment = comment;
    }


    public int getUsageUnit()
    {
        return usageUnit;
    }


    public void setUsageUnit(int usageUnit)
    {
        this.usageUnit = usageUnit;
    }


    /** 
     * Custom equals implementation
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof StockSubTypeH))
            return false;
        StockSubTypeH castOther = (StockSubTypeH) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }


    /**
     * To String
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return new ToStringBuilder(this).append("id", getId()).toString();
    }


    // Backup Object

    @Override
    protected String getColumnNames(int tableVersion)
    {
        return null;
    }


    public String getTargetName()
    {
        return null;
    }


    public int getTableVersion()
    {
        return 0;
    }


    public String dbExport(int table_version) throws Exception
    {
        return null;
    }


    public void dbImport(int tableVersion, String valueEntry, Map<String, String> headers) throws Exception
    {

    }


    // Selectable Interface

    public String getColumnValue(int num)
    {
        switch (num)
        {
            case 0:
                return getStockType().getTranslation();

            case 1:
                return getName();

            case 2:
                return getDescription();

            default:
                return "invalid data";

        }
    }


    public Object getColumnValueObject(int num)
    {
        return getColumnValue(num);
    }


    public int compareTo(SelectableInterface o)
    {
        return 0;
    }


    @Override
    protected void initTypeDisplayDefintion()
    {
        // typeDisplayDefintion =
        // DataAccess.getInstance().getDataDefinitionManager().getEntry("StockSubTypeDto");
    }


    @Override
    public boolean isFoundString(String findString)
    {
        return this.name.toLowerCase().contains(findString.toLowerCase())
                || this.description.toLowerCase().contains(findString.toLowerCase());
    }


    @Override
    public String toStringDescriptive()
    {
        return String.format(getBaseForDescriptiveString(), ("name=" + this.name));
    }


    @Override
    public Criteria getChildrenCriteria(Session session, HibernateBackupSelectableObject object)
    {
        throw new RuntimeException();
        // return null;
    }

}
