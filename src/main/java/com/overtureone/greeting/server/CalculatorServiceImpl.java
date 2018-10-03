package com.overtureone.greeting.server;

import com.overtureone.proto.CalculatorServiceGrpc;
import com.overtureone.proto.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {

    @Override
    public void squareRoot(SquareRootRequest request, StreamObserver<SquareRootResponse> responseObserver) {

        int number = request.getNumber();

        if (number >= 0) {
            double numberRoot = Math.sqrt(number);
            responseObserver.onNext(
                    SquareRootResponse.newBuilder()
                            .setNumberRoot(numberRoot)
                            .build()
            );
            responseObserver.onCompleted();


        } else {
            //Create exception
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Number is not positive")
                            .augmentDescription("Number sent: " + number)
                            .asRuntimeException()
            );
        }

    }

}
