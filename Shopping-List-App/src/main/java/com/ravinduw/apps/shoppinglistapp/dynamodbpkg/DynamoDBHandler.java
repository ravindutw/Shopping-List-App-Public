package com.ravinduw.apps.shoppinglistapp.dynamodbpkg;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;

public class DynamoDBHandler {

    protected DynamoDbClient ddb;
    protected String tableName;
    protected String dbRequestID;

    public DynamoDBHandler(String tableName) {

        this.tableName = tableName;

        Region region = Region.AP_SOUTHEAST_1;

        ddb = DynamoDbClient.builder()
                .region(region)
                .build();

    }

    public void putItem(DynamoDBAttributeValueHandler attributes) throws Exception {

        HashMap<String, AttributeValue> putAttributeValueMap = attributes.getAttributeValueMap();

        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(putAttributeValueMap)
                .build();

        PutItemResponse response = ddb.putItem(request);

        dbRequestID = response.responseMetadata().requestId();

    }

    public Map<String, AttributeValue> getItem(String searchKey, String searchValue) throws Exception {

        HashMap<String, AttributeValue> getMap = new HashMap<>();

        boolean searchStatus = false;

        getMap.put(searchKey, AttributeValue.builder()
                .s(searchValue)
                .build());

        GetItemRequest request = GetItemRequest.builder()
                .key(getMap)
                .tableName(tableName)
                .build();

        return ddb.getItem(request).item();

    }

    public boolean checkAvailability(String searchKey, String searchValue) throws Exception {

        QueryResponse returnedItem = query(searchKey, searchValue);

        return returnedItem.count() != 0;


    }

    public QueryResponse query(String searchKey, String searchValue) throws Exception {

        HashMap<String, AttributeValue> getMap = new HashMap<>();

        getMap.put(":" + searchKey, AttributeValue.builder()
                .s(searchValue)
                .build());

        QueryRequest request = QueryRequest.builder()
                .tableName(tableName)
                .keyConditionExpression(searchKey + (" = :" + searchKey))
                .expressionAttributeValues(getMap)
                .build();


        return ddb.query(request);

    }

    public int queryDatabaseCount(String searchKey, String searchValue) throws Exception {

        QueryResponse response = query(searchKey, searchValue);
        return response.count();

    }

    public void deleteItem(DynamoDBAttributeValueHandler attributes) throws Exception {

        HashMap<String, AttributeValue> deleteAttributes = attributes.getAttributeValueMap();

        DeleteItemRequest deleteReq = DeleteItemRequest.builder()
                .tableName(tableName)
                .key(deleteAttributes)
                .build();

        ddb.deleteItem(deleteReq);

    }

    public void updateItem(DynamoDBAttributeValueHandler key, DynamoDBAttributeValueHandler attributes) throws Exception {

        HashMap<String, AttributeValueUpdate> updateAttributeValueMap = attributes.getUpdateAttributeValueMap();
        HashMap<String, AttributeValue> keyAttributeValueMap = key.getAttributeValueMap();

        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(tableName)
                .key(keyAttributeValueMap)
                .attributeUpdates(updateAttributeValueMap)
                .build();

        ddb.updateItem(request);

    }

    public ScanResponse scan() {

        ScanRequest request = ScanRequest.builder()
                .tableName(tableName)
                .build();

        return ddb.scan(request);
    }


}