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
import java.awt.Container;
import java.awt.Event;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;

import ggc.data.DailyValues;
import ggc.data.DailyValuesRow;
import ggc.util.DataAccess;
import ggc.util.GGCProperties;
import ggc.util.I18nControl;

// fix this

public class DailyRowDialog extends JDialog implements ActionListener, KeyListener
{

    private I18nControl m_ic = I18nControl.getInstance();
    private DataAccess m_da = DataAccess.getInstance();
    private GGCProperties props = m_da.getSettings();

    private boolean m_actionDone = false;

//    static AddRowFrame singleton = null;

    JTextField DateField, TimeField, BGField, Ins1Field, Ins2Field, BUField,
            ActField, CommentField, UrineField;

    JLabel label_title = new JLabel();
    JLabel label_food;

    JButton AddButton;

    String sDate = null;

    DailyValues dV = null;
    DailyValuesRow m_dailyValuesRow = null;

    //AbstractTableModel mod = null;

    //GGCProperties props = GGCProperties.getInstance();
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

    Font f_normal = m_da.getFont(DataAccess.FONT_NORMAL);
    Font f_bold = m_da.getFont(DataAccess.FONT_NORMAL);



    
    private boolean m_add_action = true;
    private Container m_parent = null;


    

    public DailyRowDialog(DailyValues ndV, String nDate, JDialog dialog) 
    {
        super(dialog, "", true);
        m_parent = dialog;
        initParameters(ndV,nDate);
    }


    public DailyRowDialog(DailyValues ndV, String nDate, JFrame frame) 
    {
        super(frame, "", true);
        m_parent = frame;
        initParameters(ndV,nDate);
    }


    public DailyRowDialog(DailyValuesRow ndr, JDialog dialog) 
    {
        super(dialog, "", true);
        m_parent = dialog;
        initParameters(ndr);
    }


    public DailyRowDialog(DailyValuesRow ndr, JFrame frame) 
    {
        super(frame, "", true);
        m_parent = frame;
        initParameters(ndr);
    }



    public void initParameters(DailyValues ndV, String nDate)
    {
        //if (add) 
            setTitle(m_ic.getMessage("ADD_NEW_ROW"));
            label_title.setText(m_ic.getMessage("ADD_NEW_ROW"));

        //else
        //    setTitle(m_ic.getMessage("EDIT_NEW_ROW"));

        sDate = nDate;
        dV = ndV;
        this.m_add_action = true;
        //mod = m;
        init();
        this.setVisible(true);
    }


    public void initParameters(DailyValuesRow ndr)
    {
        //if (add) 
        //    setTitle(m_ic.getMessage("ADD_NEW_ROW"));
        //else
        setTitle(m_ic.getMessage("EDIT_NEW_ROW"));
        label_title.setText(m_ic.getMessage("EDIT_NEW_ROW"));

        sDate = ndr.getDateAsString();
        this.m_dailyValuesRow = ndr;
        
        this.m_add_action = false;
        init();
        initValues();
        this.setVisible(true);
    }


    public void initValues()
    {
        //stem.out.println("initValues");
        // to do
        TimeField.setText(this.m_dailyValuesRow.getTimeAsString());
        BGField.setText(""+this.m_dailyValuesRow.getBG());
        Ins1Field.setText(""+this.m_dailyValuesRow.getIns1());
        Ins2Field.setText(""+this.m_dailyValuesRow.getIns2());
        BUField.setText(""+this.m_dailyValuesRow.getCH());
        ActField.setText(""+this.m_dailyValuesRow.getAct());
        CommentField.setText(this.m_dailyValuesRow.getComment());
    }


    private void init()
    {
        int x = 0;
        int y = 0;
        int width = 400;
        int height = 500;

        Rectangle bnd = m_parent.getBounds();

        x = (bnd.width/2) + bnd.x - (width/2);
        y = (bnd.height/2) + bnd.y - (height/2);
        
        this.setBounds(x, y, width, height);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, width, height);
        panel.setLayout(null);

        this.getContentPane().add(panel);

        label_title.setFont(m_da.getFont(DataAccess.FONT_BIG_BOLD));
        label_title.setHorizontalAlignment(SwingConstants.CENTER);
        label_title.setBounds(0, 15, 350, 35);
        panel.add(label_title);

