package tests;

/**
 * Created by Artem Siatchinov on 7/23/2016.
 */
public class MainTest {

    public static void main(String args[]){

        System.out.println("Testing App");

        dataBaseTest();
    }

    private static void dataBaseTest() {

        DataBaseTest dbTest = new DataBaseTest();

        dbTest.loadRealDB();
    }

}
