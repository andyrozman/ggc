package ggc.core.db.hibernate.doc;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.Criteria;
import org.hibernate.Session;

import com.atech.db.hibernate.HibernateSelectableObject;
import com.atech.graphics.dialogs.selector.SelectableInterface;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

import ggc.core.db.hibernate.GGCHibernateSelectableObject;

public class DoctorAppointmentH extends GGCHibernateSelectableObject
{

    private static final long serialVersionUID = 5137365456725058472L;

    private long id;
    private long appointmentDateTime;
    private String appointmentText;
    private String extended;
    private int personId;
    private String comment;
    private DoctorH doctor;


    /** full constructor 
     * @param appointmentDateTime
     * @param appointmentText
     * @param extended 
     * @param personId
     * @param comment 
     * @param doctor */
    public DoctorAppointmentH(long appointmentDateTime, String appointmentText, String extended, int personId,
            String comment, DoctorH doctor)
    {
        this.appointmentDateTime = appointmentDateTime;
        this.appointmentText = appointmentText;
        this.extended = extended;
        this.personId = personId;
        this.comment = comment;
        this.doctor = doctor;
    }


    /** default constructor */
    public DoctorAppointmentH()
    {
    }


    public DoctorAppointmentH(I18nControlAbstract i18nControl)
    {
        super(i18nControl);
    }


    /** minimal constructor 
     * @param appointmentDateTime
     * @param personId */
    public DoctorAppointmentH(long appointmentDateTime, int personId)
    {
        this.appointmentDateTime = appointmentDateTime;
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
    public long getAppointmentDateTime()
    {
        return this.appointmentDateTime;
    }


    /**
     * Set Date/Time Info (this is long packed as AtechDateTime yyyymmddhhss)
     * 
     * @param dt_info 
     */
    public void setAppointmentDateTime(long dt_info)
    {
        this.appointmentDateTime = dt_info;
    }


    /**
     * Get Appointment Text
     * 
     * @return appointmentText values
     */
    public String getAppointmentText()
    {
        return this.appointmentText;
    }


    /**
     * Set Appointment Text
     * 
     * @param appointmentText values
     */
    public void setAppointmentText(String appointmentText)
    {
        this.appointmentText = appointmentText;
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
                ", appointmentDateTime=" + appointmentDateTime + //
                ", appointmentText='" + appointmentText + '\'' + //
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
                .append(appointmentDateTime, that.appointmentDateTime) //
                .append(personId, that.personId) //
                .append(appointmentText, that.appointmentText) //
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
                .append(appointmentDateTime) //
                .append(appointmentText) //
                .append(extended) //
                .append(personId) //
                .append(comment) //
                .append(doctor) //
                .toHashCode();
    }

    // public String dbExport(int table_version) throws Exception
    // {
    // StringBuilder sb = new StringBuilder();
    //
    // sb.append(this.getId());
    // writeDelimiter(sb);
    // sb.append(this.getDoctor().getId());
    // writeDelimiter(sb);
    // sb.append(this.getAppointmentDateTime());
    // writeDelimiter(sb);
    // sb.append(this.getAppointmentText());
    // writeDelimiter(sb);
    // sb.append(this.getPersonId());
    // writeDelimiter(sb);
    // sb.append(this.getExtended());
    // writeDelimiter(sb);
    // sb.append(this.getComment());
    // writeDelimiter(sb);
    // sb.append("\n");
    //
    // return sb.toString();
    // }


    public String getShortDescription()
    {
        return toStringDescriptive();
    }


    public String getColumnValue(int column)
    {
        switch (column)
        {
            case 0:
                return ATechDate.getDateTimeString(ATechDateType.DateAndTimeMin, this.appointmentDateTime);
            case 1:
                return this.appointmentText;

            case 2:
                return this.doctor.getName();

            case 3:
                return this.doctor.getDoctorType().getNameTranslated();

            default:
                return null;
        }
    }


    public Object getColumnValueObject(int column)
    {
        return getColumnValue(column);
    }


    public int compareTo(SelectableInterface o)
    {
        if (o instanceof DoctorAppointmentH)
        {
            DoctorAppointmentH other = (DoctorAppointmentH) o;
            return (int) (this.getAppointmentDateTime() - other.getAppointmentDateTime());
        }
        else
            return 0;
    }


    @Override
    public String toStringDescriptive()
    {
        return String.format(getBaseForDescriptiveString(),
            ("time=" + ATechDate.getDateTimeString(ATechDateType.DateAndTimeMin, this.appointmentDateTime) + ",doctor="
                    + this.doctor.getName()));
    }


    @Override
    public Criteria getChildrenCriteria(Session session, HibernateSelectableObject object)
    {
        return null;
    }


    @Override
    public boolean isFoundString(String text)
    {
        return ((getDoctor().getName().toLowerCase().contains(text.toLowerCase()))
                || (this.appointmentText.toLowerCase().contains(text.toLowerCase())));
    }

}
