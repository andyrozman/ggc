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
 *  Filename: prefTextFileSetupPane.java
 *  Purpose:  Set parameters for datastoring in textfiles.
 *
 *  Author:   schultd
 */

package ggc.gui.prefPane;


import javax.swing.*;
import java.awt.*;


public class prefTextFileSetupPane extends AbstractPrefOptionsPanel
{
    private JTextField fieldDefaultPath;
    private JCheckBox chkOpenDefault;

    public prefTextFileSetupPane()
    {
        init();
    }

    private void init()
    {
        JPanel a = new JPanel(new GridLayout(0, 1));
        a.add(new JLabel("Default File:"));
        a.add(new JLabel(""));

        JPanel b = new JPanel(new GridLayout(0, 1));
        b.add(fieldDefaultPath = new JTextField(props.getTextFilePath(), 10));
        b.add(chkOpenDefault = new JCheckBox("Open Default File upon Connect", props.getTextFileOpenDefaultFile()));

        fieldDefaultPath.getDocument().addDocumentListener(this);
        chkOpenDefault.addActionListener(this);

        Box myBox = Box.createHorizontalBox();
        myBox.add(a);
        myBox.add(b);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBorder(BorderFactory.createTitledBorder("Text File Setup:"));
        titlePanel.add(myBox, BorderLayout.CENTER);

        setLayout(new BorderLayout());

        add(titlePanel, BorderLayout.NORTH);
    }

    public void saveProps()
    {
        props.set("TextFilePath", fieldDefaultPath.getText());
        props.set("TextFileOpenDefaultFile", chkOpenDefault.isSelected());
    }
}
