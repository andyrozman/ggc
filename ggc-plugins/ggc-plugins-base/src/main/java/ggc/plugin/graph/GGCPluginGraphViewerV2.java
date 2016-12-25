package ggc.plugin.graph;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import com.atech.graphics.graphs.v2.data.GraphDefinitionDto;
import com.atech.graphics.graphs.v2.viewer.GraphViewerWithControler;

import ggc.core.data.cfg.ConfigurationManagerWrapper;
import ggc.core.data.defs.GlucoseUnitType;
import ggc.core.util.DataAccess;
import ggc.plugin.util.DataAccessPlugInBase;

public class GGCPluginGraphViewerV2 extends GraphViewerWithControler
{

    private static final long serialVersionUID = -8988443563957411350L;

    GlucoseUnitType glucoseUnitType;
    DataAccessPlugInBase dataAccessLocal;
    ConfigurationManagerWrapper configurationManagerWrapper;


    public GGCPluginGraphViewerV2(JFrame dfra, DataAccessPlugInBase dataAccess, GraphDefinitionDto defintionDto)
    {
        super(dfra, dataAccess, defintionDto, false);

        glucoseUnitType = dataAccess.getGlucoseUnitType();

        dataAccessLocal = dataAccess;
        configurationManagerWrapper = DataAccess.getInstance().getConfigurationManagerWrapper();

        this.newSize = configurationManagerWrapper.getGraphViewerSize();

        this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                configurationManagerWrapper.setGraphViewerSize(getSize());
                configurationManagerWrapper.saveConfig();
            }
        });

        finishInit();
    }


    @Override
    protected double convertDisplayValue(double value)
    {
        if (glucoseUnitType == GlucoseUnitType.mmol_L)
        {
            return dataAccessLocal.getDisplayedBG((float) value);
        }

        return value;
    }

}
