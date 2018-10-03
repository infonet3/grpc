package com.overtureone.greeting.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;

public class GreetingServer {

    public static void main(String... args) throws IOException, InterruptedException {
        System.out.println("Hello gRPC");

        Server server = ServerBuilder.forPort(50051)
                .addService(new GreetServiceImpl()) //Reference class we just built
                //.addService(new CalculatorServiceImpl()) //Reference class we just built
                .build();

        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread( () -> {
            System.out.println("Received shut down request");
            server.shutdown();
            System.out.println("Stopped the server");
        }));

        server.awaitTermination();
    }
}
