package ggc.core.db.hibernate.doc;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.hibernate.Criteria;
import org.hibernate.Session;

import com.atech.db.hibernate.HibernateSelectableObject;
import com.atech.graphics.dialogs.selector.SelectableInterface;
import com.atech.i18n.I18nControlAbstract;

import ggc.core.db.hibernate.GGCHibernateSelectableObject;
import ggc.core.util.DataAccess;

public class DoctorTypeH extends GGCHibernateSelectableObject
{

    private static final long serialVersionUID = 8775376037164641226L;
    private static final int TABLE_VERSION = 1;

    private long id;
    private String name;
    private int predefined;

    // Non Hibernate fields
    private String nameTranslated;
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


    public DoctorTypeH(I18nControlAbstract i18nControl)
    {
        super(i18nControl);
    }


    public String getNameTranslated()
    {
        if (nameTranslated == null)
            setNameTranslated();

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


    @Override
    public String toStringDescriptive()
    {
        return String.format(getBaseForDescriptiveString(), ("name=" + this.name));
    }


    @Override
    public Criteria getChildrenCriteria(Session session, HibernateSelectableObject object)
    {
        // not used
        return null;
    }


    public String getColumnValue(int num)
    {
        switch (num)
        {
            case 0:
                return this.nameTranslated;

            case 1:
                return this.predefinedTranslated;

            default:
                return null;
        }
    }


    public Object getColumnValueObject(int num)
    {
        return getColumnValue(num);
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

    // public String dbExport(int table_version) throws Exception
    // {
    // StringBuilder sb = new StringBuilder();
    //
    // sb.append(this.getId());
    // writeDelimiter(sb);
    // sb.append(this.getName());
    // writeDelimiter(sb);
    // sb.append(this.getPredefined());
    // sb.append("\n");
    //
    // return sb.toString();
    // }

}
