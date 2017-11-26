package com.angmas;

import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;

/**
 * A Camel Java8 DSL Router
 */
public class MyRouteBuilder extends RouteBuilder {
    private String secretKey = System.getenv("AWS_SECRET_ACCESS_KEY");
    private String accessKey = System.getenv("AWS_ACCESS_KEY");

    public void configure() {
        from("amqp:filequeue")
        .process()
            .message(this::setExtensionHeader)
        .choice()
            .when(header("ext").isEqualTo("txt"))
                .setHeader("CamelAwsS3Key").simple("text-files/${headers.CamelFileName}")
                .to("aws-s3://my-camel-example-bucket?region=US_EAST_1&accessKey="+accessKey+"&secretKey=RAW("+secretKey+")")
            .when(header("ext").isEqualTo("html"))
                .setHeader("CamelAwsS3Key").simple("html-files/${headers.CamelFileName}")
                .to("aws-s3://my-camel-example-bucket?region=US_EAST_1&accessKey="+accessKey+"&secretKey=RAW("+secretKey+")")
            .otherwise()
                .setHeader("CamelAwsS3Key").simple("other-files/${headers.CamelFileName}")
                .to("aws-s3://my-camel-example-bucket?region=US_EAST_1&accessKey="+accessKey+"&secretKey=RAW("+secretKey+")");

    }

    private void setExtensionHeader(Message m) {
        String fileName = (String) m.getHeader("CamelFileName");
        String ext = fileName.substring(fileName.lastIndexOf(".")+1);
        m.setHeader("ext", ext);
    }

}
