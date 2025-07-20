package com.ravinduw.apps.shoppinglistapp.awssmpkg;

import com.zaxxer.hikari.HikariDataSource;
import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class AWSSMHandler {

    @Bean
    public DataSource getDataSource() {

        JSONObject secrets = getSecret();

        if (secrets == null) throw new RuntimeException("Secrets not found");

        String host = secrets.getString("host");
        String port = secrets.getString("port");
        String dbname = secrets.getString("dbname");
        String username = secrets.getString("username");
        String password = secrets.getString("password");

        String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + dbname;

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

        return dataSource;

    }

    private JSONObject getSecret() {

        String secretName = "prod/RWApps/DB";
        Region region = Region.of("ap-southeast-1");

        // Create a Secrets Manager client
        SecretsManagerClient client = SecretsManagerClient.builder()
                .region(region)
                .build();

        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

        GetSecretValueResponse getSecretValueResponse;

        try {
            getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
            String secret = getSecretValueResponse.secretString();
            return new JSONObject(secret);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    public static String getRememberMeKey() {

        String secretName = "prod/App/Keys";
        Region region = Region.of("ap-southeast-1");

        // Create a Secrets Manager client
        SecretsManagerClient client = SecretsManagerClient.builder()
                .region(region)
                .build();

        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

        GetSecretValueResponse getSecretValueResponse;

        try {
            getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
            String secret = getSecretValueResponse.secretString();
            JSONObject secretJSON = new JSONObject(secret);
            return secretJSON.getString("groceries-app/rme/key");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

}
