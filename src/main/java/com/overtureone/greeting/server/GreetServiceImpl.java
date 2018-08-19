package com.overtureone.greeting.server;

import com.overtureone.proto.*;
import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetServiceGrpc.GreetServiceImplBase {

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
}
