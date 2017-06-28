package ggc.gui.dialogs.config;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;

import ggc.core.data.cfg.ConfigCellRenderer;
import ggc.core.data.defs.GlucoseUnitType;
import ggc.core.enums.PropertiesDialogType;
import ggc.core.util.DataAccess;
import ggc.gui.dialogs.config.panels.*;

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
 *  Filename:     PropertiesDialog
 *  Description:  Dialog for setting properties for application.
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

// REFACTOR
public class PropertiesDialog extends JDialog implements ListSelectionListener, ActionListener, HelpCapable
{

    private static final long serialVersionUID = -5813992933782713913L;

    private DataAccess dataAccess = DataAccess.getInstance();
    private I18nControlAbstract i18nControl = dataAccess.getI18nControlInstance();

    private JList list = null;
    private JPanel mainEditPanel;
    private JButton helpButton;

    private ArrayList<AbstractPrefOptionsPanel> panels = null;

    private int currentIndex = 0;
    private boolean okAction = false;

    // used for updating preferences correctly when glucoseUnit changes
    private PrefFontsAndColorPane fontsAndColorPane = null;
    private PrefRenderingQualityPane renderingQualityPane = null;
    AbstractPrefOptionsPanel selectedPanel = null;
    PropertiesDialogType propertiesDialogType;

    /**
     * Config types
     */
    public String configTypes[] = { //
                                    i18nControl.getMessage("GLOBAL"), //
                                    i18nControl.getMessage("MODE"), //
                                    i18nControl.getMessage("GENERAL"), //
                                    i18nControl.getMessage("MEDICAL_DATA"), //
                                    i18nControl.getMessage("COLORS_AND_FONTS"), //
                                    i18nControl.getMessage("RENDERING_QUALITY"), //
                                    i18nControl.getMessage("PRINTING"), //
                                    i18nControl.getMessage("LANGUAGE"), //
                                    i18nControl.getMessage("PUMP"), //
                                    i18nControl.getMessage("CGMS"), //
                                    i18nControl.getMessage("USERS") };

    /**
     * Config types
     */
    public String configTypesMinimal[] = { //
                                           i18nControl.getMessage("GLOBAL"), //
                                           i18nControl.getMessage("USERS") };


    /**
     * Constructor
     * 
     * @param da
     */
    public PropertiesDialog(DataAccess da)
    {
        this(da, PropertiesDialogType.Standard);
    }


