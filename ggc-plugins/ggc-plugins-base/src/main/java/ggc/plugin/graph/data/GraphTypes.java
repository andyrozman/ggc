package ggc.plugin.graph.data;

import java.util.Vector;

public class GraphTypes
{

    Vector<String> graph_types = null;

    public GraphTypes()
    {
        init();
    }

    public void init()
    {
        graph_types = new Vector<String>();
        graph_types.add("Line Graph");
    }

    public Vector<String> getGraphTypesVector()
    {
        return graph_types;
    }

}
