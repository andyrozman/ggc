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
 *  Filename: InfoPanel.java
 *  Purpose:  Container for all InfoPanels an the MainFrame. Responsible for
 *            managing the handling of its children.
 *
 *  Author:   schultd
 */

package ggc.gui.panels.info;


import javax.swing.*;
import java.awt.*;
import java.util.Vector;


public class InfoPanel extends JPanel
{
    private Vector<JPanel> vInfoPanels = new Vector<JPanel>();

    public InfoPanel()
    {
        setLayout(new GridLayout(0, 2));
        setBackground(Color.white);

        vInfoPanels.add(new GeneralInfoPanel());
        vInfoPanels.add(new HbA1cInfoPanel());
        vInfoPanels.add(new ScheduleInfoPanel());
        vInfoPanels.add(new StatisticsInfoPanel());

        addPanels();
    }

    private void addPanels()
    {
        for (int i = 0; i < vInfoPanels.size(); i++)
            add(vInfoPanels.get(i));
    }

    public void refreshPanels()
    {
        for (int i = 0; i < vInfoPanels.size(); i++)
            ((AbstractInfoPanel)vInfoPanels.get(i)).refreshInfo();
    }

    public void addPanelAt(int index, AbstractInfoPanel panel)
    {
        vInfoPanels.add(index, panel);
        removeAll();
        addPanels();
    }

    public void removePanelAt(int index)
    {
        vInfoPanels.remove(index);
        removeAll();
        addPanels();
    }
}
