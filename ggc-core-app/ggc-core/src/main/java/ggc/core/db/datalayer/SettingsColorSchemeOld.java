package ggc.core.db.datalayer;

import java.util.ArrayList;

import com.atech.graphics.components.tree.CheckBoxTreeNodeInterface;
import com.atech.i18n.I18nControlAbstract;

import ggc.core.db.hibernate.settings.ColorSchemeH;

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
 *  Filename:     SettingsColorScheme
 *  Description:  This is DataLayer file (data file, with methods to work with database or in 
 *                this case Hibernate). This one is used for ColorSchemes. This one is also 
 *                BackupRestoreObject.
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */
@Deprecated
public class SettingsColorSchemeOld extends SettingsColorScheme
{

    private static final long serialVersionUID = -4888702023546352015L;


    /**
     * Constructor
     */
    public SettingsColorSchemeOld()
    {
        super();
    }


    /**
     * Constructor
     *
     * @param ic
     */
    public SettingsColorSchemeOld(I18nControlAbstract ic)
    {
        super(ic);
    }


    /**
     * Constructor
     *
     * @param ch
     */
    public SettingsColorSchemeOld(ColorSchemeH ch)
    {
        super(ch);
    }


    /**
     * getBackupClassName - name of class which will be updated/restored
     * 
     * @return
     */
    public String getBackupClassName()
    {
        return "ggc.core.db.hibernate.ColorSchemeH";
    }


    /**
     * Has To Be Clean - if table needs to be cleaned before import
     * 
     * @return true if we need to clean
     */
    public boolean hasToBeCleaned()
    {
        return true;
    }


    /** 
     * Get Node Children
     */
    public ArrayList<CheckBoxTreeNodeInterface> getNodeChildren()
    {
        return null;
    }


    /** 
     * Has Node Children
     */
    public boolean hasNodeChildren()
    {
        return false;
    }

}
