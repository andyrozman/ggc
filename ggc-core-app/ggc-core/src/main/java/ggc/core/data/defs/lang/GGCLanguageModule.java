package ggc.core.data.defs.lang;

import java.util.ArrayList;
import java.util.List;

import com.atech.i18n.info.LanguageModule;

/**
 * Created by andy on 30/06/17.
 */
public enum GGCLanguageModule implements LanguageModule
{

    Core("GGC", "GGC_CORE", "GGC Core", ""), //
    PluginBase("GGCPlugin", "GGC_PLUGIN", "GGC Plugin Base", ""), //
    MeterPlugin("GGCMeterTool", "GGC_METER", "GGC Meter Plugin", ""), //
    PumpPlugin("GGCPumpTool", "GGC_PUMP", "GGC Pump Plugin", ""), //
    NutriPlugin("GGC_Nutrition", "GGC_NUTRI", "GGC Nutrition Plugin", ""), //
    CGMSPlugin("GGC_CGMSTool", "GGC_CGMS", "GGC CGMS Plugin", ""), //

    ;

    private static List<LanguageModule> moduleList;

    String rootFileName;
    String moduleId;
    String moduleDescription;
    String path;


    GGCLanguageModule(String rootFileName, String moduleId, String moduleDescription, String path)
    {
        this.rootFileName = rootFileName;
        this.moduleId = moduleId;
        this.moduleDescription = moduleDescription;
        this.path = path;
    }


    public String getLanguageFilePath()
    {
        return this.path;
    }


    public String getLanguageFileRoot()
    {
        return this.rootFileName;
    }


    public String getModuleDescription()
    {
        return this.moduleDescription;
    }


    public String getModuleId()
    {
        return this.moduleId;
    }


    public List<LanguageModule> getAllModules()
    {
        if (moduleList == null)
        {
            List<LanguageModule> outList = new ArrayList<LanguageModule>();

            for (GGCLanguageModule module : values())
            {
                outList.add(module);
            }

            moduleList = outList;
        }

        return moduleList;
    }


    public LanguageModule getMainModule()
    {
        return Core;
    }


    public String toString()
    {
        return this.moduleDescription;
    }

}
