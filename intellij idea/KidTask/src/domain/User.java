package domain;

public class User {
    protected String userID;
    protected String userName;
    protected String userRole;

    public User(String userID, String userName, String userRole) {
        this.userID = userID;
        this.userName = userName;
        this.userRole = userRole;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
