public class User {
    private String userID;
    private String userName;
    private String userPhoneNumber;
    private String userAddressNode;
    private String userPassword;

    public User(String userID, String userName, String userPhoneNumber, String userAddressNode, String userPassword) {
        this.userID = userID;
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.userAddressNode = userAddressNode;
        this.userPassword = userPassword;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserAddressNode() {
        return userAddressNode;
    }

    public void setUserAddressNode(String userAddressNode) {
        this.userAddressNode = userAddressNode;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public String toString() {
        return "\n[User Profile]" +
                "\nID      : " + userID +
                "\nName    : " + userName +
                "\nPhone   : " + userPhoneNumber +
                "\nLocation: " + userAddressNode;
    }
}
