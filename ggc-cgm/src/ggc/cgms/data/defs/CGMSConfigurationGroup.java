package ggc.cgms.data.defs;

import java.util.HashMap;
import java.util.Map;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.CodeEnumWithTranslation;

import ggc.cgms.util.DataAccessCGMS;
import ggc.plugin.data.enums.DeviceConfigurationGroup;

/**
 *  Application: GGC - GNU Gluco Control
 *  Plug-in: CGMS Tool (support for CGMS devices)
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
 *  Filename: CGMDataType
 *  Description: CGMS Data types, as used in database (undefined at this time)
 *
 *  Author: Andy {andy@atech-software.com}
 */

/**
 * Application: GGC - GNU Gluco Control
 * Plug-in: CGMS Tool (support for CGMS devices)
 *
 * See AUTHORS for copyright information.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Filename: CGMSConfigurationGroup
 * Description: CGMS Settings Configuration Groups
 *
 * Author: Andy {andy@atech-software.com}
 */

public enum CGMSConfigurationGroup implements DeviceConfigurationGroup
{
    General(1, "GROUP_GENERAL"), //
    Device(2, "GROUP_DEVICE"), //

    Transmiter(3, "GROUP_TRANSMITER"), // ????
    Warnings(4, "GROUP_WARNINGS"), //

    Sound(6, "GROUP_SOUND"), //

    Other(20, "GROUP_OTHER"), //

    ;

    static Map<String, CodeEnumWithTranslation> translationMapping = new HashMap<String, CodeEnumWithTranslation>();
    static Map<Integer, CGMSConfigurationGroup> codeMapping = new HashMap<Integer, CGMSConfigurationGroup>();

    int code;
    String i18nKey;
    String translation;

    static
    {
        I18nControlAbstract ic = DataAccessCGMS.getInstance().getI18nControlInstance();

        for (CGMSConfigurationGroup pbt : values())
        {
            pbt.setTranslation(ic.getMessage(pbt.i18nKey));
            translationMapping.put(pbt.getTranslation(), pbt);
            codeMapping.put(pbt.code, pbt);
        }

    }


    CGMSConfigurationGroup(int code, String i18nKey)
    {
        this.code = code;
        this.i18nKey = i18nKey;
    }


    public String getTranslation()
    {
        return translation;
    }


    public void setTranslation(String translation)
    {
        this.translation = translation;
    }


    public int getCode()
    {
        return code;
    }


    public String getI18nKey()
    {
        return i18nKey;
    }


    public String getName()
    {
        return this.name();
    }

}
