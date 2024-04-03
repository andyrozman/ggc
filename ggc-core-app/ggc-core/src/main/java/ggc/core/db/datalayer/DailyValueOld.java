package ggc.core.db.datalayer;

import com.atech.i18n.I18nControlAbstract;

import ggc.core.db.hibernate.pen.DayValueH;

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
 *  Filename:     DailyValue
 *  Description:  This is DataLayer file (data file, with methods to work with database or in 
 *                this case Hibernate). This one is used for DailyValue.
 *                This one is also BackupRestoreObject. 
 *
 *  Author: andyrozman {andy@atech-software.com}  
 */

// TODO: DL
@Deprecated
public class DailyValueOld extends DailyValue
{

    /**
     * Constructor
     */
    public DailyValueOld()
    {
        super();
    }


    /**
     * Constructor
     *
     * @param dvh
     */
    public DailyValueOld(DayValueH dvh)
    {
        super(dvh);
    }


    /**
     * Constructor
     *
     * @param ic
     */
    public DailyValueOld(I18nControlAbstract ic)
    {
        super(ic);
    }


    /**
     * getBackupClassName - name of class which will be updated/restored
     *
     * @return
     */
    public String getClassName()
    {
        return "ggc.core.db.hibernate.DayValueH";
    }

}
