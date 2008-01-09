package ggc.data.cfg;

import ggc.util.DataAccess;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JLabel;


public class ConfigCellRenderer extends DefaultListCellRenderer
{

    DataAccess da = DataAccess.getInstance();

    //final static ImageIcon longIcon = new ImageIcon("long.gif");
    //final static ImageIcon shortIcon = new ImageIcon("short.gif");

    /* This is the only method defined by ListCellRenderer.  We just
     * reconfigure the Jlabel each time we're called.
     */
    public Component getListCellRendererComponent(
                                                 JList list,
                                                 Object value,   // value to display
                                                 int index,      // cell index
                                                 boolean iss,    // is the cell selected
                                                 boolean chf)    // the list and the cell have the focus
    {
        /* The DefaultListCellRenderer class will take care of
         * the JLabels text property, it's foreground and background
         * colors, and so on.
         */
        super.getListCellRendererComponent(list, value, index, iss, chf);

        /* We additionally set the JLabels icon property here.
         */
        //String s = value.toString();

        //DataAccess da = DataAccess.getInstance();

        //int idx = da.getSelectedConfigTypePart(s);

        setIcon(da.config_icons[index]);

        this.setHorizontalTextPosition(JLabel.CENTER);
        this.setHorizontalAlignment(JLabel.CENTER);
        this.setVerticalTextPosition(JLabel.BOTTOM);

        return this;
    }
}
