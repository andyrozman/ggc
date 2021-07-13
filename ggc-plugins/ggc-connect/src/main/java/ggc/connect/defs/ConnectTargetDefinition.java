package ggc.connect.defs;

import ggc.plugin.data.enums.DeviceCompanyDefinition;
import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.data.enums.DeviceProgressStatus;
import ggc.plugin.manager.DeviceImplementationStatus;

/**
 * Created by andy on 10/03/18.
 */
public enum ConnectTargetDefinition {

    DiaSend, //
    NightScout, //

    ;


    int deviceId;
    String deviceName;
    String iconName;
    String instructions;
    Object internalDefintion;
    DeviceImplementationStatus implementationStatus;
    DeviceCompanyDefinition companyDefinition;
    DeviceHandlerType deviceHandlerType = DeviceHandlerType.NullHandler;
    DeviceProgressStatus deviceProgressStatus;
    String specialComment;


    @Deprecated
    ConnectTargetDefinition()
    {

    }


    ConnectTargetDefinition(int id, String name, String iconName, String instructions, Object internalDefinition,
                         DeviceImplementationStatus implementationStatus, DeviceCompanyDefinition companyDefinition,
                         DeviceHandlerType deviceHandlerType, DeviceProgressStatus progressStatus,
                          String specialComment)
    {
        this.deviceId = id;
        this.deviceName = name;
        this.iconName = iconName;
        this.instructions = instructions;
        this.internalDefintion = internalDefinition;
        this.implementationStatus = implementationStatus;
        this.companyDefinition = companyDefinition;
        this.deviceHandlerType = deviceHandlerType;

        this.deviceProgressStatus = progressStatus;
        this.specialComment = specialComment;

    }




} 