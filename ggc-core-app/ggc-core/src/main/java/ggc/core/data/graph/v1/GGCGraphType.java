package ggc.core.data.graph.v1;

/**
 * Created by andy on 26.01.16.
 */
public enum GGCGraphType
{
    // Pen Mode Graphs
    Course("COURSE_GRAPH", "GGC_BG_Graph_Course"), //
    Spread("SPREAD_GRAPH", "GGC_BG_Graph_Spread"), //
    Frequency("FREQUENCY_GRAPH", "GGC_BG_Graph_Frequency"), //
    HbA1c, //
    ;

    private String title = "undefined";
    private String helpId = "undefined";


    GGCGraphType()
    {
    }


    GGCGraphType(String title, String helpId)
    {
        this.title = title;
        this.helpId = helpId;
    }


    public String getTitle()
    {
        return title;
    }


    public String getHelpId()
    {
        return helpId;
    }
}
