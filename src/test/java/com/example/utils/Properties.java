package com.example.utils;

public class Properties {
    private static final Properties PROPERTIES = new Properties();

    public static Properties getInstance() {
        return PROPERTIES;
    }

    private Properties() {
    }

    public static String getEnvironment() {
        return System.getProperty("env");
    }

    private static PropertiesLoader getProp() {
        return PropertiesLoader.getInstance();
    }

    public static String getMySQlConnectionString() {
        return getProp().getStrProperty("spring.datasource.url");
    }

    public static String getDriverClassName() {
        return getProp().getStrProperty("spring.datasource.driver-class-name");
    }

    public static String getDbUsername(){
        return System.getProperty("dbUsername");
    }

    public static String getDbPassword(){
        return System.getProperty("dbPassword");
    }

    public static String getApiEndpoint(){
        return getProp().getStrProperty("api.endpoint");
    }
}
