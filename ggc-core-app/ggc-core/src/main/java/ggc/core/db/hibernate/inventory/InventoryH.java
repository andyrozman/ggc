package ggc.core.db.hibernate.inventory;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;

import com.atech.db.hibernate.HibernateSelectableObject;
import com.atech.graphics.dialogs.selector.SelectableInterface;

import ggc.core.db.hibernate.GGCHibernateSelectableObject;

public class InventoryH extends GGCHibernateSelectableObject
{

    private static final long serialVersionUID = -2210476820690895945L;

    private long id;
    private long datetime;
    private String description;
    private String extended;
    private int personId;
    private String comment;
    private long changed;

    private List<InventoryItemH> itemList = new ArrayList<InventoryItemH>();


    @Override
    public long getId()
    {
        return id;
    }


    @Override
    public void setId(long id)
    {
        this.id = id;
    }


    public long getDatetime()
    {
        return datetime;
    }


    public void setDatetime(long datetime)
    {
        this.datetime = datetime;
    }


    public String getDescription()
    {
        return description;
    }


    public void setDescription(String description)
    {
        this.description = description;
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


    public long getChanged()
    {
        return changed;
    }


    public void setChanged(long changed)
    {
        this.changed = changed;
    }


    public void addItem(InventoryItemH item)
    {
        this.itemList.add(item);
    }


    public List<InventoryItemH> getItems()
    {
        return this.itemList;
    }


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


    public String getColumnValue(int num)
    {
        return null;
    }


    public Object getColumnValueObject(int num)
    {
        return null;
    }


    public int compareTo(SelectableInterface o)
    {
        return 0;
    }


    public List<InventoryItemH> getItemList()
    {
        return itemList;
    }


    public void setItemList(List<InventoryItemH> itemList)
    {
        this.itemList = itemList;
    }
}
