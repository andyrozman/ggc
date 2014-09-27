package ggc.core.data.graph;

import ggc.core.data.PlotData;
import ggc.core.data.ReadablePlotData;
import ggc.core.util.DataAccess;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.atech.i18n.I18nControlAbstract;

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
 *  Filename:     DataPlotSelectorPanel
 *  Description:  A panel containing checkboxes to select the data to be plotted
 *                A graphical way (using <code>{@link JCheckBox checkboxes}</code>) 
 *                to manipulate a <code>{@link PlotData}</code>.
 * 
 *  @author rumbi
 *  @author andy {andy@atech-software.com}
 */

public class DataPlotSelectorPanel extends JPanel implements ChangeListener
{
    private static final long serialVersionUID = 6420234465982434157L;
    PlotData data = new PlotData();
    DataAccess dataAccessInstance = DataAccess.getInstance();
    private I18nControlAbstract translator = dataAccessInstance.getI18nControlInstance();

    JCheckBox BGBox = new JCheckBox();
    JCheckBox BGDayAvgBox = new JCheckBox();
    JCheckBox BGReadingsBox = new JCheckBox();

    JCheckBox CHBox = new JCheckBox();
    JCheckBox CHDayAvgBox = new JCheckBox();
    JCheckBox CHSumBox = new JCheckBox();

    JCheckBox ins1Box = new JCheckBox();
    JCheckBox ins1DayAvgBox = new JCheckBox();
    JCheckBox ins1SumBox = new JCheckBox();

    JCheckBox ins2Box = new JCheckBox();
    JCheckBox ins2DayAvgBox = new JCheckBox();
    JCheckBox ins2SumBox = new JCheckBox();

    JCheckBox insTotalBox = new JCheckBox();
    JCheckBox insTotalDayAvgBox = new JCheckBox();
    JCheckBox insTotalSumBox = new JCheckBox();

    JCheckBox insPerCHBox = new JCheckBox();
    JCheckBox mealsBox = new JCheckBox();

    /**
     * Mask: BG
     */
    public static final int BG_MASK = 1;
    /**
     * Mask: BG Avg
     */
    public static final int BG_AVG_MASK = 2;
    /**
     * Mask: BG Reading
     */
    public static final int BG_READINGS_MASK = 4;

    /**
     * Mask: CH
     */
    public static final int CH_MASK = 8;
    /**
     * Mask: CH Avg
     */
    public static final int CH_AVG_MASK = 16;
    /**
     * Mask: CH Sum
     */
    public static final int CH_SUM_MASK = 32;

    /**
     * Mask: Ins1
     */
    public static final int INS1_MASK = 64;
    /**
     * Mask: Ins1 Avg
     */
    public static final int INS1_AVG_MASK = 128;
    /**
     * Mask: Ins1 Sum
     */
    public static final int INS1_SUM_MASK = 256;

    /**
     * Mask: Ins2
     */
    public static final int INS2_MASK = 512;
    /**
     * Mask: Ins2 Avg
     */
    public static final int INS2_AVG_MASK = 1024;
    /**
     * Mask: Ins2 Sum
     */
    public static final int INS2_SUM_MASK = 2048;

    /**
     * Mask: Ins Total
     */
    public static final int INS_TOTAL_MASK = 4096;
    /**
     * Mask: Ins Total Avg
     */
    public static final int INS_TOTAL_AVG_MASK = 8192;
    /**
     * Mask: Ins Total Sum
     */
    public static final int INS_TOTAL_SUM_MASK = 16384;

    /**
     * Mask: Ins/CH
     */
    public static final int INS_PER_CH_MASK = 32768;

    /**
     * Mask: Meals
     */
    public static final int MEALS_MASK = 65536;

    /**
     * Initialises the <code>{@link JPanel panel}</code> with no checkbox
     * selected.
     */
    public DataPlotSelectorPanel()
    {
        this(0);
    }

