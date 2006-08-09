package com.atech.db.tool;

import java.awt.Font;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.util.Enumeration;


import javax.swing.text.*;
import javax.swing.event.*;


import ggc.util.I18nControl;

// WORK IN PROGRESS, PLEASE DO NOT TOUCH
// andyrozman


// TO-DO LIST:
//  automatic update of url paramater
//  actions tab
//  settings tab
//  full panel
//  actions process
//  reading of settings handle in library
//  writing of settings handle in library
//  not read-only



public class PanelDatabaseSet extends JPanel implements ActionListener, DocumentListener
{

    I18nControl ic = I18nControl.getInstance();
    DbToolAccess m_da = null;

    Font font_big, font_normal, font_normal_b;
    JLabel label;
    JButton button;

    JLabel label_class, label_dialect;
    JTextField tf_url;

    JComboBox cb_databases = null;

    //NutritionTreeDialog m_dialog = null;
    DatabaseSettings m_database_settings = null;

    Hashtable settings_table = new Hashtable();
    ArrayList<String> url_list = new ArrayList<String>(); 

    Hashtable<String, JLabel> parameters_label = new Hashtable<String, JLabel>();
    Hashtable<String, JTextField> parameters_textfield = new Hashtable<String, JTextField>();

    int posy = 320;



    public PanelDatabaseSet(DbTool dia)
    {

        super();

        //m_dialog = dia;
        m_da = DbToolAccess.getInstance();

        font_big = m_da.getFont(DbToolAccess.FONT_BIG_BOLD);
        font_normal_b = m_da.getFont(DbToolAccess.FONT_NORMAL_BOLD);
        font_normal = m_da.getFont(DbToolAccess.FONT_NORMAL);

        createPanel();

    }



    public void createPanel()
    {

	this.setSize(500, 560);
        this.setLayout(null);

        Font fnt_18 = new Font("Times New Roman", Font.BOLD, 18);


        label = new JLabel(ic.getMessage("DB_CONFIGURATION"));
        label.setBounds(0, 25, 500, 40);
        label.setFont(font_big); 
        label.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(label, null);


        label = new JLabel(ic.getMessage("DB_PRESS_HELP"));    //"DB_CONFIG_DESC"));
        label.setBounds(40, 90, 410, 25);
        label.setFont(font_normal); 
        this.add(label, null);

	
        label = new JLabel(ic.getMessage("DATABASE_TYPE")+":");
        label.setBounds(40, 140, 200, 25);
        label.setFont(font_normal); 
        this.add(label, null);

	cb_databases = new JComboBox(m_da.getAvailableDatabases());
	cb_databases.setBounds(200, 140, 190, 25);
	cb_databases.setFont(font_normal);
	this.add(cb_databases, null);


	label = new JLabel(ic.getMessage("DATABASE_CLASSNAME")+":");
	label.setBounds(40, 170, 200, 25);
	label.setFont(font_normal); 
	this.add(label, null);

	label_class = new JLabel("");
	label_class.setBounds(200, 170, 200, 25);
	label_class.setFont(font_normal); 
	this.add(label_class, null);


	label = new JLabel(ic.getMessage("HIBERNATE_DIALECT")+":");
	label.setBounds(40, 200, 200, 25);
	label.setFont(font_normal); 
	this.add(label, null);

	label_dialect = new JLabel("");
	label_dialect.setBounds(200, 200, 200, 25);
	label_dialect.setFont(font_normal); 
	this.add(label_dialect, null);


	label = new JLabel(ic.getMessage("JDBC_URL")+":");
	label.setBounds(40, 230, 200, 25);
	label.setFont(font_normal); 
	this.add(label, null);

	tf_url = new JTextField();
	tf_url.setBounds(40, 255, 400, 25);
	tf_url.setFont(font_normal); 
	this.add(tf_url, null);


	label = new JLabel(ic.getMessage("USERNAME")+":");
	label.setBounds(40, 290, 200, 25);
	label.setFont(font_normal); 
	this.add(label, null);

	JTextField tf_username = new JTextField();
	tf_username.setBounds(200, 290, 200, 25);
	tf_username.setFont(font_normal); 
	tf_username.setActionCommand("textfield");
	tf_username.addActionListener(this);
	this.add(tf_username, null);

	this.parameters_textfield.put("<username>", tf_username);


	label = new JLabel(ic.getMessage("PASSWORD")+":");
	label.setBounds(40, 320, 200, 25);
	label.setFont(font_normal); 
	this.add(label, null);

	JTextField tf_password = new JTextField();
	tf_password.setBounds(200, 320, 180, 25);
	tf_password.setFont(font_normal); 
	tf_password.setActionCommand("textfield");
	tf_password.addActionListener(this);
	this.add(tf_password, null);

	this.parameters_textfield.put("<password>", tf_password);



/*
        button = new JButton(ic.getMessage("ADD_"));
        button.setBounds(110, 190, 170, 25);
        button.setFont(font_normal);
        button.addActionListener(this);
        button.setActionCommand("add_");
        this.add(button);
*/
/*        button = new JButton(ic.getMessage("ADD_"));
        button.setBounds(110, 220, 170, 25);
        button.setFont(font_normal);
        button.setActionCommand("add_p");
        button.addActionListener(this);
        //this.add(button);

        label = new JLabel(ic.getMessage("EDIT_VIEW"));
        label.setBounds(40, 280, 300, 30);
        label.setFont(fnt_18); 
        //this.add(label, null);

        label = new JLabel(ic.getMessage("EDIT_VIEW_DESC"));
        label.setBounds(40, 310, 300, 60);
        label.setFont(font_normal); 
        //this.add(label, null);
*/
//	System.out.println("Pabnel paint");
        return;
    }


