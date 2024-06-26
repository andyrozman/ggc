/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: <filename>
 *
 *  Purpose:  <enter purpose here>
 *
 *  Author:   andyrozman {andy@atech-software.com}
 *
 *
 * DEPRECATED ??
 *
 */
package ggc.gui.dialogs.stock;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;

import ggc.core.db.hibernate.doc.DoctorH;
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
 *  Filename:     zzz  
 *  Description:  zzz
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

// fix this

// list of stocks and all entry

public class StockListDialog extends JDialog implements ActionListener
{

    private static final long serialVersionUID = 5357938989788436466L;
    private DataAccess m_da = DataAccess.getInstance();
    private I18nControlAbstract m_ic = m_da.getI18nControlInstance();

    // x private boolean m_actionDone = false;

    // x private JTextField tfName;
    private JComboBox cb_template = null;
    private JTable t_stocks = null;
    // x private String[] schemes_names = null;

    GregorianCalendar gc = null;
    JSpinner sl_year = null, sl_month = null;

    /**
     * Filter Type
     */
    public String[] filter_types = { m_ic.getMessage("FILTER_VISIBLE"), m_ic.getMessage("FILTER_ALL") };

    @SuppressWarnings("unused")
    private ArrayList<DoctorH> list_full;
    private ArrayList<DoctorH> active_list = new ArrayList<DoctorH>();

    Font font_normal, font_normal_bold;


    public StockListDialog()
    {
        setBounds(new Rectangle(0, 0, 700, 500));
        setBounds(175, 150, 758, 512);
        initComponents();
        this.setVisible(true);
    }


    /**
     * Constructor
     * 
     * @param frame
     */
    public StockListDialog(JFrame frame)
    {
        super(frame, "", true);

        Rectangle rec = frame.getBounds();
        int x = rec.x + rec.width / 2;
        int y = rec.y + rec.height / 2;

        setBounds(x - 175, y - 150, 450, 380);

        initComponents();

        this.list_full = new ArrayList<DoctorH>();
        populateList();

        // this.cb_template.setSelectedIndex(type-1);

        this.setVisible(true);
    }


    private void initComponents()
    {
        ATSwingUtils.initLibrary();

        getContentPane().setLayout(null);

        font_normal = ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL);
        font_normal_bold = ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL_BOLD);

        gc = new GregorianCalendar();

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 750, 478);
        panel.setLayout(null);

        this.getContentPane().add(panel);

        JLabel label = new JLabel(m_ic.getMessage("STOCKS_LIST"));
        label.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_BIG_BOLD));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBounds(0, 20, 450, 35);
        panel.add(label);

        label = new JLabel(m_ic.getMessage("FILTER") + ":");
        label.setFont(this.font_normal_bold);
        label.setBounds(40, 75, 100, 25);
        panel.add(label);

        cb_template = new JComboBox(filter_types);
        cb_template.setFont(this.font_normal);
        cb_template.setBounds(120, 75, 80, 25);
        panel.add(cb_template);

        this.t_stocks = new JTable(new AbstractTableModel()
        {

            /**
                     * 
                     */
            private static final long serialVersionUID = 9088931662879087375L;


            public int getColumnCount()
            {
                // TODO Auto-generated method stub
                return 2;
            }


            public int getRowCount()
            {
                active_list.size();
                return 0;
            }


            public Object getValueAt(int row, int column)
            {
                // TODO Auto-generated method stub
                DoctorH dh = active_list.get(row);

                switch (column)
                {
                    case 0:
                        return dh.getName();

                    case 1:
                        return m_ic.getMessage(dh.getDoctorType().getName());
                }

                return null;
            }

        });

        JScrollPane scp = new JScrollPane(this.t_stocks);
        scp.setBounds(40, 120, 653, 303);
        panel.add(scp);

        String[] names = { m_ic.getMessage("ADD"), m_ic.getMessage("EDIT"), m_ic.getMessage("CLOSE"), };

        String[] cmds = { m_ic.getMessage("add"), m_ic.getMessage("edit"), m_ic.getMessage("close"), };

        int[] coords = { 120, 150, 220, };

        JButton button;

        for (int i = 0; i < coords.length; i++)
        {
            button = new JButton(names[i]);
            button.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL));
            button.setActionCommand(cmds[i]);
            button.addActionListener(this);
            button.setBounds(340, coords[i], 80, 25);
            panel.add(button);
        }

    }


    private void populateList()
    {
    }


    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
        String action = e.getActionCommand();

        if (action.equals("close"))
        {
            this.dispose();
        }
        else if (action.equals("add"))
        {
            System.out.println("Add not implemented");
        }
        else if (action.equals("edit"))
        {
            System.out.println("Edit not implemented");
        }
        else
        {
            System.out.println("DoctorsDialog: Unknown command: " + action);
        }

    }

    // DROP TABLE stock_subtypes;
    //
    // CREATE TABLE stock_subtypes
    // (
    // id bigint NOT NULL,
    // stock_type_id bigint,
    // name character varying(512),
    // description character varying(2000),
    // content_pkg bigint,
    // usage_type int,
    // usage_min int,
    // usage_max int,
    // active boolean,
    // extended text,
    // comment character varying(2000),
    // CONSTRAINT stock_subtypes_pkey PRIMARY KEY (id)
    // )
    // WITH (
    // OIDS=FALSE
    // );
    // ALTER TABLE stock_subtypes
    // OWNER TO ggc;

    // -- Table: stock_stocks
    //
    // DROP TABLE stock_stocks;
    //
    // CREATE TABLE stock_stocks
    // (
    // id bigint NOT NULL,
    // stock_subtype_id bigint,
    // stocktaking_id bigint,
    // amount bigint,
    // "location" character varying(1000),
    //
    // extended text,
    // person_id integer NOT NULL,
    // comment character varying(2000),
    // CONSTRAINT stock_stocks_pkey PRIMARY KEY (id)
    // )
    // WITH (
    // OIDS=FALSE
    // );
    // ALTER TABLE stock_stocks
    // OWNER TO ggc;

    // -- Table: stock_stocktaking

    // --DROP TABLE stock_stocktaking;
    //
    // CREATE TABLE stock_stocktaking
    // (
    // id bigint NOT NULL,
    // dt_stocktaking bigint,
    // description character varying(2000),
    // amount bigint,
    //
    //
    // extended text,
    // person_id integer NOT NULL,
    // comment character varying(2000),
    // CONSTRAINT stock_stocktaking_pkey PRIMARY KEY (id)
    // )
    // WITH (
    // OIDS=FALSE
    // );
    // ALTER TABLE stock_stocks
    // OWNER TO ggc;

}
