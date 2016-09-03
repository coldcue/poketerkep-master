package hu.poketerkep.master.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoDBConfig {
    /**
     * Default AWS Credentials
     * http://docs.aws.amazon.com/java-sdk/latest/developer-guide/credentials.html
     *
     * @return AWS Credentials
     */
    @Bean
    AWSCredentialsProvider awsCredentialsProvider() {
        return new DefaultAWSCredentialsProviderChain();
    }

    @Bean
    AmazonDynamoDBAsync amazonDynamoDBAsync() {
        return AmazonDynamoDBAsyncClientBuilder.standard()
                .withCredentials(awsCredentialsProvider())
                .withRegion(Regions.EU_WEST_1).build();
    }

    @Bean
    DynamoDBMapper dynamoDBMapper() {
        return new DynamoDBMapper(amazonDynamoDBAsync());
    }
}