    public void setData(DatabaseSettings intr)
    {
	m_database_settings = intr;
	reDraw();
	processExistingUrl(intr.url);
    }

    public void reDraw()
    {


	System.out.println("Redraw Not working fully");

	String dbName = m_database_settings.db_name;
	cb_databases.setEnabled(false);

	Object av[] = m_da.getAvailableDatabases();

	for (int i=0; i<av.length; i++)
	{
	    DatabaseDefObject ddo = (DatabaseDefObject)av[i];
	    if (ddo.name.equals(dbName))
	    {
		cb_databases.setSelectedIndex(i);
	    }
	}

	//System.out.println("m_da: " + m_da);
	//System.out.println("tableOfDb: " + m_da.m_tableOfDatabases);
	//System.out.println("dbName: " + dbName);

	DatabaseDefObject ddo = (DatabaseDefObject)m_da.m_tableOfDatabases.get(dbName);

	label_class.setText(ddo.driver);
	label_dialect.setText(ddo.short_dialect);

	//System.out.println(ddo.url);


	// remove old

	for (Enumeration en = this.parameters_textfield.keys(); en.hasMoreElements(); )
	{

	    String key = (String)en.nextElement();

	    System.out.println("key=" + key);

	    if ((key.equals("<username>")) || (key.equals("<password>")))
	    {
		continue;
	    }

	    this.remove(this.parameters_textfield.get(key));

	}

	this.repaint();
	posy = 320;


	unpackURL(ddo.url);



    }


    public void unpackURL(String url)
    {
	StringTokenizer tok = new StringTokenizer(url, "<");
	url_list.clear();


	//ArrayList list = new ArrayList();

	while (tok.hasMoreTokens())
	{
	    String str = tok.nextToken();

	    if (str.indexOf(">")!=-1)
	    {
		str = "<" + str;

		String s1 = str.substring(0, str.indexOf(">")+1);
		//System.out.println("x " + s1);
		url_list.add(s1);

		String s2 = str.substring(str.indexOf(">")+1);

		if (s2.trim().length()!=0)
		{
		    url_list.add(s2);
		}
	    }
	    else
                url_list.add(str);
	    //System.out.println(tok.nextToken());
	}

/*
	Iterator it = url_list.iterator();

	while (it.hasNext())
	{
	    System.out.println(it.next());
	}
*/
	processParameters();


    }


