package com.cit.micro.analysis.service;

import com.cit.micro.analysis.AnalyseGrpc;
import com.cit.micro.analysis.client.GrpcDataClient;
import com.cit.micro.analysis.client.GrpcLoggerClient;
import com.cit.micro.analysis.Id;
import com.cit.micro.analysis.LogData;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class GrpcAnalysis extends AnalyseGrpc.AnalyseImplBase {
    private final GrpcLoggerClient logger ;
    private final GrpcDataClient dataClient;

    public GrpcAnalysis(){
        this.dataClient = new GrpcDataClient();
        this.logger = new GrpcLoggerClient();
    }

    @Override
    public void getAll(Id id, StreamObserver<LogData> responseObserver){
        logger.info("getting everything ");
        List<LogData> logDataList = dataClient.getAll();
        for (LogData logData: logDataList
             ) {
            responseObserver.onNext(logData);
        }
        responseObserver.onCompleted();
    }
}
