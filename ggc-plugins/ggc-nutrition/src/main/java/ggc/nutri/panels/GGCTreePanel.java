package ggc.nutri.panels;

import ggc.nutri.util.DataAccessNutri;

import java.awt.Component;

import javax.swing.JButton;

import com.atech.graphics.components.EditableAbstractPanel;
import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;

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
 *  Filename:     GGCTreePanel
 *  Description:  Abstract panel for elements of Nutrition tree
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public abstract class GGCTreePanel extends EditableAbstractPanel implements HelpCapable // JPanel
                                                                                        // implements
                                                                                        // EditablePanel
{

    private static final long serialVersionUID = 1537683351864737939L;
    protected DataAccessNutri m_da = null;
    protected I18nControlAbstract m_ic = null;
    protected JButton help_button = null;

    /**
     * Constructor
     * 
     * @param is_editable
     */
    public GGCTreePanel(boolean is_editable)
    {
        super(is_editable, DataAccessNutri.getInstance().getI18nControlInstance());
        m_da = DataAccessNutri.getInstance();
    }

    /**
     * Set Parent Root
     * 
     * @see com.atech.graphics.components.EditableAbstractPanel#setParentRoot(java.lang.Object)
     */
    @Override
    public void setParentRoot(Object obj)
    {

    }

    public Component getComponent()
    {
        return this;
    }

    public JButton getHelpButton()
    {
        return this.help_button;
    }

}