    public void processParameters()
    {

	Iterator it = url_list.iterator();

	while (it.hasNext())
	{

	    String par = (String)it.next();

	    if (par.indexOf("<")!=-1)
	    {

		if ((par.equals("<username>")) || 
		    (par.equals("<password>")))
		    continue;

		String p1 = par.substring(1, par.length()-1).toUpperCase();
		posy += 30;

		JLabel param_label = new JLabel(ic.getMessage(p1)+":");
		param_label.setBounds(40, posy, 200, 25);
		param_label.setFont(font_normal); 
		this.add(param_label, null);
		this.parameters_label.put(par, param_label);

		JTextField textfield = new JTextField();
		textfield.setBounds(200, posy, 180, 25);
		textfield.setFont(font_normal); 
		this.add(textfield, null);
		this.parameters_textfield.put(par, textfield);

	    }
	}

    }


    public void processExistingUrl(String url)
    {
	String url2 = url;

	System.out.println("Url current: " + url);

	int sz = this.url_list.size();
	System.out.println("sz=" + sz);

	for (int i=0; i<sz; i+=2)
	{

	    System.out.println("i=" + i);

	    if ((i+2)<sz)
	    {
		String s1 = this.url_list.get(i);
		String s2 = this.url_list.get(i+1);
		String s3 = this.url_list.get(i+2);

		String par = url2.substring(s1.length());
		par = par.substring(0, par.indexOf(s3));

		url2 = url2.substring(url2.indexOf(par) + par.length());

		System.out.println(s2 + " = " + par);

		JTextField tf = this.parameters_textfield.get(s2);
		tf.setText(par);

	    }
	    else if ((i+1)<sz)
	    {
		String s1 = this.url_list.get(i);
		String s2 = this.url_list.get(i+1);

		String par = url2.substring(s1.length());

		System.out.println(s2 + " = " + par);

		JTextField tf = this.parameters_textfield.get(s2);
		tf.setText(par);
	    }
	    else
	    {
		System.out.println("ERROR: Error in program.");
	    }
	}




    }


    // Document Event

    /**
     * Gives notification that there was an insert into the document.  The
     * range given by the DocumentEvent bounds the freshly inserted region.
     *
     * @param e the document event
     */
    public void insertUpdate(DocumentEvent e)
    {
	System.out.println("insert");
	//filterEntries();
    }

    /**
     * Gives notification that a portion of the document has been
     * removed.  The range is given in terms of what the view last
     * saw (that is, before updating sticky positions).
     *
     * @param e the document event
     */
    public void removeUpdate(DocumentEvent e)
    {
	System.out.println("remove");
	//filterEntries();
    }

    public void changedUpdate(DocumentEvent e) {}



    public void addDocListener(JTextField field)
    {

	AbstractDocument doc = null; 
	Document styledDoc = field.getDocument();

	if (styledDoc instanceof AbstractDocument)
	{
	    doc = (AbstractDocument)styledDoc;
	    //doc.setDocumentFilter(new DocumentSizeFilter(MAX_CHARACTERS));
	}
	else
	{
	    System.err.println("Text pane's document isn't an AbstractDocument!");
	    System.exit(-1);
	} 

	doc.addDocumentListener(this);

    }


    /**
     *  Action Listener
     */
    public void actionPerformed(ActionEvent e) 
    {

        String action = e.getActionCommand();

	if (action.equals("textfield"))
	{
	    System.out.println("Textfield");
	}
	else
            System.out.println("PanelDatabaseSet::Unknown command: " + action);
  
    }

}
    
    

