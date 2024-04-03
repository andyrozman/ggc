package ggc.core.db.hibernate.inventory;

import org.hibernate.Criteria;
import org.hibernate.Session;

import com.atech.db.hibernate.HibernateSelectableObject;
import com.atech.graphics.dialogs.selector.SelectableInterface;

import ggc.core.db.hibernate.GGCHibernateSelectableObject;
import ggc.core.util.DataAccess;

/**
 * Created by andy on 31.03.17.
 */
public class InventoryItemH extends GGCHibernateSelectableObject
{

    private static final long serialVersionUID = -7735713499828302653L;

    private long id;
    private InventoryItemTypeH inventoryItemType;
    private long inventoryId;
    private long amount;
    private String location;
    private Long itemChanged;
    private Long itemUsedTillMin;
    private Long itemUsedTillMax;

    private String extended;
    private int personId;
    private String comment;


    @Override
    public boolean isFoundString(String findString)
    {
        return false;
    }


    @Override
    public String toStringDescriptive()
    {
        return null;
    }


    @Override
    public Criteria getChildrenCriteria(Session session, HibernateSelectableObject object)
    {
        return null;
    }


    @Override
    public long getId()
    {
        return this.id;
    }


    @Override
    public void setId(long id)
    {
        this.id = id;
    }


    public String getColumnValue(int columnIndex)
    {
        switch (columnIndex)
        {
            case 0:
                return this.inventoryItemType.getDisplayString(InventoryItemTypeH.InventoryItemTypeDisplay.Line1, null);

            case 1:
                return "" + this.amount + " " + DataAccess.getInstance().getI18nControlInstance()
                        .getMessage(this.inventoryItemType.getPackageContentUnit());
            case 2:
                return this.location;

            default:
                return "Unknown";

        }

    }


    public Object getColumnValueObject(int columnIndex)
    {
        return getColumnValue(columnIndex);
    }


    public int compareTo(SelectableInterface o)
    {
        return 0;
    }


    public InventoryItemTypeH getInventoryItemType()
    {
        return inventoryItemType;
    }


    public void setInventoryItemType(InventoryItemTypeH inventoryItemType)
    {
        this.inventoryItemType = inventoryItemType;
    }


    public long getInventoryId()
    {
        return inventoryId;
    }


    public void setInventoryId(long inventoryId)
    {
        this.inventoryId = inventoryId;
    }


    public long getAmount()
    {
        return amount;
    }


    public void setAmount(long amount)
    {
        this.amount = amount;
    }


    public String getLocation()
    {
        return location;
    }


    public void setLocation(String location)
    {
        this.location = location;
    }


    public String getExtended()
    {
        return extended;
    }


    public void setExtended(String extended)
    {
        this.extended = extended;
    }


    public int getPersonId()
    {
        return personId;
    }


    public void setPersonId(int personId)
    {
        this.personId = personId;
    }


    public String getComment()
    {
        return comment;
    }


    public void setComment(String comment)
    {
        this.comment = comment;
    }


    public Long getItemChanged()
    {
        return itemChanged;
    }


    public void setItemChanged(Long itemChanged)
    {
        this.itemChanged = itemChanged;
    }


    public Long getItemUsedTillMin()
    {
        return itemUsedTillMin;
    }


    public void setItemUsedTillMin(Long itemUsedTillMin)
    {
        this.itemUsedTillMin = itemUsedTillMin;
    }


    public Long getItemUsedTillMax()
    {
        return itemUsedTillMax;
    }


    public void setItemUsedTillMax(Long itemUsedTillMax)
    {
        this.itemUsedTillMax = itemUsedTillMax;
    }
}
