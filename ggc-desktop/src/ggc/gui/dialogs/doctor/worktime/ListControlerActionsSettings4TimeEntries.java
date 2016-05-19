package ggc.gui.dialogs.doctor.worktime;

import javax.swing.*;

import com.atech.graphics.components.jlist.JListControlerActions;
import com.atech.graphics.components.jlist.JListControlerSettings;
import ggc.core.db.dto.WorkingTimeTimeEntry;

/**
 * Created by andy on 15.02.16.
 */
public class ListControlerActionsSettings4TimeEntries implements JListControlerSettings, JListControlerActions
{

    private JDialog parentDialog;


    public Object executeItemAddAction()
    {
        WorkingTimeTimeDialog workingTimeTimeDialog = new WorkingTimeTimeDialog(this.parentDialog);

        if (workingTimeTimeDialog.wasOperationSuccessful())
        {
            return workingTimeTimeDialog.getObject();
        }

        return null;
    }


    public void executeItemEditAction(Object item)
    {
        WorkingTimeTimeDialog workingTimeTimeDialog = new WorkingTimeTimeDialog(this.parentDialog,
                (WorkingTimeTimeEntry) item, true);
    }


    public void executeItemDeleteAction(Object item)
    {

    }


    public Double getLeftMargin()
    {
        return 40d;
    }


    public Double getSpliterWidth()
    {
        return 10d;
    }


    public Double getRightMargin()
    {
        return 60d;
    }


    public Integer getButtonWidth()
    {
        return 30;
    }


    public Integer getButtonHeight()
    {
        return 30;
    }


    public Double getButtonTopMargin()
    {
        return 1d;
    }


    public Double getButtonBottomMargin()
    {
        return 1d;
    }


    public Double getButtonSpliter()
    {
        return 10d;
    }


    public JDialog getParentDialog()
    {
        return parentDialog;
    }


    public void setParentDialog(JDialog dialog)
    {
        this.parentDialog = dialog;
    }

}