        addLabel(m_ic.getMessage("DATE") + ":", 60, panel);
        addLabel(m_ic.getMessage("TIME") + ":", 90, panel);
        addLabel(m_ic.getMessage("BLOOD_GLUCOSE") + ":", 120, panel);
        addLabel(props.getIns1Name() + " (" + props.getIns1Abbr() + ") :", 150, panel);
        addLabel(props.getIns2Name() + " (" + props.getIns2Abbr() + "):", 180, panel);
        addLabel(m_ic.getMessage("CH_LONG") + ":", 210, panel);
        addLabel(m_ic.getMessage("FOOD") + ":", 240, panel);
        addLabel(m_ic.getMessage("URINE") + ":", 270, panel);
        addLabel(m_ic.getMessage("ACTIVITY") + ":", 300, panel);
        addLabel(m_ic.getMessage("COMMENT") + ":", 330, panel);

        addComponent(BGField = new JTextField(), 160, 118, 35, panel);
        addComponent(Ins2Field = new JTextField(), 160, 148, 35, panel);
        addComponent(Ins2Field = new JTextField(), 160, 178, 35, panel);
        addComponent(BUField = new JTextField(), 160, 208, 35, panel);
        addComponent(label_food = new JLabel(m_ic.getMessage("NONE")), 110, 240, 200, panel);
        addComponent(UrineField = new JTextField(), 110, 268, 220, panel);
        addComponent(ActField = new JTextField(), 110, 298, 220, panel);
        addComponent(CommentField = new JTextField(), 110, 328, 220, panel);

        String button_command[] = { "update_ch", m_ic.getMessage("UPDATE_FROM_FOOD"),
                                    "edit", m_ic.getMessage("EDIT"),
                                    "ok", m_ic.getMessage("OK"),
                                    "cancel", m_ic.getMessage("CANCEL"),
                                    "help", m_ic.getMessage("HELP")
        };

        int button_coord[] = { 210, 208, 120, 0, 
                               250, 238, 80, 0,
                               60, 370, 80, 1,
                               140, 370, 80, 1,
                               240, 370, 80, 0
        };

        JButton button;
        //int j=0;
        for (int i=0, j=0; i<button_coord.length; i+=4, j+=2)
        {
            button = new JButton(button_command[j+1]);
            button.setActionCommand(button_command[j]);

            if (button_coord[i+3]==0)
            {
                button.setEnabled(false);
            }

            addComponent(button, button_coord[i], button_coord[i+1], button_coord[i+2], panel);
            
            
        }

        
        
        //b.add(ActField = new JTextField());

        //d.add(BUField = new JTextField());
        //d.add(Ins2Field = new JTextField());
        //d.add(CommentField = new JTextField());



