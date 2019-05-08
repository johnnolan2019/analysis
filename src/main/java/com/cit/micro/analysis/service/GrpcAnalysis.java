package com.cit.micro.analysis.service;

import com.cit.micro.analysis.*;
import com.cit.micro.analysis.client.GrpcDataClient;
import com.cit.micro.analysis.client.GrpcLoggerClient;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrpcAnalysis extends AnalyzeGrpc.AnalyzeImplBase {
    private final GrpcLoggerClient logger ;
    private final GrpcDataClient dataClient;

    public GrpcAnalysis(){
        this.dataClient = new GrpcDataClient();
        this.logger = new GrpcLoggerClient();
    }

    @Override
    public void getAll(Id id, StreamObserver<LogData> responseObserver){
        logger.info("getting all table entries from DB");
        List<LogData> logDataList = dataClient.getAll();
        for (LogData logData: logDataList
             ) {
            responseObserver.onNext(logData);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void getSystemLogs(Uid uid, StreamObserver<LogData> responseObserver){
        logger.info(String.format("getting all table entries from DB for %s", uid.getUid()));
        List<LogData> logDataList = dataClient.getSystemLogs(uid);
        for (LogData logData: logDataList
        ) {
            responseObserver.onNext(logData);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void delete(Id id, StreamObserver<Result> responseObserver){
        logger.info(String.format("Attempting to delete table entry for %s", id.getId()));
        com.cit.micro.data.Id dataId = com.cit.micro.data.Id.newBuilder().setId(id.getId()).build();
        com.cit.micro.data.Result dataResult = dataClient.delete(dataId);
        Result analysisResult =  Result.newBuilder().setSuccess(dataResult.getSuccess()).build();
        responseObserver.onNext(analysisResult);
        responseObserver.onCompleted();
    }
}
