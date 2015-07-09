package ggc.cgms.data.graph.v2.defs;

import com.atech.graphics.graphs.v2.defs.GraphDataSourceTarget;
import com.atech.graphics.graphs.v2.defs.GraphDataSourceType;

/**
 * Created by andy on 28.05.15.
 */
public enum CGMSDataSourceTargetType implements GraphDataSourceTarget
{
    BGReading(CGMSDataSourceType.CGMSData), //
    BGCalibration(CGMSDataSourceType.CGMSData), //

    ;

    GraphDataSourceType dataSource;


    private CGMSDataSourceTargetType(GraphDataSourceType dataSource)
    {
        this.dataSource = dataSource;
    }


    public String getKey()
    {
        return this.name();
    }


    public GraphDataSourceType getDataSource()
    {
        return this.dataSource;
    }
}
