package ggc.plugin.graph.panel;

import ggc.plugin.util.DataAccessPlugInBase;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;

public class AxesEditorPanel extends JPanel
{

    DataAccessPlugInBase m_da = null;
    I18nControlAbstract m_ic = null;

    public AxesEditorPanel(DataAccessPlugInBase da)
    {
        super();
        this.m_da = da;
        this.m_ic = da.getI18nControlInstance();
        init();
    }

    private void init()
    {
        ATSwingUtils.initLibrary();
        this.setLayout(null);

        setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("AXES_DEFINITION")));
        this.setBackground(Color.magenta);

        ATSwingUtils.getLabel("Axes definitions", 20, 0, 120, 25, this, ATSwingUtils.FONT_NORMAL_BOLD);

    }

}
