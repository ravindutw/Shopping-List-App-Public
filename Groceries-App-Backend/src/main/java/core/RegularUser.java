package core;

public class RegularUser extends User{

    public RegularUser(String userName) {
        super(userName);
        type = "user";
    }

    @Override
    public String getType() {
        return type;
    }
}
