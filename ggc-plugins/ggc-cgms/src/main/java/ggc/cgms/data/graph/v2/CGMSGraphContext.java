package ggc.cgms.data.graph.v2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.atech.graphics.graphs.v2.data.GraphContext;
import com.atech.graphics.graphs.v2.defs.GraphDataSourceTarget;
import com.atech.graphics.graphs.v2.defs.GraphDataSourceType;
import ggc.cgms.data.graph.v2.defs.CGMSDataSourceType;

/**
 * Created by andy on 30.04.15.
 */
public class CGMSGraphContext implements GraphContext
{

    List<GraphDataSourceType> dataSources;


    public CGMSGraphContext()
    {
        dataSources = new ArrayList<GraphDataSourceType>();
        dataSources.add(CGMSDataSourceType.CGMSData);
    }


    public List<GraphDataSourceType> getDataSources()
    {
        return dataSources;
    }


    public HashMap<GraphDataSourceType, GraphDataSourceTarget> getDataSourceTargets()
    {
        return null;
    }
}
