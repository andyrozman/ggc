package ggc.core.db.dto;

import ggc.core.db.hibernate.StockH;


/**
 * Created by andy on 02.03.15.
 */
public class StockDTO extends StockH
{

    // history

    public String hasChanges()
    {
        return "YES";

        // NO
    }



}
