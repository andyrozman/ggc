package ggc.core.data.graph.v2;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import com.atech.graphics.graphs.v2.data.GraphDefinitionDto;
import com.atech.graphics.graphs.v2.viewer.GraphViewerWithControler;

import ggc.core.data.cfg.ConfigurationManagerWrapper;
import ggc.core.data.defs.GlucoseUnitType;
import ggc.core.util.DataAccess;

/**
 * Created by andy on 19.12.15.
 */
public class GGCGraphViewerV2 extends GraphViewerWithControler
{

    private static final long serialVersionUID = -8988443563957411350L;
    private final ConfigurationManagerWrapper configurationManagerWrapper;

    GlucoseUnitType glucoseUnitType;
    DataAccess dataAccessLocal;


    public GGCGraphViewerV2(JFrame dfra, DataAccess dataAccess, GraphDefinitionDto defintionDto)
    {
        super(dfra, dataAccess, defintionDto, false);

        glucoseUnitType = dataAccess.getGlucoseUnitType();

        dataAccessLocal = dataAccess;

        configurationManagerWrapper = dataAccessLocal.getConfigurationManagerWrapper();

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
        if (this.graphDefintionDto.getValueType() == GGCGraphValueType.BloodGlucose_mmol_L)
        {
            return dataAccessLocal.getDisplayedBG((float) value);
        }

        return value;
    }

}
