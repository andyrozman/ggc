package ggc.pump.manager.company;

import ggc.plugin.manager.company.AbstractDeviceCompany;


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
     * @param ic the ic
     * @param value the value
     */
    public AbstractPumpDeviceCompany(boolean value)
    {
        super(value);
    }
    
    
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


