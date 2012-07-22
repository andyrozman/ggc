package ggc.plugin.device.impl.minimed.cmd;

import ggc.plugin.device.PlugInBaseException;


public class MinimedCommandHistoryCGMS extends MinimedCommandHistoryData
{
    void setupCommandParameters(int i)
    {
        /*
        historyPage.command_parameters_count = 4;
        historyPage.command_parameters[0] = utils.getHexUtils().getLowNibble(i >> 24);
        historyPage.command_parameters[1] = MedicalDevice.Util.getLowByte(i >> 16);
        historyPage.command_parameters[2] = MedicalDevice.Util.getLowByte(i >> 8);
        historyPage.command_parameters[3] = MedicalDevice.Util.getLowByte(i);
        */
        // Util.getLowByte  --> getLOwNibble ????????????????????!!!!!!!!
    }

    void executeSumCommand()
        throws PlugInBaseException
    {
        /*
        IntRange intrange = (IntRange)m_extraObject;
        execute(intrange.getMaximum(), intrange.getMinimum());*/
    }

    

    public MinimedCommandHistoryCGMS(int i, String s, int j, int k)
    {
        super(i, s, j, k);
    }

    public MinimedCommandHistoryCGMS(int i)
    {
        super(i);
    }

}
