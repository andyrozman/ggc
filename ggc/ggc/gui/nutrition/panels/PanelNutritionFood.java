package ggc.gui.nutrition.panels;

//import java.awt.Color;
import ggc.db.datalayer.FoodDescription;
import ggc.db.datalayer.NutritionDefinition;
import ggc.gui.nutrition.NutritionTreeDialog;
import ggc.util.DataAccess;
import ggc.util.I18nControl;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumnModel;

import com.atech.graphics.components.ATTableData;
import com.atech.graphics.components.ATTableModel;
import com.atech.graphics.layout.ZeroLayout;
import com.atech.i18n.I18nControlAbstract;

// WORK IN PROGRESS, PLEASE DO NOT TOUCH
// andyrozman


public class PanelNutritionFood extends GGCTreePanel /*JPanel*/ implements ActionListener
{

    I18nControl ic = I18nControl.getInstance();
    DataAccess m_da = null;

    Font font_big, font_normal, font_normal_b;
    JLabel label, label_refuse, label_name, label_name_i18n;
    JButton button;

    NutritionTreeDialog m_dialog = null;
    ArrayList<NutritionDataDisplay> list_nutrition = new ArrayList<NutritionDataDisplay>();
    ArrayList<HomeWeightDataDisplay> list_weight = new ArrayList<HomeWeightDataDisplay>();

    JTable table_1, table_2 = null;
    JScrollPane scroll_1, scroll_2 = null;

    ATTableModel model_2 = null;


    HomeWeightDataDisplay hwd = null;
    NutritionDataDisplay ndd = null;


    public PanelNutritionFood(NutritionTreeDialog dia)
    {

        super();

        m_dialog = dia;
        m_da = DataAccess.getInstance();


	this.hwd = new HomeWeightDataDisplay(ic);
	this.ndd = new NutritionDataDisplay(ic);

	font_big = m_da.getFont(DataAccess.FONT_BIG_BOLD);
        font_normal_b = m_da.getFont(DataAccess.FONT_NORMAL_BOLD);
        font_normal = m_da.getFont(DataAccess.FONT_NORMAL);

        createPanel();

    }