    /**
     * Constructor
     *
     * @param da
     */
    public PropertiesDialog(DataAccess da, PropertiesDialogType propertiesDialogType)
    {
        super(da.getMainParent(), "", true);

        this.propertiesDialogType = propertiesDialogType;

        setSize(640, 480);
        setTitle(i18nControl.getMessage("PREFERENCES_DEFAULT"));

        ATSwingUtils.initLibrary();

        ATSwingUtils.centerJDialog(this, da.getMainParent());

        helpButton = ATSwingUtils.createHelpButtonBySize(120, 25, this, dataAccess);
        createPanels();

        init();
        selectPanel(0);
        this.setResizable(false);
        this.setVisible(true);
    }


    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void init()
    {
        Dimension dim = new Dimension(120, 25);

        list = new JList(getConfigTypes());
        list.addListSelectionListener(this);
        ConfigCellRenderer renderer = new ConfigCellRenderer(this.propertiesDialogType);
        renderer.setPreferredSize(new Dimension(100, 75));
        list.setCellRenderer(renderer);
        list.setSelectedIndex(0);

        JScrollPane prefTreePane = new JScrollPane(list, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        mainEditPanel = new JPanel(new BorderLayout());

        // set the buttons up...
        JButton okButton = new JButton("   " + i18nControl.getMessage("OK"));
        okButton.setPreferredSize(dim);
        okButton.setIcon(ATSwingUtils.getImageIcon_22x22("ok.png", this, dataAccess));
        okButton.setActionCommand("ok");
        okButton.addActionListener(this);

        JButton cancelButton = new JButton("   " + i18nControl.getMessage("CANCEL"));
        cancelButton.setPreferredSize(dim);
        cancelButton.setIcon(ATSwingUtils.getImageIcon_22x22("cancel.png", this, dataAccess));
        cancelButton.setActionCommand("cancel");
        cancelButton.addActionListener(this);

        JButton applyButton = new JButton("   " + i18nControl.getMessage("APPLY"));
        applyButton.setPreferredSize(dim);
        applyButton.setIcon(ATSwingUtils.getImageIcon_22x22("flash.png", this, dataAccess));
        applyButton.setActionCommand("apply");
        applyButton.addActionListener(this);

        // ...and align them in a row at the buttom.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);

        buttonPanel.add(helpButton);

        mainEditPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainEditPanel.add(panels.get(0), BorderLayout.CENTER);

        getContentPane().add(prefTreePane, BorderLayout.WEST);
        // getContentPane().add(list, BorderLayout.WEST);
        getContentPane().add(mainEditPanel, BorderLayout.CENTER);
    }


    /**
     * Create Panels 
     */
    public void createPanels()
    {
        // Each node must have a panel, and panel numbers must be as they are
        // added
        // to. You must add panels in same order as you add, nodes.

        panels = new ArrayList<AbstractPrefOptionsPanel>();
        // panel_id = new Hashtable<String, String>();

        if (this.propertiesDialogType == PropertiesDialogType.Standard)
        {
            addPanel(new PrefGlobalPane(this));
            addPanel(new PrefModePane(this));
            addPanel(new PrefGeneralPane(this));
            addPanel(new PrefMedicalDataPane(this));
            addPanel(fontsAndColorPane = new PrefFontsAndColorPane(this)); //
            addPanel(renderingQualityPane = new PrefRenderingQualityPane(this));
            addPanel(new PrefPrintingPane(this));
            addPanel(new PrefLanguagePane(this));
            addPanel(new ConfigPumpPanel(this));
            addPanel(new ConfigCGMSPanel(this));
            addPanel(new ConfigUsersPanel(this));
        }
        else
        {
            addPanel(new PrefGlobalPane(this));
            addPanel(new ConfigUsersPanel(this));
        }
    }


    protected String[] getConfigTypes()
    {
        if (this.propertiesDialogType == PropertiesDialogType.Standard)
            return configTypes;
        else
            return configTypesMinimal;
    }


    // ---
    // --- End
    // ---

    private void addPanel(AbstractPrefOptionsPanel panel)
    {
        panels.add(panel);
    }

    // /**
    // * Select Panel (string)
    // *
    // * @param s
    // */
    // public void selectPanel(String s)
    // {
    //
    // if (!panel_id.containsKey(s))
    // {
    // System.out.println("No such panel: " + s);
    // return;
    // }
    //
    // String id = panel_id.get(s);
    //
    // mainEditPanel.remove(1);
    // mainEditPanel.add(panels.get(Integer.parseInt(id)), BorderLayout.CENTER);
    // selectedPanel = panels.get(Integer.parseInt(id));
    // mainEditPanel.invalidate();
    // mainEditPanel.validate();
    // mainEditPanel.repaint();
    //
    // dataAccess.enableHelp(this);
    // }


    public void updateGlucoseUnitType(GlucoseUnitType unitType)
    {
        fontsAndColorPane.updateGlucoseUnitType(unitType);
        fontsAndColorPane.updateGraphView();

        renderingQualityPane.updateGlucoseUnitType(unitType);
        renderingQualityPane.updateGraphView();
    }


    /**
     * Select Panel (int)
     * 
     * @param index
     */
    public void selectPanel(int index)
    {
        mainEditPanel.remove(1);
        mainEditPanel.add(panels.get(index), BorderLayout.CENTER);
        selectedPanel = panels.get(index);
        mainEditPanel.invalidate();
        mainEditPanel.validate();
        mainEditPanel.repaint();

        dataAccess.enableHelp(this);
    }


    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
        String action = e.getActionCommand();

        if (action.equals("ok"))
        {
            save();
            okAction = true;
            this.dispose();
        }
        else if (action.equals("cancel"))
        {
            reset();
            this.dispose();
        }
        else if (action.equals("apply"))
        {
            save();
        }
        else
        {
            System.out.println("PropertiesFrame: Unknown command: " + action);
        }

    }


    /**
     * Was Action Successful
     * 
     * @return true if action was successful (dialog closed with OK)
     */
    public boolean actionSuccessful()
    {
        return okAction;
    }


    private void save()
    {
        for (int i = 0; i < panels.size(); i++)
        {
            AbstractPrefOptionsPanel pn = (AbstractPrefOptionsPanel) panels.get(i);
            pn.saveProps();
        }

        dataAccess.getSettings().save();
        dataAccess.getSettings().load();

        /*
         * if (dataAccess.getDbConfig().hasChanged())
         * {
         * dataAccess.getDbConfig().saveConfig();
         * }
         */
    }


    private void reset()
    {
        DataAccess.getInstance().getSettings().reload();
    }


    /**
     * Called whenever the value of the selection changes.
     * @param e the event that characterizes the change.
     */
    public void valueChanged(ListSelectionEvent e)
    {
        if (currentIndex != list.getSelectedIndex())
        {
            currentIndex = list.getSelectedIndex();
            selectPanel(currentIndex);
            // System.out.println(list.getSelectedValue());
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
        return this.selectedPanel.getHelpId();
    }

    // ---

}
