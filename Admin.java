public class Admin {
    private String adminID;
    private String adminName;
    private String adminPassword;

    public Admin(String adminID, String adminName, String adminPassword) {
        this.adminID = adminID;
        this.adminName = adminName;
        this.adminPassword = adminPassword;
    }

    public String getAdminID() {
        return adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    @Override
    public String toString() {
        return "[Admin Profile]" +
               "\nID      : " + adminID + 
               "\nName    : " + adminName;
    }
}

