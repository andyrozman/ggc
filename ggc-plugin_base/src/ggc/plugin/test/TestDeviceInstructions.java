package ggc.plugin.test;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.atech.utils.ATSwingUtils;

public class TestDeviceInstructions implements ActionListener
{
    JTextArea ta_html;
    JLabel label_ins;
    
    public TestDeviceInstructions()
    {
        JFrame mainFrame = new JFrame("HTML");
        mainFrame.setLayout(null);
        mainFrame.setBounds(20, 20, 700, 700);
        ATSwingUtils.initLibrary();
        
        ta_html = new JTextArea();
        ta_html.setBounds(20, 20, 600, 175);
        ta_html.setLineWrap(true);

        mainFrame.add(ta_html, null);

        JButton button1 = new JButton("<html><font color='#FF0000'>JButton Text</font></html>");
        JLabel label1 = new JLabel("<html><font color='#FF0000'>Red Text</font>"
                + "<br /><font color='#00FF00'>Blue Text</font>" + "<br /><font color='#0000FF'>Green Text</font>"
                + "<br /><font color='#000000'>Black Text</font>"
                + "<br /><font color='#FFFFFF'>Blue Text</font></html>");
        button1.setBounds(20,320,100, 25);
        button1.addActionListener(this);
        
        mainFrame.add(button1, null);
        
        JPanel panel_instruct = ATSwingUtils.getPanel(300, 190, 330, 200, 
            new FlowLayout(), 
            new TitledBorder("INSTRUCTIONS"), 
            mainFrame.getContentPane());
        
        label_ins = ATSwingUtils.getLabel("", 
            5, 0, 280, 180, panel_instruct, ATSwingUtils.FONT_NORMAL_SMALLER);
        label_ins.setVerticalAlignment(SwingConstants.TOP);
        label_ins.setHorizontalAlignment(SwingConstants.LEFT);
        label_ins.setBackground(Color.green);
        
        
        //mainFrame.add(button1);
        mainFrame.add(panel_instruct);

        mainFrame.setDefaultCloseOperation(mainFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        //mainFrame.pack();
    }

    public static void main(String[] args)
    {
        new TestDeviceInstructions();
    }

    public void actionPerformed(ActionEvent e)
    {
        // TODO Auto-generated method stub
        this.label_ins.setText(ta_html.getText());
        
    }
}