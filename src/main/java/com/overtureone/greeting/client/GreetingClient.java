package com.overtureone.greeting.client;

import com.overtureone.proto.*;

import com.proto.dummy.DummyServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingClient {
    public static void main(String[] mainArgs) {
        System.out.println("Hello I'm a gRPC Client");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        System.out.println("Create Stub");
        //DummyServiceGrpc.DummyServiceBlockingStub syncClient = DummyServiceGrpc.newBlockingStub(channel);
        //DummyServiceGrpc.DummyServiceFutureStub asyncClient = DummyServiceGrpc.newFutureStub(channel);

        //FOR GREET SERVICE
        /*
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

        Greeting greeting = Greeting.newBuilder()
                .setFirstName("Matt")
                .setLastName("Jones")
                .build();

        GreetRequest greetRequest = GreetRequest.newBuilder()
                .setGreeting(greeting)
                .build();

        GreetResponse greetResponse = greetClient.greet(greetRequest);
        System.out.println(greetResponse.getResult());
        */

        /*
        SumServiceGrpc.SumServiceBlockingStub sumClient = SumServiceGrpc.newBlockingStub(channel);

        Arguments args = Arguments.newBuilder()
                .setNum1(10)
                .setNum2(110)
                .build();

        SumRequest sumRequest = SumRequest.newBuilder()
                .setArgs(args)
                .build();

        SumResponse sumResponse = sumClient.sum(sumRequest);

        //GreetResponse greetResponse = greetClient.greet(greetRequest);
        System.out.println(sumResponse.getResult());
        */

        //Server Streaming---------------------------------------------------------------------------------------------
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

        GreetManyTimesRequest greetManyTimesRequest = GreetManyTimesRequest.newBuilder()
                .setGreeting(Greeting.newBuilder().setFirstName("Matt").setLastName("Jones"))
                .build();

        greetClient.greetManyTimes(greetManyTimesRequest)
                .forEachRemaining(greetManyTimesResponse -> {
                    System.out.println(greetManyTimesResponse.getResult());
                });



        System.out.println("Shutdown");
        channel.shutdown();

    }
}
