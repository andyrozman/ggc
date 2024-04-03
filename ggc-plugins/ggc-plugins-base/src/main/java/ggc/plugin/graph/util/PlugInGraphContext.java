package ggc.plugin.graph.util;

import ggc.plugin.graph.data.GraphTypes;

public abstract class PlugInGraphContext
{
    GraphTypes graph_types = null;

    public PlugInGraphContext()
    {
        this.loadMappings();
        init();
    }

    public void init()
    {
        graph_types = new GraphTypes();
    }

    public abstract void loadMappings();

    public abstract int getObjectMapping(int object_base_type, int type);

    public GraphTypes getGraphTypes()
    {
        return this.graph_types;
    }

}
