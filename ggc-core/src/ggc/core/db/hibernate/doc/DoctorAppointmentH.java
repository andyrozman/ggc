package ggc.core.db.hibernate.doc;

import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.atech.db.hibernate.HibernateBackupSelectableObject;
import com.atech.graphics.dialogs.selector.SelectableInterface;

/** @author Hibernate CodeGenerator */
public class DoctorAppointmentH extends HibernateBackupSelectableObject
{

    private static final long serialVersionUID = 5137365456725058472L;

    private long id;
    private long dt_apoint;
    private String apoint_text;
    private String extended;
    private int personId;
    private String comment;
    private DoctorH doctor;


    /** full constructor 
     * @param dt_apoint 
     * @param apoint_text 
     * @param extended 
     * @param personId
     * @param comment 
     * @param doctor */
    public DoctorAppointmentH(long dt_apoint, String apoint_text, String extended, int personId, String comment,
            DoctorH doctor)
    {
        this.dt_apoint = dt_apoint;
        this.apoint_text = apoint_text;
        this.extended = extended;
        this.personId = personId;
        this.comment = comment;
        this.doctor = doctor;
    }


    /** default constructor */
    public DoctorAppointmentH()
    {
    }


    /** minimal constructor 
     * @param dt_apoint 
     * @param personId */
    public DoctorAppointmentH(long dt_apoint, int personId)
    {
        this.dt_apoint = dt_apoint;
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
    public long getDt_apoint()
    {
        return this.dt_apoint;
    }


    /**
     * Set Date/Time Info (this is long packed as AtechDateTime yyyymmddhhss)
     * 
     * @param dt_info 
     */
    public void setDt_apoint(long dt_info)
    {
        this.dt_apoint = dt_info;
    }


    /**
     * Get Appointment Text
     * 
     * @return apoint_text values
     */
    public String getApoint_text()
    {
        return this.apoint_text;
    }


    /**
     * Set Appointment Text
     * 
     * @param apoint_text values
     */
    public void setApoint_text(String apoint_text)
    {
        this.apoint_text = apoint_text;
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
     * Get Doctor
     * 
     * @return doctor value (DoctorH instance)
     */
    public DoctorH getDoctor()
    {
        return this.doctor;
    }


    /**
     * Set Doctor
     * 
     * @param doctor value (DoctorH instance)
     */
    public void setDoctor(DoctorH doctor)
    {
        this.doctor = doctor;
    }


    /**
     * To String
     *
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        return "DoctorAppointmentH [" + "id=" + id + //
                ", dt_apoint=" + dt_apoint + //
                ", apoint_text='" + apoint_text + '\'' + //
                ", extended='" + extended + '\'' + //
                ", personId=" + personId + //
                ", comment='" + comment + '\'' //
                + ", doctor=" + doctor + ']'; //
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        DoctorAppointmentH that = (DoctorAppointmentH) o;

        return new EqualsBuilder() //
                .append(id, that.id) //
                .append(dt_apoint, that.dt_apoint) //
                .append(personId, that.personId) //
                .append(apoint_text, that.apoint_text) //
                .append(extended, that.extended) //
                .append(comment, that.comment) //
                .append(doctor, that.doctor) //
                .isEquals();
    }


    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(17, 37) //
                .append(id) //
                .append(dt_apoint) //
                .append(apoint_text) //
                .append(extended) //
                .append(personId) //
                .append(comment) //
                .append(doctor) //
                .toHashCode();
    }


    @Override
    public boolean isFoundString(String findString)
    {
        return false;
    }


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


    public String getBackupFile()
    {
        return null;
    }


    public String getShortDescription()
    {
        return null;
    }


    public int getColumnCount()
    {
        return 0;
    }


    public String getColumnName(int num)
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


    public int getColumnWidth(int num, int width)
    {
        return 0;
    }


    public int compareTo(SelectableInterface o)
    {
        return 0;
    }
}
