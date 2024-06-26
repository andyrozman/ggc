package ggc.plugin.util;

import com.atech.i18n.I18nControlLangMgrDual;
import com.atech.i18n.I18nControlRunner;
import com.atech.i18n.mgr.LanguageManager;
import ggc.core.plugins.GGCPluginType;
import ggc.core.util.GGCI18nControlContext;

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

public class I18nControlPlugin extends I18nControlLangMgrDual
{

    protected I18nControlLangMgrDual m_ic_core = null;

    /**
     * Constructor
     * 
     * @param lm
     * @param icr
     */
    public I18nControlPlugin(LanguageManager lm, I18nControlRunner icr, GGCPluginType pluginType)
    {
        super(lm, icr);

        loadLanguageToContext(pluginType);
    }


    public void loadLanguageToContext(GGCPluginType pluginType)
    {
        GGCI18nControlContext ctx = GGCI18nControlContext.getInstance();

        if (!ctx.isSelectedLanguageDefaultLanguage())
        {
            ctx.put(pluginType.getKey() + "_" + ctx.getDefaultLanguage(), this.getDefaultLanguageInstance());
        }

        ctx.put(pluginType.getKey() + "_" + ctx.getSelectedLanguage(), this);

        loadPluginI18n();
    }



    @Override
    public synchronized String getMessageFromCatalog(String msg)
    {
        if (!checkIfValidMessageKey(msg))
            return msg;

        String ret = this.getMessageFromCatalogSelectedLanaguge(msg);

        if (ret.equals(msg))
        {
            if (this.m_ic_core == null)
            {
                this.loadPluginI18n();
            }

            ret = this.m_ic_core.getMessageFromCatalogSelectedLanaguge(msg);

            if (ret.equals(msg))
            {
                if (this.language_manager.findUntraslatedKeys())
                    return ret;

                ret = this.getMessageFromCatalogDefaultLangauge(msg);

                if (ret.equals(msg))
                {
                    ret = this.m_ic_core.getMessageFromCatalogDefaultLangauge(msg);
                }
            }
        }

        return ret;
    }

    @Override
    public String getMessage(String key)
    {
        String tmp = super.getMessage(key);

        if (tmp.equals(key)) // && (this.m_ic_core!=null))
        {
            if (this.m_ic_core == null)
            {
                this.loadPluginI18n();
            }

            tmp = this.m_ic_core.getMessage(key);
        }

        return tmp;
    }

    @Override
    public void initAdditional()
    {
        loadPluginI18n();
    }

    /**
     * Load PlugIn I18n
     */
    public void loadPluginI18n()
    {
        // System.out.println("Creating i18nControlAbstract core");
        this.m_ic_core = new I18nControlLangMgrDual(this.language_manager, new GGCPluginICRunner());

        GGCI18nControlContext ctx = GGCI18nControlContext.getInstance();

        if (!ctx.isGGCPluginTypeLoaded(GGCPluginType.PluginBase))
        {
            if (!ctx.isSelectedLanguageDefaultLanguage())
            {
                ctx.put(GGCPluginType.PluginBase.getKey() + "_" + ctx.getDefaultLanguage(), this.m_ic_core.getDefaultLanguageInstance());
            }

            ctx.put(GGCPluginType.PluginBase.getKey() + "_" + ctx.getSelectedLanguage(), this.m_ic_core);
        }
    }

}
