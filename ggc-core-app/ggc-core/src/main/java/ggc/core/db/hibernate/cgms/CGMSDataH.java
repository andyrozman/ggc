package ggc.core.db.hibernate.cgms;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.atech.db.hibernate.HibernateObject;

public class CGMSDataH extends HibernateObject
{

    private static final long serialVersionUID = 9052976780137113098L;

    private long id;
    private long dtInfo;
    private int baseType;
    private int subType;
    private String value;
    private String extended;
    private int personId;
    private String comment;
    private long changed;


    /**
     * full constructor
     * 
     * @param dtInfo
     * @param baseType
     * @param subType
     * @param value
     * @param extended
     * @param personId
     * @param comment
     * @param changed
     * */
    public CGMSDataH(long dtInfo, int baseType, int subType, String value, String extended, int personId,
            String comment, long changed)
    {
        this.dtInfo = dtInfo;
        this.baseType = baseType;
        this.subType = subType;
        this.value = value;
        this.extended = extended;
        this.personId = personId;
        this.comment = comment;
        this.changed = changed;
    }


    /**
     * default constructor
     */
    public CGMSDataH()
    {
    }


    /**
     * minimal constructor
     * 
     * @param dtInfo
     * @param personId
     */
    public CGMSDataH(long dtInfo, int personId)
    {
        this.dtInfo = dtInfo;
        this.personId = personId;
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
     * Get Date/Time Info (this is long packed as AtechDateTime yyyymmddhhss)
     * @return
     */
    public long getDtInfo()
    {
        return this.dtInfo;
    }


    /**
     * Set Date/Time Info (this is long packed as AtechDateTime yyyymmddhhss)
     * 
     * @param dtInfo
     */
    public void setDtInfo(long dtInfo)
    {
        this.dtInfo = dtInfo;
    }


    /**
     * Get Base Type 
     * 
     * @return baseType value
     */
    public int getBaseType()
    {
        return this.baseType;
    }


    /**
     * Set Base Type 
     * 
     * @param baseType value
     */
    public void setBaseType(int baseType)
    {
        this.baseType = baseType;
    }


    /**
     * Get Sub Type 
     * 
     * @return subType value
     */
    public int getSubType()
    {
        return this.subType;
    }


    /**
     * Set Sub Type 
     * 
     * @param subType value
     */
    public void setSubType(int subType)
    {
        this.subType = subType;
    }


    /**
     * Get Value 
     * 
     * @return value value
     */
    public String getValue()
    {
        return this.value;
    }


    /**
     * Set Extended
     *  
     * @param value parameter
     */
    public void setValue(String value)
    {
        this.value = value;
    }


    /**
     * Get Extended 
     * 
     * @return extended value
     */
    public String getExtended()
    {
        return this.extended;
    }


    /**
     * Set Extended
     *  
     * @param extended parameter
     */
    public void setExtended(String extended)
    {
        this.extended = extended;
    }


    /**
     * Get Person Id
     * 
     * @return person id parameter
     */
    public int getPersonId()
    {
        return this.personId;
    }


    /**
     * Set Person Id
     * 
     * @param personId parameter
     */
    public void setPersonId(int personId)
    {
        this.personId = personId;
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


    /**
     * Get Changed
     * 
     * @return changed value
     */
    public long getChanged()
    {
        return this.changed;
    }


    /**
     * Set Changed
     * 
     * @param changed parameter
     */
    public void setChanged(long changed)
    {
        this.changed = changed;
    }


    /**
     * Equals - method to check equalicy of object
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        CGMSDataH cgmsDataH = (CGMSDataH) o;

        return new EqualsBuilder() //
                .append(id, cgmsDataH.id) //
                .append(dtInfo, cgmsDataH.dtInfo) //
                .append(baseType, cgmsDataH.baseType) //
                .append(subType, cgmsDataH.subType) //
                .append(personId, cgmsDataH.personId) //
                .append(changed, cgmsDataH.changed) //
                .append(value, cgmsDataH.value) //
                .append(extended, cgmsDataH.extended) //
                .append(comment, cgmsDataH.comment) //
                .isEquals();
    }

}
