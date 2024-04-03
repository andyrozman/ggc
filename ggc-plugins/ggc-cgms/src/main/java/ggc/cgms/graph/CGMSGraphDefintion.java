package ggc.cgms.graph;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import com.atech.graphics.graphs.v2.data.GraphDefinitionDto;
import com.atech.graphics.graphs.v2.defs.DataRangeMainType;
import com.atech.graphics.graphs.v2.defs.DataRangeValue;
import com.atech.graphics.graphs.v2.defs.PlotType;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;

import ggc.cgms.util.DataAccessCGMS;
import ggc.core.data.defs.GlucoseUnitType;
import ggc.core.data.graph.v2.GGCGraphValueType;
import ggc.plugin.DevicePlugInServer;
import ggc.plugin.graph.GGCPluginGraphViewerV2;
import ggc.plugin.graph.PluginGraphDefinition;

/**
 * Created by andy on 19.12.15.
 */
public class CGMSGraphDefintion implements PluginGraphDefinition
{

    DataAccessCGMS dataAccessCGMS;
    I18nControlAbstract i18nControl;
    DevicePlugInServer pluginServer;
    Map<String, GraphDefinitionDto> graphDefintions;


    public CGMSGraphDefintion(DataAccessCGMS dataAccessCGMS)
    {
        this.dataAccessCGMS = dataAccessCGMS;
        this.i18nControl = dataAccessCGMS.getI18nControlInstance();

        createGraphDefintions();
    }


    public void createGraphDefintions()
    {
        // GraphDefinitionDto definitionDto = new
        // GraphDefinitionDto("CGMS_DAILY_GRAPH",
        // DataRangeMainType.RangeSelectable,
        // DataRangeValue.Day, new Dimension(640, 480), PlotType.ScatterTime,
        // GGCGraphValueType.BG, 0, 400);
        //
        // definitionDto.addMarker(Color.red, 240);
        // definitionDto.addMarker(Color.magenta, 80);

        this.graphDefintions = new HashMap<String, GraphDefinitionDto>();

        graphDefintions.put("cgms_graph_daily", null);
        graphDefintions.put("cgms_graph_daily_mgdl", createGraphDefinition("CGMS_DAILY_GRAPH", //
            DataRangeValue.Day, PlotType.ScatterTime, GGCGraphValueType.BloodGlucose_mg_dL));

        graphDefintions.put("cgms_graph_daily_mmoll", createGraphDefinition("CGMS_DAILY_GRAPH", //
            DataRangeValue.Day, PlotType.ScatterTime, GGCGraphValueType.BloodGlucose_mmol_L));

        // definitionDto = new GraphDefinitionDto("CGMS_WEEKLY_GRAPH",
        // DataRangeMainType.RangeSelectable,
        // DataRangeValue.Week, new Dimension(640, 480), PlotType.ScatterTime,
        // GGCGraphValueType.BG, 0, 400);
        //
        // definitionDto.addMarker(Color.red, 240);
        // definitionDto.addMarker(Color.magenta, 80);

        // graphDefintions.put("cgms_graph_weekly", definitionDto);

        graphDefintions.put("cgms_graph_weekly", null);
        graphDefintions.put("cgms_graph_weekly_mgdl", createGraphDefinition("CGMS_WEEKLY_GRAPH", //
            DataRangeValue.Week, PlotType.ScatterTime, GGCGraphValueType.BloodGlucose_mg_dL));

        graphDefintions.put("cgms_graph_weekly_mmoll", createGraphDefinition("CGMS_WEEKLY_GRAPH", //
            DataRangeValue.Week, PlotType.ScatterTime, GGCGraphValueType.BloodGlucose_mmol_L));

    }


    private GraphDefinitionDto createGraphDefinition(String title, DataRangeValue rangeValue, PlotType plotType,
            GGCGraphValueType graphValueType)
    {
        GraphDefinitionDto definitionDto = new GraphDefinitionDto(title, DataRangeMainType.RangeSelectable, rangeValue,
                new Dimension(640, 480), plotType, graphValueType, 0, 400, 20, null, "CGMSTool_Graphs");

        definitionDto.addMarker(Color.red, 240);
        definitionDto.addMarker(Color.magenta, 80);

        return definitionDto;
    }


    public JMenu[] getPlugInGraphMenus(DevicePlugInServer pluginServer)
    {
        this.pluginServer = pluginServer;

        JMenu menuGraphCgms = ATSwingUtils.createMenu("MN_CGMS", "MN_CGMS_GRAPH_DESC", this.i18nControl);

        addMenuItem(menuGraphCgms, "CGMS_DAILY_GRAPH", "cgms_graph_daily");
        addMenuItem(menuGraphCgms, "CGMS_WEEKLY_GRAPH", "cgms_graph_weekly");

        JMenu[] mns = new JMenu[1];
        mns[0] = menuGraphCgms;

        return mns;
    }


    public void startPlugInGraphMenuAction(String actionCommand)
    {
        if (this.graphDefintions.containsKey(actionCommand))
        {
            String actionCommandS = actionCommand;

            if (dataAccessCGMS.getGlucoseUnitType() == GlucoseUnitType.mg_dL)
            {
                actionCommandS += "_mgdl";
            }
            else
            {
                actionCommandS += "_mmoll";
            }

            GGCPluginGraphViewerV2 viewerV2 = new GGCPluginGraphViewerV2((JFrame) this.pluginServer.getParent(),
                    DataAccessCGMS.getInstance(), graphDefintions.get(actionCommandS));

        }
        else
        {
            System.out.println("CGMSPlugInServer::Unknown Graph Command: " + actionCommand);
        }
    }


    private void addMenuItem(JMenu menuReports, String name, String actionCommand)
    {
        ATSwingUtils.createMenuItem(menuReports, name, name + "_DESC", actionCommand, pluginServer, "print.png",
            i18nControl, dataAccessCGMS, pluginServer.getParent());
    }
}
