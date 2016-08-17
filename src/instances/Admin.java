package instances;

/**
 * Created by Artem Siatchinov on 7/14/2016.
 */
public class Admin extends Person{

    private String USER_NAME;
    private String PASSWORD;




    @Override public String toString() {
        return "Администратор: " +
                "логин - " + getUSER_NAME() +
                ", Пароль - " + getPASSWORD() +
                ", " +
                super.toStringMethod();
    }

    //Getters
    public String getUSER_NAME() {
        return USER_NAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    //Setters
    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }


}
