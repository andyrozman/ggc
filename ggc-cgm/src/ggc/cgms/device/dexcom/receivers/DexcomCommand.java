package ggc.cgms.device.dexcom.receivers;

import ggc.cgms.device.dexcom.receivers.data.CommandParameter;

public interface DexcomCommand
{

    int getCommandId();

    // int getCommandParameterSize();

    CommandParameter getCommandParameter();

    int getExpectedResponseLength();

}
