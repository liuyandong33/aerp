package build.dream.aerp.utils;

import build.dream.aerp.database.SearchModel;
import build.dream.aerp.domains.Config;

public class ConfigUtils {
    public static Config obtainConfig(String name) {
        SearchModel searchModel = SearchModel.builder()
                .equal(Config.ColumnName.NAME, name)
                .build();
        return DatabaseUtils.find(ApplicationHandler.application, Config.class, searchModel);
    }
}
