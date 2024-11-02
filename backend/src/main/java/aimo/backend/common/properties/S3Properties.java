package aimo.backend.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
@ConfigurationProperties(prefix = "cloud.aws.s3")
public class S3Properties {
	private String accessKey;
	private String secretKey;
	private String region;
	private String bucketName;
}
