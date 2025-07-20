package com.ravinduw.apps.shoppinglistapp.service;

import com.ravinduw.apps.shoppinglistapp.entity.User;
import dynamodbpkg.DynamoDBAttributeValueHandler;
import dynamodbpkg.DynamoDBHandler;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import utils.Utils;

@Service
@NoArgsConstructor
public class ItemService {

    private final String dbName = "groceries-app-db";
    private final DynamoDBHandler db = new DynamoDBHandler(dbName);

    public void newItem(User user, String name, String location) throws Exception {

        String id = Utils.generateRandomID();
        String date = Utils.getDateAndTime();

        DynamoDBAttributeValueHandler attributes = new DynamoDBAttributeValueHandler();

        attributes.addStringAttribute("id", id);
        attributes.addStringAttribute("name", name);
        attributes.addStringAttribute("created_date", date);
        attributes.addStringAttribute("created_user", user.getUserName());
        attributes.addStringAttribute("location", "NULL");
        attributes.addStringAttribute("checked", "false");

        db.putItem(attributes);

    }

    public void checkItem(String id, User user) throws Exception {

        String date = Utils.getDateAndTime();

        DynamoDBAttributeValueHandler keyAttributes = new DynamoDBAttributeValueHandler();
        DynamoDBAttributeValueHandler attributes = new DynamoDBAttributeValueHandler();

        keyAttributes.addStringAttribute("id", id);
        attributes.addStringUpdateAttribute("checked", "true");
        attributes.addStringUpdateAttribute("checked_user", user.getUserName());
        attributes.addStringUpdateAttribute("checked_date", date);

        db.updateItem(keyAttributes, attributes);

    }

}
