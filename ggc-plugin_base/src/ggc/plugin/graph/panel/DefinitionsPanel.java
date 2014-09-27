package ggc.plugin.graph.panel;

import ggc.plugin.util.DataAccessPlugInBase;

import java.awt.Color;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;

public class DefinitionsPanel extends JPanel
{
    DataAccessPlugInBase m_da = null;
    I18nControlAbstract m_ic = null;

    public DefinitionsPanel(DataAccessPlugInBase da)
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

        setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("GRAPH_DEFINITION")));
        this.setBackground(Color.yellow);

        ATSwingUtils.getLabel("Graph Definitions:", 10, 15, 200, 25, this, ATSwingUtils.FONT_NORMAL_BOLD);

        Vector<String> defs = new Vector<String>();
        defs.add("Custom...");

        ATSwingUtils.getComboBox(defs, 10, 35, 220, 25, this, ATSwingUtils.FONT_NORMAL);

        ATSwingUtils.getLabel("Graph Types:", 10, 90, 120, 25, this, ATSwingUtils.FONT_NORMAL_BOLD);

        ATSwingUtils.getComboBox(m_da.getGraphContext().getGraphTypes().getGraphTypesVector(), 10, 110, 220, 25, this,
            ATSwingUtils.FONT_NORMAL);

    }

}
