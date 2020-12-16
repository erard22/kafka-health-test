package ch.erard22.quarkus;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class DemoConsumer {


    @Incoming("demo")
    public void consume(String message) {
        throw new RuntimeException("failed to process: " + message);
    }

}