    public void createPanel()
    {

        this.setSize(520, 520);
        this.setLayout(new ZeroLayout(this.getSize()));

	Font fnt_14_bold = new Font("Times New Roman", Font.BOLD, 14);
	Font fnt_14 = new Font("Times New Roman", Font.PLAIN, 14);

	/*
	this.setId(ch.getId());
	this.setFood_group_id(ch.getFood_group_id());
	this.setName(ch.getName());
	this.setI18n_name(ch.getI18n_name());
	this.setRefuse(ch.getRefuse());
	this.setNutritions(ch.getNutritions());
	*/



        label = new JLabel(ic.getMessage("FOOD_DESCRIPTION"));
        label.setBounds(30, 30, 460, 40);
        label.setFont(font_big); 
	//label.setBackground(Color.red);
	//label.setBorder(BorderFactory.createLineBorder(Color.blue));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(label, null);



	label = new JLabel(ic.getMessage("FOOD_NAME") + ":" );
	label.setBounds(30, 100, 100, 30);
	label.setFont(fnt_14_bold); 
	this.add(label, null);

	label_name = new JLabel();
	label_name.setBounds(30, 125, 460, 50);
	label_name.setVerticalAlignment(JLabel.TOP);
	label_name.setFont(fnt_14); 
	this.add(label_name, ZeroLayout.DYNAMIC);


	label = new JLabel(ic.getMessage("TRANSLATED_NAME") + ":");
	label.setBounds(30, 140, 300, 60);
	label.setFont(fnt_14_bold); 
	this.add(label, null);


	this.label_name_i18n = new JLabel();  // 180
	this.label_name_i18n.setBounds(30, 165, 460, 50);
	label_name.setVerticalAlignment(JLabel.TOP);
	this.label_name_i18n.setFont(fnt_14); 
	this.add(this.label_name_i18n, ZeroLayout.DYNAMIC);



	label = new JLabel(ic.getMessage("REFUSE") + ":");
	label.setBounds(30, 210, 300, 30);
	label.setFont(fnt_14_bold); 
	this.add(label, null);


	label_refuse = new JLabel();
	label_refuse.setBounds(300, 210, 50, 30);
	label_refuse.setFont(fnt_14); 
	this.add(label_refuse, null);


	label = new JLabel(ic.getMessage("NUTRITIONS_FOOD") + ":");
	label.setBounds(30, 235, 300, 60);
	label.setFont(fnt_14_bold); 
	this.add(label, null);


        table_1 = new JTable();

	this.createModel(this.list_nutrition, this.table_1, this.ndd);

/*
	new AbstractTableModel()
                           {

                               public int getColumnCount() 
                               {
                                   return 4; //list.getColumnCount();
                               }


                               public int getRowCount() 
                               {
				   //System.out.println(list.size());
				   //return list.size();
				   return list_nutrition.size();
                               }


                               public Object getValueAt(int rowIndex, int columnIndex) 
                               {
				   NutritionDataDisplay ndd = (NutritionDataDisplay)list_nutrition.get(rowIndex);

				   switch(columnIndex)
				   {
				       case 1:
					   return ndd.getName();
				       case 2:
					   return ndd.getValue();
				       case 3:
					   return ndd.getWeightUnit();

				       case 0:
				       default:
					   return ndd.getId();


				   }

                                   //return list.get(rowIndex);
                               }
                           });  // table


        int twidth = this.getWidth()-50;
        table_1.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        TableColumnModel cm = table_1.getColumnModel();

	NutritionDataDisplay ndd = new NutritionDataDisplay();
	
        for (int i=0; i< ndd.getColumnsCount(); i++)
        {
            cm.getColumn(i).setHeaderValue(ndd.getColumnHeader(i));
            cm.getColumn(i).setPreferredWidth(ndd.getColumnWidth(i, 430)); 
	    //cm.getColumn(i).setPreferredWidth(

        }
*/
        table_1.setRowSelectionAllowed(true);
        table_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table_1.setDoubleBuffered(true);

        scroll_1 = new JScrollPane(table_1);
        scroll_1.setBounds(30, 280, 460, 100);
        this.add(scroll_1, ZeroLayout.DYNAMIC);
        scroll_1.repaint();

        scroll_1.updateUI();



	// home weight


	label = new JLabel(ic.getMessage("HOME_WEIGHTS") + ":");
	label.setBounds(30, 370, 300, 60);
	label.setFont(fnt_14_bold); 
	this.add(label, null);

//	HomeWeightDataDisplay hwd = new HomeWeightDataDisplay(ic);

	//System.out.println(hwd.getColumnsCount());

	table_2 = new JTable();

	createModel(this.list_weight, this.table_2, this.hwd);

/*
        this.model_2 = new ATTableModel(this.list_weight, hwd);

	table_2.setModel(this.model_2);
*/

/*
	new AbstractTableModel()
			   {

			       public int getColumnCount() 
			       {
				   return 3; //list.getColumnCount();
			       }


			       public int getRowCount() 
			       {
				   //System.out.println(list.size());
				   //return list.size();
				   return list_weight.size();
			       }


			       public Object getValueAt(int rowIndex, int columnIndex) 
			       {
				   HomeWeightDataDisplay hwd = (HomeWeightDataDisplay)list_weight.get(rowIndex);
				   //NutritionDataDisplay ndd = (NutritionDataDisplay)list.get(rowIndex);

				   switch(columnIndex)
				   {
				       case 1:
					   return "" + hwd.getAmount();
				       case 2:
					   return "" + hwd.getWeight();

				       case 0:
				       default:
					   return hwd.getWeightType();

				   }

				   //return list.get(rowIndex);
			       }
			   });  // table
*/
/*
	int twidth2 = this.getWidth()-50;
	table_2.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
	TableColumnModel cm2 = table_2.getColumnModel();

	for (int i=0; i< hwd.getColumnsCount(); i++)
	{
	    cm2.getColumn(i).setHeaderValue(hwd.getColumnHeader(i));
	    cm2.getColumn(i).setPreferredWidth(hwd.getColumnWidth(i, 430)); 
	    //cm.getColumn(i).setPreferredWidth(

	}
*/
	table_2.setRowSelectionAllowed(true);
	table_2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	table_2.setDoubleBuffered(true);

	scroll_2 = new JScrollPane(table_2);
	scroll_2.setBounds(30, 415, 460, 80);
	//scroll_2.
	this.add(scroll_2, ZeroLayout.DYNAMIC);
	scroll_2.repaint();
	scroll_2.updateUI();


        return;
    }

    
    public void setParent(Object obj)
    {
	
    }


