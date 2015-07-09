package ggc.plugin.data.enums;

import ggc.plugin.manager.DeviceImplementationStatus;

/**
 * Created by andy on 22.01.15.
 */
public enum DeviceCompanyDefinition
{

    // 2xx = Pump

    // 4xx = Pump + CGMS

    Roche(2, "Roche", "", DeviceImplementationStatus.Partitial), // Disetronic(2),

    Deltec(5),

    Insulet(206, "Insulet", "", DeviceImplementationStatus.InProgress),

    Sooil(7),

    // 1xx = Meter
    Ascensia(1),

    // 3xx = CGMS
    Dexcom(301, "Dexcom", "", DeviceImplementationStatus.Partitial),

    // 4xx = Pump + CGMS
    Animas(401, "Animas", "", DeviceImplementationStatus.Testing), Minimed(402, "Minimed", "",
            DeviceImplementationStatus.Planned),

    ;

    int id;
    String companyName;
    String companyDescription;
    DeviceImplementationStatus companyImplementationStatus;


    private DeviceCompanyDefinition(int code)
    {

    }


    private DeviceCompanyDefinition(int id, String name, String description,
            DeviceImplementationStatus implementationStatus)
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
