package nl.daxte1.dax1;

public class User {
    public String name, email;
    public Integer rechten = 0;



    public User(){

    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getRechten() {
        return rechten;
    }

    public void setRechten(Integer rechten) {
        this.rechten = rechten;
    }
}
