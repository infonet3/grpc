package com.overtureone.greeting.client;

import com.overtureone.proto.SumRequest;
import com.overtureone.proto.SumResponse;
import com.overtureone.proto.SumServiceGrpc;
import com.overtureone.proto.Arguments;

import com.overtureone.proto.GreetRequest;
import com.overtureone.proto.GreetResponse;
import com.overtureone.proto.GreetServiceGrpc;
import com.overtureone.proto.Greeting;
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



        System.out.println("Shutdown");
        channel.shutdown();

    }
}
