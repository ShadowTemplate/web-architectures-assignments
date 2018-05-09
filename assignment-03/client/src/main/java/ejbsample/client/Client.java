package ejbsample.client;

import ejbsample.common.TimeI;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Client {

    private static final String DEFAULT_TIME_EJB = "server-ear/server-ejb/TimeBean!ejbsample.common.TimeI";

    public static void main(String[] args) {
        String endPoint = System.getProperty("ejb", DEFAULT_TIME_EJB);
        System.out.println("\nTrying to connect to EJB located at " + endPoint + "\n");
        try {
            Context initialContext = new InitialContext();
            TimeI timeRemote = (TimeI) initialContext.lookup(endPoint);
            System.out.println("\nServer time: " + timeRemote.getDate());
            System.out.println("\nServer time: " + timeRemote.getDate());
        } catch (NamingException e) {
            System.err.println("Unable to connect to remote EJB...\n" + e.getMessage());
            e.printStackTrace();
        }
    }
}

