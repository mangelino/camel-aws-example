package com.angmas;

import org.apache.camel.main.Main;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.apache.camel.component.amqp.AMQPConnectionDetails;

/**
 * A Camel Application
 */
public class MainApp {
    public static String BROKER_URL = System.getenv("BROKER_URL");
    public static String AMQP_URL = "amqps://"+BROKER_URL+":5671";
    public static String BROKER_USERNAME = System.getenv("BROKER_USERNAME");
    public static String BROKER_PASSWORD = System.getenv("BROKER_PASSWORD");
    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.bind("amqp", getAMQPconnection());
        main.bind("s3Client", AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build());
        main.addRouteBuilder(new MyRouteBuilder());
        main.addRouteBuilder(new MessageProducerBuilder());
        main.run(args);
    }

    public static AMQPConnectionDetails getAMQPconnection() {
        return new AMQPConnectionDetails(AMQP_URL, BROKER_USERNAME, BROKER_PASSWORD);
    }

}

