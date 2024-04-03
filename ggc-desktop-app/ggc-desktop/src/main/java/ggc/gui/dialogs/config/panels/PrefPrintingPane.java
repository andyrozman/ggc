package ggc.gui.dialogs.config.panels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileView;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

import ggc.gui.dialogs.config.PropertiesDialog;

/**
 * Application: GGC - GNU Gluco Control
 * 
 * See AUTHORS for copyright information.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Filename:     PrefPrintingPane
 * Description:  Preferences Panel to configure printer configurationManagerWrapper.
 * 
 * Author: andyrozman {andy@atech-software.com}
 */

public class PrefPrintingPane extends AbstractPrefOptionsPanel
{

    private static final long serialVersionUID = 50092067007913327L;
    private JTextField fieldEmpty, fieldPDFViewer, fieldLunchST, fieldDinnerST, fieldNightST;
    private JButton buttonBrowse;
    private JCheckBox checkUseExternalPdfViewer;


    /**
     * Constructor
     * 
     * @param dia
     */
    public PrefPrintingPane(PropertiesDialog dia)
    {
        super(dia);
        init();
        // dataAccess.enableHelp(this);
    }


    private void init()
    {
        setLayout(new BorderLayout());

        JPanel b = new JPanel(new GridLayout(0, 2));
        b.setBorder(new TitledBorder(i18nControl.getMessage("BASIC_SETTINGS")));
        b.add(new JLabel(i18nControl.getMessage("EMPTY_VALUE") + ":"));
        b.add(fieldEmpty = new JTextField(configurationManagerWrapper.getIns1Name(), 20));

        JPanel a = new JPanel(new GridLayout(0, 2));
        a.setBorder(new TitledBorder(i18nControl.getMessage("PDF_VIEWER_SETTINGS")));
        a.add(getLabelWithColon("USE_EXTERNAL_PDF_VIEWER"));
        a.add(checkUseExternalPdfViewer = new JCheckBox());

        a.add(new JLabel(i18nControl.getMessage("PDF_VIEWER") + ":"));
        a.add(fieldPDFViewer = new JTextField(configurationManagerWrapper.getIns1Name(), 27));
        a.add(new JLabel(""));
        a.add(buttonBrowse = new JButton(i18nControl.getMessage("BROWSE")));

        buttonBrowse.addActionListener(this);

        checkUseExternalPdfViewer.addItemListener(new ItemListener()
        {

            public void itemStateChanged(ItemEvent e)
            {
                setUseExternalStatus();
            }
        });

        // bg 1
        JPanel c1 = new JPanel(new GridLayout(0, 1));
        c1.setBorder(new TitledBorder(i18nControl.getMessage("SIMPLE_MONTHLY_REPORT")));

        JPanel c = new JPanel(new GridLayout(0, 3));
        // c.setBorder(new
        // TitledBorder(i18nControl.getMessage("SIMPLE_MONTHLY_REPORT")));

        c.add(getLabelWithColon("LUNCH_START_TIME"));
        c.add(new JLabel());
        c.add(this.fieldLunchST = new JTextField("", 6));

        c.add(new JLabel(i18nControl.getMessage("DINNER_START_TIME") + ":"));
        c.add(new JLabel());
        c.add(this.fieldDinnerST = new JTextField("", 3));

        c.add(new JLabel(i18nControl.getMessage("NIGHT_START_TIME") + ":"));
        c.add(new JLabel());
        c.add(this.fieldNightST = new JTextField("", 4));

        c1.add(c);

        Box i = Box.createVerticalBox();
        i.add(b);
        i.add(a);
        // i.add(d);
        i.add(c1);
        // i.add(b);

        add(i, BorderLayout.NORTH);

        // init values
        fieldEmpty.setText(configurationManagerWrapper.getPrintEmptyValue());
        fieldPDFViewer.setText(configurationManagerWrapper.getExternalPdfVieverPath());

        fieldLunchST.setText(getTimeString(configurationManagerWrapper.getPrintLunchStartTime()));
        fieldDinnerST.setText(getTimeString(configurationManagerWrapper.getPrintDinnerStartTime()));
        fieldNightST.setText(getTimeString(configurationManagerWrapper.getPrintNightStartTime()));

        this.checkUseExternalPdfViewer.setSelected(configurationManagerWrapper.getUseExternalPdfViewer());
        setUseExternalStatus();
    }


