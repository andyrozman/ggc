package main.java.ggc.pump.plugin;

import com.atech.db.hibernate.transfer.BackupRestoreObject;
import com.atech.db.hibernate.transfer.BackupRestoreRunner;
import com.atech.i18n.I18nControlAbstract;
import com.atech.plugin.BackupRestorePlugin;

import ggc.core.db.hibernate.pump.PumpDataExtendedH;
import ggc.core.db.hibernate.pump.PumpDataH;
import ggc.core.db.hibernate.pump.PumpProfileH;
import ggc.core.db.tool.transfer.GGCExporter;
import ggc.core.db.tool.transfer.GGCImporter;
import main.java.ggc.pump.db.PumpData;
import main.java.ggc.pump.db.PumpDataExtended;
import main.java.ggc.pump.db.PumpProfile;
import main.java.ggc.pump.util.DataAccessPump;

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
 *  Filename:     PluginDb
 *  Description:  This is master class for using Db instance within plug-in. In most cases, we
 *                would want data to be handled by outside authority (GGC), but in some cases
 *                we wouldn't want that.
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class BackupRestorePumpHandler extends BackupRestorePlugin
{

    I18nControlAbstract ic = DataAccessPump.getInstance().getI18nControlInstance();

    private String[] object_desc = { ic.getMessage("PUMP_DATA"), ic.getMessage("PUMP_DATA_EXTENDED"),
                                    ic.getMessage("PUMP_PROFILE") };

    private String[] object_name = { "ggc.core.db.hibernate.pump.PumpDataH",
                                    "ggc.core.db.hibernate.pump.PumpDataExtendedH",
                                    "ggc.core.db.hibernate.pump.PumpProfileH" };


    /**
     * Constructor
     */
    public BackupRestorePumpHandler()
    {
        // i18nControlAbstract = I18nControl.getInstance();
    }


    /**
     * Do Backup
     */
    @Override
    public void doBackup(BackupRestoreRunner brr)
    {

        for (int i = 0; i < this.object_desc.length; i++)
        {
            if (brr.isBackupObjectSelected(this.object_desc[i]))
            {
                // System.out.println("Selected: " + this.object_desc[i]);
                brr.setTask(this.object_desc[i]);
                GGCExporter ge = new GGCExporter(brr);
                ge.exportData(this.object_name[i]);
                brr.setStatus(100);
            }
            else
            {
                // System.out.println("NOT Selected: " + this.object_desc[i]);
            }

        }

    }


    /**
     * Do Restore
     */
    @Override
    public void doRestore(BackupRestoreRunner brr)
    {

        for (int i = 0; i < this.object_desc.length; i++)
        {
            if (brr.isRestoreObjectSelected(this.object_name[i]))
            {
                brr.setTask(this.object_desc[i]);
                GGCImporter ge = new GGCImporter(brr, brr.getRestoreObject(this.object_name[i]));
                ge.importData(this.object_name[i]);
                brr.setStatus(100);
            }
        }

    }


    /**
     * Get Backup Restore Object
     */
    @Override
    public BackupRestoreObject getBackupRestoreObject(String class_name)
    {
        if (class_name.equals("ggc.core.db.hibernate.pump.PumpDataH"))
            return new PumpData();
        else if (class_name.equals("ggc.core.db.hibernate.pump.PumpDataExtendedH"))
            return new PumpDataExtended();
        if (class_name.equals("ggc.core.db.hibernate.pump.PumpProfileH"))
            return new PumpProfile();
        else
            return null;
    }


    /**
     * Get Backup Restore Object
     */
    @Override
    public BackupRestoreObject getBackupRestoreObject(Object obj, BackupRestoreObject bro)
    {
        if (bro.getBackupClassName().equals("ggc.core.db.hibernate.pump.PumpDataH"))
        {
            PumpDataH eh = (PumpDataH) obj;
            return new PumpData(eh);
        }
        else if (bro.getBackupClassName().equals("ggc.core.db.hibernate.pump.PumpDataExtendedH"))
        {
            PumpDataExtendedH eh = (PumpDataExtendedH) obj;
            return new PumpDataExtended(eh);
        }
        else if (bro.getBackupClassName().equals("ggc.core.db.hibernate.pump.PumpProfileH"))
        {
            PumpProfileH eh = (PumpProfileH) obj;
            return new PumpProfile(eh);
        }
        else
            return null;

    }


    /**
     * Does Contain Backup Restore Object
     */
    @Override
    public boolean doesContainBackupRestoreObject(String bro_name)
    {
        for (String element : this.object_name)
        {
            if (element.equals(bro_name))
                return true;
        }

        return false;
    }

}
