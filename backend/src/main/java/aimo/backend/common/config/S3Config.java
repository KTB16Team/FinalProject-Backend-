package aimo.backend.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import aimo.backend.common.properties.S3Properties;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class S3Config {

	private final S3Properties s3Properties;

	@Bean
	public AmazonS3 amazonS3Client(){
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(
			s3Properties.getAccessKey(),
			s3Properties.getSecretKey()
		);

		return AmazonS3ClientBuilder
			.standard()
			.withRegion(s3Properties.getRegion())
			.withCredentials(new AWSStaticCredentialsProvider(awsCreds))
			.build();
	}

}
