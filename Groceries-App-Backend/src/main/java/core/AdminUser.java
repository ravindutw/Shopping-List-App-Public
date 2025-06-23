package core;

public class AdminUser extends User{

    public AdminUser(String userName) {
        super(userName);
        type = "admin";
    }

    @Override
    public String getType() {
        return type;
    }

    public void newUser(String userName, String name, String password) {



    }

}
