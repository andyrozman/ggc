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
 *  Filename: prefMedicalDataPane.java
 *  Purpose:  Medical Options such as insulin names and BG-Values.
 *
 *  Author:   schultd
 */

package ggc.gui.prefPane;


import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;


public class prefMedicalDataPane extends AbstractPrefOptionsPanel
{
    private JTextField fieldIns1Name;
    private JTextField fieldIns2Name;
    private JTextField fieldIns1Abbr;
    private JTextField fieldIns2Abbr;

    private JTextField fieldHighBG;
    private JTextField fieldLowBG;

    private JTextField fieldTargetHighBG;
    private JTextField fieldTargetLowBG;

    public prefMedicalDataPane()
    {
        init();
    }

    private void init()
    {
        JPanel a = new JPanel(new GridLayout(0, 1));
        a.add(new JLabel(m_ic.getMessage("INSULIN")+" 1 "+m_ic.getMessage("NAME")+":"));
        a.add(new JLabel(m_ic.getMessage("INSULIN")+" 1 "+m_ic.getMessage("ABBR")+":"));
        a.add(new JLabel(m_ic.getMessage("INSULIN")+" 2 "+m_ic.getMessage("NAME")+":"));
        a.add(new JLabel(m_ic.getMessage("INSULIN")+" 2 "+m_ic.getMessage("ABBR")+":"));

        JPanel b = new JPanel(new GridLayout(0, 1));
        b.add(fieldIns1Name = new JTextField(props.getIns1Name(), 10));
        b.add(fieldIns1Abbr = new JTextField(props.getIns1Abbr(), 10));
        b.add(fieldIns2Name = new JTextField(props.getIns2Name(), 10));
        b.add(fieldIns2Abbr = new JTextField(props.getIns2Abbr(), 10));

        fieldIns1Name.getDocument().addDocumentListener(this);
        fieldIns2Name.getDocument().addDocumentListener(this);
        fieldIns1Abbr.getDocument().addDocumentListener(this);
        fieldIns2Abbr.getDocument().addDocumentListener(this);

        Box insBox = Box.createHorizontalBox();
        insBox.add(a);
        insBox.add(b);

        JPanel insPanel = new JPanel(new BorderLayout());
        insPanel.setBorder(new TitledBorder(new EtchedBorder(), m_ic.getMessage("INSULIN_SETTINGS")));
        insPanel.add(insBox, BorderLayout.NORTH);


        JPanel c = new JPanel(new GridLayout(0, 1));
        c.add(new JLabel(m_ic.getMessage("HIGH_BG")+":"));
        c.add(new JLabel(m_ic.getMessage("LOW_BG")+":"));
        c.add(new JLabel(m_ic.getMessage("TARGET_HIGH_BG")+":"));
        c.add(new JLabel(m_ic.getMessage("TARGET_LOW_BG")+":"));

        JPanel d = new JPanel(new GridLayout(0, 1));
        d.add(fieldHighBG = new JTextField(props.getHighBGAsString(), 10));
        d.add(fieldLowBG = new JTextField(props.getLowBGAsString(), 10));
        d.add(fieldTargetHighBG = new JTextField(props.getTargetHighBGAsString(), 10));
        d.add(fieldTargetLowBG = new JTextField(props.getTargetLowBGAsString(), 10));

        fieldHighBG.getDocument().addDocumentListener(this);
        fieldLowBG.getDocument().addDocumentListener(this);
        fieldTargetHighBG.getDocument().addDocumentListener(this);
        fieldTargetLowBG.getDocument().addDocumentListener(this);

        Box BGBox = Box.createHorizontalBox();
        BGBox.add(c);
        BGBox.add(d);

        JPanel BGPanel = new JPanel(new BorderLayout());
        BGPanel.setBorder(new TitledBorder(new EtchedBorder(), m_ic.getMessage("BLOOD_GLUCOSE_SETTINGS")));
        BGPanel.add(BGBox, BorderLayout.NORTH);

        setLayout(new BorderLayout());

        Box i = Box.createHorizontalBox();
        i.add(insPanel);
        i.add(BGPanel);

        add(i, BorderLayout.NORTH);
    }

    public void saveProps()
    {
        props.set("Ins1Name", fieldIns1Name.getText());
        props.set("Ins2Name", fieldIns2Name.getText());
        props.set("Ins1Abbr", fieldIns1Abbr.getText());
        props.set("Ins2Abbr", fieldIns2Abbr.getText());
        props.set("HighBG", fieldHighBG.getText());
        props.set("LowBG", fieldLowBG.getText());
        props.set("TargetHighBG", fieldTargetHighBG.getText());
        props.set("TargetLowBG", fieldTargetLowBG.getText());
    }
}
