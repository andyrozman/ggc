package ggc.core.data.defs;

/**
 * Created by andy on 14.02.16.
 */
public enum WeekDay
{
    Monday("MONDAY"), //
    Tuesday("TUESDAY"), //
    Wedneday("WEDNESDAY"), //
    Thursday("THURSDAY"), //
    Friday("FRIDAY"), //
    Saturday("SATURDAY"), //
    Sunday("SUNDAY"), //
    ;

    String dayI18nKey;


    WeekDay(String dayI18nKey)
    {
        this.dayI18nKey = dayI18nKey;
    }


    public String getDayI18nKey()
    {
        return this.dayI18nKey;
    }

}
