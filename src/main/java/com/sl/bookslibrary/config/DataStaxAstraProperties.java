package com.sl.bookslibrary.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.File;

@ConfigurationProperties(prefix = "datastax.astra")
@Data
public class DataStaxAstraProperties {

    private File secureConnectBundle;
}
