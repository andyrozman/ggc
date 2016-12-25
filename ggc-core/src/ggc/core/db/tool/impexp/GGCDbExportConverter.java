package ggc.core.db.tool.impexp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.db.hibernate.tool.data.DatabaseTableConfiguration;
import com.atech.db.hibernate.tool.data.management.export.DbExportConverterAbstract;
import com.atech.db.hibernate.transfer.ExportTool;

import ggc.core.db.hibernate.food.*;
import ggc.core.db.tool.data.GGCDatabaseTableConfiguration;

/**
 * Created by andy on 15/12/16.
 */
public class GGCDbExportConverter extends DbExportConverterAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(GGCDbExportConverter.class);


    public String convert(DatabaseTableConfiguration tableConfiguration, Object hibernateObject)
    {
        GGCDatabaseTableConfiguration ggcTableConfig = (GGCDatabaseTableConfiguration) tableConfiguration;

        switch (ggcTableConfig)
        {
            case DayValueH:
                break;
            case SettingsH:
                break;
            case ColorSchemeH:
                break;

            case PumpDataH:
                break;
            case PumpDataExtendedH:
                break;
            case PumpProfileH:

                break;

            case CGMSDataH:
                break;
            case CGMSDataExtendedH:
                break;

            case FoodGroupH:
                {
                    FoodGroupH eh = (FoodGroupH) hibernateObject;

                    return createTableExportString(eh.getId(), eh.getName(), eh.getName_i18n(), eh.getDescription());
                }

            case FoodDescriptionH:
                {
                    FoodDescriptionH eh = (FoodDescriptionH) hibernateObject;

                    return createTableExportString(eh.getId(), eh.getGroup_id(), eh.getName(), eh.getName_i18n(),
                        eh.getRefuse(), eh.getNutritions(), eh.getHome_weights());
                }

            case FoodUserGroupH:
                {
                    FoodUserGroupH eh = (FoodUserGroupH) hibernateObject;

                    return createTableExportString(eh.getId(), eh.getName(), eh.getName_i18n(), eh.getDescription(),
                        eh.getParent_id(), eh.getChanged());
                }

            case FoodUserDescriptionH:
                {
                    FoodUserDescriptionH eh = (FoodUserDescriptionH) hibernateObject;

                    String nutr = eh.getNutritions();
                    if (nutr == null)
                    {
                        nutr = "";
                    }
                    else
                    {
                        nutr = nutr.replace(",", ".");
                    }

                    return createTableExportString(eh.getId(), eh.getName(), eh.getName_i18n(), eh.getGroup_id(),
                        eh.getRefuse(), eh.getDescription(), eh.getHome_weights(), nutr, eh.getChanged());
                }

            case MealH:
                {
                    MealH eh = (MealH) hibernateObject;

                    return createTableExportString(eh.getId(), eh.getName(), eh.getName_i18n(), eh.getGroup_id(),
                        eh.getDescription(), eh.getParts(), eh.getNutritions(), eh.getExtended(), eh.getComment(),
                        eh.getChanged());
                }

            case MealGroupH:
                {
                    MealGroupH eh = (MealGroupH) hibernateObject;

                    return createTableExportString(eh.getId(), eh.getName(), eh.getName_i18n(), eh.getDescription(),
                        eh.getParent_id(), eh.getChanged());
                }

            case NutritionDefinitionH:
                {
                    NutritionDefinitionH eh = (NutritionDefinitionH) hibernateObject;

                    return createTableExportString(eh.getId(), eh.getWeight_unit(), eh.getTag(), eh.getName(),
                        eh.getDecimal_places(), eh.getStatic_entry());
                }

            case NutritionHomeWeightTypeH:
                {
                    NutritionDefinitionH eh = (NutritionDefinitionH) hibernateObject;

                    return createTableExportString(eh.getId(), eh.getName(), eh.getStatic_entry());
                }

            default:
                String errorString = String.format(
                    "GGCDbExportConverter doesn't support this database object for export (%s)",
                    tableConfiguration.getObjectName());
                LOG.error(errorString);
                throw new RuntimeException(errorString);

        }

        return null;

    }


    private String createTableExportString(Object... elements)
    {
        StringBuilder stringBuilder = new StringBuilder();

        for (Object element : elements)
        {
            stringBuilder.append(ExportTool.DELIMITER);
            stringBuilder.append(element);
        }

        return stringBuilder.substring(ExportTool.DELIMITER.length());
    }

}
