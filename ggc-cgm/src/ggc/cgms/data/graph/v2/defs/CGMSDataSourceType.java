package ggc.cgms.data.graph.v2.defs;

import com.atech.graphics.graphs.v2.defs.GraphDataSourceType;

/**
 * Created by andy on 28.05.15.
 */
public enum CGMSDataSourceType implements GraphDataSourceType
{
    CGMSData;

    public String getKey()
    {
        return this.name();
    }
}
