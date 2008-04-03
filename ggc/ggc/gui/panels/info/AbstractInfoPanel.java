/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: AbstractInfoPanel.java
 *  Purpose:  defines Methods all InfoPanels must define.
 *
 *  Author:   schultd
 */

package ggc.gui.panels.info;

import javax.swing.JPanel;
import javax.swing.BorderFactory;

import ggc.core.util.DataAccess;
import ggc.core.util.I18nControl;

import javax.swing.border.TitledBorder;


public abstract class AbstractInfoPanel extends JPanel
{
    protected I18nControl m_ic = I18nControl.getInstance();
    protected DataAccess m_da = DataAccess.getInstance();
    protected boolean first_refresh = true;

    public AbstractInfoPanel(String title)
    {
        super();
        setBorder(BorderFactory.createTitledBorder(title));
        setOpaque(false);
    }

    public void setTitle(String title)
    {
        TitledBorder tb = (TitledBorder)this.getBorder();
        tb.setTitle(title);
    }

    public abstract void refreshInfo();

    public void invalidateFirstRefresh()
    {
	this.first_refresh = true;
    }

}
