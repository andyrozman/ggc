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
 *  Filename: DailyGraphFrame.java
 *  Purpose:  The Frame which contains the DailyValuesView.
 *
 *  Author:   schultd
 */

package ggc.gui.dialogs;


import ggc.data.DailyValues;
import ggc.util.I18nControl;
import ggc.gui.view.DailyGraphView;

import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// FIX

public class DailyGraphDialog extends JDialog
{
    private I18nControl m_ic = I18nControl.getInstance();    
    private DailyGraphView dGV = null;


    public DailyGraphDialog(JDialog dialog)
    {
        super(dialog, "DailyGraphFrame", true);
        setTitle(m_ic.getMessage("DAILYGRAPHFRAME"));

        dGV = new DailyGraphView();

        Rectangle rec = dialog.getBounds();
        int x = rec.x + (rec.width/2);
        int y = rec.y + (rec.height/2);

        setBounds(x-200, y-150, 400, 300);
        //dWindowListener(new CloseListener());

        //setBounds(300, 300, 300, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().add(dGV, BorderLayout.CENTER);
        setVisible(true);
    }


    public DailyGraphDialog(JDialog dialog, DailyValues dV)
    {
        this(dialog);
        setDailyValues(dV);
/*
    	super(dialog, "DailyGraphFrame", true);
    	setTitle(m_ic.getMessage("DAILYGRAPHFRAME"));
    
    	dGV = new DailyGraphView();
    
    	Rectangle rec = dialog.getBounds();
    	int x = rec.x + (rec.width/2);
    	int y = rec.y + (rec.height/2);
    
    	setBounds(x-200, y-150, 400, 300);
    	//addWindowListener(new CloseListener());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    
    	dGV.setDailyValues(dV);
    	//setBounds(300, 300, 300, 300);
    	//setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    	getContentPane().add(dGV, BorderLayout.CENTER);
    	setVisible(true);
        */
    }


    public DailyGraphDialog(JFrame frame)
    {
        super(frame, "DailyGraphFrame", true);
        setTitle(m_ic.getMessage("DAILYGRAPHFRAME"));

        dGV = new DailyGraphView();

        Rectangle rec = frame.getBounds();
        int x = rec.x + (rec.width/2);
        int y = rec.y + (rec.height/2);

        setBounds(x-200, y-150, 400, 300);
        //dWindowListener(new CloseListener());

        //setBounds(300, 300, 300, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().add(dGV, BorderLayout.CENTER);
        setVisible(true);
    }



    public void setDailyValues(DailyValues dV)
    {
        dGV.setDailyValues(dV);
        this.repaint();
        //redraw();
    }





    private void closeDialog()
    {
    	dGV = null;
    	this.dispose();
    }


}