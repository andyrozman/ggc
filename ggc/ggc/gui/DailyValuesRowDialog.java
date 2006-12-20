/*
 * Created on 16.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.gui;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import ggc.data.DailyValuesRow;
import ggc.util.BGInputVerifier;
import ggc.util.DataAccess;
import ggc.util.DateInputVerifier;
import ggc.util.GGCProperties;
import ggc.util.I18nControl;
import ggc.util.TimeInputVerifier;


/**
 * @author stephan
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class DailyValuesRowDialog extends JDialog implements ActionListener
{

    private I18nControl m_ic = I18nControl.getInstance();    
    private DataAccess m_da = DataAccess.getInstance();

    private DailyValuesRow dailyValuesRow = null;

    private JTextField dateField = new JTextField();
    private JTextField timeField = new JTextField();
    private JTextField bgField = new JTextField();
    private JTextField ins1Field = new JTextField();
    private JTextField ins2Field = new JTextField();
    private JTextField buField = new JTextField();
    private JTextField actField = new JTextField();
    private JTextArea commentField = new JTextArea();

    /**
     * @see java.awt.Window#Window(Frame)
     */
    public DailyValuesRowDialog(Frame owner)
    {
        this(owner, null);
    }

    /**
     * Method DailyValuesRowDialog.
     * @param owner
     * @param row
     */
    public DailyValuesRowDialog(Frame owner, DailyValuesRow row)
    {
        super(owner);

	dailyValuesRow = row;
        //if (row == null)
        //    dailyValuesRow = new DailyValuesRow();
        initialize();
    }

    /**
     * @see java.awt.Dialog#Dialog(Dialog)
     */
    public DailyValuesRowDialog(Dialog owner)
    {
        this(owner, null);
    }

    /**
     * Method DailyValuesRowDialog.
     * @param owner
     * @param row
     */
    public DailyValuesRowDialog(Dialog owner, DailyValuesRow row)
    {
        super(owner);

	dailyValuesRow = row;
	//if (row == null)
        //    dailyValuesRow = new DailyValuesRow();
        initialize();
    }

    /**
     * Method getNewDailyValuesRow.
     * @param owner
     * @return DailyValuesRow
     */
    public static DailyValuesRow getNewDailyValuesRow(Frame owner)
    {
        DailyValuesRowDialog dialog = new DailyValuesRowDialog(owner);
        dialog.setModal(true);

        dialog.setLocationRelativeTo(owner);
        dialog.setVisible(true);

        return dialog.getDailyValuesRow();
    }

    /**
     * Method getNewDailyValuesRow.
     * @param owner
     * @return DailyValuesRow
     */
    public static DailyValuesRow getNewDailyValuesRow(Dialog owner)
    {
        DailyValuesRowDialog dialog = new DailyValuesRowDialog(owner);
        dialog.setModal(true);

        dialog.setLocationRelativeTo(owner);
        dialog.pack();
        //dialog.show();

        return dialog.getDailyValuesRow();
    }

    /**
     * Returns the dailyValuesRow.
     * @return DailyValuesRow
     */
    public DailyValuesRow getDailyValuesRow()
    {
        return dailyValuesRow;
    }

    /**
     * Sets the dailyValuesRow.
     * @param dailyValuesRow The dailyValuesRow to set
     */
    public void setDailyValuesRow(DailyValuesRow dailyValuesRow)
    {
        this.dailyValuesRow = dailyValuesRow;
    }

    protected void initialize()
    {

        JPanel content = new JPanel(new BorderLayout(5, 15))
        {
            @Override
            public Insets getInsets()
            {
                return new Insets(8, 8, 8, 8);
            }

            @Override
            public Insets getInsets(Insets insets)
            {
                insets.top = 8;
                insets.left = 8;
                insets.bottom = 8;
                insets.right = 8;
                return insets;
            }
        };
        setContentPane(content);

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        JLabel label = null;

        JPanel dataContainer = new JPanel(gridbag);
        dataContainer.setBorder(new TitledBorder(m_ic.getMessage("ADD_NEW_VALUES")));
        content.add(dataContainer, "Center");


        dateField.setInputVerifier(new DateInputVerifier());
        addComponent(m_ic.getMessage("DATE") + " [dd.mm.yyyy] *", dateField, 0.0f, dataContainer);

        timeField.setInputVerifier(new TimeInputVerifier());
        addComponent(m_ic.getMessage("TIME") + " [hh:mm] *", timeField, 0.0f, dataContainer);

        bgField.setInputVerifier(new BGInputVerifier(false));
        addComponent(m_ic.getMessage("BG"), bgField, 0.0f, dataContainer);

        addComponent(m_da.getSettings().getIns1Abbr(), ins1Field, 0.0f, dataContainer);

        addComponent(m_da.getSettings().getIns2Abbr(), ins2Field, 0.0f, dataContainer);

        addComponent(m_ic.getMessage("BU"), buField, 0.0f, dataContainer);

        addComponent(m_ic.getMessage("ACT"), actField, 0.0f, dataContainer);

        commentField.setPreferredSize(new Dimension(200, 50));
        addComponent(m_ic.getMessage("COMMENT"), new JScrollPane(commentField), 1.0f, dataContainer);
        //addComponent("Comment", commentField, dataContainer);



        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton(m_ic.getMessage("CANCEL")); //new CloseAction());
	closeButton.addActionListener(this);
	closeButton.setActionCommand("cancel");
	closeButton.setMnemonic(KeyEvent.VK_C);


//	super("Cancel");
//	setName(m_ic.getMessage("CANCEL"));
//	putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_C));

        JButton addButton = new JButton(); //new AddAction());
	addButton.addActionListener(this);
	addButton.setActionCommand("add");
	addButton.setMnemonic(KeyEvent.VK_O);
	//putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));



        buttonPanel.add(addButton);
        buttonPanel.add(closeButton);
        content.add(buttonPanel, "South");

    }


    private int y = 0;

    public void addComponent(String label, Component comp, float weighty, Container container)
    {
        GridBagConstraints cons = new GridBagConstraints();
        GridBagLayout gridBag = (GridBagLayout)container.getLayout();
        cons.gridy = y++;
        cons.gridheight = 1;
        cons.gridwidth = 1;
        cons.weightx = 0.0f;
        cons.insets = new Insets(1, 0, 1, 0);
        cons.fill = GridBagConstraints.BOTH;
        cons.anchor = GridBagConstraints.NORTH;

        JLabel l = new JLabel(label, SwingConstants.LEFT);
        l.setVerticalAlignment(SwingConstants.TOP);
        l.setBorder(new EmptyBorder(0, 0, 0, 12));
        gridBag.setConstraints(l, cons);
        container.add(l);

        cons.gridx = 1;
        cons.weightx = 1.0f;
        cons.weighty = weighty;
        gridBag.setConstraints(comp, cons);
        container.add(comp);
    }


    public boolean verifyInput()
    {
        InputVerifier verifier = dateField.getInputVerifier();
        if (!verifier.shouldYieldFocus(dateField)) 
        {
            dateField.requestFocus();
            return false;
        }

        verifier = timeField.getInputVerifier();
        if (!verifier.shouldYieldFocus(timeField)) 
        {
            timeField.requestFocus();
            return false;
        }

        verifier = bgField.getInputVerifier();
        if (!verifier.shouldYieldFocus(bgField)) 
        {
            bgField.requestFocus();
            return false;
        }

        return true;
    }


    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
    	String action = e.getActionCommand();
    
    	if (action.equals("add"))
    	{
    	    if (!verifyInput())
    		return;
    
    	    dailyValuesRow = new DailyValuesRow(dateField.getText(), timeField.getText(), bgField.getText(), ins1Field.getText(), ins2Field.getText(), buField.getText(), actField.getText(), commentField.getText());
    	    this.dispose();
    	}
    	else if (action.equals("cancel"))
    	{
    	    this.dispose();
    	}
    }
    

    //////////////////////////////////////////////////////////////
    // Action classes
/*    protected class AddAction extends AbstractAction
    {
        public AddAction()
        {
            super();
            putValue(Action.NAME, m_ic.getMessage("OK"));
            putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_O));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
         */
/*        public void actionPerformed(ActionEvent e)
        {
            if (!verifyInput())
                return;

            dailyValuesRow = new DailyValuesRow(dateField.getText(), timeField.getText(), bgField.getText(), ins1Field.getText(), ins2Field.getText(), buField.getText(), actField.getText(), commentField.getText());

            DailyValuesRowDialog.this.dispose();
        }

    }

    protected class CloseAction extends AbstractAction
    {
        public CloseAction()
        {
            super("Cancel");
            setName(m_ic.getMessage("CANCEL"));
            putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_C));
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
         */
/*        public void actionPerformed(ActionEvent e)
        {
            dailyValuesRow = null;
            DailyValuesRowDialog.this.dispose();
        }

    } */




}
