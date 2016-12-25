package main.java.ggc.pump.gui.pdtc;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.*;

import com.atech.graphics.components.TimeComponent;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;

import ggc.core.data.cfg.ConfigurationManager;
import main.java.ggc.pump.util.DataAccessPump;

/**
 * Created by andy on 12/11/16.
 */
public class TemporaryBasalRateComponent extends JPanel implements ItemListener
{

    private DataAccessPump m_da = DataAccessPump.getInstance();
    private I18nControlAbstract ic = m_da.getI18nControlInstance();

    // I18nControlAbstract i18nControlAbstract =
    // DataAccessPump.getInstance().getI18nControlInstance();
    ConfigurationManager cm = m_da.getConfigurationManager();

    private static final long serialVersionUID = -1192269467658397557L;
    String[] vals = { "-", "+" };
    String[] units = { ic.getMessage("TBR_TYPE_UNIT"), ic.getMessage("TBR_TYPE_PROC") };

    JSpinner spinner = null;
    JComboBox cb_sign = null;
    JComboBox cb_unit = null;
    JLabel label_1_1, label_2_1;

    TimeComponent cmp_time = null;

    SpinnerNumberModel model_unit;
    SpinnerNumberModel model_proc;


    public TemporaryBasalRateComponent()
    {
        super();
        this.setLayout(null);
        this.init();
    }


    private void init()
    {
        label_1_1 = new JLabel(ic.getMessage("TEMPORARY_BASAL_RATE_SHORT") + ":");
        label_1_1.setBounds(0, 0, 140, 25);
        label_1_1.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL_BOLD));
        this.add(label_1_1);

        cb_sign = new JComboBox(vals);
        cb_sign.setBounds(110, 0, 53, 25);
        cb_sign.setSelectedIndex(1);
        this.add(cb_sign);

        model_unit = new SpinnerNumberModel(0, cm.getFloatValue("PUMP_UNIT_MIN"), cm.getFloatValue("PUMP_UNIT_MAX"),
                cm.getFloatValue("PUMP_UNIT_STEP"));

        model_proc = new SpinnerNumberModel(100, cm.getFloatValue("PUMP_PROC_MIN"), cm.getFloatValue("PUMP_PROC_MAX"),
                cm.getFloatValue("PUMP_PROC_STEP"));

        spinner = new JSpinner();
        // spinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
        spinner.setBounds(168, 0, 48, 25);
        spinner.setValue(100);
        this.add(spinner);

        cb_unit = new JComboBox(units);
        cb_unit.setBounds(220, 0, 110, 25);
        cb_unit.addItemListener(this);
        this.add(cb_unit);

        int tbr_type = cm.getIntValue("PUMP_TBR_TYPE");

        if (tbr_type != 0)
        {
            cb_unit.setEnabled(false);
            cb_unit.setSelectedIndex(tbr_type - 1);
        }

        setSpinnerModel();

        label_2_1 = ATSwingUtils.getLabel(ic.getMessage("DURATION_SHORT") + ":", 175, 0, 120, 25, this,
            ATSwingUtils.FONT_NORMAL_BOLD);
        this.add(label_2_1);

        cmp_time = new TimeComponent();
        cmp_time.setBounds(245, 0, 50, 25);
        this.add(cmp_time);

        /*
         * label_2_1 = new JLabel("%");
         * label_2_1.setBounds(320, 0, 40, 25);
         * this.add(label_2_1);
         */

    }


    private void setSpinnerModel()
    {
        if (this.cb_unit.getSelectedIndex() == 0)
        {
            spinner.setModel(model_unit);
        }
        else
        {
            spinner.setModel(model_proc);
        }
    }


    @Override
    public void setBounds(int x, int y, int width, int height)
    {
        super.setBounds(x, y, 350, 30);
    }


    public String getValue()
    {
        String sign = "", unit = "%", val = "";

        if (this.cb_sign.getSelectedIndex() == 0)
        {
            sign = "-";
        }

        if (this.cb_unit.getSelectedIndex() == 0)
        {
            unit = "U";
            // FIXME
            // val = Rounding.specialRounding(dataAccess.getDoinput_val,
            // cm.getStringValue("PEN_BASAL_PRECISSION"));
        }
        else
        {
            val = "" + m_da.getIntValue(spinner.getValue());
        }

        return String.format("TBR_VALUE=%s%s;TBR_UNIT=%s;DURATION=%s", sign, val, unit, this.cmp_time.getTimeString());

    }


    private String[] getParsedValues(String val)
    {
        ArrayList<String> lst = new ArrayList<String>();

        StringTokenizer strtok = new StringTokenizer(val, ";");

        while (strtok.hasMoreTokens())
        {
            String tk = strtok.nextToken();
            lst.add(tk.substring(tk.indexOf("=") + 1));
        }

        String ia[] = new String[lst.size()];
        return lst.toArray(ia);
    }


    public void setValue(String val)
    {
        String[] str = getParsedValues(val);
        String s = str[0];

        if (s.startsWith("-"))
        {
            s = s.substring(1);
            this.cb_sign.setSelectedIndex(0);
        }
        else
        {
            this.cb_sign.setSelectedIndex(1);
        }

        if (str[1].equals("%"))
        {
            this.cb_unit.setSelectedIndex(1);
        }
        else
        {
            this.cb_unit.setSelectedIndex(0);
        }

    }


    public boolean isValueSet()
    {
        return true;
    }


    public void itemStateChanged(ItemEvent ie)
    {
        setSpinnerModel();
    }

}
