package ggc.gui.panels.info;

import ggc.core.data.CollectionValues;
import ggc.core.util.DataAccess;

import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.misc.statistics.StatisticsCollection;

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
 *  Filename:     StatisticsInfoPanel  
 *  Description:  Display statistics for last week
 *
 *  Author: schultd
 *          andyrozman {andy@atech-software.com}  
 */


public class StatisticsInfoPanel extends AbstractInfoPanel
{
    private static final long serialVersionUID = 2075057980606217010L;
    GregorianCalendar endDate = null;
    GregorianCalendar startDate = null; //new Date(endDate.getTime() - (518400000L)); //now - 6 days in millisec
    private static Log log = LogFactory.getLog(StatisticsInfoPanel.class);

    JLabel lblAvgBG, lblBGReadings, lblBGReadingsDay;
    JLabel lblSumBU, lblBUDay, lblCountBU, lblAvgBU, lblBUCountDay;
    JLabel lblSumIns1, lblIns1Day, lblCountIns1, lblAvgIns1, lblIns1CountDay;
    JLabel lblSumIns2, lblIns2Day, lblCountIns2, lblAvgIns2, lblIns2CountDay;

    JLabel lbl_sum_ins1_day_name, lbl_sum_ins1_name, lbl_sum_ins2_day_name, lbl_sum_ins2_name;

    JPanel PanelIns1, PanelIns2;
    DecimalFormat dec_format;



    /**
     * Constructor
     */
    public StatisticsInfoPanel()
    {
        super("");

        m_da = DataAccess.getInstance();

        endDate = new GregorianCalendar();
        startDate = new GregorianCalendar();
        startDate.add(java.util.Calendar.DAY_OF_MONTH, -6);
        
        endDate.set(GregorianCalendar.HOUR_OF_DAY, 23);
        endDate.set(GregorianCalendar.MINUTE, 59);
        endDate.set(GregorianCalendar.SECOND, 59);
        
        startDate.set(GregorianCalendar.HOUR_OF_DAY, 0);
        startDate.set(GregorianCalendar.MINUTE, 0);
        startDate.set(GregorianCalendar.SECOND, 0);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        dec_format = new DecimalFormat("#0.00");

        ((TitledBorder)this.getBorder()).setTitle(m_ic.getMessage("STATISTICS_FOR_LAST_WEEK")+" (" + sdf.format(startDate.getTime()) + " - " + sdf.format(endDate.getTime()) + ")");

        init();
        refreshInfo();
    }

