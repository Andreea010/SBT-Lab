public class DatabaseObject {
    private String memberID = "";
    private String name = "";
    private String email = "";
    private String password = "";
    public DatabaseObject(){

    }

    public String getMemberID(){
        return memberID;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

