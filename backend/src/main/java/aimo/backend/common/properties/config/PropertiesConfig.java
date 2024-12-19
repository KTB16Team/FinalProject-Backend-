package aimo.backend.common.properties.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import aimo.backend.common.properties.AiServerProperties;
import aimo.backend.common.properties.CorsProperties;
import aimo.backend.common.properties.EmailProperties;
import aimo.backend.common.properties.FrontProperties;
import aimo.backend.common.properties.JwtProperties;
import aimo.backend.common.properties.RabbitMqProperties;
import aimo.backend.common.properties.RedisProperties;
import aimo.backend.common.properties.S3Properties;
import aimo.backend.common.properties.SecurityProperties;

@Configuration
@EnableConfigurationProperties(value = {
	JwtProperties.class,
	SecurityProperties.class,
	CorsProperties.class,
	AiServerProperties.class,
	S3Properties.class,
	FrontProperties.class,
	RedisProperties.class,
	RabbitMqProperties.class,
	EmailProperties.class
})
public class PropertiesConfig {
}
