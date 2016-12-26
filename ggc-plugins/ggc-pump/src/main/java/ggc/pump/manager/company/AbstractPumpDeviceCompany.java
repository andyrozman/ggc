package ggc.pump.manager.company;

import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.pump.util.DataAccessPump;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractPumpDeviceCompany.
 */
public abstract class AbstractPumpDeviceCompany extends AbstractDeviceCompany
{

    /**
     * The profile_names
     */
    protected String[] profile_names;


    /**
     * Instantiates a new abstract pump device company
     * 
     * @param value the value
     */
    public AbstractPumpDeviceCompany(boolean value)
    {
        super(value, DataAccessPump.getInstance());
        this.initProfileNames();
    }


    /**
     * Constructor 
     * 
     * @param company_id_
     * @param company_name
     * @param short_company_name
     * @param company_desc
     * @param implementation_status
     */
    public AbstractPumpDeviceCompany(int company_id_, String company_name, String short_company_name,
            String company_desc, DeviceImplementationStatus implementation_status)
    {
        super(false, company_id_, company_name, short_company_name, company_desc, implementation_status,
                DataAccessPump.getInstance());
        this.initProfileNames();
    }


    /**
     * Init Profile Names (for Profile Editor)
     */
    public abstract void initProfileNames();


    /**
     * Gets the profile names
     * 
     * @return the profile names
     */
    public String[] getProfileNames()
    {
        return this.profile_names;
    }

}
