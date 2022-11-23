package chapter.one.LearnSpringBoot.config;

public class DBConfig {
    private static DBConfig instance;

    private DBConfig() {}

    public static DBConfig getInstance() {
        if (instance == null) {
            setInstance(new DBConfig());
        }
        return instance;
    }

    private static void setInstance(DBConfig dbConfig) {
        instance = dbConfig;
    }
}
