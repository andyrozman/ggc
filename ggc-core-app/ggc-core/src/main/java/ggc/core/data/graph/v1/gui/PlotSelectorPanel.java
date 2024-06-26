package ggc.core.data.graph.v1.gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.atech.i18n.I18nControlAbstract;

import ggc.core.data.PlotData;
import ggc.core.data.cfg.ConfigurationManagerWrapper;
import ggc.core.data.graph.v1.data.PlotSelectorData;
import ggc.core.util.DataAccess;

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

public class PlotSelectorPanel extends JPanel implements ChangeListener
{

    private static final long serialVersionUID = 6420234465982434157L;

    PlotSelectorData data = new PlotSelectorData();
    DataAccess dataAccessInstance = DataAccess.getInstance();
    private I18nControlAbstract i18nControl = dataAccessInstance.getI18nControlInstance();

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
     * Selection Mode: Single
     */
    public static final int SELECTION_MODE_SINGLE = 1;

    /**
     * Selection Mode: Multiple
     */
    public static final int SELECTION_MODE_MULTIPLE = 2;

    int selection_mode = SELECTION_MODE_MULTIPLE;

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

    ConfigurationManagerWrapper configurationManagerWrapper = dataAccessInstance.getConfigurationManagerWrapper();


    /**
     * Initialises the <code>{@link JPanel panel}</code> with no checkbox
     * selected.
     */
    public PlotSelectorPanel()
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
    public PlotSelectorPanel(int initialSelection)
    {
        super(new GridLayout(4, 5), true);
        setBorder(BorderFactory.createTitledBorder(i18nControl.getMessage("TO_BE_DRAWN") + ":"));
        initBoxes();
        addBoxes();
        selectBoxes(initialSelection, true);
    }


    private void initBoxes()
    {
        setCheckBoxSettings(BGBox, "BG");
        setCheckBoxSettings(BGDayAvgBox, "AVG_BG_PER_DAY");
        setCheckBoxSettings(BGReadingsBox, "READINGS");

        setCheckBoxSettings(CHBox, "BU");
        setCheckBoxSettings(CHDayAvgBox, "AVG_MEAL_SIZE");
        setCheckBoxSettings(CHSumBox, "SUM_BU");

        setCheckBoxSettings(ins1Box, "BOLUS_INSULIN_SHORT");
        setCheckBoxSettings(ins1DayAvgBox,
            i18nControl.getMessage("AVG") + " " + i18nControl.getMessage("BOLUS_INSULIN_SHORT"));
        setCheckBoxSettings(ins1SumBox,
            i18nControl.getMessage("SUM") + " " + i18nControl.getMessage("BOLUS_INSULIN_SHORT"));

        setCheckBoxSettings(ins2Box, "BASAL_INSULIN_SHORT");
        setCheckBoxSettings(ins2DayAvgBox,
            i18nControl.getMessage("SUM") + " " + i18nControl.getMessage("BASAL_INSULIN_SHORT"));
        setCheckBoxSettings(ins2SumBox,
            i18nControl.getMessage("SUM") + " " + i18nControl.getMessage("BASAL_INSULIN_SHORT"));

        setCheckBoxSettings(insTotalBox,
            i18nControl.getMessage("BOLUS_INSULIN_SHORT") + " + " + i18nControl.getMessage("BASAL_INSULIN_SHORT"));
        setCheckBoxSettings(insTotalDayAvgBox, "AVG_INS");
        setCheckBoxSettings(insTotalSumBox, "SUM_INSULIN");

        setCheckBoxSettings(insPerCHBox, "INS_SLASH_BU");
        setCheckBoxSettings(mealsBox, "MEALS");

    }


