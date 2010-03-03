package ggc.plugin.util;

import com.atech.i18n.I18nControlLangMgr;
import com.atech.i18n.I18nControlRunner;
import com.atech.i18n.mgr.LanguageManager;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
 *
 *  See AUTHORS for copyright information.
 * 
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 * 
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 *  Filename:     I18nControlPlugin
 *  Description:  
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class I18nControlPlugin extends I18nControlLangMgr
{
    
    protected I18nControlLangMgr m_ic_core = null;
    
    
    /**
     * Constructor
     * 
     * @param lm
     * @param icr
     */
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
    
    
    /**
     * Load PlugIn I18n
     */
    public void loadPluginI18n()
    {
        //System.out.println("Creating ic core");
        this.m_ic_core = new I18nControlLangMgr(this.language_manager, new GGCPluginICRunner());
        //System.out.println("Creating ic core: " + this.m_ic_core);
    }
    

    
    
}
