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

package ggc.gui.dialogs.graphs;

import ggc.core.data.DailyValues;
import ggc.core.util.DataAccess;
import ggc.gui.graphs.DailyGraphView;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.atech.i18n.I18nControlAbstract;

// FIX

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
 *  Filename:     DailyGraphDialog
 *  Description:  Daily Graph Dialog (DEPRECATED - WILL BE REMOVED)
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 *  
 *  
 *  @deprecated This class shouldn't be used. We now use GraphViewer, together with Views (look into atech-tools package)
 */

// DailyGraphDialog is already added with new framework

public class DailyGraphDialog extends JDialog
{
    /**
     * 
     */
    private static final long serialVersionUID = 2490681813475803663L;
    private DailyGraphView dGV = null;
    private I18nControlAbstract m_ic = DataAccess.getInstance().getI18nControlInstance();

    /**
     * Constructor
     * 
     * @param dialog
     */
    public DailyGraphDialog(JDialog dialog)
    {
        this(dialog, null);
        /*
         * super(dialog, "DailyGraphFrame", true);
         * setTitle(m_ic.getMessage("DAILYGRAPHFRAME"));
         * 
         * dGV = new DailyGraphView();
         * 
         * Rectangle rec = dialog.getBounds(); int x = rec.x + (rec.width/2);
         * int y = rec.y + (rec.height/2);
         * 
         * setBounds(x-200, y-150, 400, 300); //dWindowListener(new
         * CloseListener());
         * 
         * //setBounds(300, 300, 300, 300);
         * setDefaultCloseOperation(DISPOSE_ON_CLOSE); getContentPane().add(dGV,
         * BorderLayout.CENTER); setVisible(true);
         */
    }

    /**
     * Constructor
     * 
     * @param dialog
     * @param dV
     */
    public DailyGraphDialog(JDialog dialog, DailyValues dV)
    {

        super(dialog, "DailyGraphFrame", true);
        setTitle(m_ic.getMessage("DAILYGRAPHFRAME"));
        setLayout(new BorderLayout());

        dGV = new DailyGraphView();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().add(dGV, BorderLayout.CENTER);

        if (dV != null)
            setDailyValues(dV);

        setSize(dGV.getPreferredSize());
        setVisible(true);
    }

    /**
     * Constructor
     * 
     * @param frame
     */
    public DailyGraphDialog(JFrame frame)
    {
        this(frame, null);
    }

    /**
     * Constructor
     * 
     * @param frame
     * @param dV
     */
    public DailyGraphDialog(JFrame frame, DailyValues dV)
    {
        super(frame, "DailyGraphFrame", true);
        setTitle(m_ic.getMessage("DAILYGRAPHFRAME"));
        setLayout(new BorderLayout());

        dGV = new DailyGraphView();

        if (dV != null)
            setDailyValues(dV);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().add(dGV, BorderLayout.CENTER);
        setSize(dGV.getPreferredSize());
        setVisible(true);
    }

    /**
     * Set Daily Values
     * 
     * @param dV
     */
    public void setDailyValues(DailyValues dV)
    {
        dGV.setDailyValues(dV);
        this.repaint();
        
        /*
        System.out.println("TimeZones\n================================\n");
        
        String tz[] = TimeZone.getAvailableIDs();
        
        for(int i =0; i<tz.length; i++)
        {
            System.out.println(tz[i]);
        }*/

        
        
        // redraw();
    }

    /*
     * private void closeDialog() { dGV = null; this.dispose(); }
     */

}