    private void setCheckBoxSettings(JCheckBox checkBox, String key)
    {
        checkBox.setText(" " + this.i18nControl.getMessage(key));
        checkBox.addChangeListener(this);
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


    private boolean checkMask(int value, int mask)
    {
        return (value & mask) == mask;
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
     * Remove Choice
     * 
     * @param choiceMask
     */
    public void removeChoice(int choiceMask)
    {
        this.removeAll();

        // normal
        if (!checkMask(choiceMask, BG_MASK))
        {
            this.add(BGBox);
        }

        if (!checkMask(choiceMask, CH_MASK))
        {
            this.add(CHBox);
        }

        if (!checkMask(choiceMask, INS1_MASK))
        {
            this.add(ins1Box);
        }

        if (!checkMask(choiceMask, INS2_MASK))
        {
            this.add(ins2Box);
        }

        if (!checkMask(choiceMask, INS_TOTAL_MASK))
        {
            this.add(insTotalBox);
        }

        // average
        if (!checkMask(choiceMask, BG_AVG_MASK))
        {
            this.add(BGDayAvgBox);
        }

        if (!checkMask(choiceMask, CH_AVG_MASK))
        {
            this.add(CHDayAvgBox);
        }

        if (!checkMask(choiceMask, INS1_AVG_MASK))
        {
            this.add(ins1DayAvgBox);
        }

        if (!checkMask(choiceMask, INS2_AVG_MASK))
        {
            this.add(ins2DayAvgBox);
        }

        if (!checkMask(choiceMask, INS_TOTAL_AVG_MASK))
        {
            this.add(insTotalDayAvgBox);
        }

        // sum
        if (!checkMask(choiceMask, CH_SUM_MASK))
        {
            this.add(CHSumBox);
        }

        if (!checkMask(choiceMask, INS1_SUM_MASK))
        {
            this.add(ins1SumBox);
        }

        if (!checkMask(choiceMask, INS2_SUM_MASK))
        {
            this.add(ins2SumBox);
        }

        if (!checkMask(choiceMask, INS_TOTAL_SUM_MASK))
        {
            this.add(insTotalSumBox);
        }

        // other
        if (!checkMask(choiceMask, BG_READINGS_MASK))
        {
            this.add(BGReadingsBox);
        }

        if (!checkMask(choiceMask, INS_PER_CH_MASK))
        {
            this.add(insPerCHBox);
        }

        if (!checkMask(choiceMask, MEALS_MASK))
        {
            this.add(mealsBox);
        }

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
     * @return Returns the <code>{@link PlotSelectorData}</code> containing
     *         information on which kinds of data should be plotted in the
     *         graph.
     */
    public PlotSelectorData getPlotData()
    {

        data.setPlotBG(BGBox.isSelected());
        data.setPlotBGDayAvg(BGDayAvgBox.isSelected());
        data.setPlotBGReadings(BGReadingsBox.isSelected());
        data.setPlotCH(CHBox.isSelected());
        data.setPlotCHDayAvg(CHDayAvgBox.isSelected());
        data.setPlotCHSum(CHSumBox.isSelected());
        data.setPlotIns1(ins1Box.isSelected());
        data.setPlotIns1DayAvg(ins1DayAvgBox.isSelected());
        data.setPlotIns1Sum(ins1SumBox.isSelected());
        data.setPlotIns2(ins2Box.isSelected());
        data.setPlotIns2DayAvg(ins2DayAvgBox.isSelected());
        data.setPlotIns2Sum(ins2SumBox.isSelected());
        data.setPlotInsTotal(insTotalBox.isSelected());
        data.setPlotInsTotalDayAvg(insTotalDayAvgBox.isSelected());
        data.setPlotInsTotalSum(insTotalSumBox.isSelected());
        data.setPlotInsPerCH(insPerCHBox.isSelected());
        data.setPlotMeals(mealsBox.isSelected());

        return data;
    }


    /**
     * Set Selection Mode
     * 
     * @param mode
     */
    public void setSelectionMode(int mode)
    {
        this.selection_mode = mode;
    }

    boolean in_process = false;


    /**
     * State Changed
     * 
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    public void stateChanged(ChangeEvent e)
    {

        if (in_process)
            return;
        else
        {
            in_process = true;
        }

        if (this.selection_mode == PlotSelectorPanel.SELECTION_MODE_SINGLE)
        {
            JCheckBox cb = (JCheckBox) e.getSource();

            if (!cb.isSelected())
            {
                cb.setSelected(true);
            }
            else
            {
                unselectAll();
                cb.setSelected(true);
                checkData(e);
            }
        }
        else
        {
            checkData(e);
            /*
             * if (e.getSource() == BGBox)
             * {
             * data.setPlotBG(BGBox.isSelected());
             * }
             * else if (e.getSource() == BGDayAvgBox)
             * {
             * data.setPlotBGDayAvg(BGDayAvgBox.isSelected());
             * }
             * else if (e.getSource() == BGReadingsBox)
             * {
             * data.setPlotBGReadings(BGReadingsBox.isSelected());
             * }
             * else if (e.getSource() == CHBox)
             * {
             * data.setPlotCH(CHBox.isSelected());
             * }
             * else if (e.getSource() == CHDayAvgBox)
             * {
             * data.setPlotCHDayAvg(CHDayAvgBox.isSelected());
             * }
             * else if (e.getSource() == CHSumBox)
             * {
             * data.setPlotCHSum(CHSumBox.isSelected());
             * }
             * else if (e.getSource() == ins1Box)
             * {
             * data.setPlotIns1(ins1Box.isSelected());
             * }
             * else if (e.getSource() == ins1DayAvgBox)
             * {
             * data.setPlotIns1DayAvg(ins1DayAvgBox.isSelected());
             * }
             * else if (e.getSource() == ins1SumBox)
             * {
             * data.setPlotIns1Sum(ins2SumBox.isSelected());
             * }
             * else if (e.getSource() == ins2Box)
             * {
             * data.setPlotIns2(ins2Box.isSelected());
             * }
             * else if (e.getSource() == ins2DayAvgBox)
             * {
             * data.setPlotIns2DayAvg(ins2DayAvgBox.isSelected());
             * }
             * else if (e.getSource() == ins2SumBox)
             * {
             * data.setPlotIns2Sum(ins2SumBox.isSelected());
             * }
             * else if (e.getSource() == insTotalBox)
             * {
             * data.setPlotInsTotal(insTotalBox.isSelected());
             * }
             * else if (e.getSource() == insTotalDayAvgBox)
             * {
             * data.setPlotInsTotalDayAvg(insTotalDayAvgBox.isSelected());
             * }
             * else if (e.getSource() == insTotalSumBox)
             * {
             * data.setPlotInsTotalSum(insTotalSumBox.isSelected());
             * }
             * else if (e.getSource() == insPerCHBox)
             * {
             * data.setPlotInsPerCH(insPerCHBox.isSelected());
             * }
             * else if (e.getSource() == mealsBox)
             * {
             * data.setPlotMeals(mealsBox.isSelected());
             * }
             */
        }

        in_process = false;

    }


    private void checkData(ChangeEvent e)
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


    @SuppressWarnings("deprecation")
    private void unselectAll()
    {
        for (int i = 0; i < this.countComponents(); i++)
        {
            if (this.getComponent(i) instanceof JCheckBox)
            {
                JCheckBox cb = (JCheckBox) this.getComponent(i);
                cb.setSelected(false);
            }
        }
    }

}
