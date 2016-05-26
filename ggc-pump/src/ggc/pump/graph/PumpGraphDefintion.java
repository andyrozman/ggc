package ggc.pump.graph;

import java.awt.*;
import java.util.Map;

import javax.swing.*;

import com.atech.graphics.graphs.GraphViewer;
import com.atech.graphics.graphs.v2.data.GraphDefinitionDto;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;

import ggc.core.data.defs.GlucoseUnitType;
import ggc.core.data.graph.v1.gui.HbA1cDialog;
import ggc.core.data.graph.v1.view.GraphViewAbstract;
import ggc.core.data.graph.v1.view.GraphViewCourse;
import ggc.core.data.graph.v1.view.GraphViewFrequency;
import ggc.core.data.graph.v1.view.GraphViewSpread;
import ggc.core.util.DataAccess;
import ggc.plugin.DevicePlugInServer;
import ggc.plugin.graph.GGCPluginGraphViewerV2;
import ggc.plugin.graph.PluginGraphDefinition;
import ggc.pump.data.graph.v1.GraphV1DbRetrieverPump;
import ggc.pump.util.DataAccessPump;

/**
 * Created by andy on 19.12.15.
 */
public class PumpGraphDefintion implements PluginGraphDefinition
{

    DataAccess dataAccessCore;
    DataAccessPump dataAccessPump;
    I18nControlAbstract i18nControl;
    DevicePlugInServer pluginServer;
    Map<String, GraphDefinitionDto> graphDefintions;
    GraphV1DbRetrieverPump graphV1DbRetrieverPump;


    public PumpGraphDefintion(DataAccessPump dataAccessPump)
    {
        this.dataAccessPump = dataAccessPump;
        this.i18nControl = dataAccessPump.getI18nControlInstance();

        graphV1DbRetrieverPump = new GraphV1DbRetrieverPump();

        createGraphDefintions();
    }


    public void createGraphDefintions()
    {
    }


    // private GraphDefinitionDto createGraphDefinition(String title,
    // DataRangeValue rangeValue, PlotType plotType,
    // GGCGraphValueType graphValueType)
    // {
    // GraphDefinitionDto definitionDto = new GraphDefinitionDto(title,
    // DataRangeMainType.RangeSelectable, rangeValue,
    // new Dimension(640, 480), plotType, graphValueType, 0, 400, 20, null,
    // "CGMSTool_Graphs");
    //
    // definitionDto.addMarker(Color.red, 240);
    // definitionDto.addMarker(Color.magenta, 80);
    //
    // return definitionDto;
    // }

    public JMenu[] getPlugInGraphMenus(DevicePlugInServer pluginServer)
    {
        this.pluginServer = pluginServer;

        JMenu menuGraphPump = ATSwingUtils.createMenu("MN_PUMP", "MN_PUMP_GRAPH_DESC", this.i18nControl);

        // this.addMenuItem(menuGraphPump, "MN_COURSE", "pump_graph_course",
        // "line-chart.png");
        // this.addMenuItem(menuGraphPump, "MN_SPREAD", "pump_graph_spread",
        // "dot-chart.png");
        // this.addMenuItem(menuGraphPump, "MN_FREQUENCY", "pump_graph_freq",
        // "column-chart.png");
        this.addMenuItem(menuGraphPump, "MN_HBA1C", "pump_graph_hba1c", "pie-chart.png");

        JMenu[] mns = new JMenu[1];
        mns[0] = menuGraphPump;

        return mns;
    }


    public void startPlugInGraphMenuAction(String actionCommand)
    {

        if (actionCommand.equals("pump_graph_course"))
        {
            startGraphViewer(new GraphViewCourse(null, graphV1DbRetrieverPump));
        }
        else if (actionCommand.equals("pump_graph_spread"))
        {
            startGraphViewer(new GraphViewSpread(null, graphV1DbRetrieverPump));
        }
        else if (actionCommand.equals("pump_graph_freq"))
        {
            startGraphViewer(new GraphViewFrequency(null, graphV1DbRetrieverPump));
        }
        else if (actionCommand.equals("pump_graph_hba1c"))
        {
            new HbA1cDialog(DataAccess.getInstance(), graphV1DbRetrieverPump);
        }
        else if (this.graphDefintions.containsKey(actionCommand))
        {
            String actionCommandS = actionCommand;

            if (dataAccessPump.getGlucoseUnitType() == GlucoseUnitType.mg_dL)
            {
                actionCommandS += "_mgdl";
            }
            else
            {
                actionCommandS += "_mmoll";
            }

            GGCPluginGraphViewerV2 viewerV2 = new GGCPluginGraphViewerV2((JFrame) this.pluginServer.getParent(),
                    dataAccessPump, graphDefintions.get(actionCommandS));

        }
        else
        {
            System.out.println("PumpPlugInServer::Unknown Graph Command: " + actionCommand);
        }
    }


    private void startGraphViewer(GraphViewAbstract graphViewAbstract)
    {
        Dimension size = dataAccessCore.getConfigurationManagerWrapper().getGraphViewerSize();

        graphViewAbstract.setInitialSize(size);

        GraphViewer graphViewer = new GraphViewer(graphViewAbstract, dataAccessCore,
                (JFrame) this.pluginServer.getParent(), true);

        size = graphViewer.getSize();

        dataAccessCore.getConfigurationManagerWrapper().setGraphViewerSize(size);
    }


    private void addMenuItem(JMenu menuReports, String name, String actionCommand, String imageFilename)
    {
        ATSwingUtils.createMenuItem(menuReports, name, name + "_DESC", actionCommand, pluginServer,
            imageFilename == null ? "print.png" : imageFilename, i18nControl, dataAccessPump, pluginServer.getParent());
    }
}
