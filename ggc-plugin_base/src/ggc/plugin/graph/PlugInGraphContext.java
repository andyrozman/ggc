package ggc.plugin.graph;

public abstract class PlugInGraphContext
{

    
    public PlugInGraphContext()
    {
        this.loadMappings();
    }
    
    
    public abstract void loadMappings();
    
    
    public abstract int getObjectMapping(int object_base_type, int type);

    
}
