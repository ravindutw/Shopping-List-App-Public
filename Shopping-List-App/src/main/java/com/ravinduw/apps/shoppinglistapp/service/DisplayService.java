package com.ravinduw.apps.shoppinglistapp.service;

import dynamodbpkg.DynamoDBHandler;
import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

import java.util.Map;

@Service
@NoArgsConstructor
public class DisplayService {

    private final String dbName = "Replace with your DynamoDB table name"; // Replace with your DynamoDB table name
    private final DynamoDBHandler db = new DynamoDBHandler(dbName);

    public JSONArray getItems() throws Exception {

        ScanResponse scanResponse = db.scan();
        JSONArray sendResponse = new JSONArray();

        for (Map<String, AttributeValue> item : scanResponse.items()) {
            if (item.get("checked").s().equals("false")) {

                JSONObject itemJSON = new JSONObject();
                itemJSON.put("id", item.get("id").s());
                itemJSON.put("name", item.get("name").s());
                itemJSON.put("checked", item.get("checked").s());

                sendResponse.put(itemJSON);

            };
        }

        return sendResponse;

    }

}
