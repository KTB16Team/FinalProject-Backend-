package aimo.backend.common.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import aimo.backend.common.properties.AiServerProperties;
import aimo.backend.common.properties.CorsProperties;
import aimo.backend.common.properties.JwtProperties;
import aimo.backend.common.properties.SecurityProperties;

@Configuration
@EnableConfigurationProperties(value = {
	JwtProperties.class,
	SecurityProperties.class,
	CorsProperties.class,
	AiServerProperties.class
})
public class PropertiesConfig {
}
