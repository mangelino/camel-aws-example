package com.angmas;

import org.apache.camel.builder.RouteBuilder;

/**
 * A Camel Java8 DSL Router
 */
public class MessageProducerBuilder extends RouteBuilder {
    /**
     * Let's configure the Camel routing rules using Java code...
     */
    public void configure() {
        from("file://input?delete=false&noop=true")
        .log("Content ${body} ${headers.CamelFileName}")
        .to("amqp:filequeue");
    }
}
