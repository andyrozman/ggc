package com.atech.db.tool;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import java.awt.Component;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.JTree;


import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
import java.util.*;


public class DbToolTreeCellRenderer extends DefaultTreeCellRenderer
{

    /** Last tree the renderer was painted in. */
    //private JTree tree;

    public Font defFont = null;
    public Font boldFont = null;

    public DbToolTreeCellRenderer()
    {
	super();

	DbToolAccess da = DbToolAccess.getInstance();

	defFont = da.getFont(DbToolAccess.FONT_NORMAL);
	boldFont = da.getFont(DbToolAccess.FONT_NORMAL_BOLD);
	/*defFont = super.getFont();
	defFont = 

	System.out.println(defFont);
	boldFont = defFont.deriveFont(Font.BOLD);*/
    }

    


    /**
      * Configures the renderer based on the passed in components.
      * The value is set from messaging the tree with
      * <code>convertValueToText</code>, which ultimately invokes
      * <code>toString</code> on <code>value</code>.
      * The foreground color is set based on the selection and the icon
      * is set based on on leaf and expanded.
      */
    public Component getTreeCellRendererComponent(JTree tree, Object value,
						  boolean sel,
						  boolean expanded,
						  boolean leaf, int row,
						  boolean hasFocus) 
    {
	String stringValue = tree.convertValueToText(value, sel,
					  expanded, leaf, row, hasFocus);
	setFont(this.defFont);

	//System.out.println(value);
	if (value instanceof DatabaseSettings)
	{
	    DatabaseSettings ds = (DatabaseSettings)value;

	    if (ds.isDefault)
		setFont(this.boldFont);
	}


        //this.tree = tree;
	this.hasFocus = hasFocus;
	setText(stringValue);
	if(sel)
	    setForeground(getTextSelectionColor());
	else
	    setForeground(getTextNonSelectionColor());
	// There needs to be a way to specify disabled icons.
	if (!tree.isEnabled()) 
	{
	    setEnabled(false);
	    if (leaf) {
		setDisabledIcon(getLeafIcon());
	    } else if (expanded) {
		setDisabledIcon(getOpenIcon());
	    } else {
		setDisabledIcon(getClosedIcon());
	    }
	}
	else 
	{
	    setEnabled(true);
	    if (leaf) {
		setIcon(getLeafIcon());
	    } else if (expanded) {
		setIcon(super.getOpenIcon());
	    } else {
		setIcon(super.getClosedIcon());
	    }
	}
        setComponentOrientation(tree.getComponentOrientation());
	    
	selected = sel;

	return this;
    }


}
