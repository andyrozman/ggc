package ggc.core.enums;

/**
 * Created by andy on 01.12.15.
 */
public enum UpgradeCheckStatus
{
    NotChecked(0), //
    CheckedNoUpdate(1), //
    CheckedProblem(2), //
    CheckedUpgradeAvailable(3), //
    DownloadAvailable(4)// not used
    ;

    private int code;


    UpgradeCheckStatus(int code)
    {
        this.code = code;
    }


    public int getCode()
    {
        return code;
    }
}
