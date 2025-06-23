package core;
 abstract class User {

    protected String userName;
    protected String name;
    protected String type;

    User(String userName) {
        userName = userName;
    }

    public String getUserName() {
        return userName;
    };

    public String getName() {
        return name;
    };

    public abstract String getType();

}
