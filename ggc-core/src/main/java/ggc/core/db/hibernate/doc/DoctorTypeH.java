package ggc.core.db.hibernate.doc;

import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.atech.db.hibernate.HibernateBackupSelectableObject;
import com.atech.graphics.dialogs.selector.SelectableInterface;
import ggc.core.util.DataAccess;

/** @author Hibernate CodeGenerator */
public class DoctorTypeH extends HibernateBackupSelectableObject
{

    private static final long serialVersionUID = 8775376037164641226L;
    private static final int TABLE_VERSION = 1;

    /** identifier field */
    private long id;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private int predefined;

    // non persistent field
    private String nameTranslated;

    // non persistent field
    private String predefinedTranslated;


    /** full constructor 
     * @param name 
     * @param predefined */
    public DoctorTypeH(String name, int predefined)
    {
        this.name = name;
        this.predefined = predefined;
        setNameTranslated();
    }


    public String getNameTranslated()
    {
        return this.nameTranslated;
    }


    public void setNameTranslated()
    {
        // System.out.println("setNameTranslated,");
        if (predefined == 1)
        {
            if (name.contains("="))
            {
                name = name.substring(0, name.indexOf("="));
            }

            this.nameTranslated = DataAccess.getInstance().getI18nControlInstance().getMessage(this.name);
        }
        else
        {
            this.nameTranslated = this.name;
        }

        this.predefinedTranslated = DataAccess.getInstance().getI18nControlInstance()
                .getMessage(this.predefined == 1 ? "YES" : "NO");
    }


    /** default constructor */
    public DoctorTypeH()
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
        setNameTranslated();
    }


    /**
     * Get Predefined
     * 
     * @return predefined value
     */
    public int getPredefined()
    {
        return this.predefined;
    }


    /**
     * Set Predefined
     * 
     * @param predefined value
     */
    public void setPredefined(int predefined)
    {
        this.predefined = predefined;
    }


    /** 
     * Custom equals implementation
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof DoctorTypeH))
            return false;
        DoctorTypeH castOther = (DoctorTypeH) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }


    public String getShortDescription()
    {
        return null;
    }


    public int getColumnCount()
    {
        return 2;
    }


    public String getColumnName(int num)
    {
        switch (num)
        {
            case 2:
                return "DTCMP_PREDEFINED";

            default:
                return "DOCTOR_TYPE";
        }
    }


    public String getColumnValue(int num)
    {
        switch (num)
        {
            case 2:
                return this.predefinedTranslated;

            default:
                return this.nameTranslated;
        }
    }


    public Object getColumnValueObject(int num)
    {
        return getColumnValue(num);
    }


    public int getColumnWidth(int num, int width)
    {
        switch (num)
        {
            case 2:
                return (int) (width * 0.2);

            default:
                return (int) (width * 0.8);
        }
    }


    @Override
    public boolean isFoundString(String findString)
    {
        return nameTranslated.toLowerCase().contains(findString.toLowerCase());
    }


    public void setSearchContext()
    {
        if (this.nameTranslated == null)
            setNameTranslated();
    }


    public int compareTo(SelectableInterface o)
    {
        if (!(o instanceof DoctorTypeH))
        {
            return 0;
        }

        DoctorTypeH dt = (DoctorTypeH) o;

        return nameTranslated.compareToIgnoreCase(dt.nameTranslated);

    }


    @Override
    protected String getColumnNames(int tableVersion)
    {
        return null;
    }


    public String getTargetName()
    {
        return DataAccess.getInstance().getI18nControlInstance().getMessage("DOCTOR_TYPES");
    }


    public int getTableVersion()
    {
        return TABLE_VERSION;
    }


    public String dbExport(int table_version) throws Exception
    {
        return null;
    }


    public void dbImport(int tableVersion, String valueEntry, Map<String, String> headers) throws Exception
    {

    }

}
