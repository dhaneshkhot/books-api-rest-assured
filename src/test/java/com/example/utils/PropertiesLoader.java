package com.example.utils;


import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.core.io.ClassPathResource;

import static java.lang.String.format;

public class PropertiesLoader {
    private static final PropertiesLoader PROPERTIES_LOADER = new PropertiesLoader();
    private final PropertiesConfiguration properties = new PropertiesConfiguration();
    private static final String ENCODING = "UTF-8";
    private static final String DEFAULT_ENV = "test";

    private PropertiesLoader() {
        String env = Properties.getEnvironment();
        if (env == null) {
            env = DEFAULT_ENV;
        }

        try {
            properties.load(new ClassPathResource(format("%s.properties", env)).getInputStream());
        } catch (Exception e) {
            throw new RuntimeException(format("Can't load properties file for ENV = %s", env), e);
        }

    }

    public static PropertiesLoader getInstance() {
        return PROPERTIES_LOADER;
    }

    /**
     * Returns String property
     * @param name
     * @return
     */
    public String getStrProperty(String name) {

        String overrideProperty = System.getProperty(name);
        String valueToReturn;

        if (!ValidationUtils.isEmpty(overrideProperty)) {
            valueToReturn = overrideProperty;
        } else {
            valueToReturn = properties.getString(name);
        }

        return valueToReturn;
    }

    /**
     * Returns Int property
     * @param name
     * @return
     */
    public int getIntProperty(String name) {

        String overrideProperty = System.getProperty(name);
        int valueToReturn;

        if (!ValidationUtils.isEmpty(overrideProperty)) {
            valueToReturn = Integer.parseInt(overrideProperty);
        } else {
            valueToReturn = properties.getInt(name);
        }

        return valueToReturn;
    }

}
