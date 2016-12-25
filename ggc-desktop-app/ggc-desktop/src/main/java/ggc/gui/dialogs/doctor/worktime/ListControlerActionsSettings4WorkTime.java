package ggc.gui.dialogs.doctor.worktime;

import javax.swing.*;

import com.atech.graphics.components.jlist.JListControlerActions;
import com.atech.graphics.components.jlist.JListControlerSettings;
import ggc.core.db.dto.WorkingTimeDTO;

/**
 * Created by andy on 15.02.16.
 */
public class ListControlerActionsSettings4WorkTime implements JListControlerSettings, JListControlerActions
{

    private JDialog parentDialog;


    public Object executeItemAddAction()
    {
        WorkingTimeDialog workingTimeDialog = new WorkingTimeDialog(this.parentDialog);

        if (workingTimeDialog.wasOperationSuccessful())
        {
            return workingTimeDialog.getObject();
        }

        return null;
    }


    public void executeItemEditAction(Object item)
    {
        WorkingTimeDialog workingTimeDialog = new WorkingTimeDialog(this.parentDialog, (WorkingTimeDTO) item, true);
    }


    public void executeItemDeleteAction(Object item)
    {

    }


    public Double getLeftMargin()
    {
        return 0.1d;
    }


    public Double getSpliterWidth()
    {
        return 10d;
    }


    public Double getRightMargin()
    {
        return 0.1d;
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
        return 5d;
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
