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
 *  Filename: AddRowFrame.java
 *  Purpose:  Add a new row with Values to ReadMeterFrame or DailyValuesFrame.
 *
 *  Author:   schultd
 */

package ggc.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Event;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;

import ggc.datamodels.DailyValues;
import ggc.datamodels.DailyValuesRow;
import ggc.errors.DateTimeError;
import ggc.util.GGCProperties;
import ggc.util.I18nControl;


// fix this

public class DailyRowDialog extends JDialog implements ActionListener, KeyListener
{

    private I18nControl m_ic = I18nControl.getInstance();

    private boolean m_actionDone = false;

//    static AddRowFrame singleton = null;

    JTextField DateField, TimeField, BGField, Ins1Field, Ins2Field, BUField,
            ActField, CommentField;

    JButton AddButton;

    String sDate = null;

    DailyValues dV = null;
    DailyValuesRow m_dailyValuesRow = null;

    //AbstractTableModel mod = null;

    GGCProperties props = GGCProperties.getInstance();
    JComponent components[] = new JComponent[9];
/*
    public AddRowFrame(AbstractTableModel m, DailyValues ndV, DailyStatsFrame dialog) 
    {
        super(dialog, "", true);
        setTitle(m_ic.getMessage("ADD_NEW_ROW"));
        dV = ndV;
        mod = m;
        init();
    }
    */

    private boolean m_add_action = true;



    public DailyRowDialog(DailyValues ndV, String nDate, JDialog dialog) 
    {
        super(dialog, "", true);
        initParameters(ndV,nDate);
    }


    public DailyRowDialog(DailyValues ndV, String nDate, JFrame frame) 
    {
        super(frame, "", true);
        initParameters(ndV,nDate);
    }


    public DailyRowDialog(DailyValuesRow ndr, JDialog dialog) 
    {
        super(dialog, "", true);
        initParameters(ndr);
    }


    public DailyRowDialog(DailyValuesRow ndr, JFrame frame) 
    {
        super(frame, "", true);
        initParameters(ndr);
    }



    public void initParameters(DailyValues ndV, String nDate)
    {
        //if (add) 
            setTitle(m_ic.getMessage("ADD_NEW_ROW"));
        //else
        //    setTitle(m_ic.getMessage("EDIT_NEW_ROW"));

        sDate = nDate;
        dV = ndV;
        this.m_add_action = true;
        //mod = m;
        init();
    }


    public void initParameters(DailyValuesRow ndr)
    {
        //if (add) 
        //    setTitle(m_ic.getMessage("ADD_NEW_ROW"));
        //else
            setTitle(m_ic.getMessage("EDIT_NEW_ROW"));

        this.m_add_action = false;
        sDate = ndr.getDateAsString();
        this.m_dailyValuesRow = ndr;
        
        this.m_add_action = false;
        init();
        initValues();
    }


    public void initValues()
    {
        // to do
        

    }



    /*
    public static AddRowFrame getInstance(AbstractTableModel m, DailyValues ndV) {
        if (singleton == null)
            singleton = new AddRowFrame(m, ndV);
        return singleton;
    }

    public static AddRowFrame getInstance(AbstractTableModel m,
            DailyValues ndV, String nDate) {
        if (singleton == null)
            singleton = new AddRowFrame(m, ndV, nDate);
        return singleton;
    }
    */

