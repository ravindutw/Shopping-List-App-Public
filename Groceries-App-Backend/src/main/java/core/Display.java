package core;

import dynamodbpkg.DynamoDBHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

import java.util.Map;
import java.util.Objects;

public class Display {

    private RegularUser user;
    private String location;
    private final String dbName = "groceries-app-db";
    private final DynamoDBHandler db = new DynamoDBHandler(dbName);

    public Display(RegularUser user, String location) {
        this.user = user;
        this.location = location;
    }

    public JSONArray getItems() throws Exception {

        QueryResponse response = db.query("checked", "false");
        JSONArray sendResponse = new JSONArray();

        for (Map<String, AttributeValue> item : response.items()) {
            if (String.valueOf(item.get("location")).equals(location)) {

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
