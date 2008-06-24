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
 *  Filename: GGCTreeRoot
 *  Purpose:  Used for holding tree information for nutrition and meals
 *
 *  Author:   andyrozman
 */

package ggc.pump.list;

import ggc.core.util.DataAccess;
import ggc.pump.util.DataAccessPump;
import ggc.pump.util.I18nControl;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import com.atech.graphics.components.web.MiniBrowserPanel;



public class PumpListCompanyPanel extends PumpListAbstractPanel 
{
	
	static final long serialVersionUID = 0L;

	
	
    I18nControl ic = I18nControl.getInstance();
    DataAccessPump m_da = null;
    JEditorPane editor;
    
    Font font_big, font_normal, font_normal_b;
    JLabel label, label_test;
    JButton button;

    MiniBrowserPanel mbp;
    
    //JEditorPane editor;

    PumpListDialog m_dialog = null;

    String[] nutrition_db = {
	    "",
	    "USDA_NUTRITION_DATABASE",
	    "USER_NUTRITION_DATABASE",
	    "MEAL_DATABASE"
    };
    
	
	
    public PumpListCompanyPanel(PumpListDialog dia)
    {

        super();

        m_dialog = dia;
        m_da = DataAccessPump.getInstance();

        font_big = m_da.getFont(DataAccess.FONT_BIG_BOLD);
        font_normal_b = m_da.getFont(DataAccess.FONT_NORMAL_BOLD);
        font_normal = m_da.getFont(DataAccess.FONT_NORMAL);

        //createPanel();
        //init();
        initBrowser();

    }


    public void init()
    {
    	
    	System.out.println("init()");
    	this.setBounds(0,0,500,500);
        this.setLayout(new java.awt.BorderLayout());

        this.editor  = new JEditorPane();


        JScrollPane jScrollPane1 = new JScrollPane(this.editor);
        //jScrollPane1.setPreferredSize(new java.awt.Dimension(13, 1200));

        this.editor.setEditable(false);
        this.editor.setContentType("text/html");
        jScrollPane1.setViewportView(this.editor);
        this.editor.setText("<html><body><font color=\"#CCCCCC\"><h1>Test</h1></font></body></html>");

        this.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        this.editor.select(0,0);
    	
    	
    	
    }

    
    public void initBrowser()
    {
    	
    	this.setBounds(0,0,582,565);
        this.setLayout(null);

        //MiniBrowserPanel mbp;
        
        this.mbp = new MiniBrowserPanel();
        this.mbp.setBounds(0,0,582,565);

/*
        JScrollPane jScrollPane1 = new JScrollPane(this.editor);
        //jScrollPane1.setPreferredSize(new java.awt.Dimension(13, 1200));

        this.editor.setEditable(false);
        this.editor.setContentType("text/html");
        jScrollPane1.setViewportView(this.editor);
        this.editor.setText("<html><body><font color=\"#CCCCCC\"><h1>Test</h1></font></body></html>");

        this.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        this.editor.select(0,0);
*/
        this.add(this.mbp, null); //, java.awt.BorderLayout.CENTER);
        
    }
    
    
    public void createPanel()
    {
    	
    	
        //this.setLayout(new java.awt.BorderLayout());
    	this.setLayout(null);

        editor = new JEditorPane();


        JScrollPane jScrollPane1 = new JScrollPane(editor);
        jScrollPane1.setBounds(0,0,300,300);
        jScrollPane1.setBackground(Color.blue);
        
        
        //jScrollPane1.setPreferredSize(new java.awt.Dimension(13, 1200));

        editor.setEditable(false);
        editor.setContentType("text/html");
        //jScrollPane1.setViewportView(editor);
        editor.setText("<html><body><font color=\"#CCCCCC\"><h1>Test</h1></font></body></html>");

        this.add(jScrollPane1, null);

        //editor.select(0,0);
    	
    	
    	
	/*
	
        this.setSize(460, 520);
        this.setLayout(new BorderLayout());
        
        this.editor = new JEditorPane();
        this.editor.setContentType("html");
        this.editor.s
        
        
        JScrollPane scroll = new JScrollPane();
    */    
        
        /*

        Font fnt_18 = new Font("Times New Roman", Font.PLAIN, 14);

        //String nut_db = nutrition_db[this.m_dialog.getType()];
        
        String nut_db = "SSS";

        label = new JLabel(ic.getMessage(nut_db));
        label.setBounds(0, 35, 520, 40);
        label.setFont(font_big); 
        label.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(label, null);


        label = new JLabel(ic.getMessage(nut_db + "_DESC"));
        label.setBounds(40, 120, 400, 250);
        label.setVerticalAlignment(JLabel.TOP);
        label.setFont(fnt_18); 
        this.add(label, null);

        
        label_test = new JLabel(ic.getMessage("ADD_DIOCESE_DESC"));
        label_test.setBounds(40, 330, 300, 60);
        label_test.setFont(font_normal); 
        this.add(label_test, null);



        label = new JLabel(ic.getMessage("EDIT_VIEW"));
        label.setBounds(40, 280, 300, 30);
        label.setFont(fnt_18); 
//        this.add(label, null);

        label = new JLabel(ic.getMessage("EDIT_VIEW_DESC"));
        label.setBounds(40, 310, 300, 60);
        //label.setFont(font_normal); 
//        this.add(label, null);
*/
        
        return;
    }


    public void loadFile(String name)
    {
    	String res = m_da.metersUrl.get(name);
    	
    	String web = "http://localhost:88/meters/" + res;
    	
    	System.out.println(web);
    	
    	
    	this.mbp.setPage(web);
    	
    	/*
    	//abbott_diabetes_care.html
        try
        {
            URL url = new URL("http://localhost:88/meters/" + res);          //this.getClass().getResource("/html/abbott_diabetes_care.html");
            InputStreamReader ins = new InputStreamReader(url.openStream());
            BufferedReader br = new BufferedReader( ins );
            String line;
            StringBuffer sb = new StringBuffer();

            while ((line = br.readLine())!=null)
            {
                sb.append(line);
            }

            //System.out.println("Sb: " + sb);

            this.editor.setText(sb.toString());
            //this.jEditorPane1.setText(sb.toString());
        }
        catch(Exception ex)
        {
            System.out.println("PumpListCompanyPanel::error reading. Ex: " + ex);
            ex.printStackTrace();
        }
    	*/
    }
    
    
    public void setData(Object obj)
    {
    	//System.out.println((String)obj);
    	
    	//editor.setText("<html><body><font color=\"#CCCCCC\"><h1>" + (String)obj + "</h1></font></body></html>");
    	//editor.setText("<html><body><h1>" + (String)obj + "</h1></body></html>");
    	
    	loadFile((String)obj);
    }
	
	
	
    

}
