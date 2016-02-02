package ggc.gui.main.panels;

import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import com.atech.i18n.I18nControlAbstract;

import ggc.core.data.cfg.ConfigurationManagerWrapper;
import ggc.core.util.DataAccess;
import info.clearthought.layout.TableLayout;

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
 *  Filename:     AbstractInfoPanel  
 *  Description:  Abstract class for info panels
 *
 *  Author: schultd
 *          andyrozman {andy@atech-software.com}  
 */

public abstract class AbstractInfoPanel extends JPanel
{

    private static final long serialVersionUID = -1104646965456304940L;
    protected DataAccess m_da = DataAccess.getInstance();
    protected I18nControlAbstract m_ic = m_da.getI18nControlInstance();
    protected ConfigurationManagerWrapper configurationManagerWrapper = m_da.getConfigurationManagerWrapper();
    protected boolean first_refresh = true;

    protected Map<String, JLabel> valueLabels;
    protected int currentLine = 1;


    /**
     * Constructor
     * 
     * @param title Title of panel
     */
    public AbstractInfoPanel(String title)
    {
        this(title, true);
    }


    /**
     * Constructor
     * 
     * @param title
     * @param createBorder
     */
    public AbstractInfoPanel(String title, boolean createBorder)
    {
        super();

        if (createBorder)
        {
            setBorder(BorderFactory.createTitledBorder(this.m_ic.getMessage(title) + ":"));
        }

        setOpaque(false);
    }


    /**
     * Refresh Information 
     */
    public void refreshInfo()
    {
        doRefresh();
    }


    /**
     * Invalidate First Refresh
     */
    public void invalidateFirstRefresh()
    {
        this.first_refresh = true;
    }


    public abstract InfoPanelType getPanelType();


    /**
     * Is Tab Checked (by PanelType)
     *
     * @param panelTypes
     * @return
     */
    public boolean isPanelTaged(InfoPanelType... panelTypes)
    {
        for (InfoPanelType panelType : panelTypes)
        {
            if (panelType == this.getPanelType())
            {
                return true;
            }
        }

        return false;
    }


    /**
     * RefreshInfo - Refresh info by panel type(s)
     *
     * @param panelTypes
     */
    public void refreshInfo(InfoPanelType... panelTypes)
    {
        if (this.isPanelTaged(panelTypes))
        {
            doRefresh();
        }
    }


    /**
     * Do Refresh - This method can do Refresh
     */
    public abstract void doRefresh();


    protected void initWithTableLayoutAndDisplayPairs(double[][] sizes, String... keysForDisplayPairs)
    {
        setLayout(new TableLayout(sizes));

        valueLabels = new HashMap<String, JLabel>();

        for (String key : keysForDisplayPairs)
        {
            addDisplayLabelsToPanel(key);
        }
    }


    protected void addDisplayLabelsToPanel(String i18nKey)
    {
        add(new JLabel(m_ic.getMessage(i18nKey) + ":"), "1, " + currentLine);

        JLabel label = new JLabel("N/A");

        valueLabels.put(i18nKey, label);

        add(label, "3, " + currentLine);

        currentLine++;
    }


    protected void setValueOnDisplayLabel(String key, String value)
    {
        valueLabels.get(key).setText(value);
    }

}
