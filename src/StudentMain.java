import operations.*;
import student.pd150258_BuyerOperations;
import student.pd150258_CityOperations;
import student.pd150258_GeneralOperations;
import student.pd150258_ShopOperations;

import org.junit.Test;
import tests.TestHandler;
import tests.TestRunner;

import java.util.Calendar;

public class StudentMain {

    public static void main(String[] args) {

        ArticleOperations articleOperations = null; // Change this for your implementation (points will be negative if interfaces are not implemented).
        BuyerOperations buyerOperations = new pd150258_BuyerOperations();
        CityOperations cityOperations = new pd150258_CityOperations();
        GeneralOperations generalOperations = new pd150258_GeneralOperations();
        OrderOperations orderOperations = null;
        ShopOperations shopOperations = new pd150258_ShopOperations();
        TransactionOperations transactionOperations = null;
//
//        Calendar c = Calendar.getInstance();
//        c.clear();
//        c.set(2010, Calendar.JANUARY, 01);
//
//
//        Calendar c2 = Calendar.getInstance();
//        c2.clear();
//        c2.set(2010, Calendar.JANUARY, 01);
//
//        if(c.equals(c2)) System.out.println("jednako");
//        else System.out.println("nije jednako");

        TestHandler.createInstance(
                articleOperations,
                buyerOperations,
                cityOperations,
                generalOperations,
                orderOperations,
                shopOperations,
                transactionOperations
        );

        TestRunner.runTests();
    }
}
