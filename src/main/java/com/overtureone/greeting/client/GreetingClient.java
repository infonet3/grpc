package com.overtureone.greeting.client;

import com.overtureone.proto.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.Arrays;

public class GreetingClient {

    public void run() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        //doUnaryCall(channel);
        //doServerStreamingCall(channel);
        //doClientStreamingCall(channel);
        doBiDiStreamingCall(channel);

        System.out.println("Shutdown");
        channel.shutdown();

    }

    private void doBiDiStreamingCall(ManagedChannel channel) {

        GreetServiceGrpc.GreetServiceStub asyncClient = GreetServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<GreetEveryoneRequest> requestObserver = asyncClient.greetEveryone(new StreamObserver<GreetEveryoneResponse>() {
            @Override
            public void onNext(GreetEveryoneResponse value) {
                System.out.println("Response from server: " + value.getResult());
            }

            @Override
            public void onError(Throwable throwable) {
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("Server is done sending data");
                latch.countDown();
            }

        });

        Arrays.asList("Stephane", "John", "Marc", "Matt").forEach(name -> {
            System.out.println("Sending: " + name);
            requestObserver.onNext(GreetEveryoneRequest.newBuilder().setGreeting(Greeting.newBuilder().setFirstName(name)).build());});

        requestObserver.onCompleted();


        //Use the latch to stay in the function
        try {
            latch.await(3, TimeUnit.SECONDS);
        } catch(Exception exc) {
            exc.printStackTrace();
        }
    }

    private void doClientStreamingCall(ManagedChannel channel) {

        GreetServiceGrpc.GreetServiceStub asyncClient = GreetServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<LongGreetRequest> requestObserver = asyncClient.longGreet(new StreamObserver<LongGreetResponse>() {
            @Override
            public void onNext(LongGreetResponse value) {
                //Response from the server
                System.out.println("Received a response from the server");
                System.out.println(value.getResult());
            }

            @Override
            public void onError(Throwable throwable) {
                //Error from the server
            }

            @Override
            public void onCompleted() {
                //Server is done sending data
                System.out.println("Server has completed sending us somethings");
                latch.countDown();
            }
        });

        requestObserver.onNext(LongGreetRequest.newBuilder()
                .setGreeting(Greeting.newBuilder()
                    .setFirstName("Geronimo")
                    .setLastName("IsHome")
                    .build())
                .build());

        requestObserver.onNext(LongGreetRequest.newBuilder()
                .setGreeting(Greeting.newBuilder()
                        .setFirstName("Carter")
                        .setLastName("Jones")
                        .build())
                .build());

        //Tell the server the client is done sending data
        requestObserver.onCompleted();

        //Use the latch to stay in the function
        try {
            latch.await(3, TimeUnit.SECONDS);
        } catch(Exception exc) {
            exc.printStackTrace();
        }
    }

    private void doUnaryCall(ManagedChannel channel) {

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

    }

    private void doServerStreamingCall(ManagedChannel channel) {

        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

        GreetManyTimesRequest greetManyTimesRequest = GreetManyTimesRequest.newBuilder()
                .setGreeting(Greeting.newBuilder().setFirstName("Matt").setLastName("Jones"))
                .build();

        greetClient.greetManyTimes(greetManyTimesRequest)
                .forEachRemaining(greetManyTimesResponse -> {
                    System.out.println(greetManyTimesResponse.getResult());
                });

    }

    public static void main(String[] mainArgs) {
        System.out.println("Hello I'm a gRPC Client");

        GreetingClient main = new GreetingClient();
        main.run();

    }
}
