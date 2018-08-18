package com.overtureone.greeting.server;

import com.overtureone.proto.GreetRequest;
import com.overtureone.proto.GreetResponse;
import com.overtureone.proto.GreetServiceGrpc;
import com.overtureone.proto.Greeting;
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

}
