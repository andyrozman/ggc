package ggc.connect.software.web.nightscout.data;

/**
 * Created by andy on 4/1/18.
 */
public enum ImportType {

    SGVYearDataCount("/api/v1/count/entries/where?find[dateString][$gte]=YEAR-01-01&find[dateString][$lte]=YEAR-12-31&find[type]=sgv"), //
    SGVMonthDataCount("/api/v1/count/entries/where?find[dateString][$gte]=YEAR-MONTH-01&find[dateString][$lte]=YEAR-MONTH-LAST_DAY&find[type]=sgv"), //
    EntryMonthDataCount("/api/v1/count/entries/where?find[dateString][$gte]=YEAR-MONTH-01&find[dateString][$lte]=YEAR-MONTH-LAST_DAY"), //
    EntryMonthData("/api/v1/entries.json?find[dateString][$gte]=YEAR-MONTH-01&find[dateString][$lte]=YEAR-MONTH-LAST_DAY&count=COUNT"), //
    TreatmentMonthDataCount("/api/v1/count/treatment/where?find[dateString][$gte]=YEAR-MONTH-01&find[dateString][$lte]=YEAR-MONTH-LAST_DAY"), //
    TreatmentMonthData("/api/v1/treatments.json?find[created_at][$gte]=YEAR-MONTH-01&find[created_at][$lte]=YEAR-MONTH-LAST_DAY&count=COUNT"), //
    ;

    private String url;


    ImportType(String url)
    {
        this.url = url;
    }


    public String getUrl() {
        return url;
    }
}