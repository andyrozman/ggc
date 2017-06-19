package ggc.core.db.dto;

import com.atech.data.mng.DataDefinitionEntry;
import com.atech.graphics.dialogs.selector.ColumnSorter;
import com.atech.graphics.dialogs.selector.SelectableInterface;

import ggc.core.db.hibernate.StockSubTypeH;

// just for selectable interface
public class StockSubTypeDto extends StockSubTypeH implements SelectableInterface
{

    private static final long serialVersionUID = -7342652032510468375L;

    private static DataDefinitionEntry typeDisplayDefintion;

    private StockSubTypeH subTypeItem;


    public StockSubTypeDto(StockSubTypeH subType)
    {
        if (typeDisplayDefintion == null)
        {
            // typeDisplayDefintion =
            // DataAccess.getInstance().getDataDefinitionManager().getEntry("StockSubTypeDto");
        }

        this.subTypeItem = subType;
    }


    public StockSubTypeH getItem()
    {
        return subTypeItem;
    }


    public long getItemId()
    {
        return 0;
    }


    public String getShortDescription()
    {
        return null;
    }


    public String getColumnValue(int num)
    {
        switch (num)
        {
            case 0:
                return this.subTypeItem.getStockType().getTranslation();

            case 1:
                return this.subTypeItem.getName();

            case 2:
                return this.subTypeItem.getDescription();

            default:
                return "invalid";

        }
    }


    public Object getColumnValueObject(int num)
    {
        return null;
    }


    public int getColumnCount()
    {
        return typeDisplayDefintion.getColumnCount();
    }


    public String getColumnName(int index)
    {
        return typeDisplayDefintion.getColumnName(index);
    }


    public int getColumnWidth(int index, int width)
    {
        return typeDisplayDefintion.getColumnWidth(index, width);
    }


    public boolean isFound(String text)
    {
        return true;
    }


    public boolean isFound(int value)
    {
        return true;
    }


    public boolean isFound(int from, int till, int state)
    {
        return true;
    }


    public void setSearchContext()
    {

    }


    public void setColumnSorter(ColumnSorter cs)
    {

    }


    public int compareTo(SelectableInterface o)
    {
        return 0;
    }
}
