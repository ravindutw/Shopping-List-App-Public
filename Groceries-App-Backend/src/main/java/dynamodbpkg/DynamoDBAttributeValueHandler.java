package dynamodbpkg;

import software.amazon.awssdk.services.dynamodb.model.AttributeAction;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.HashMap;

public class DynamoDBAttributeValueHandler {

    protected HashMap<String, AttributeValue> putAttributeValueMap;
    protected HashMap<String, AttributeValueUpdate> updateAttributeValueMap;

    public DynamoDBAttributeValueHandler() {

        putAttributeValueMap = new HashMap<>();
        updateAttributeValueMap = new HashMap<>();

    }

    public void addStringAttribute(String key, String value) throws Exception {

        putAttributeValueMap.put(key, AttributeValue.builder().s(value).build());

    }

    public void addIntAttribute(String key, int values) throws DynamoDbException {

        putAttributeValueMap.put(key, AttributeValue.builder().n(String.valueOf(values)).build());

    }

    public void addStringUpdateAttribute(String key, String values) throws Exception {

        updateAttributeValueMap.put(key, AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(values).build())
                .action(AttributeAction.PUT)
                .build());

    }

    public void addIntUpdateAttribute(String key, int values) throws Exception {

        updateAttributeValueMap.put(key, AttributeValueUpdate.builder()
                .value(AttributeValue.builder().n(String.valueOf(values)).build())
                .action(AttributeAction.PUT)
                .build());

    }

    public HashMap<String, AttributeValue> getAttributeValueMap() throws Exception {

        return putAttributeValueMap;

    }

    public HashMap<String, AttributeValueUpdate> getUpdateAttributeValueMap() throws Exception {

        return updateAttributeValueMap;

    }

}