package skeleton;

public class Setting extends Entity {
    String typeJDBC;
    String serverName;
    String baseName;
    String userName;
    String password;

    public Setting() {

    }

    public void setTypeJDBC(String typeJDBC) {
        this.typeJDBC = typeJDBC;
    }

    public String getTypeJDBC() {
        return typeJDBC;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerName() {
        return serverName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
