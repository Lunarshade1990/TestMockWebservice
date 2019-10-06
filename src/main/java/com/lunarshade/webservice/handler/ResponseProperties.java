package com.lunarshade.webservice.handler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Getter
@Setter
@Component
public class ResponseProperties {

    private String goodResponsePath;
    private String badResponsePath;
    private String idXpathPattern;
    private String dateFormatPattern;
    private final static String PROP_NAME = "response.properties";

    public ResponseProperties() {
        loadProperties();
    }

    private void loadProperties() {
        Properties properties = new Properties();
        try(InputStream resourceStream = this.getClass().getClassLoader().getResourceAsStream(PROP_NAME)) {
            properties.load(resourceStream);
            this.goodResponsePath = properties.getProperty("good_response");
            this.badResponsePath = properties.getProperty("bad_response");
            this.idXpathPattern = properties.getProperty("id_pattern");
            this.dateFormatPattern = properties.getProperty("dateFormatPattern");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
