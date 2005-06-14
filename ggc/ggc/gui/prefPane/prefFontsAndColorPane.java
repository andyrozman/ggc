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
 *  Filename: prefFontsAndColorPane.java
 *  Purpose:  Preferences Pane to choose Fonts and Colors.
 *
 *  Author:   schultd
 */

package ggc.gui.prefPane;


import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;


public class prefFontsAndColorPane extends AbstractPrefOptionsPanel
{
    private JList itemList;
    private JLabel lblcolor;
    private Vector items;

    public prefFontsAndColorPane()
    {
        items = new Vector();
        items.add(new NameAndColor(m_ic.getMessage("BG_HIGH_ZONE"), "HighBG", props.getColorHighBG()));
        items.add(new NameAndColor(m_ic.getMessage("BG_TARGET_ZONE"), "TargetBG", props.getColorTargetBG()));
        items.add(new NameAndColor(m_ic.getMessage("BG_LOW_ZONE"), "LowBG", props.getColorLowBG()));
        items.add(new NameAndColor(m_ic.getMessage("BG"), "BG", props.getColorBG()));
        items.add(new NameAndColor(m_ic.getMessage("BG_AVERAGE"), "AvgBG", props.getColorAvgBG()));
        items.add(new NameAndColor(m_ic.getMessage("BREAD_UNITS"), "BU", props.getColorBU()));
        items.add(new NameAndColor(m_ic.getMessage("INSULIN")+" " + props.getIns1Abbr(), "Ins1", props.getColorIns1()));
        items.add(new NameAndColor(m_ic.getMessage("INSULIN")+" " + props.getIns2Abbr(), "Ins2", props.getColorIns2()));
        items.add(new NameAndColor(m_ic.getMessage("INSULIN"), "Ins", props.getColorIns()));
        items.add(new NameAndColor(m_ic.getMessage("INS_SLASH_BU_QUOTIENT"), "InsPerBU", props.getColorInsPerBU()));

        init();
    }

    private void init()
    {
        setLayout(new BorderLayout());
        itemList = new JList(items);
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemList.setPreferredSize(new Dimension(150, 200));

        itemList.addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent e)
            {
                lblcolor.setBackground(((NameAndColor)items.elementAt(itemList.getSelectedIndex())).getColor());
            }
        });

        JPanel colorPanel = new JPanel(new BorderLayout());
        colorPanel.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("COLOR")));

        JPanel a = new JPanel(new GridLayout(2, 1));
        a.add(new JLabel(m_ic.getMessage("COLOR")+":"));
        a.add(lblcolor = new JLabel(" "));

        lblcolor.setOpaque(true);
        lblcolor.setPreferredSize(new Dimension(200, 25));
        lblcolor.setBorder(BorderFactory.createLineBorder(Color.black));

        lblcolor.addMouseListener(new MouseClicker());

        colorPanel.add(itemList, BorderLayout.WEST);
        colorPanel.add(a, BorderLayout.CENTER);

        add(colorPanel, BorderLayout.NORTH);

        itemList.setSelectedIndex(0);
    }

    private class MouseClicker extends MouseAdapter
    {
        public void mouseClicked(MouseEvent e)
        {
            Color col = JColorChooser.showDialog(null, m_ic.getMessage("CHOOSE_A_COLOR"), Color.black);
            if (col != null) {
                ((NameAndColor)items.elementAt(itemList.getSelectedIndex())).setColor(col);
                lblcolor.setBackground(col);
                changed = true;
            }
        }
    }

    private class NameAndColor
    {
        String name;
        String id;
        Color color;

        public NameAndColor(String name, String id, Color color)
        {
            this.name = name;
            this.color = color;
            this.id = id;
        }

        public String getName()
        {
            return name;
        }

        public Color getColor()
        {
            return color;
        }

        public String getID()
        {
            return id;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public void setID(String id)
        {
            this.id = id;
        }

        public void setColor(Color color)
        {
            this.color = color;
        }

        public String toString()
        {
            return name;
        }
    }

    public void saveProps()
    {
        for (int i = 0; i < items.size(); i++) {
            String name = "Color" + ((NameAndColor)items.elementAt(i)).getID();
            Color color = ((NameAndColor)items.elementAt(i)).getColor();

            props.set(name, color);
        }
    }
}
