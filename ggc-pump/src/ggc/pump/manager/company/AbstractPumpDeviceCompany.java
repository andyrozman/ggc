package ggc.pump.manager.company;

import ggc.plugin.manager.company.AbstractDeviceCompany;

import com.atech.i18n.I18nControlAbstract;


public abstract class AbstractPumpDeviceCompany extends AbstractDeviceCompany
{
    
    protected String[] profile_names;
    
    
    public AbstractPumpDeviceCompany(I18nControlAbstract ic, boolean value)
    {
        super(ic, value);
    }
    
    
    public String[] getProfileNames()
    {
        return this.profile_names;
    }
    
    
    
}


