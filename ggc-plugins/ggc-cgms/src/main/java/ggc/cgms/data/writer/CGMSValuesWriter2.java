package ggc.cgms.data.writer;

import ggc.cgms.data.defs.CGMSExtendedDataType;
import ggc.core.data.defs.ExerciseStrength;
import ggc.core.data.defs.Health;
import ggc.plugin.data.DeviceValuesWriter;
import ggc.plugin.output.OutputWriter;


/**
 *  Application: GGC - GNU Gluco Control
 *  Plug-in: CGMS Tool (support for CGMS devices)
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
 *  Filename: CGMSValuesWriter2
 *  Description: CGMS Values Writer 2 - this one is for extended table, where there is one database line for
 *       one entry (standard writer)
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class CGMSValuesWriter2 extends DeviceValuesWriter
{

    private static final long serialVersionUID = 7027755346944497780L;
    static CGMSValuesWriter2 staticInstance;


    private CGMSValuesWriter2()
    {
        super();
        loadDeviceDefinitions();
    }


    public static CGMSValuesWriter2 getInstance(OutputWriter writer)
    {
        if (staticInstance == null)
        {
            staticInstance = new CGMSValuesWriter2();
        }

        staticInstance.setOutputWriter(writer);

        return staticInstance;
    }


    private void loadDeviceDefinitions()
    {

        // ========= Additional (Extended) Data =========

        addConfiguration("AdditionalData_InsulinLongActing", new CGMSWriterValues(CGMSWriterValues.OBJECT_EXT, //
                CGMSExtendedDataType.InsulinLongActing, true));

        addConfiguration("AdditionalData_Carbs", new CGMSWriterValues(CGMSWriterValues.OBJECT_EXT, //
                CGMSExtendedDataType.Carbs, true));

        addConfiguration("AdditionalData_Ketones", new CGMSWriterValues(CGMSWriterValues.OBJECT_EXT, //
                CGMSExtendedDataType.Ketones, true));

        addConfiguration("AdditionalData_InsulinShortActing", new CGMSWriterValues(CGMSWriterValues.OBJECT_EXT, //
                CGMSExtendedDataType.InsulinShortActing, true));

        addConfiguration("AdditionalData_ManualReading", new CGMSWriterValues(CGMSWriterValues.OBJECT_EXT, //
                CGMSExtendedDataType.ManualReading, true));


        for (ExerciseStrength exerciseStrength : ExerciseStrength.getAllValues())
        {
            addConfiguration("AdditionalData_Exercise_" + exerciseStrength.name(), //
                    new CGMSWriterValues(CGMSWriterValues.OBJECT_EXT, //
                            CGMSExtendedDataType.Exercise, exerciseStrength, true));
        }

        for (Health health : Health.getAllValues())
        {
            addConfiguration("AdditionalData_Health_" + health.name(), //
                    new CGMSWriterValues(CGMSWriterValues.OBJECT_EXT, //
                            CGMSExtendedDataType.Health, health, false));
        }
    }


}