    private void init() 
    {
        this.setBounds(150, 150, 300, 150);

        JPanel a = new JPanel(new GridLayout(0, 1));
        a.add(new JLabel(m_ic.getMessage("DATE") + ":", SwingConstants.RIGHT));
        a.add(new JLabel(m_ic.getMessage("BG") + ":", SwingConstants.RIGHT));
        a.add(new JLabel(props.getIns1Abbr() + ":", SwingConstants.RIGHT));
        a.add(new JLabel(m_ic.getMessage("ACT") + ":", SwingConstants.RIGHT));

        JPanel b = new JPanel(new GridLayout(0, 1));
        DateField = new JTextField(10);
        if (sDate != null) 
        {
            DateField.setText(sDate);
            DateField.setEditable(false);
        }
        b.add(DateField);


        b.add(BGField = new JTextField());
	components[1] = BGField;
	BGField.addKeyListener(this);

        b.add(Ins1Field = new JTextField());
	components[3] = Ins1Field;
	Ins1Field.addKeyListener(this);


        b.add(ActField = new JTextField());
	components[5] = ActField;
	ActField.addKeyListener(this);


        JPanel c = new JPanel(new GridLayout(0, 1));
        c.add(new JLabel(m_ic.getMessage("TIME") + ":", SwingConstants.RIGHT));
        c.add(new JLabel(m_ic.getMessage("BU") + ":", SwingConstants.RIGHT));
        c.add(new JLabel(props.getIns2Abbr() + ":", SwingConstants.RIGHT));
        c.add(new JLabel(m_ic.getMessage("COMMENT") + ":", SwingConstants.RIGHT));

        JPanel d = new JPanel(new GridLayout(0, 1));
        d.add(TimeField = new JTextField(10));
	components[0] = TimeField;
	TimeField.addKeyListener(this);


        d.add(BUField = new JTextField());
	components[2] = BUField;
	BUField.addKeyListener(this);


        d.add(Ins2Field = new JTextField());
	components[4] = Ins2Field;
	Ins2Field.addKeyListener(this);


        d.add(CommentField = new JTextField());
	components[6] = CommentField;
	CommentField.addKeyListener(this);


        Box e = Box.createHorizontalBox();
        e.add(a);
        e.add(b);
        e.add(c);
        e.add(d);

        Box g = Box.createHorizontalBox();
        AddButton = new JButton(m_ic.getMessage("OK"));
	components[7] = AddButton;
	AddButton.addKeyListener(this);
	AddButton.setActionCommand("ok");
	AddButton.addActionListener(this);

        g.add(Box.createHorizontalGlue());
        getRootPane().setDefaultButton(AddButton);

        g.add(AddButton);
        JButton CloseButton = new JButton(m_ic.getMessage("CANCEL"));
	components[8] = CloseButton;
	CloseButton.addKeyListener(this);
	CloseButton.setActionCommand("close");
	CloseButton.addActionListener(this);


        g.add(Box.createHorizontalStrut(10));
        g.add(CloseButton);
        g.add(Box.createHorizontalGlue());
        this.getContentPane().add(g, BorderLayout.SOUTH);

        getContentPane().add(e, BorderLayout.NORTH);
        getContentPane().add(g, BorderLayout.SOUTH);

	this.setVisible(true);
    }


    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
	String action = e.getActionCommand();

	if (action.equals("close"))
	{
	    this.dispose();
	}
	else if (action.equals("ok"))
	{
            if (this.m_add_action) 
            {
                // add
                dV.setNewRow(new DailyValuesRow(DateField.getText(),
                        TimeField.getText(), BGField.getText(), Ins1Field
                                .getText(), Ins2Field.getText(), BUField
                                .getText(), ActField.getText(),
                        CommentField.getText()));
                //mod.fireTableChanged(null);
                //clearFields();
                this.m_actionDone = true;
                this.dispose();
            }
            else
            {
                // edit
            }
	}

    }

    public boolean actionSuccesful()
    {
        return m_actionDone;
    }


    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {}

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     */
    public void keyReleased(KeyEvent e)
    {
	int change = 0;

//	System.out.println(e.getKeyText(e.getKeyCode()));

	if ((e.getKeyCode() == KeyEvent.VK_LEFT)) // || (e.getKeyCode() == KeyEvent.VK_TAB)
	    change = -1;
	else if ((e.getKeyCode() == KeyEvent.VK_RIGHT) || (e.getKeyCode() == KeyEvent.VK_TAB))
	    change = 1;
	else if (e.getKeyCode() == KeyEvent.VK_DOWN)
	    change = 2;
	else if (e.getKeyCode() == KeyEvent.VK_UP)
	    change = -2;

//	System.out.println("Change: " + change);

	if (change==0)
	    return;

	JComponent cmp = (JComponent)e.getComponent();
	
	int search = 0;

	for (int i=0; i<9; i++)
	{
	    if (components[i].equals(cmp))
	    {
		search = i;
//		System.out.println("Search: " + search);
		break;
	    }
	}

	int newres = search+change;

//	System.out.println("New Res: " + newres);

	if (newres<0)
	{
	    newres = 8+newres;
//	    System.out.println("New Res2: " + newres);
	}
	else if (newres>8)
	{
	    newres = newres-8;
//	    System.out.println("New Res2: " + newres);
	}

	components[newres].requestFocus();

//	System.out.println("Key Event: ");
    }


/*
    private void close() 
    {
        this.dispose();
        singleton = null;
    }
*/
 /*   private void clearFields() 
    {
        TimeField.setText("");
        BGField.setText("");
        Ins1Field.setText("");
        Ins2Field.setText("");
        BUField.setText("");
        ActField.setText("");
        CommentField.setText("");
    }*/

    /*
    private class CloseListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            close();
        }
    }*/
}