    /**
     * Initialises the <code>{@link JPanel panel}</code> with the specified
     * checkboxes selected.<br>
     * <br>
     * Use the <code>*_MASK</code> constants to select the
     * <code>{@link JCheckBox checkboxes}</code>.
     * <code>{@link #INS2_MASK} & {@link #CH_MASK}</code> will apply the action
     * to <code>{@link #ins2Box}</code> and <code>{@link #CHBox}</code>, for
     * example.
     * 
     * @param initialSelection
     *            A bitmask specifying the checkboxes to be preselected.
     */
    public DataPlotSelectorPanel(int initialSelection)
    {
        super(new GridLayout(4, 5), true);
        setBorder(BorderFactory.createTitledBorder(translator.getMessage("TO_BE_DRAWN") + ":"));
        initBoxes();
        addBoxes();
        selectBoxes(initialSelection, true);
    }

    private void initBoxes()
    {
        BGBox.setText(translator.getMessage("BG"));
        BGBox.addChangeListener(this);
        BGDayAvgBox.setText(translator.getMessage("AVG_BG_PER_DAY"));
        BGDayAvgBox.addChangeListener(this);
        BGReadingsBox.setText(translator.getMessage("READINGS"));
        BGReadingsBox.addChangeListener(this);

        CHBox.setText(translator.getMessage("BU"));
        CHBox.addChangeListener(this);
        CHDayAvgBox.setText(translator.getMessage("AVG_MEAL_SIZE"));
        CHDayAvgBox.addChangeListener(this);
        CHSumBox.setText(translator.getMessage("SUM_BU"));
        CHSumBox.addChangeListener(this);

        ins1Box.setText(dataAccessInstance.getSettings().getIns1Abbr());
        ins1Box.addChangeListener(this);
        ins1DayAvgBox.setText(translator.getMessage("AVG") + " " + dataAccessInstance.getSettings().getIns1Abbr());
        ins1DayAvgBox.addChangeListener(this);
        ins1SumBox.setText(translator.getMessage("SUM") + " " + dataAccessInstance.getSettings().getIns1Abbr());
        ins1SumBox.addChangeListener(this);

        ins2Box.setText(dataAccessInstance.getSettings().getIns2Abbr());
        ins2Box.addChangeListener(this);
        ins2DayAvgBox.setText(translator.getMessage("AVG") + " " + dataAccessInstance.getSettings().getIns2Abbr());
        ins2DayAvgBox.addChangeListener(this);
        ins2SumBox.setText(translator.getMessage("SUM") + " " + dataAccessInstance.getSettings().getIns2Abbr());
        ins2SumBox.addChangeListener(this);

        insTotalBox.setText(translator.getMessage("INSULIN"));
        insTotalBox.addChangeListener(this);
        insTotalDayAvgBox.setText(translator.getMessage("AVG_INS"));
        insTotalDayAvgBox.addChangeListener(this);
        insTotalSumBox.setText(translator.getMessage("SUM_INSULIN"));
        insTotalSumBox.addChangeListener(this);

        insPerCHBox.setText(translator.getMessage("INS_SLASH_BU"));
        insPerCHBox.addChangeListener(this);
        mealsBox.setText(translator.getMessage("MEALS"));
        mealsBox.addChangeListener(this);
    }

    private void addBoxes()
    {
        // columns contain related checkboxes, the last row contains unrelated
        // ones

        add(BGBox);
        add(CHBox);
        add(ins1Box);
        add(ins2Box);
        add(insTotalBox);

        add(BGDayAvgBox);
        add(CHDayAvgBox);
        add(ins1DayAvgBox);
        add(ins2DayAvgBox);
        add(insTotalDayAvgBox);

        add(BGReadingsBox);
        add(CHSumBox);
        add(ins1SumBox);
        add(ins2SumBox);
        add(insTotalSumBox);

        add(insPerCHBox);
        add(mealsBox);
    }

