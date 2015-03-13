package ggc.core.db.dto;

import ggc.core.db.hibernate.StocktakingH;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andy on 28.02.15.
 */
public class StocktakingDTO
{

    private StocktakingH stocktakingH;

    private List<StockDTO> stockEntries = new ArrayList<StockDTO>();





    public void setStocktakingH(StocktakingH stocktakingH)
    {
        this.stocktakingH = stocktakingH;
    }

    public List<StockDTO> getEntries()
    {
        return stockEntries;
    }
}