    public void setData(Object obj)
    {
	FoodDescription fd = (FoodDescription)obj;

	//JLabel label, label_refuse, label_name, label_name_i18n;

	this.label_name.setText("<html><body>" +  fd.getName() + "</body></html>");
	this.label_name_i18n.setText("<html><body>" + fd.getName_i18n()+ "</body></html>");
	this.label_refuse.setText("" + fd.getRefuse() +" %");

	StringTokenizer strtok = new StringTokenizer(fd.getNutritions(), ";");
	list_nutrition.clear();

	while(strtok.hasMoreTokens())
	{
	    NutritionDataDisplay ndd1 = new NutritionDataDisplay(ic, strtok.nextToken());
	    ndd1.setNutritionDefinition(this.m_da.getDb().nutrition_defs.get(ndd1.getId()));
	    list_nutrition.add(ndd1);
	}

	this.createModel(this.list_nutrition, this.table_1, this.ndd);


	this.list_weight.clear();
	
	
	if ((fd.getHome_weights()!=null) && (!fd.getHome_weights().equals("")) && (!fd.getHome_weights().equals("null")))
	{
	    strtok = new StringTokenizer(fd.getHome_weights(), ";");
       
            while(strtok.hasMoreTokens())
            {
                HomeWeightDataDisplay hwd1 = new HomeWeightDataDisplay(ic, strtok.nextToken());
                //System.out.println("Home Weight: " + name);
                hwd1.setHomeWeightDefinition(this.m_da.getDb().homeweight_defs.get(hwd1.getId()).getName());
                this.list_weight.add(hwd1);
            }
	}

	this.createModel(this.list_weight, this.table_2, this.hwd);

    }
    

    public static final int MODEL_NUTRITIONS = 1;
    public static final int MODEL_HOME_WEIGHTS = 2;

    private void createModel(ArrayList<?> lst, JTable table, ATTableData object)
    {
	ATTableModel model = new ATTableModel(lst, object);
	table.setModel(model);	

//	int twidth2 = this.getWidth()-50;
	table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
	TableColumnModel cm2 = table.getColumnModel();

	for (int i=0; i< object.getColumnsCount(); i++)
	{
	    cm2.getColumn(i).setHeaderValue(object.getColumnHeader(i));
	    cm2.getColumn(i).setPreferredWidth(object.getColumnWidth(i, 430)); 
	}

    }



    /**
     *  Action Listener
     */
    public void actionPerformed(ActionEvent e) 
    {

        String action = e.getActionCommand();
        System.out.println("PanelNutritionFood::Unknown command: " + action);
  
    }


    private class NutritionDataDisplay extends ATTableData
    {

	private String id;
	private String value;
	private String name;
	private String weight_unit;


	public NutritionDataDisplay(I18nControlAbstract ic)
	{
	    super(ic);
	}


	public NutritionDataDisplay(I18nControlAbstract ic, String full)
	{
	    super(ic);
	//    System.out.println("Nutr: " + full);
	    int index = full.indexOf("=");
	    this.id = full.substring(0, index);
	    this.value = full.substring(index+1);
	}


	private void setNutritionDefinition(NutritionDefinition def)
	{
	    this.id = "" + def.getId();
	    this.name = def.getName();
	    //this.value = def.get.getTag();
	    this.weight_unit = def.getWeight_unit();
	}


	public void init()
	{
	    String[] col = { "ID", "NUTRITION", "AMOUNT", "UNITS" };
	    float[] col_size = { 0.1f, 0.5f, 0.2f, 0.2f  };

	    init(col, col_size);
	}

	public String getId()
	{
	    return this.id;
	}


	public String getColumnValue(int column)
	{
	    switch(column)
	    {
		case 1:
		    return this.name;
		case 2:
		    return this.value;
		case 3:
		    return this.weight_unit;

		case 0:
		default:
		    return "" + this.id;

	    }
	}
    }


    private class HomeWeightDataDisplay extends ATTableData
    {

	private String id;
	private String weight_type;
	private float amount = 1.0f;
	private float weight;


	public HomeWeightDataDisplay(I18nControlAbstract ic)
	{
	    super(ic);
	}


	public HomeWeightDataDisplay(I18nControlAbstract ic, String full)
	{
	    super(ic);

	    int index = full.indexOf("=");
	    this.id = full.substring(0, index);

	    String val = full.substring(index+1);

	    if (val.indexOf("*")>-1)
	    {
		index = val.indexOf("*");
		this.amount = Float.parseFloat(val.substring(0, index));

		String ww = val.substring(index+1);

		if (ww.startsWith("."))
		    ww = "0" + ww;

		this.weight = Float.parseFloat(ww);
	    }
	    else
	    {
		this.weight = Float.parseFloat(val);
	    }
	}



	public void init()
	{
	    String[] cols = { /*"ID",*/ "WEIGHT_TYPE", "AMOUNT", "WEIGHT" };
	    float[] cols_size = { /*0.1f,*/ 0.5f, 0.25f, 0.25f  };

	    init(cols, cols_size);
	}



	public String getColumnValue(int column)
	{
	    switch(column)
	    {
		case 1:
		    return "" + this.amount;

		case 2:
		    return "" + this.weight;

		case 0:
		default:
		    return this.weight_type;


	    }
	}


	public String getId()
	{
	    return this.id;
	}

	private void setHomeWeightDefinition(String name)
	{
	    this.weight_type = ic.getMessage(name);
	}

    }


}
    
    