    /**
     * Use the <code>*_MASK</code> constants to select the
     * <code>{@link JCheckBox checkboxes}</code>.
     * <code>{@link #INS2_MASK} & {@link #CH_MASK}</code> will apply the action
     * to <code>{@link #ins2Box}</code> and <code>{@link #CHBox}</code>, for
     * example.
     * 
     * @param initialSelection
     *            Specifies which <code>{@link JCheckBox checkboxes}</code> to
     *            de-/select.
     * @param enable
     *            Whether to set or unset the specified
     *            <code>{@link JCheckBox checkboxes}</code>.
     */
    public void selectBoxes(int initialSelection, boolean enable)
    {
        if (initialSelection == 0)
            return;

        if ((initialSelection & BG_MASK) == BG_MASK)
        {
            BGBox.setSelected(enable);
        }
        if ((initialSelection & BG_AVG_MASK) == BG_AVG_MASK)
        {
            BGDayAvgBox.setSelected(enable);
        }
        if ((initialSelection & BG_READINGS_MASK) == BG_READINGS_MASK)
        {
            BGReadingsBox.setSelected(enable);
        }

        if ((initialSelection & CH_MASK) == CH_MASK)
        {
            CHBox.setSelected(enable);
        }
        if ((initialSelection & CH_AVG_MASK) == CH_AVG_MASK)
        {
            CHDayAvgBox.setSelected(enable);
        }
        if ((initialSelection & CH_SUM_MASK) == CH_SUM_MASK)
        {
            CHSumBox.setSelected(enable);
        }

        if ((initialSelection & INS1_MASK) == INS1_MASK)
        {
            ins1Box.setSelected(enable);
        }
        if ((initialSelection & INS1_AVG_MASK) == INS1_AVG_MASK)
        {
            ins1DayAvgBox.setSelected(enable);
        }
        if ((initialSelection & INS1_SUM_MASK) == INS1_SUM_MASK)
        {
            ins1SumBox.setSelected(enable);
        }

        if ((initialSelection & INS2_MASK) == INS2_MASK)
        {
            ins2Box.setSelected(enable);
        }
        if ((initialSelection & INS2_AVG_MASK) == INS2_AVG_MASK)
        {
            ins2DayAvgBox.setSelected(enable);
        }
        if ((initialSelection & INS2_SUM_MASK) == INS2_SUM_MASK)
        {
            ins2SumBox.setSelected(enable);
        }

        if ((initialSelection & INS_TOTAL_MASK) == INS_TOTAL_MASK)
        {
            insTotalBox.setSelected(enable);
        }
        if ((initialSelection & INS_TOTAL_AVG_MASK) == INS_TOTAL_AVG_MASK)
        {
            insTotalDayAvgBox.setSelected(enable);
        }
        if ((initialSelection & INS_TOTAL_SUM_MASK) == INS_TOTAL_SUM_MASK)
        {
            insTotalSumBox.setSelected(enable);
        }

        if ((initialSelection & INS_PER_CH_MASK) == INS_PER_CH_MASK)
        {
            insPerCHBox.setSelected(enable);
        }
        if ((initialSelection & MEALS_MASK) == MEALS_MASK)
        {
            mealsBox.setSelected(enable);
        }
    }

    /**
     * Enables the specified checkboxes.
     * 
     * @param choiceMask
     *            A bitmask specifying the checkboxes to be enabled.
     */
    public void enableChoice(int choiceMask)
    {
        setChoice(choiceMask, true);
    }

    /**
     * Disables the specified checkboxes.
     * 
     * @param choiceMask
     *            A bitmask specifying the checkboxes to be disabled.
     */
    public void disableChoice(int choiceMask)
    {
        setChoice(choiceMask, false);
    }

