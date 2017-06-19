package ggc.core.db.hibernate.doc;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.atech.db.hibernate.HibernateSelectableObject;
import com.atech.graphics.dialogs.selector.SelectableInterface;
import com.atech.i18n.I18nControlAbstract;

import ggc.core.db.hibernate.GGCHibernateSelectableObject;

public class DoctorH extends GGCHibernateSelectableObject
{

    private static final long serialVersionUID = -203432582979938654L;

    private long id;
    private String name;
    private String address;
    private String phoneGsm;
    private String phone;
    private String email;
    private String workingTime;
    private long activeFrom;
    private long activeTill;
    private String extended;
    private int visible;
    private int personId;
    private String comment;
    private DoctorTypeH doctorType;


    /**
     * full constructor
     *
     * @param name
     * @param address
     * @param phoneGsm
     * @param phone
     * @param workingTime
     * @param extended
     * @param visible
     * @param personId
     * @param comment
     * @param doctorType
     */
    public DoctorH(String name, String address, String phoneGsm, String phone, String workingTime, String extended,
            int visible, int personId, String comment, DoctorTypeH doctorType, long activeFrom, long activeTill)
    {
        this.name = name;
        this.address = address;
        this.phoneGsm = phoneGsm;
        this.phone = phone;
        this.workingTime = workingTime;
        this.extended = extended;
        this.visible = visible;
        this.personId = personId;
        this.comment = comment;
        this.doctorType = doctorType;
        this.activeFrom = activeFrom;
        this.activeTill = activeTill;
    }


    /** default constructor */
    public DoctorH()
    {
    }


    public DoctorH(I18nControlAbstract i18nControl)
    {
        super(i18nControl);
    }


    /**
     * minimal constructor
     *
     * @param personId
     */
    public DoctorH(int personId)
    {
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
     * Get Address
     *
     * @return
     */
    public String getAddress()
    {
        return this.address;
    }


    /**
     * Set Address
     *
     * @param address
     */
    public void setAddress(String address)
    {
        this.address = address;
    }


    /**
     * Get Phone GSM
     *
     * @return
     */
    public String getPhoneGsm()
    {
        return this.phoneGsm;
    }


    /**
     * Set Phone GSM
     *
     * @param phoneGsm
     */
    public void setPhoneGsm(String phoneGsm)
    {
        this.phoneGsm = phoneGsm;
    }


    /**
     * Get Phone
     *
     * @return
     */
    public String getPhone()
    {
        return this.phone;
    }


    /**
     * Set Phone
     *
     * @param phone
     */
    public void setPhone(String phone)
    {
        this.phone = phone;
    }


    /**
     * Get Working Time
     *
     * @return
     */
    public String getWorkingTime()
    {
        return this.workingTime;
    }


    /**
     * Set Working Time
     *
     * @param workingTime
     */
    public void setWorkingTime(String workingTime)
    {
        this.workingTime = workingTime;
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
     * Get Visible
     *
     * @return
     */
    public int getVisible()
    {
        return this.visible;
    }


    /**
     * Set Visible
     *
     * @param visible
     */
    public void setVisible(int visible)
    {
        this.visible = visible;
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
     * Get Doctor Type
     *
     * @return
     */
    public DoctorTypeH getDoctorType()
    {
        return this.doctorType;
    }


    /**
     * Set Doctor Type
     *
     * @param doctorType
     */
    public void setDoctorType(DoctorTypeH doctorType)
    {
        this.doctorType = doctorType;
    }


    public String getEmail()
    {
        return email;
    }


    public void setEmail(String email)
    {
        this.email = email;
    }


    public long getActiveFrom()
    {
        return activeFrom;
    }


    public void setActiveFrom(long activeFrom)
    {
        this.activeFrom = activeFrom;
    }


    public long getActiveTill()
    {
        return activeTill;
    }


    public void setActiveTill(long activeTill)
    {
        this.activeTill = activeTill;
    }


    /**
     * Custom equals implementation
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof DoctorH))
            return false;
        DoctorH castOther = (DoctorH) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }


    public int compareTo(SelectableInterface o)
    {
        if (o instanceof DoctorH)
        {
            DoctorH d = (DoctorH) o;
            return this.getName().compareTo(d.getName());
        }
        else
            return 0;
    }


    public String getColumnValue(int num)
    {
        switch (num)
        {
            case 0:
                return this.getName();

            case 1:
                return this.getDoctorType().getNameTranslated();

            default:
                return null;
        }

    }


    public Object getColumnValueObject(int num)
    {
        return getColumnValue(num);
    }


    @Override
    public boolean isFoundString(String text)
    {
        return ((this.getDoctorType().getNameTranslated().toLowerCase().contains(text.toLowerCase()))
                || this.getName().toLowerCase().contains(text.toLowerCase()));

    }

    // public String dbExport(int table_version) throws Exception
    // {
    // StringBuilder sb = new StringBuilder();
    //
    // sb.append(this.getId());
    // writeDelimiter(sb);
    // sb.append(this.getName());
    // writeDelimiter(sb);
    // sb.append(this.getDoctorType().getId());
    // writeDelimiter(sb);
    // sb.append(this.getAddress());
    // writeDelimiter(sb);
    // sb.append(this.getPhone());
    // writeDelimiter(sb);
    // sb.append(this.getPhoneGsm());
    // writeDelimiter(sb);
    // sb.append(this.getEmail());
    // writeDelimiter(sb);
    // sb.append(XmlPrettyPrintUtil.unPrettyPrint(this.getWorkingTime()));
    // writeDelimiter(sb);
    // sb.append(this.getActiveFrom());
    // writeDelimiter(sb);
    // sb.append(this.getActiveTill());
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

    // @Override
    // protected String getColumnNames(int tableVersion)
    // {
    // return "id; name; doctor_type_id; address; phone; phone_gsm; email;
    // working_time; active_from; "
    // + "active_till; person_id; extended; comment";
    // }


    // public void dbImport(int tableVersion, String valueEntry, Map<String,
    // String> headers) throws Exception
    // {
    // DataAccess da = DataAccess.getInstance();
    //
    // String[] arr = getSplittedValues(valueEntry);
    //
    // this.setId(da.getLongValueFromString(arr[0]));
    // this.setName(arr[1]);
    //
    // long doctorTypeId = da.getLongValueFromString(arr[2]);
    //
    // this.setAddress(arr[3]);
    // this.setPhone(arr[4]);
    // this.setPhoneGsm(arr[5]);
    // this.setEmail(arr[6]);
    // this.setWorkingTime(XmlPrettyPrintUtil.prettyPrint(arr[7]));
    // this.setActiveFrom(da.getLongValueFromString(arr[8]));
    // this.setActiveTill(da.getLongValueFromString(arr[9]));
    // this.setPersonId(da.getIntValueFromString(arr[10]));
    // this.setExtended(arr[11]);
    // this.setComment(arr[12]);
    //
    // this.setDoctorType(da.getDb().getCachedObject(DoctorTypeH.class,
    // doctorTypeId));
    // }

    @Override
    public String toStringDescriptive()
    {
        return String.format(getBaseForDescriptiveString(), ("name=" + this.name));
    }


    @Override
    public Criteria getChildrenCriteria(Session session, HibernateSelectableObject object)
    {
        DoctorH doctor = (DoctorH) object;

        Criteria criteria = session.createCriteria(DoctorAppointmentH.class);
        criteria.add(Restrictions.eq("personId", doctor.getPersonId()));
        criteria.add(Restrictions.eq("doctor", doctor));

        return criteria;
    }

}
