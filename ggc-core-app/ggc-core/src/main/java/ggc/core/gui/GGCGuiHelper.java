package ggc.core.gui;

import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.swing.*;

import org.slf4j.Logger;

import com.atech.db.hibernate.HibernateConfiguration;
import com.atech.help.HelpContext;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;
import com.l2fprod.gui.plaf.skin.SkinLookAndFeel;

import ggc.core.util.DataAccess;

/**
 * Should contain code that would be the same for ggc-desktop, ggc-desktop-little and perhaps even
 * ggc-doctor-desktop.
 */
public class GGCGuiHelper
{

    private static Logger LOG;
    private static final String skinLFdir = "../data/skinlf_themes/";
    private static SkinLookAndFeel s_skinlf;
    private static Map<String, Object> skinLfOverrides;
    private DataAccess dataAccess;
    private I18nControlAbstract i18nControl;


    public GGCGuiHelper()
    {
    }


    public static void setLog(Logger log)
    {
        LOG = log;
    }


    public void setDataAccess(DataAccess dataAccess)
    {
        this.dataAccess = dataAccess;
        this.i18nControl = dataAccess.getI18nControlInstance();
    }


    /**
     * Set Look & Feel
     */
    public static void setLookAndFeel()
    {

        try
        {

            String data[] = DataAccess.getLFData();

            if (data == null)
                return;
            else
            {
                if (data[0].equals("com.l2fprod.gui.plaf.skin.SkinLookAndFeel"))
                {
                    SkinLookAndFeel.setSkin(SkinLookAndFeel.loadThemePack(skinLFdir + data[1]));

                    s_skinlf = new com.l2fprod.gui.plaf.skin.SkinLookAndFeel();
                    UIManager.setLookAndFeel(s_skinlf);

                    setExceptionsForLookAndFeel(data[1], s_skinlf);
                }
                else
                {
                    UIManager.setLookAndFeel(data[0]);
                }

                JFrame.setDefaultLookAndFeelDecorated(true);
                JDialog.setDefaultLookAndFeelDecorated(true);
            }
        }
        catch (Exception ex)
        {
            System.err.println("Error loading L&F: " + ex);
        }

    }


    public static void resetLookAndFeel()
    {
        try
        {
            UIManager.setLookAndFeel(s_skinlf);
        }
        catch (Exception ex)
        {
            System.err.println("Error reseting L&F: " + ex);
        }

        // MainFrame.setLookAndFeel();

        // SwingUtilities.updateComponentTreeUI(this);
        // this.pack();
    }


    public static void setExceptionsForLookAndFeel(String skinName, SkinLookAndFeel skinLookAndFeel)
    {
        System.out.println("Skin Name: " + skinName);

        if (skinName.equals("modernthemepack_orig.zip"))
        {
            skinLfOverrides = new HashMap<String, Object>();

            skinLfOverrides.put("JTableHeader.backgroundColor", new Color(102, 178, 255));
            skinLfOverrides.put("JTableHeader.borderColor", Color.gray);
        }

        // for (Object keyo : skinLookAndFeel.getDefaults().keySet())
        // {
        // String key = (String) keyo;
        //
        // // if (key.contains("Table"))
        // {
        // System.out.println("Key2: " + key);
        // }
        // }

    }


    public static Map<String, Object> getSkinLfOverrides()
    {
        return skinLfOverrides;
    }


    public JLabel getEmptyLabel(int width, int height)
    {
        JLabel labelEmpty = new JLabel("");
        labelEmpty.setPreferredSize(new Dimension(width, height));

        return labelEmpty;
    }


    public void helpInit()
    {
        LOG.debug("JavaHelp - START");

        HelpContext hc = dataAccess.getHelpContext();

        LOG.debug("JavaHelp - HelpContext: " + hc);

        JMenuItem helpItem = new JMenuItem(i18nControl.getMessage("HELP") + "...");
        helpItem.setIcon(new ImageIcon(getClass().getResource("/icons/help.gif")));
        hc.setHelpItem(helpItem);

        String mainHelpSetName = "/" + dataAccess.getLanguageManager().getHelpSet();

        LOG.debug("JavaHelp - MainHelpSetName: " + mainHelpSetName);

        hc.setMainHelpSetName(mainHelpSetName);

        // try to find the helpset and create a HelpBroker object
        if (hc.getMainHelpBroker() == null)
        {
            HelpSet main_help_set = null;

            try
            {
                URL hsURL = getClass().getResource(mainHelpSetName);
                main_help_set = new HelpSet(null, hsURL);
            }
            catch (HelpSetException ex)
            {
                LOG.error("HelpSet " + mainHelpSetName + " could not be opened.", ex);
            }

            HelpBroker main_help_broker = null;

            if (main_help_set != null)
            {
                LOG.debug("JavaHelp - Main Help Set present, creating broker.");
                main_help_broker = main_help_set.createHelpBroker();
            }

            CSH.DisplayHelpFromSource csh = null;

            if (main_help_broker != null)
            {
                // CSH.DisplayHelpFromSource is a convenience class to display
                // the helpset
                csh = new CSH.DisplayHelpFromSource(main_help_broker);

                if (csh != null)
                {
                    // listen to ActionEvents from the helpItem
                    hc.getHelpItem().addActionListener(csh);
                }
            }

            hc.setDisplayHelpFromSourceInstance(csh);
            hc.setMainHelpBroker(main_help_broker);
            hc.setMainHelpSet(main_help_set);

            CSH.trackCSEvents();
        }

        LOG.debug("JavaHelp - END");
    }


    public String getDbSettingsAndCause(boolean forToolTip)
    {
        HibernateConfiguration hc = dataAccess.getDb().getHibernateConfiguration();

        StringBuilder sb = new StringBuilder();

        if (!forToolTip)
        {
            return String.format(i18nControl.getMessage("DB_PROBLEM_NOT_CONNECTED"), hc.db_num, hc.db_conn_name,
                hc.db_driver_class);
        }
        else
        {
            // sb.append(
            // "There was problem connecting/getting metadata from you database.
            // Likely cause of this might be misconfiguration of your database
            // connection and/or missing database driver (JDBC).");
            // sb.append("<br><b>Currently selected configuration is: </b>");
            // sb.append(hc.db_num);
            // sb.append(" - " + hc.db_conn_name);
            // sb.append("<br><b>Driver Class:</b> " + hc.db_driver_class);
            // sb.append("<br>Connection: " + hc.db_conn_url);
            // sb.append("<br>Username: " + hc.db_conn_username);
            // sb.append("<br>Password: " + hc.db_conn_password);
            // sb.append("<br>Dialect: " + hc.db_hib_dialect);

            return String.format(i18nControl.getMessage("DB_PROBLEM_NOT_CONNECTED_TOOLTIP"), hc.db_num, hc.db_conn_name,
                hc.db_driver_class, hc.db_conn_url, hc.db_conn_username, hc.db_conn_password, hc.db_hib_dialect);
        }
        //
        // return sb.toString();
    }


    public void setApplicationIcon(JFrame frame)
    {
        frame.setIconImage(ATSwingUtils.getImageIcon_22x22("diabetesbluecircle.png", frame, dataAccess).getImage());
    }
}
