package ggc.gui.dialogs.defs;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JTable;

import com.atech.i18n.I18nControlAbstract;

public abstract class GUIListDefAbstract
{
    
    public static final int ACTION_ADD = 1;
    public static final int ACTION_EDIT = 2;
    public static final int ACTION_DELETE = 3;
    
    protected I18nControlAbstract ic = null;
    protected String translation_root = null;
    
    protected JTable table;
    protected ArrayList full_list;
    protected ArrayList active_list;
    
    
    // ic, translation root
    public abstract void init();
    
    public abstract boolean hasFilter();
    
    public abstract String getTitle();
    
    public String getMessage(String keyword)
    {
        return ic.getMessage(this.translation_root + "_" + keyword);
    }

    public String getTranslationRoot()
    {
        return this.translation_root;
    }
    
    public abstract JTable getJTable();

    
    public abstract void doTableAction(int action);
    
    public abstract String getDefName();
    
    public abstract Dimension getWindowSize();
    
    public abstract Rectangle getTableSize();
    
    
}
