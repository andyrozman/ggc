package ggc.plugin.list;

import com.atech.utils.ATSwingUtils;
import ggc.plugin.util.DataAccessPlugInBase;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;

import com.atech.graphics.components.web.MiniBrowserPanel;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATDataAccessAbstract;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
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
 *  Filename:     BaseListBrowserPanel
 *  Description:  Base List Browser Panel
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class BaseListBrowserPanel extends BaseListAbstractPanel
{

    private static final long serialVersionUID = -3840758557586889169L;
    I18nControlAbstract ic = null;
    DataAccessPlugInBase m_da = null;
    JEditorPane editor;

    Font font_big, font_normal, font_normal_b;
    JLabel label, label_test;
    JButton button;

    MiniBrowserPanel mbp;

    BaseListDialog m_dialog = null;

    /**
     * Constructor
     * 
     * @param dia
     */
    public BaseListBrowserPanel(BaseListDialog dia)
    {

        super();

        m_dialog = dia;
        m_da = dia.m_da;

        ATSwingUtils.initLibrary();

        font_big = ATSwingUtils.getFont(ATSwingUtils.FONT_BIG_BOLD);
        font_normal_b = ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL_BOLD);
        font_normal = ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL);

        // createPanel();
        // init();
        initBrowser();

    }

    /**
     * Init
     */
    /*
     * public void init()
     * {
     * this.setBounds(0,0,500,500);
     * this.setLayout(new java.awt.BorderLayout());
     * this.editor = new JEditorPane();
     * JScrollPane jScrollPane1 = new JScrollPane(this.editor);
     * this.editor.setEditable(false);
     * this.editor.setContentType("text/html");
     * jScrollPane1.setViewportView(this.editor);
     * this.editor.setText(
     * "<html><body><font color=\"#CCCCCC\"><h1>Test</h1></font></body></html>"
     * );
     * this.add(jScrollPane1, java.awt.BorderLayout.CENTER);
     * this.editor.select(0,0);
     * }
     */

    /**
     * Init Browser
     */
    public void initBrowser()
    {
        /*
         * //this.setBounds(0,0,640,560);
         * //this.setLayout(null);
         * this.mbp = new MiniBrowserPanel();
         * this.mbp.setBounds(0,0,640,560);
         * this.add(this.mbp, null); //, java.awt.BorderLayout.CENTER);
         */

        // this.setBounds(0,0,640,560);
        this.setLayout(new BorderLayout());

        this.mbp = new MiniBrowserPanel();
        // this.mbp.setBounds(0,0,640,560);

        this.add(this.mbp, BorderLayout.CENTER);

    }

    /**
     * Create Panel
     */
    /*
     * public void createPanel()
     * {
     * this.setLayout(null);
     * editor = new JEditorPane();
     * JScrollPane jScrollPane1 = new JScrollPane(editor);
     * jScrollPane1.setBounds(0,0,300,300);
     * jScrollPane1.setBackground(Color.blue);
     * editor.setEditable(false);
     * editor.setContentType("text/html");
     * editor.setText(
     * "<html><body><font color=\"#CCCCCC\"><h1>Test</h1></font></body></html>"
     * );
     * this.add(jScrollPane1, null);
     * return;
     * }
     */

    /**
     * Load Page
     * 
     * @param url_src
     */
    public void loadPage(String url_src)
    {
        try
        {

            String url_x = "http://localhost:" + m_da.getWebListerPort() + url_src;

            // System.out.println("url_x: " + url_x);
            /*
             * URL url = new URL("http://localhost: " + dataAccess.getWebListerPort()
             * + url_src);
             * //this.getClass().getResource("/html/abbott_diabetes_care.html");
             * System.out.println("url: " + url.toString());
             * InputStreamReader ins = new InputStreamReader(url.openStream());
             * BufferedReader br = new BufferedReader( ins );
             * String line;
             * StringBuffer sb = new StringBuffer();
             * while ((line = br.readLine())!=null)
             * {
             * sb.append(line);
             * }
             */
            // this.mbp.setPage("http://localhost:444" + url_src);
            this.mbp.setPage(url_x);

        }
        catch (Exception ex)
        {
            System.out.println("BaseListBrowserPanel Ex: " + ex);
            ex.printStackTrace();
        }

    }

    /**
     * Set Data
     * 
     * @see ggc.plugin.list.BaseListAbstractPanel#setData(java.lang.Object)
     */
    @Override
    public void setData(Object obj)
    {
        BaseListEntry ble = (BaseListEntry) obj;
        loadPage(ble.page);
    }

}