    private void init()
    {
        JPanel PanelBG = new JPanel(new GridLayout(3,2));
        PanelBG.setOpaque(false);
        PanelBG.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("BG_STATISTICS")+":"));
        PanelBG.add(new JLabel(m_ic.getMessage("AVG_BG")+":"));
        PanelBG.add(lblAvgBG = new JLabel());
        lblAvgBG.setHorizontalAlignment(SwingConstants.CENTER);
        PanelBG.add(new JLabel(m_ic.getMessage("READINGS")+":"));
        PanelBG.add(lblBGReadings = new JLabel());
        lblBGReadings.setHorizontalAlignment(SwingConstants.CENTER);
        PanelBG.add(new JLabel(m_ic.getMessage("READINGS_SLASH_DAY")+":"));
        PanelBG.add(lblBGReadingsDay = new JLabel());
        lblBGReadingsDay.setHorizontalAlignment(SwingConstants.CENTER);
        
        
        JPanel PanelBU = new JPanel(new GridLayout(5, 2));
        PanelBU.setOpaque(false);
        PanelBU.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("BU_STATISTICS")+":"));
        PanelBU.add(new JLabel(m_ic.getMessage("SUM_BU")+":"));
        PanelBU.add(lblSumBU = new JLabel());
        lblSumBU.setHorizontalAlignment(SwingConstants.CENTER);
        PanelBU.add(new JLabel(m_ic.getMessage("BU_PER_DAY")+":"));
        PanelBU.add(lblBUDay = new JLabel());
        lblBUDay.setHorizontalAlignment(SwingConstants.CENTER);
        PanelBU.add(new JLabel(m_ic.getMessage("MEALS")+":"));
        PanelBU.add(lblCountBU = new JLabel());
        lblCountBU.setHorizontalAlignment(SwingConstants.CENTER);
        PanelBU.add(new JLabel(m_ic.getMessage("AVG_MEAL_SIZE")+":"));
        PanelBU.add(lblAvgBU = new JLabel());
        lblAvgBU.setHorizontalAlignment(SwingConstants.CENTER);
        PanelBU.add(new JLabel(m_ic.getMessage("MEALS_PER_DAY")+":"));
        PanelBU.add(lblBUCountDay = new JLabel());
        lblBUCountDay.setHorizontalAlignment(SwingConstants.CENTER);

        PanelIns1 = new JPanel(new GridLayout(5, 2));
        PanelIns1.setOpaque(false);
        PanelIns1.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("BOLUS_INSULIN_SHORT") + " " +m_ic.getMessage("STATISTICS") + ":"));
        PanelIns1.add(lbl_sum_ins1_name = new JLabel(m_ic.getMessage("SUM_INSULIN") + ":"));
        PanelIns1.add(lblSumIns1 = new JLabel());
        lblSumIns1.setHorizontalAlignment(SwingConstants.CENTER);
        PanelIns1.add(lbl_sum_ins1_day_name = new JLabel(m_ic.getMessage("INS_PER_DAY") +":"));
        PanelIns1.add(lblIns1Day = new JLabel());
        lblIns1Day.setHorizontalAlignment(SwingConstants.CENTER);
        PanelIns1.add(new JLabel(m_ic.getMessage("DOSE")+":"));
        PanelIns1.add(lblCountIns1 = new JLabel());
        lblCountIns1.setHorizontalAlignment(SwingConstants.CENTER);
        PanelIns1.add(new JLabel(m_ic.getMessage("AVG_DOSE_SIZE")+":"));
        PanelIns1.add(lblAvgIns1 = new JLabel());
        lblAvgIns1.setHorizontalAlignment(SwingConstants.CENTER);
        PanelIns1.add(new JLabel(m_ic.getMessage("DOSES_PER_DAY")+":"));
        PanelIns1.add(lblIns1CountDay = new JLabel());
        lblIns1CountDay.setHorizontalAlignment(SwingConstants.CENTER);

        PanelIns2 = new JPanel(new GridLayout(5, 2));
        PanelIns2.setOpaque(false);
        PanelIns2.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("BASAL_INSULIN_SHORT") + " " + m_ic.getMessage("STATISTICS")+":"));
        PanelIns2.add(lbl_sum_ins2_name = new JLabel(m_ic.getMessage("SUM_INSULIN") + ":"));
        PanelIns2.add(lblSumIns2 = new JLabel());
        lblSumIns2.setHorizontalAlignment(SwingConstants.CENTER);
        PanelIns2.add(lbl_sum_ins2_day_name = new JLabel(m_ic.getMessage("INS_PER_DAY") + ":"));
        PanelIns2.add(lblIns2Day = new JLabel());
        lblIns2Day.setHorizontalAlignment(SwingConstants.CENTER);
        PanelIns2.add(new JLabel(m_ic.getMessage("DOSE")+":"));
        PanelIns2.add(lblCountIns2 = new JLabel());
        lblCountIns2.setHorizontalAlignment(SwingConstants.CENTER);
        PanelIns2.add(new JLabel(m_ic.getMessage("AVG_DOSE_SIZE")+":"));
        PanelIns2.add(lblAvgIns2 = new JLabel());
        lblAvgIns2.setHorizontalAlignment(SwingConstants.CENTER);
        PanelIns2.add(new JLabel(m_ic.getMessage("DOSES_PER_DAY")+":"));
        PanelIns2.add(lblIns2CountDay = new JLabel());
        lblIns2CountDay.setHorizontalAlignment(SwingConstants.CENTER);

        setLayout(new GridLayout(2,2));

        add(PanelBG);
        add(PanelBU);
        add(PanelIns1);
        add(PanelIns2);
    }

    
    
    /**
     * Get Tab Name
     * 
     * @return name as string
     */
    public String getTabName()
    {
        return "StatisticInfo";
    }
    
    
    /**
     * Do Refresh - This method can do Refresh
     */
    public void doRefresh()
    {
        if (!m_da.isDatabaseInitialized()) 
            return;

        //System.out.println("Statictics::doRefresh()");

        
        if (m_da.getSoftwareMode()==DataAccess.GGC_MODE_PEN_INJECTION)
        {

            log.debug("Statistics - Pen/Injection Mode");
        
            CollectionValues sV = new CollectionValues(startDate, endDate);
    
            lblAvgBG.setText(dec_format.format(sV.getAvgBG()));
            lblBGReadings.setText(sV.getBGCount() + "");
            lblBGReadingsDay.setText(DataAccess.Decimal2Format.format((sV.getBGCount()/7.0f)));
    
            lblSumBU.setText(dec_format.format(sV.getSumCH()));
            lblBUDay.setText(dec_format.format(sV.getSumCHPerDay()));
            lblCountBU.setText(sV.getCHCount() + "");
            lblAvgBU.setText(dec_format.format(sV.getAvgCH()));
            lblBUCountDay.setText(dec_format.format(sV.getCHCountPerDay()));
    
            lblSumIns1.setText(dec_format.format(sV.getSumBolus()));
            lblIns1Day.setText(dec_format.format(sV.getSumBolusPerDay()));
            lblCountIns1.setText(sV.getBolusCount() + "");
            lblAvgIns1.setText(dec_format.format(sV.getAvgBolus()));
            lblIns1CountDay.setText(dec_format.format(sV.getBolusCountPerDay()));
    
            lblSumIns2.setText(dec_format.format(sV.getSumBasal()));
            lblIns2Day.setText(dec_format.format(sV.getSumBasalPerDay()));
            lblCountIns2.setText(sV.getBasalCount() + "");
            lblAvgIns2.setText(dec_format.format(sV.getAvgBasal()));
            lblIns2CountDay.setText(dec_format.format(sV.getBasalCountPerDay()));
        }
        else
        {
            log.debug("Statistics - Pump Mode");
            
            GregorianCalendar[] gcs = new GregorianCalendar[2];
            gcs[0] = startDate;
            gcs[1] = endDate;
            
            Object obj = m_da.getPlugIn(DataAccess.PLUGIN_PUMPS).getReturnObject(100, gcs);
            
            if (obj!=null)
            {
                if (obj instanceof StatisticsCollection)
                {
                    StatisticsCollection sc = (StatisticsCollection)obj;
                    
                    lblAvgBG.setText(dec_format.format(m_da.getDisplayedBG(sc.getItemStatisticsValue(13)))) ; //sV.getAvgBG())); // 13
                    lblBGReadings.setText(sc.getItemStatisticValueAsStringInt(16)); //sV.getBGCount() + "");  // 16
                    lblBGReadingsDay.setText(DataAccess.Decimal2Format.format(sc.getItemStatisticsValue(16)/7.0d)); //(sV.getBGCount()/7.0f)));
            
            
                    lblSumBU.setText(dec_format.format(sc.getItemStatisticsValue(10))); // CH_SUM=10 //sV.getSumCH())); 
                    lblBUDay.setText(dec_format.format(sc.getItemStatisticsValue(10)/7.0d)); //sV.getSumCHPerDay()));
                    lblCountBU.setText(sc.getItemStatisticValueAsStringInt(12)) ; // MEALS=12 // sV.getCHCount() + "");
                    lblAvgBU.setText(dec_format.format(sc.getItemStatisticsValue(11))); // CH_AVG; 11 sV.getAvgCH()));
                    lblBUCountDay.setText(dec_format.format(sc.getItemStatisticsValue(12)/7.0d)); //sV.getCHCountPerDay()));
            
                    lblSumIns1.setText(dec_format.format(sc.getItemStatisticsValue(1))); // INS_SUM_BOLUS; 1 //sV.getSumBolus()));
                    lblIns1Day.setText(dec_format.format(sc.getItemStatisticsValue(1)/7.0d));  //sV.getSumBolusPerDay()));
                    lblCountIns1.setText(sc.getItemStatisticValueAsStringInt(7)); // INS_DOSES_BOLUS; 7 //sV.getBolusCount() + "");
                    lblAvgIns1.setText(dec_format.format(sc.getItemStatisticsValue(4))); //INS_AVG_BOLUS; // 4 //sV.getAvgBolus()));
                    lblIns1CountDay.setText(dec_format.format(sc.getItemStatisticsValue(7)/7.0d)); // // INS_DOSES_BOLUS; 7 sV.getBolusCountPerDay()));
            
                    lblSumIns2.setText("N/A");
                    lblIns2Day.setText("N/A");
                    lblCountIns2.setText("N/A");
                    lblAvgIns2.setText("N/A");
                    lblIns2CountDay.setText("N/A");
                }
                else
                {
                    log.error("We got wrong type returned: " + obj);
                }
                
            }
            else
                log.warn("  Nothing returned, error communicating with plugin");
            
        }
        
        
    }
    
    
    /**
     * Get Panel Id
     * 
     * @return id of panel
     */
    @Override
    public int getPanelId()
    {
        return InfoPanelsIds.INFO_PANEL_STATISTICS;
    }
    
}
