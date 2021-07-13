package ggc.core.data.defs;

import java.util.Hashtable;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.CodeEnumWithTranslation;

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
 *  Filename:     ExerciseStrength
 *  Description:  Exercise Strength
 *
 *  Author:    Andy {andy@atech-software.com}
 */

public enum ExerciseStrength implements CodeEnumWithTranslation
{
    Undefined(0, "EXERCISE_UNDEFINED"), //
    Light(1, "EXERCISE_LIGHT"), //
    Medium(2, "EXERCISE_MEDIUM"), //
    Heavy(3, "EXERCISE_HEAVY"), //
    MaxValue(4, "EXERCISE_MAX"), //
    ;




    static Hashtable<String, ExerciseStrength> translationMapping = new Hashtable<String, ExerciseStrength>();
    static Hashtable<Integer, ExerciseStrength> codeMapping = new Hashtable<Integer, ExerciseStrength>();

    static
    {
        for (ExerciseStrength pbt : values())
        {
            codeMapping.put(pbt.code, pbt);
        }
    }





    public static void translateKeywords(I18nControlAbstract ic)
    {
        if (translated)
            return;

        for (ExerciseStrength pbt : values())
        {
            pbt.setTranslation(ic.getMessage(pbt.i18nKey));
            translationMapping.put(pbt.getTranslation(), pbt);
        }

        translated = true;
    }

    int code;
    String i18nKey;
    String translation;
    static boolean translated = false;


    ExerciseStrength(int code, String i18nKey)
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


    public static ExerciseStrength getByCode(int code)
    {
        if (codeMapping.containsKey(code))
            return codeMapping.get(code);
        else
            return ExerciseStrength.Undefined;
    }

    public static ExerciseStrength[] getAllValues()
    {
        return values();
    }

}
