package service;

import com.ravinduw.apps.groceriesappbackend.entity.User;
import dynamodbpkg.DynamoDBAttributeValueHandler;
import dynamodbpkg.DynamoDBHandler;
import utils.Utils;

public class ItemService {

    private String id;
    private String name;
    private String date;
    private User user;
    private String location;
    private final String dbName = "groceries-app-db";
    private final DynamoDBHandler db = new DynamoDBHandler(dbName);

    public ItemService(User user) {
        this.user = user;
        date = Utils.getDateAndTime();
    }

    public ItemService(String id, User user) {
        this.id = id;
        this.user = user;
        date = Utils.getDateAndTime();
    }

    public void newItem(String name, String location) throws Exception {

        this.name = name;
        this.location = location;
        id = Utils.generateRandomID();

        DynamoDBAttributeValueHandler attributes = new DynamoDBAttributeValueHandler();

        attributes.addStringAttribute("id", id);
        attributes.addStringAttribute("name", this.name);
        attributes.addStringAttribute("created_date", date);
        attributes.addStringAttribute("created_user", user.getUsername());
        attributes.addStringAttribute("location", this.location);
        attributes.addStringAttribute("checked", "false");

        db.putItem(attributes);

    }

    public void checkItem() throws Exception {

        DynamoDBAttributeValueHandler keyAttributes = new DynamoDBAttributeValueHandler();
        DynamoDBAttributeValueHandler attributes = new DynamoDBAttributeValueHandler();

        keyAttributes.addStringAttribute("id", id);
        attributes.addStringUpdateAttribute("checked", "true");
        attributes.addStringUpdateAttribute("checked_user", user.getUsername());
        attributes.addStringUpdateAttribute("checked_date", date);

        db.updateItem(keyAttributes, attributes);

    }

}
