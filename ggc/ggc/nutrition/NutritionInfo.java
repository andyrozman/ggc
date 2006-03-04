/**
 * A 1.4 application that requires the following additional files:
 *   TreeDemoHelp.html
 *    arnold.html
 *    bloch.html
 *    chan.html
 *    jls.html
 *    swingtutorial.html
 *    tutorial.html
 *    tutorialcont.html
 *    vm.html
 */
package ggc.nutrition;
 

public class NutritionInfo 
{
    
    int m_id;
    int m_type;
    String m_name;
    String m_name_int;


    public NutritionInfo(int id, int type, String name)
    {
        this.m_id = id;
        this.m_type = type;
        this.m_name = name;
    }


    public NutritionInfo(int id, int type, String name, String name_int)
    {
        this.m_id = id;
        this.m_type = type;
        this.m_name = name;
        this.m_name_int = name_int;
    }


    public String toString() 
    {
        return m_name;
    }


}
