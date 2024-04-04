package ggc.core.util;

import com.atech.i18n.mgr.LanguageManagerRunnerDual;

/**
 *  Application:   GGC - GNU Gluco Control
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
 *  Filename:     GGCLanguageInfo  
 *  Description:  Langauge Info for GGC, used for new Language framework 
 * 
 *  Author:  andyrozman {andy@atech-software.com}  
 */

public class GGCLanguageManagerRunner extends LanguageManagerRunnerDual
{

    //UserDataDirectory dataDirectory = UserDataDirectory.getInstance();

    /**
     * Constructor
    
     */
    public GGCLanguageManagerRunner()
    {
        super();
    }

    /**
     * Get Langauge Config File
     */
    @Override
    public String getLanguageConfigFile()
    {
        //return "%USER_DATA_DIR%/lang/GGC_Languages.properties";
        return "GGC_Languages.properties";
    }

    @Override
    public String getLanguageSelectionConfigFile()
    {
        return "%USER_DATA_DIR%/GGC_Config.properties";
    }

    @Override
    public String getTranslationToolConfigFile()
    {
        return "%USER_DATA_DIR%/tools/translation/TranslationGGC.config";
    }

    @Override
    public boolean isTranslationToolLanguageSupported() {
        return false;
    }

    @Override
    public String getDefaultLanguage()
    {
        return "en";
    }

    @Override
    public boolean findUntranslatedKeysInLanguage()
    {
        return true;
    }

    @Override
    public boolean isLanguageConfigStatic() {
        return true;
    }

}