    /**
     * Disables or enables the specified checkboxes, depending on the value of
     * <code>enable</code>.
     * 
     * @param choiceMask
     *            A bitmask specifying the checkboxes to be enabled or disabled.
     * @param enable
     *            The checkboxes will be enabled when this is set to
     *            <code>true</code> and disabled otherwise.
     */
    public void setChoice(int choiceMask, boolean enable)
    {
        if (choiceMask == 0)
            return;

        if ((choiceMask & BG_MASK) == BG_MASK)
        {
            BGBox.setEnabled(enable);
        }
        if ((choiceMask & BG_AVG_MASK) == BG_AVG_MASK)
        {
            BGDayAvgBox.setEnabled(enable);
        }
        if ((choiceMask & BG_READINGS_MASK) == BG_READINGS_MASK)
        {
            BGReadingsBox.setEnabled(enable);
        }

        if ((choiceMask & CH_MASK) == CH_MASK)
        {
            CHBox.setEnabled(enable);
        }
        if ((choiceMask & CH_AVG_MASK) == CH_AVG_MASK)
        {
            CHDayAvgBox.setEnabled(enable);
        }
        if ((choiceMask & CH_SUM_MASK) == CH_SUM_MASK)
        {
            CHSumBox.setEnabled(enable);
        }

        if ((choiceMask & INS1_MASK) == INS1_MASK)
        {
            ins1Box.setEnabled(enable);
        }
        if ((choiceMask & INS1_AVG_MASK) == INS1_AVG_MASK)
        {
            ins1DayAvgBox.setEnabled(enable);
        }
        if ((choiceMask & INS1_SUM_MASK) == INS1_SUM_MASK)
        {
            ins1SumBox.setEnabled(enable);
        }

        if ((choiceMask & INS2_MASK) == INS2_MASK)
        {
            ins2Box.setEnabled(enable);
        }
        if ((choiceMask & INS2_AVG_MASK) == INS2_AVG_MASK)
        {
            ins2DayAvgBox.setEnabled(enable);
        }
        if ((choiceMask & INS2_SUM_MASK) == INS2_SUM_MASK)
        {
            ins2SumBox.setEnabled(enable);
        }

        if ((choiceMask & INS_TOTAL_MASK) == INS_TOTAL_MASK)
        {
            insTotalBox.setEnabled(enable);
        }
        if ((choiceMask & INS_TOTAL_AVG_MASK) == INS_TOTAL_AVG_MASK)
        {
            insTotalDayAvgBox.setEnabled(enable);
        }
        if ((choiceMask & INS_TOTAL_SUM_MASK) == INS_TOTAL_SUM_MASK)
        {
            insTotalSumBox.setEnabled(enable);
        }

        if ((choiceMask & INS_PER_CH_MASK) == INS_PER_CH_MASK)
        {
            insPerCHBox.setEnabled(enable);
        }
        if ((choiceMask & MEALS_MASK) == MEALS_MASK)
        {
            mealsBox.setEnabled(enable);
        }
    }

    /**
     * @return Returns the <code>{@link ReadablePlotData}</code> containing
     *         information on which kinds of data should be plotted in the
     *         graph.
     */
    public ReadablePlotData getPlotData()
    {
        return data;
    }

    /**
     * State Changed
     * 
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    public void stateChanged(ChangeEvent e)
    {

        if (e.getSource() == BGBox)
        {
            data.setPlotBG(BGBox.isSelected());
        }
        else if (e.getSource() == BGDayAvgBox)
        {
            data.setPlotBGDayAvg(BGDayAvgBox.isSelected());
        }
        else if (e.getSource() == BGReadingsBox)
        {
            data.setPlotBGReadings(BGReadingsBox.isSelected());
        }
        else if (e.getSource() == CHBox)
        {
            data.setPlotCH(CHBox.isSelected());
        }
        else if (e.getSource() == CHDayAvgBox)
        {
            data.setPlotCHDayAvg(CHDayAvgBox.isSelected());
        }
        else if (e.getSource() == CHSumBox)
        {
            data.setPlotCHSum(CHSumBox.isSelected());
        }
        else if (e.getSource() == ins1Box)
        {
            data.setPlotIns1(ins1Box.isSelected());
        }
        else if (e.getSource() == ins1DayAvgBox)
        {
            data.setPlotIns1DayAvg(ins1DayAvgBox.isSelected());
        }
        else if (e.getSource() == ins1SumBox)
        {
            data.setPlotIns1Sum(ins2SumBox.isSelected());
        }
        else if (e.getSource() == ins2Box)
        {
            data.setPlotIns2(ins2Box.isSelected());
        }
        else if (e.getSource() == ins2DayAvgBox)
        {
            data.setPlotIns2DayAvg(ins2DayAvgBox.isSelected());
        }
        else if (e.getSource() == ins2SumBox)
        {
            data.setPlotIns2Sum(ins2SumBox.isSelected());
        }
        else if (e.getSource() == insTotalBox)
        {
            data.setPlotInsTotal(insTotalBox.isSelected());
        }
        else if (e.getSource() == insTotalDayAvgBox)
        {
            data.setPlotInsTotalDayAvg(insTotalDayAvgBox.isSelected());
        }
        else if (e.getSource() == insTotalSumBox)
        {
            data.setPlotInsTotalSum(insTotalSumBox.isSelected());
        }
        else if (e.getSource() == insPerCHBox)
        {
            data.setPlotInsPerCH(insPerCHBox.isSelected());
        }
        else if (e.getSource() == mealsBox)
        {
            data.setPlotMeals(mealsBox.isSelected());
        }

    }

}
