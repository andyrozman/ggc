package ggc.gui.dialogs.config.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.graphics.components.JDecimalTextField;
import com.atech.i18n.I18nControlAbstract;

import ggc.core.data.cfg.ConfigurationManagerWrapper;
import ggc.core.util.DataAccess;
import ggc.core.util.GGCProperties;
import ggc.gui.dialogs.config.PropertiesDialog;

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
 *  Filename:     AbstractPrefOptionsPanel.java
 *  Description:  A abstract OptionsPanel to easily change the option panels on
 *                the preferences dialog.
 * 
 *  Author: schultd
 *          andyrozman {andy@atech-software.com}  
 */

public abstract class AbstractPrefOptionsPanel extends JPanel
        implements DocumentListener, ItemListener, ActionListener, PropertyChangeListener
{

    private static final long serialVersionUID = 6154601886618634718L;
    private static final Logger LOG = LoggerFactory.getLogger(AbstractPrefOptionsPanel.class);

    protected DataAccess dataAccess = DataAccess.getInstance();
    protected I18nControlAbstract i18nControl = dataAccess.getI18nControlInstance();
    protected GGCProperties ggcProperties = dataAccess.getSettings();

    protected ConfigurationManagerWrapper configurationManagerWrapper = dataAccess.getConfigurationManagerWrapper();

    protected boolean changed = false;
    PropertiesDialog parent;


    /**
     * Constructor
     * 
     * @param dialog
     */
    public AbstractPrefOptionsPanel(PropertiesDialog dialog)
    {
        this.parent = dialog;
    }


    /**
     * Set Changed
     * 
     * @param change
     */
    public void setChanged(boolean change)
    {
        changed = change;
    }


    /**
     * Has Changed (for this to work correctly, we need to tie all elements into our change listeners
     * or if we have implemented their own, they need to set changed flag themselves)
     * 
     * @return true if any of ggcProperties has changed
     *
     */
    public boolean hasChanged()
    {
        return changed;
    }


    /**
     * {@inheritDoc}
     */
    public void insertUpdate(DocumentEvent e)
    {
        changed = true;
    }


    /**
     * {@inheritDoc}
     */
    public void removeUpdate(DocumentEvent e)
    {
        changed = true;
    }


    /**
     * {@inheritDoc}
     */
    public void changedUpdate(DocumentEvent e)
    {
    }


    /**
     * {@inheritDoc}
     */
    public void actionPerformed(ActionEvent e)
    {
        changed = true;
        System.out.println("Changed");
    }


    /**
     * {@inheritDoc}
     */
    public void itemStateChanged(ItemEvent e)
    {
        changed = true;
    }


    /**
     * Save Properties from panel
     */
    public abstract void saveProps();


    protected void registerForChange(JComponent component)
    {
        if (component instanceof JCheckBox)
        {
            JCheckBox cb = (JCheckBox) component;
            cb.addActionListener(this);
        }
        else if (component instanceof JComboBox)
        {
            JComboBox cb = (JComboBox) component;
            cb.addActionListener(this);
        }
        else
        {
            LOG.debug("registerForChange not implemented for : " + component.getClass().getSimpleName());
        }
    }


    protected void registerForChange(JComboBox[] comboBoxes)
    {
        for (JComboBox cb : comboBoxes)
        {
            cb.addItemListener(this);
        }
    }


    public void registerForChange(JCheckBox[] checkBoxes)
    {
        for (JCheckBox cb : checkBoxes)
        {
            cb.addActionListener(this);
        }
    }


    public void registerForChange(JDecimalTextField[] numericFields)
    {
        for (JDecimalTextField cb : numericFields)
        {
            cb.addPropertyChangeListener("value", this);
        }
    }


    protected String getI18nText(String key)
    {
        return i18nControl.getMessage(key);
    }


    protected String getI18nTextWithColon(String key)
    {
        return getI18nText(key) + ":";
    }


    protected JLabel getLabelWithColon(String key)
    {
        return new JLabel(getI18nTextWithColon(key));
    }


    /**
     * Returns Help Id for this specific Panel. This will be read by Properties Dialog, which implements
     * help capable and requires this data.
     */
    public String getHelpId()
    {
        return "GGC_Prefs_General";
    }


    public void propertyChange(PropertyChangeEvent evt)
    {
        if (evt.getPropertyName().equals("value"))
        {
            changed = true;
            System.out.println("Changed");
        }
    }


    public JDialog getParentDialog()
    {
        return this.parent;
    }
}