        //addLabel(m_ic.getMessage("DATE") + ":", 40, panel);
        //a.add(new JLabel(m_ic.getMessage("ACT") + ":", SwingConstants.RIGHT));

        
/*
        JPanel a = new JPanel(new GridLayout(0, 1));
        a.add(new JLabel(m_ic.getMessage("DATE") + ":", SwingConstants.RIGHT));
        a.add(new JLabel(m_ic.getMessage("BG") + ":", SwingConstants.RIGHT));
        a.add(new JLabel(m_da.getSettings().getIns1Abbr() + ":", SwingConstants.RIGHT));
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
        c.add(new JLabel(m_da.getSettings().getIns2Abbr() + ":", SwingConstants.RIGHT));
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

  */      

/*        
        label = new JLabel(m_ic.getMessage("NAME") + ":");
        label.setFont(m_da.getFont(DataAccess.FONT_NORMAL_BOLD));
        label.setBounds(40, 90, 80, 25);
        panel.add(label);

        tfName = new JTextField();
        tfName.setBounds(120, 90, 160, 25);
        tfName.setFont(m_da.getFont(DataAccess.FONT_NORMAL));
        panel.add(tfName);

        label = new JLabel(m_ic.getMessage("USE_AS_TEMPLATE") + ":" );
        label.setFont(m_da.getFont(DataAccess.FONT_NORMAL));
        label.setBounds(40, 130, 280, 25);
        panel.add(label);

        cb_template = new JComboBox(this.schemes_names);
        cb_template.setFont(m_da.getFont(DataAccess.FONT_NORMAL));
        cb_template.setBounds(40, 160, 230, 25);
        panel.add(cb_template);

        JButton button = new JButton(m_ic.getMessage("OK"));
        button.setFont(m_da.getFont(DataAccess.FONT_NORMAL));
        button.setActionCommand("ok");
        button.addActionListener(this);
        button.setBounds(90, 210, 80, 25);
        panel.add(button);

        button = new JButton(m_ic.getMessage("CANCEL"));
        button.setFont(m_da.getFont(DataAccess.FONT_NORMAL));
        button.setActionCommand("cancel");
        button.addActionListener(this);
        button.setBounds(180, 210, 80, 25);
        panel.add(button);
*/
        
    }


    public void addLabel(String text, int posY, JPanel parent)
    {
        JLabel label = new JLabel(text);
        label.setBounds(30, posY, 100, 25);
        label.setFont(f_bold);
        parent.add(label);
        //a.add(new JLabel(m_ic.getMessage("DATE") + ":", SwingConstants.RIGHT));
        
    }

    public void addComponent(JComponent comp, int posX, int posY, int width, JPanel parent)
    {
        //JLabel label = new JLabel(text);
        comp.setBounds(posX, posY, width, 23);
        comp.setFont(f_normal);
        parent.add(comp);
    }
    

    
    private void init_old() 
    {
        this.setBounds(150, 150, 300, 150);

        JPanel a = new JPanel(new GridLayout(0, 1));
        a.add(new JLabel(m_ic.getMessage("DATE") + ":", SwingConstants.RIGHT));
        a.add(new JLabel(m_ic.getMessage("BG") + ":", SwingConstants.RIGHT));
        a.add(new JLabel(m_da.getSettings().getIns1Abbr() + ":", SwingConstants.RIGHT));
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
        c.add(new JLabel(m_da.getSettings().getIns2Abbr() + ":", SwingConstants.RIGHT));
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
                        TimeField.getText(), 
                        checkDecimalFields(BGField.getText()), 
                        checkDecimalFields(Ins1Field.getText()), 
                        checkDecimalFields(Ins2Field.getText()), 
                        checkDecimalFields(BUField.getText()), 
                        ActField.getText(),
                        CommentField.getText()));
                //mod.fireTableChanged(null);
                //clearFields();
                this.m_actionDone = true;
                this.dispose();
            }
            else
            {
                // edit
                this.m_dailyValuesRow.setDateTime(DateField.getText(), TimeField.getText()); 
                this.m_dailyValuesRow.setBG(checkDecimalFields(BGField.getText()));
                this.m_dailyValuesRow.setIns1(checkDecimalFields(Ins1Field.getText()));
                this.m_dailyValuesRow.setIns2(checkDecimalFields(Ins2Field.getText())); 
                this.m_dailyValuesRow.setCH(checkDecimalFields(BUField.getText()));
                this.m_dailyValuesRow.setAct(ActField.getText());
                this.m_dailyValuesRow.setComment(CommentField.getText());
                //mod.fireTableChanged(null);
                //clearFields();
                this.m_actionDone = true;
                this.dispose();
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

        if ((e.getKeyCode() == KeyEvent.VK_LEFT)) // || (e.getKeyCode() == KeyEvent.VK_TAB)
            change = -1;
        else if ((e.getKeyCode() == KeyEvent.VK_RIGHT) || (e.getKeyCode() == KeyEvent.VK_TAB))
            change = 1;
        else if (e.getKeyCode() == KeyEvent.VK_DOWN)
            change = 2;
        else if (e.getKeyCode() == KeyEvent.VK_UP)
            change = -2;


        if (change==0)
            return;

        JComponent cmp = (JComponent)e.getComponent();
	
        int search = 0;

        for (int i=0; i<9; i++)
        {
            if (components[i].equals(cmp))
            {
                search = i;
                break;
            }
        }

        int newres = search+change;
        
        if (newres<0)
        {
            newres = 8+newres;
        }
        else if (newres>8)
        {
            newres = newres-8;
        }

        components[newres].requestFocus();

    }

    public String checkDecimalFields(String field)
    {
        field = field.replace(',', '.');
        return field;
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
