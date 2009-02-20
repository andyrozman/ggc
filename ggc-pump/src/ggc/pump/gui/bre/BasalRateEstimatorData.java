package ggc.pump.gui.bre;

import ggc.pump.util.DataAccessPump;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import com.atech.utils.ATSwingUtils;

public class BasalRateEstimatorData 
{
    
    public static final int DATA_TYPE_NONE = 0;
    public static final int DATA_TYPE_BG = 1;
    public static final int DATA_TYPE_BASAL_OLD = 2;
    public static final int DATA_TYPE_BASAL_NEW = 3;

    
    public BasalRateEstimatorData(int time_i, float value, int type)
    {
        this.time = time_i;
        this.data_type = type;
        this.value = value;
    }

    /*
    public BasalRateEstimatorData(String time, int time_i, float insulin, int i)
    {
        this.time = time;
        this.time_dt = time_i;
        this.insulin = insulin;
    }*/
    
    public int data_type = 0;
    public int time;
    //int time_dt;
    
    public float value = 0.0f;
    
    
    
}

