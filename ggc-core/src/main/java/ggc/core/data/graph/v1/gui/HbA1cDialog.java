package ggc.core.data.graph.v1.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.util.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.graphics.graphs.GraphViewerPanel;
import com.atech.graphics.layout.TableLayoutUtil;
import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;

import ggc.core.data.HbA1cValues;
import ggc.core.data.graph.v1.db.GraphV1DbRetriever;
import ggc.core.data.graph.v1.view.GraphViewHbA1c;
import ggc.core.util.DataAccess;
import info.clearthought.layout.TableLayout;

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
 *  Filename:     HbA1cDialog2  
 *  Description:  Dialog for HbA1c, using new graph framework
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class HbA1cDialog extends JDialog implements ActionListener, HelpCapable, ChangeListener, ItemListener
{

    private static final long serialVersionUID = 8918250955552579544L;
    private static final Logger LOG = LoggerFactory.getLogger(HbA1cDialog.class);

    private JButton helpButton;

    private DataAccess m_da = null;
    private I18nControlAbstract m_ic = null;

    GraphViewHbA1c graphView;
    private HbA1cValues hbValues;

    Map<String, JLabel> mapValueLabels = new HashMap<String, JLabel>();
    private JSpinner spinnerEnd;
    private SpinnerDateModel endSpinnerDateModel = null;
    GregorianCalendar calendarSelected;
    private JComboBox methodCombo;
    private JButton drawButton;


    // public HbA1cDialog()
    // {
    // init();
    // }

    /**
     * Constructor
     * 
     * @param da
     */
    public HbA1cDialog(DataAccess da, GraphV1DbRetriever dbRetriever)
    {
        super(da.getMainParent(), "HbA1c", true);
        this.m_da = da;
        this.m_ic = da.getI18nControlInstance();
        m_da.addComponent(this);

        this.calendarSelected = new GregorianCalendar();
        graphView = new GraphViewHbA1c(dbRetriever);
        graphView.setCalendar(this.calendarSelected);
        this.hbValues = graphView.getDataObject();

        init();
        updateValueLabels();

        this.m_da.enableHelp(this);
        this.m_da.centerJDialog(this);

        this.setVisible(true);
    }


    private void updateValueLabels()
    {
        setValueLabel("HBA1C_VALUE", DataAccess.Decimal2Format.format(getHbA1cValue()) + " %");
        setValueLabel("VALUATION", hbValues.getValuation());
        setValueLabel("BG_AVG", DataAccess.Decimal2Format.format(hbValues.getAvgBG()));
        setValueLabel("READINGS", hbValues.getReadings() + "");
        setValueLabel("READINGS_SLASH_DAY", DataAccess.Decimal2Format.format(hbValues.getReadingsPerDay()));
    }


    private float getHbA1cValue()
    {
        if (this.methodCombo.getSelectedIndex() == 0)
            return hbValues.getHbA1c_Method1();
        else if (this.methodCombo.getSelectedIndex() == 1)
            return hbValues.getHbA1c_Method2();
        else
            return hbValues.getHbA1c_Method3();
    }


    private void init()
    {
        ATSwingUtils.initLibrary();

        this.setTitle(m_ic.getMessage("CALCULATED_HBA1C"));

        getContentPane().setLayout(new BorderLayout());

        this.setLayout(TableLayoutUtil.createHorizontalLayout(0.65, 0.35));

        GraphViewerPanel gvp = new GraphViewerPanel(graphView);
        getContentPane().add(gvp, "0, 0");

        // right panel
        JPanel rightPanel = new JPanel(new BorderLayout());
        getContentPane().add(rightPanel, "1, 0");

        rightPanel.setLayout(TableLayoutUtil.createVerticalLayout(0.2, 0.2, 0.30, TableLayout.FILL, 35));

        // panel: calculated HbA1c
        JPanel panelTitle = new JPanel();
        panelTitle.setLayout(TableLayoutUtil.createVerticalLayout(0.33, TableLayout.FILL, 0.33));

        JLabel label = new JLabel(m_ic.getMessage("CALCULATED_HBA1C"));
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panelTitle.add(label, "0, 1");
        rightPanel.add(panelTitle, "0, 0");

        // panel: current hba1c

        JPanel panelCurrentHbA1cPanel = new JPanel();
        panelCurrentHbA1cPanel.setLayout(TableLayoutUtil.createVerticalLayout(0.1, 0.35, 0.05, 0.35, TableLayout.FILL));

        // current hba1c
        label = new JLabel(m_ic.getMessage("YOUR_CURRENT_HBA1C") + ":");
        label.setFont(new Font("Dialog", Font.BOLD, 14));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        panelCurrentHbA1cPanel.add(label, "0, 1");

        label = createValueLabel("HBA1C_VALUE");
        label.setFont(new Font("Dialog", Font.BOLD, 18));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panelCurrentHbA1cPanel.add(label, "0, 3");

        rightPanel.add(panelCurrentHbA1cPanel, "0, 1");

        // values
        JPanel panelValues = new JPanel();

        double sizes[][] = { { 0.1, 0.35, 0.05, 0.4, 0.1 }, { 0.25, 0.1, 0.2, 0.2, 0.2, 0.05 } };
        panelValues.setLayout(new TableLayout(sizes));

        // // valuation
        panelValues.add(new JLabel(m_ic.getMessage("VALUATION") + ":"), "1, 0");
        panelValues.add(createValueLabel("VALUATION"), "3, 0");

        panelValues.add(new JLabel(m_ic.getMessage("BG") + " " + m_ic.getMessage("AVG") + ":"), "1, 2");
        panelValues.add(createValueLabel("BG_AVG"), "3, 2");

        panelValues.add(new JLabel(m_ic.getMessage("READINGS") + ":"), "1, 3");
        panelValues.add(createValueLabel("READINGS"), "3, 3");

        panelValues.add(new JLabel(m_ic.getMessage("READINGS_SLASH_DAY") + ":"), "1, 4");
        panelValues.add(createValueLabel("READINGS_SLASH_DAY"), "3, 4");

        rightPanel.add(panelValues, "0, 2");

        // controler
        JPanel panelControler = new JPanel();

        double sizes3[][] = { { 0.1, 0.35, 0.05, 0.4, 0.1 }, { 0.45, 28, 28, 30, 0.1, 28, 0.45 } };
        panelControler.setLayout(new TableLayout(sizes3));

        panelControler.add(new JLabel(m_ic.getMessage("STARTING_DATE") + ":"), "1, 1");
        panelControler.add(createValueLabel("STARTING_DATE"), "3, 1");

        panelControler.add(new JLabel(m_ic.getMessage("ENDING_DATE") + ":"), "1, 2");

        endSpinnerDateModel = new SpinnerDateModel();
        endSpinnerDateModel.setCalendarField(Calendar.DAY_OF_WEEK);

        setEndDate();

        panelControler.add(spinnerEnd = new JSpinner(endSpinnerDateModel), "3, 2");
        ((JSpinner.DateEditor) spinnerEnd.getEditor()).getFormat().applyPattern("dd.MM.yyyy");

        spinnerEnd.addChangeListener(this);

        panelControler.add(new JLabel(m_ic.getMessage("METHOD") + ":"), "1, 3");

        String[] methods = { String.format(m_ic.getMessage("METHOD_NR"), 1), //
                             String.format(m_ic.getMessage("METHOD_NR"), 2), //
                             String.format(m_ic.getMessage("METHOD_NR"), 3), //
        };

        panelControler.add(methodCombo = new JComboBox(methods), "3, 3");
        methodCombo.setSelectedIndex(2);
        methodCombo.addItemListener(this);

        JButton button;

        panelControler.add(button = new JButton(m_ic.getMessage("DRAW")), "3, 5");
        button.addActionListener(this);
        button.setActionCommand("draw");
        // button.setEnabled(false);

        drawButton = button;

        rightPanel.add(panelControler, "0, 3");

        // panel: buttons

        JPanel panelButtons = new JPanel();

        double sizes2[][] = { { 0.1, 0.375, 0.05, 0.375, 0.1 }, { 0.1, 0.8, 0.1 } };

        panelButtons.setLayout(new TableLayout(sizes2));

        button = new JButton(" " + m_ic.getMessage("CLOSE"));
        button.setIcon(ATSwingUtils.getImageIcon_22x22("cancel.png", this, m_da));
        button.addActionListener(this);
        button.setActionCommand("close");

        panelButtons.add(button, "1, 1");

        this.helpButton = ATSwingUtils.createHelpButton(this, m_da);

        panelButtons.add(this.helpButton, "3, 1");

        rightPanel.add(panelButtons, "0, 4");

        setSize(750, 550);
    }


    private void setEndDate()
    {
        System.out.println("DateRangeSelectorPanel: " + m_da.getGregorianCalendarDateAsString(calendarSelected));

        // endSpinnerDateModel.setValue(calendarSelected.getTime());

        GregorianCalendar calStart = (GregorianCalendar) calendarSelected.clone();
        calStart.add(Calendar.MONTH, -3);

        setValueLabel("STARTING_DATE", m_da.getGregorianCalendarDateAsString(calStart));
    }


    private JLabel createValueLabel(String key)
    {
        JLabel label = new JLabel();
        mapValueLabels.put(key, label);

        return label;
    }


    private void setValueLabel(String key, String value)
    {
        mapValueLabels.get(key).setText(value);
    }


    private void closeDialog()
    {
        graphView = null;
        this.dispose();
        m_da.removeComponent(this);
    }


    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
        String action = e.getActionCommand();

        if (action.equals("close"))
        {
            this.closeDialog();
        }
        else if (action.equals("draw"))
        {
            this.graphView.setCalendar(this.calendarSelected);
            this.hbValues = this.graphView.getDataObject();
            this.updateValueLabels();
            // this.drawButton.setEnabled(false);
        }
        else
        {
            System.out.println("HbA1cDialog:Unknown command: " + action);
        }
    }


    // ****************************************************************
    // ****** HelpCapable Implementation *****
    // ****************************************************************

    /**
     * getComponent - get component to which to attach help context
     */
    public Component getComponent()
    {
        return this.getRootPane();
    }


    /**
     * getHelpButton - get Help button
     */
    public JButton getHelpButton()
    {
        return this.helpButton;
    }


    /**
     * getHelpId - get id for Help
     */
    public String getHelpId()
    {
        return graphView.getHelpId();
    }

    long lastChange = 0L;


    public void stateChanged(ChangeEvent e)
    {
        if (checkLastChange())
        {
            try
            {
                this.spinnerEnd.commitEdit();

            }
            catch (ParseException ex)
            {
                LOG.debug("Parse Exception: {}.", ex.getMessage(), ex);
            }

            GregorianCalendar gc = new GregorianCalendar();
            Date d = (Date) this.spinnerEnd.getValue();

            // LOG.debug("Spiner: {}.{}.{}", d.getDay(), (d.getMonth() + 1),
            // (d.getYear() + 1900));
            //
            // Date dv = (Date) this.spinnerEnd.getModel().getValue();
            //
            // LOG.debug("SpinerModel: {}.{}.{}", dv.getDay(), (dv.getMonth() +
            // 1), (dv.getYear() + 1900));

            gc.setTimeInMillis(d.getTime());

            this.calendarSelected = gc;
            setEndDate();
            // this.drawButton.setEnabled(true);
        }
    }


    private boolean checkLastChange()
    {
        if (System.currentTimeMillis() - lastChange > 1000)
        {
            this.lastChange = System.currentTimeMillis();
            return true;
        }

        return false;
    }


    public void itemStateChanged(ItemEvent e)
    {
        if (checkLastChange())
        {
            this.lastChange = System.currentTimeMillis();
            // this.drawButton.setEnabled(true);
        }
    }
}
