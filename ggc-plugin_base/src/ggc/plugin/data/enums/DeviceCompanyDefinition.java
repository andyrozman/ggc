package ggc.plugin.data.enums;

import ggc.plugin.manager.DeviceImplementationStatus;

/**
 * Created by andy on 22.01.15.
 */
public enum DeviceCompanyDefinition
{

    Minimed(1, "Minimed", "", DeviceImplementationStatus.IMPLEMENTATION_PLANNED),

    Roche(2),

    Disetronic(2),

    Animas(3),

    Deltec(5),

    Insulet(6),

    Sooil(7),


    // Meter

    Ascensia(1)

    ;

    int id;
    String companyName;
    String companyDescription;
    DeviceImplementationStatus companyImplementationStatus;

    private DeviceCompanyDefinition(int code)
    {

    }


    private DeviceCompanyDefinition(int id, String name, String description, DeviceImplementationStatus implementationStatus)
    {
        this.id = id;
        this.companyName = name;
        this.companyDescription = description;
        this.companyImplementationStatus = implementationStatus;
    }

    public String getName()
    {
        return this.companyName;
    }

    public int getCompanyId()
    {
        return this.id;
    }

    public String getDescription()
    {
        return this.companyDescription;
    }

    public DeviceImplementationStatus getImplementationStatus()
    {
        return this.companyImplementationStatus;
    }

}
