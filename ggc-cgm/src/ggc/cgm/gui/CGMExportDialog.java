package ggc.cgm.gui;

import ggc.cgm.data.CGMValuesEntry;
import ggc.cgm.data.cfg.CGMConfigEntry;
import ggc.cgm.output.OutputWriter;
import ggc.cgm.plugin.CGMPlugInServer;
import ggc.cgm.util.DataAccessCGM;
import ggc.cgm.util.I18nControl;
import ggc.core.db.hibernate.DayValueH;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.LineBorder;

import com.atech.graphics.components.StatusReporterInterface;

public class CGMExportDialog extends JDialog implements ActionListener, StatusReporterInterface
{

    CGMPlugInServer server;
    
    
    /* 
     * setStatus
     */
    public void setStatus(int status)
    {
        if (this.progress!=null)
            this.progress.setValue(status);
        
        if (status==100)
        {
            bt_close.setEnabled(true);
            this.lbl_status.setText(m_ic.getMessage("EXPORT_STATUS_FINISHED"));

        }
    }



    private DataAccessCGM m_da = DataAccessCGM.getInstance();
    private I18nControl m_ic = m_da.getI18nInstance();

    public JProgressBar progress = null;


    
    private Hashtable<String,ArrayList<DayValueH>> meter_data = null;
    CGMConfigEntry configured_meter;
    
    
    private JButton bt_close, bt_start;
    JLabel lbl_status;

    
    
    
    
    
    
    
    /*
     * Constructor for ReadMeterDialog.
     * 
     * @param owner
     * 
     * @throws HeadlessException
     */
/*    public MeterDisplayDataDialog(JFrame owner, MeterInterface mi)
    {
        super(owner);
        m_da.addComponent(this);
        meter_interface = mi;
        this.parentMy = owner;

        dialogPreInit();
    }

    public MeterDisplayDataDialog(MeterInterface mi)
    {
        super();
        m_da.addComponent(this);
        meter_interface = mi;

        dialogPreInit();
    }
*/
    
    public CGMExportDialog()
    {
        super();
        dialogPreInit(false);
    }

    
    public CGMExportDialog(ArrayList<CGMValuesEntry> lst, OutputWriter writer)
    {
        super();
        dialogPreInit(false);
    }
    

    
    public CGMExportDialog(CGMConfigEntry mce)
    {
        super();

        this.configured_meter = mce;
        dialogPreInit(false);
    }
    

    public CGMExportDialog(JDialog parent, Hashtable<String,ArrayList<DayValueH>> meter_data, CGMPlugInServer server)
    {
        super();

        this.meter_data = meter_data;
        this.m_da.centerJDialog(this, parent);
        this.server = server;
        
        dialogPreInit(true);
    }
    
    
    


    private void dialogPreInit(boolean start)
    {
        setTitle(m_ic.getMessage("EXPORT_METER_DATA"));

        m_da.addComponent(this);

        init();
     
        if (start)
        {
            this.bt_start.setVisible(false);
            setStart();
        }

        this.setVisible(true);
    }
    
    
    private void setStart()
    {
        this.started = true;
        this.server.setReturnData(this.meter_data, this);
        this.bt_close.setEnabled(false);
        this.lbl_status.setText(m_ic.getMessage("EXPORT_STATUS_EXPORTING"));
    }
    
    
    


    protected void init()
    {

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setSize(320, 380);

        JLabel label;

        setBounds(0, 0, 320, 380);
        m_da.centerJDialog(this);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().add(panel, BorderLayout.CENTER);

        
        label = new JLabel(m_ic.getMessage("EXPORT_METER_DATA"));
        label.setFont(m_da.getFont(DataAccessCGM.FONT_BIG_BOLD));
        label.setBounds(0, 15, 320, 35);
        label.setHorizontalAlignment(JLabel.CENTER);
        panel.add(label);
        
        
        Font normal = m_da.getFont(DataAccessCGM.FONT_NORMAL);
        Font bold = m_da.getFont(DataAccessCGM.FONT_NORMAL_BOLD);


        label = new JLabel(m_ic.getMessage("EXPORT_OUTPUT") + ":");
        label.setBounds(30, 70, 310, 25);
        label.setFont(bold);
        panel.add(label);

        String[] exp = 
        {
             m_ic.getMessage("GGC_APPLICATION")
        };
        
        
        JComboBox cb = new JComboBox(exp);
        cb.setBounds(30,95,250,23);
        //cb.setEnabled(false);
        panel.add(cb);
        
        // progress
        label = new JLabel(m_ic.getMessage("EXPORT_PROGRESS") + ":");
        label.setBounds(30, 155, 200, 25);
        label.setFont(bold);
        panel.add(label);


        lbl_status = new JLabel(m_ic.getMessage("EXPORT_STATUS_READY"));
        lbl_status.setBounds(130, 155, 150, 25);
        lbl_status.setHorizontalAlignment(JLabel.RIGHT);
        lbl_status.setFont(normal);
        panel.add(lbl_status);
        
        
        this.progress = new JProgressBar();
        this.progress.setBounds(30, 180, 250, 20);
        this.progress.setStringPainted(true);
        //this.progress.setBorderPainted(true);
        this.progress.setBorder(new LineBorder(Color.black));
        this.progress.setForeground(Color.black);
        panel.add(this.progress);

        
        
        
        bt_start = new JButton(m_ic.getMessage("START"));
        bt_start.setBounds(50, 250, 100, 25);
        // bt_break.setEnabled(this.m_mim.isStatusOK());
        bt_start.setActionCommand("start");
        bt_start.addActionListener(this);
        panel.add(bt_start);

        JButton help_button = m_da.createHelpButtonByBounds(30, 310, 110, 25, this);
        panel.add(help_button);

        bt_close = new JButton(m_ic.getMessage("CLOSE"));
        bt_close.setBounds(170, 250, 100, 25);
        bt_close.setEnabled(false);
        bt_close.setActionCommand("close");
        bt_close.addActionListener(this);
        panel.add(bt_close);

    }


    

    public JButton createButton(String command_text, String tooltip, String image_d)
    {
        JButton b = new JButton();
        b.setIcon(m_da.getImageIcon(image_d, 15, 15, this));
        b.addActionListener(this);
        b.setActionCommand(command_text);
        b.setToolTipText(tooltip);
        return b;
    }


    /*
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
        String action = e.getActionCommand();

        if (action.equals("close"))
        {
            if (started)
                this.action = true;
            
            this.m_da.removeComponent(this);
            this.dispose();
        }
        else
            System.out.println("MeterExportDialog::Unknown command: " + action);

    }

    boolean started = false;
    boolean action = false;
    
    public boolean wasAction()
    {
        return this.action;
        
    }
    
    
    
    public static void main(String[] args)
    {
        // MeterReadDialog mrd =
        new CGMExportDialog(); // new AscensiaContour("COM12", new
                                      // ConsoleOutputWriter()));
    }


}
