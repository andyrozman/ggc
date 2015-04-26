package ggc.gui.panels.prefs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.atech.i18n.I18nControlAbstract;

import ggc.core.util.DataAccess;
import ggc.core.util.GGCProperties;
import ggc.gui.dialogs.PropertiesDialog;

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

public abstract class AbstractPrefOptionsPanel extends JPanel implements DocumentListener, ItemListener, ActionListener
{

    private static final long serialVersionUID = 6154601886618634718L;
    protected DataAccess m_da = DataAccess.getInstance();
    protected I18nControlAbstract m_ic = m_da.getI18nControlInstance();
    protected GGCProperties settings = m_da.getSettings();
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
     * Has Changed
     * 
     * @return
     */
    public boolean hasChanged()
    {
        return changed;
    }


    /**
     * Insert Update
     * 
     * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
     */
    public void insertUpdate(DocumentEvent e)
    {
        changed = true;
    }


    /**
     * Remove Update
     * 
     * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
     */
    public void removeUpdate(DocumentEvent e)
    {
        changed = true;
    }


    /**
     * Changed Update
     * 
     * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
     */
    public void changedUpdate(DocumentEvent e)
    {
    }


    /**
     * Action Performed
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
    {
        changed = true;
    }


    /**
     * Item State Changed
     * 
     * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     */
    public void itemStateChanged(ItemEvent e)
    {
        changed = true;
    }


    /**
     * Save Properties
     */
    public abstract void saveProps();


    /**
     * Kill - If changed, it asks if values should be changed
     */
    public void kill()
    {
        if (changed)
        {
            int res = JOptionPane.showConfirmDialog(null, m_ic.getMessage("SOME_VALUES_CHANGED_LIKE_TO_SAVE"),
                m_ic.getMessage("VALUES_CHANGED"), JOptionPane.YES_NO_OPTION);
            if (res == JOptionPane.YES_OPTION)
            {
                saveProps();
            }
        }
    }


    protected String getI18nText(String key)
    {
        return m_ic.getMessage(key);
    }


    protected String getI18nTextWithColon(String key)
    {
        return getI18nText(key) + ":";
    }


    protected JLabel getLabelWithColon(String key)
    {
        return new JLabel(getI18nTextWithColon(key));
    }

}
