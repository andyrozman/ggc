package ggc.plugin.device.v2;

import ggc.plugin.data.enums.DeviceCompanyDefinition;
import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.data.enums.DevicePortParameterType;
import ggc.plugin.data.enums.DeviceProgressStatus;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.protocol.DeviceConnectionProtocol;

/**
 * Created by andy on 06.02.15.
 */
public interface DeviceDefinition
{

    /**
     * Name of entry
     *
     * @return name of device
     */
    String name();


    /**
     * Get Device Id.
     *
     * @return device Id
     */
    int getDeviceId();


    /**
     * Get Device Name.
     *
     * @return
     */
    String getDeviceName();


    /**
     * Get Icon Name for Device
     *
     * @return
     */
    String getIconName();


    /**
     * Get Instructions I18n Key
     *
     * @return
     */
    String getInstructionsI18nKey();


    /**
     * Get Device Implementation status
     *
     * @return
     */
    DeviceImplementationStatus getDeviceImplementationStatus();


    /**
     * Get Internal Definition (some devices have Internal definitions, which can in turn provide special data).
     *
     * @return
     */
    Object getInternalDefintion();


    /**
     * Get Device Company
     *
     * @return
     */
    DeviceCompanyDefinition getDeviceCompany();


    /**
     * Get Device handler key
     * @return
     */
    DeviceHandlerType getDeviceHandlerKey();


    /**
     * Device Port Parameter Type
     * @return
     */
    DevicePortParameterType getDevicePortParameterType();


    /**
     * Get Connection Protocol
     *
     * @return
     */
    DeviceConnectionProtocol getConnectionProtocol();


    /**
     * Get Device Progress Status. It determines how device progress is determined. In most casess we use Special
     * progress which is then implemented by Handler.
     *
     * @return
     */
    DeviceProgressStatus getDeviceProgressStatus();


    String getSpecialComment();

}
