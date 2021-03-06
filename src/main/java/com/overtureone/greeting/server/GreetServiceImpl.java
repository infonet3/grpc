package com.overtureone.greeting.server;

import com.overtureone.proto.*;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetServiceGrpc.GreetServiceImplBase {

    @Override
    public void greetWithDeadline(GreetWithDeadlineRequest request, StreamObserver<GreetWithDeadlineResponse> responseObserver) {

        Context current = Context.current();

        try {

            for (int i = 0; i < 3; i++) {
                if (!current.isCancelled()) {
                    System.out.println("Sleep");
                    Thread.sleep(100);
                } else {
                    return;
                }
            }

            System.out.println("Send Response");
            responseObserver.onNext(
                    GreetWithDeadlineResponse.newBuilder()
                            .setResult("hello " + request.getGreeting().getFirstName())
                            .build());

            responseObserver.onCompleted();

        } catch (Exception exc) {
            exc.printStackTrace();
        }


    }

    @Override
    public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {

        //Get fields which we need
        Greeting greeting = request.getGreeting();
        String firstName = greeting.getFirstName();
        String lastName = greeting.getLastName();

        String result = "Hello " + firstName + ", " + lastName;
        GreetResponse response = GreetResponse.newBuilder()
                .setResult(result)
                .build();

        //Send the response
        responseObserver.onNext(response);

        //Complete call
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<GreetEveryoneRequest> greetEveryone(StreamObserver<GreetEveryoneResponse> responseObserver) {

        StreamObserver<GreetEveryoneRequest> requestObserver =
                new StreamObserver<GreetEveryoneRequest>() {
                    @Override
                    public void onNext(GreetEveryoneRequest value) {
                        String result = "Hello " + value.getGreeting().getFirstName();
                        GreetEveryoneResponse greetEveryoneResponse =
                                GreetEveryoneResponse.newBuilder()
                                        .setResult(result)
                                        .build();

                        responseObserver.onNext(greetEveryoneResponse);
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onCompleted() {
                        responseObserver.onCompleted();
                    }
                };

        return super.greetEveryone(responseObserver);

    }

    @Override
    public void greetManyTimes(GreetManyTimesRequest request, StreamObserver<GreetManyTimesResponse> responseObserver) {

        String firstName = request.getGreeting().getFirstName();
        String lastName = request.getGreeting().getLastName();

        try {

            for (int i = 0; i < 10; i++) {
                String result = "Hello " + firstName + ", " + lastName + ", response number: " + i;
                GreetManyTimesResponse response = GreetManyTimesResponse.newBuilder()
                        .setResult(result)
                        .build();

                responseObserver.onNext(response);
                Thread.sleep(1000);
            }

        } catch (Exception exc) {

            exc.printStackTrace();

        } finally {

            responseObserver.onCompleted();

        }

    }

    @Override
    public StreamObserver<LongGreetRequest> longGreet(StreamObserver<LongGreetResponse> responseObserver) {

        StreamObserver<LongGreetRequest> requestObserver = new StreamObserver<LongGreetRequest>() {

            String result = "";

            @Override
            public void onNext(LongGreetRequest value) {
                //Client sends a message
                result += "Hello " + value.getGreeting().getFirstName() + "| ";
            }

            @Override
            public void onError(Throwable throwable) {
                //client sends an error
            }

            @Override
            public void onCompleted() {
                //Client is done

                responseObserver.onNext(
                        LongGreetResponse.newBuilder()
                                .setResult(result)
                                .build());

                responseObserver.onCompleted();
            }
        };

        return requestObserver;

    }
}
