package service;

import com.ravinduw.apps.groceriesappbackend.entity.User;
import dynamodbpkg.DynamoDBHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

import java.util.Map;

public class DisplayService {

    private User user;
    private String location;
    private final String dbName = "groceries-app-db";
    private final DynamoDBHandler db = new DynamoDBHandler(dbName);

    public DisplayService(User user, String location) {
        this.user = user;
        this.location = location;
    }

    public JSONArray getItems() throws Exception {

        ScanResponse scanResponse = db.scan();
        JSONArray sendResponse = new JSONArray();

        for (Map<String, AttributeValue> item : scanResponse.items()) {
            if (item.get("checked").s().equals("false") && item.get("location").s().equals(location)) {

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
