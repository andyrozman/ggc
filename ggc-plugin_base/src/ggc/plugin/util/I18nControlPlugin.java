package ggc.plugin.util;

import com.atech.i18n.I18nControlLangMgr;
import com.atech.i18n.I18nControlRunner;
import com.atech.i18n.mgr.LanguageManager;

public class I18nControlPlugin extends I18nControlLangMgr
{
    
    protected I18nControlLangMgr m_ic_core = null;
    
    
    public I18nControlPlugin(LanguageManager lm, I18nControlRunner icr) //  I18nControlLangMgr ic_core)
    {
        super(lm, icr);
        
    }
    
    
    
    
    public String getMessage(String key)
    {
        String tmp = super.getMessage(key);
        
        if ((tmp.equals(key))) //&& (this.m_ic_core!=null))
        {
            if (this.m_ic_core==null)
                this.loadPluginI18n();
            
            tmp = this.m_ic_core.getMessage(key);
        }
        
        return tmp;
    }
    
    
    public void initAdditional()
    {
        loadPluginI18n();
    }
    
    
    public void loadPluginI18n()
    {
        //System.out.println("Creating ic core");
        this.m_ic_core = new I18nControlLangMgr(this.language_manager, new GGCPluginICRunner());
        //System.out.println("Creating ic core: " + this.m_ic_core);
    }
    

    
    
}
