package ggc.plugin.data.enums;

/**
 * Created by andy on 22.01.15.
 */
public enum DeviceCompanyDefinition
{

    Minimed(1, "Minimed", "", 0),

    Roche(2),

    Disetronic(2),

    Animas(3),

    Deltec(5),

    Insulet(6),

    Sooil(7),

    ;


//    String getName();
//
//    int getCompanyId();
//
//    String getDescription();
//
//    int getImplementationStatus();

    private DeviceCompanyDefinition(int code)
    {

    }


    private DeviceCompanyDefinition(int id, String name, String description, int implementationStatus)
    {

    }


}
