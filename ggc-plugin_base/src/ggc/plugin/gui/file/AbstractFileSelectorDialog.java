package ggc.plugin.gui.file;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;

import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.util.DataAccessPlugInBase;

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
 *  Filename:     ----  
 *  Description:  ----
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public abstract class AbstractFileSelectorDialog extends JDialog implements ActionListener, HelpCapable
{

    private static final long serialVersionUID = -7597376030031612506L;
    JButton helpButton = null;
    protected DataAccessPlugInBase m_da = null;
    protected I18nControlAbstract m_ic = null;
    protected JDialog dialogParent = null;
    protected DeviceDataHandler deviceDataHandler = null;
    protected DownloadSupportType downloadSupportType;


    // public abstract int[] getPanelSizes();

    /**
     * Constructor
     * 
     * @param da
     * @param ddh
     * @param parent
     */
    public AbstractFileSelectorDialog(DataAccessPlugInBase da, DeviceDataHandler ddh, JDialog parent,
            DownloadSupportType downloadSupportType)
    {
        super(parent);
        this.dialogParent = parent;
        this.deviceDataHandler = ddh;
        this.m_da = da;
        this.downloadSupportType = downloadSupportType;
        m_ic = da.getI18nControlInstance();
        init();
        setSize(getSize());
        ATSwingUtils.centerJDialog(this, this.dialogParent);
        this.setVisible(true);
    }


    /**
     * Init
     */
    public abstract void init();


    @Override
    public abstract Dimension getSize();


    public Component getComponent()
    {
        return this;
    }


    public JButton getHelpButton()
    {
        return this.helpButton;
    }

}
