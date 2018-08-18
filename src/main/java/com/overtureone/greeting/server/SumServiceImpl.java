package com.overtureone.greeting.server;

import com.overtureone.proto.SumRequest;
import com.overtureone.proto.SumResponse;
import com.overtureone.proto.SumServiceGrpc;
import com.overtureone.proto.Arguments;
import io.grpc.stub.StreamObserver;

public class SumServiceImpl extends SumServiceGrpc.SumServiceImplBase {

    @Override
    public void sum(SumRequest request, StreamObserver<SumResponse> responseObserver) {

        Arguments args = request.getArgs();
        int num1 = args.getNum1();
        int num2 = args.getNum2();

        int sum = num1 + num2;

        SumResponse response = SumResponse.newBuilder()
                .setResult("" + sum)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }
}
