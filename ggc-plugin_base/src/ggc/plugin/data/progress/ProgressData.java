package ggc.plugin.data.progress;

public class ProgressData
{

    private ProgressType currentProgressType;
    private int progressStatic;
    private int progressDynamic;
    private int progressStaticPercentage;
    private int progressStaticMax;
    private int progressDynamicMax;
    private int currentProgress;
    private ProgressType baseProgressType;

    public ProgressType getCurrentProgressType()
    {
        return currentProgressType;
    }

    public void setCurrentProgressType(ProgressType currentProgressType)
    {
        this.currentProgressType = currentProgressType;
    }

    public int getProgressStatic()
    {
        return progressStatic;
    }

    public void setProgressStatic(int progressStatic)
    {
        this.progressStatic = progressStatic;
    }

    public int getProgressDynamic()
    {
        return progressDynamic;
    }

    public void setProgressDynamic(int progressDynamic)
    {
        this.progressDynamic = progressDynamic;
    }

    public int getProgressStaticPercentage()
    {
        return progressStaticPercentage;
    }

    public void setProgressStaticPercentage(int progressStaticPercentage)
    {
        this.progressStaticPercentage = progressStaticPercentage;
    }

    public int getProgressStaticMax()
    {
        return progressStaticMax;
    }

    public void setProgressStaticMax(int progressStaticMax)
    {
        this.progressStaticMax = progressStaticMax;
    }

    public int getProgressDynamicMax()
    {
        return progressDynamicMax;
    }

    public void setProgressDynamicMax(int progressDynamicMax)
    {
        this.progressDynamicMax = progressDynamicMax;
    }

    public int getCurrentProgress()
    {
        return currentProgress;
    }

    public void setCurrentProgress(int currentProgress)
    {
        this.currentProgress = currentProgress;
    }

    public ProgressType getBaseProgressType()
    {
        return baseProgressType;
    }

    public void setBaseProgressType(ProgressType baseProgressType)
    {
        this.baseProgressType = baseProgressType;
    }

    public void addToProgressAndCheckIfCanceled(ProgressType progressType, int progressAdd)
    {
        if (currentProgressType == ProgressType.Static)
        {
            progressStatic += progressAdd;
        }
        else
        {
            progressDynamic += progressAdd;
        }
    }

    public void configureProgressReporter(ProgressType baseProgressType, int staticProgressPercentage,
            int staticMaxElements, int dynamicMaxElements)
    {
        this.baseProgressType = baseProgressType;
        this.progressStaticPercentage = staticProgressPercentage;
        this.progressStaticMax = staticMaxElements;
        this.progressDynamicMax = dynamicMaxElements;
    }

    public int calculateProgress()
    {
        if (baseProgressType == ProgressType.Static)
        {
            double p = this.progressStatic / (this.progressStaticMax * 1.0);
            p *= 100.0;

            this.currentProgress = (int) p;
        }
        else if (baseProgressType == ProgressType.Dynamic)
        {
            double p = this.progressDynamic / (this.progressDynamicMax * 1.0);
            p *= 100.0;

            this.currentProgress = (int) p;
        }
        else
        {
            double st = this.progressStatic / (this.progressStaticMax * 1.0);
            st *= 100.0;

            double dy = this.progressDynamic / (this.progressDynamicMax * 1.0);
            dy *= 100.0;

            double progr = (((st * this.progressStaticPercentage) / 100.0) + ((dy * 100.0) / (100 - this.progressStaticPercentage)));

            this.currentProgress = (int) progr;

        }

        return this.currentProgress;
    }

}
