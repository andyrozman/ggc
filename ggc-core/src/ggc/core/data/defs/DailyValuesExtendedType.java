package ggc.core.data.defs;

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
 *  Filename:     DailyValuesExtendedType
 *  Description:  Daily Values Extended Type
 *
 *  Author:    Andy {andy@atech-software.com}
 */

public enum DailyValuesExtendedType
{
    Activity(0), //
    Urine(1), //
    Urine_mgdL(1), //
    Urine_mmolL(1), //
    FoodDescription(2), //
    FoodCH(3), //
    DecimalPart_Insulin1(4), //
    DecimalPart_Insulin2(5), //
    Insulin_3(6), //
    Source(7); //

    int code;


    private DailyValuesExtendedType(int code)
    {
        this.code = code;
    }


    public int getCode()
    {
        return code;
    }

}
