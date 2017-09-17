package ggc.core.db.datalayer;

import com.atech.i18n.I18nControlAbstract;

import ggc.core.db.hibernate.settings.SettingsH;

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
 *  Filename:     Settings  
 *  Description:  This is datalayer file (data file, with methods to work with database or in 
 *      this case Hibernate). This one is used for Settings.
 * 
 *  Author: Andy {andy@atech-software.com}  
 */
@Deprecated
public class SettingsOld extends Settings
{

    private static final long serialVersionUID = 533548318516266674L;


    /**
     * Constructor
     */
    public SettingsOld()
    {
        super();
    }


    /**
     * Constructor
     *
     * @param ic
     */
    public SettingsOld(I18nControlAbstract ic)
    {
        super(ic);
    }


    /**
     * Constructor
     *
     * @param ch
     */
    public SettingsOld(SettingsH ch)
    {
        super(ch);
    }


    /**
     * Get Class Name
     * 
     * @see com.atech.db.hibernate.transfer.BackupRestoreBase#getClassName()
     */
    public String getClassName()
    {
        return "ggc.core.db.hibernate.SettingsH";
    }

}