    private String getTimeString(int time)
    {
        return ATechDate.getTimeString(ATechDateType.TimeOnlyMin, time);
    }


    private void setUseExternalStatus()
    {
        this.fieldPDFViewer.setEnabled(this.checkUseExternalPdfViewer.isSelected());
        this.buttonBrowse.setEnabled(this.checkUseExternalPdfViewer.isSelected());
    }


    /**
     * Create Little Panel
     * 
     * @param _parent
     * @param comp
     */
    public void createLittlePanel(JPanel _parent, JComponent comp)
    {
        JPanel pp = new JPanel();
        pp.setLayout(new GridLayout(0, 2));
        pp.add(comp);
        pp.add(new JLabel());

        _parent.add(pp);
    }


    /**
     * Create Little Panel
     * 
     * @param _parent
     * @param comp1
     * @param comp2
     */
    public void createLittlePanel(JPanel _parent, JComponent comp1, JComponent comp2)
    {
        JPanel pp = new JPanel();
        pp.setLayout(new GridLayout(0, 3));
        pp.add(comp1);
        pp.add(comp2);
        // pp.add(new JLabel());

        _parent.add(pp);
    }


    private int getTimeValue(JTextField field, int default_value)
    {
        try
        {
            String st = field.getText();
            st = st.replaceAll(":", "");

            return Integer.parseInt(st);
        }
        catch (Exception ex)
        {
            return default_value;
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        JFileChooser jfc = new JFileChooser();

        jfc.setCurrentDirectory(new File("."));
        jfc.setDialogTitle(i18nControl.getMessage("SELECT_PDF_VIEWER"));
        jfc.setDialogType(JFileChooser.OPEN_DIALOG);
        jfc.setFileFilter(new FileFilter()
        {

            /**
             * Whether the given file is accepted by this filter.
             */
            @Override
            public boolean accept(File f)
            {
                if (f.isDirectory())
                    return true;

                int idx = f.getName().lastIndexOf(".");

                if (idx > -1)
                {
                    String ext = f.getName().toLowerCase().substring(idx);

                    if (ext.equals(".exe"))
                        return true;
                    else
                        return false;
                }
                else
                    return false;

            }


            /**
             * The description of this filter. For example: "JPG and GIF Images"
             * 
             * @see FileView#getName
             */
            @Override
            public String getDescription()
            {
                return i18nControl.getMessage("EXECUTABLE_FILES");
            }
        });

        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int res = jfc.showOpenDialog(this);

        if (res == JFileChooser.APPROVE_OPTION)
        {
            File f = jfc.getSelectedFile();
            this.fieldPDFViewer.setText(f.getPath());
        }

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void saveProps()
    {
        configurationManagerWrapper.setPrintEmptyValue(fieldEmpty.getText());
        configurationManagerWrapper.setExternalPdfVieverPath(fieldPDFViewer.getText());

        configurationManagerWrapper.setPrintLunchStartTime(getTimeValue(fieldLunchST, 1100));
        configurationManagerWrapper.setPrintDinnerStartTime(getTimeValue(fieldDinnerST, 1800));
        configurationManagerWrapper.setPrintNightStartTime(getTimeValue(fieldNightST, 2100));
        configurationManagerWrapper.setUseExternalPdfViewer(this.checkUseExternalPdfViewer.isSelected());
    }


    /**
     * {@inheritDoc}
     */
    public String getHelpId()
    {
        return "GGC_Prefs_Printing";
    }

}
