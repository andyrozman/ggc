package ggc.connect.software.local.diasend.util;

import ggc.plugin.output.OutputWriter;

public class DiaSendUtil
{

    static OutputWriter outputWriter = null;


    public static OutputWriter getOutputWriter()
    {
        return outputWriter;
    }


    public static void setOutputWriter(OutputWriter outputWriterIn)
    {
        outputWriter = outputWriterIn;
    }


} 