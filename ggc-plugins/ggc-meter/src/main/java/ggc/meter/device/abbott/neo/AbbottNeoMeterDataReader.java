package ggc.meter.device.abbott.neo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.meter.defs.device.MeterDeviceDefinition;
import ggc.meter.device.abbott.AbbottNeoMeterHandler;
import ggc.meter.device.abbott.neo.data.NeoDataConverter;
import ggc.meter.device.abbott.neo.enums.NeoTextCommand;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.abbott.hid.AbbottHidDataReader;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.output.OutputWriter;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Meter Tool (support for Meter devices)
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 *
 *  Filename:     LibraCGMSDataReader
 *  Description:  Data reader for Arkray family
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class AbbottNeoMeterDataReader extends AbbottHidDataReader
{

    private static final Logger LOG = LoggerFactory.getLogger(AbbottNeoMeterDataReader.class);

    //private final NeoDataConverter converter;
    private MeterDeviceDefinition deviceDefinition;
    //private BitUtils bitUtils;

    //String serialNumber;
    //private GregorianCalendar currentDate;
    //String softwareVersion;
    //OutputWriter outputWriter;


    public AbbottNeoMeterDataReader(AbbottNeoMeterHandler abbottNeoMeterHandler, DeviceDefinition definition,
            String portName, OutputWriter outputWriter) throws PlugInBaseException
    {
        super(abbottNeoMeterHandler, definition, portName, outputWriter);
    }


    @Override
    protected void initReader()
    {
        converter = new NeoDataConverter(outputWriter);
    }

    

    @Override
    protected void readDataDeviceSpecific() throws PlugInBaseException
    {
//        this.readHistory();
//        addToStaticProgress(40);
//
//        this.readOtherHistory();
//        addToStaticProgress(40);
    }

    @Override
    protected void readConfigurationDeviceSpecific() throws PlugInBaseException
    {
        converter.convertData(NeoTextCommand.SerialNumber, serialNumber);
        converter.convertData(NeoTextCommand.SoftwareVersion, softwareVersion);

        //this.readDateTime();

        writeReport(NeoTextCommand.Test);
        String date = readTextResponse();
        System.out.println("Data: " + date);

//        writeReport(NeoTextCommand.Result);
//        readMultiPacket(NeoTextCommand.Result, false);


        //

// key=Result, value=6,7,11,23,17,20,8,1,11,23,17,20,10
// key=Result, value=7,5,11,23,17,20,4,1,67,0,1,0,1,27,1,0,0,0,2

//        readAndConvertData(NeoTextCommand.NtSound);
//        addToStaticProgress(20);
//        readAndConvertData(NeoTextCommand.BtSound);
//        addToStaticProgress(20);

        // Glucose Unit
        // Bg Range
    }


    


    


    

    

    


    

//    public DataAccessCGMS getDataAccess()
//    {
//        return DataAccessCGMS.getInstance();
//    }
//
//
//    public CGMSDeviceDefinition getDeviceDefinition()
//    {
//        return deviceDefinition;
//    }


    @Override
    public void initHandler()
    {

    }


    
//
//    private void readDateTime() throws PlugInBaseException
//    {
//        currentDate = new GregorianCalendar();
//        writeReport(NeoTextCommand.Date);
//        String date = readTextResponse();
//        addToStaticProgress(20);
//        writeReport(NeoTextCommand.Time);
//        String time = readTextResponse();
//        addToStaticProgress(20);
//
//        converter.convertData(NeoTextCommand.Date, date + "," + time);
//
//        converter.convertData(NeoTextCommand.ComputerDateTime,
//            ATechDate.getATechDateFromGC(currentDate, ATechDateType.DateAndTimeSec).toString());
//    }


    private void readAndConvertData(NeoTextCommand textCommand) throws PlugInBaseException
    {
        writeReport(textCommand);
        converter.convertData(textCommand, readTextResponse());
    }









}
