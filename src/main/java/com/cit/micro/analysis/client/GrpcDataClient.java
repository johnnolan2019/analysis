package com.cit.micro.analysis.client;

import com.cit.micro.data.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class GrpcDataClient {
    private final GrpcLoggerClient logger = new GrpcLoggerClient();
    private String host = "localhost";
    private int port = 6568;

    public List<com.cit.micro.analysis.LogData> getAll() {
        List<com.cit.micro.analysis.LogData> logDataList = new ArrayList<>();
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        AccessDBGrpc.AccessDBBlockingStub stub = AccessDBGrpc.newBlockingStub(channel);
        Iterator<LogData> logResponse;
        try {
            logResponse = stub.getAll(Id.newBuilder().setId(10).build());
            while (logResponse.hasNext()) {
                LogData receiveData = logResponse.next();

                com.cit.micro.analysis.LogData converted = com.cit.micro.analysis.LogData.newBuilder().setId(receiveData
                        .getId()).setText(receiveData.getText()).setUid(receiveData.getUid()).build();
                if (!logDataList.contains(converted)) {
                    logDataList.add(converted);
                }
            }
        } catch (StatusRuntimeException ex) {
            logger.error("GRPC failed to talk to DB");
            return logDataList;
        }

        channel.shutdown();
        return logDataList;
    }

    public List<com.cit.micro.analysis.LogData> getSystemLogs(com.cit.micro.analysis.Uid uid) {
        List<com.cit.micro.analysis.LogData> uidLogDataList = new ArrayList<>();
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        AccessDBGrpc.AccessDBBlockingStub stub = AccessDBGrpc.newBlockingStub(channel);
        Iterator<LogData> logResponse;
        try {
            logResponse = stub.getSystemLogs(Uid.newBuilder().setUid(uid.getUid()).build());
            while (logResponse.hasNext()) {
                LogData receiveData = logResponse.next();
                // convert object and only add to list if unique
                com.cit.micro.analysis.LogData converted = com.cit.micro.analysis.LogData.newBuilder().setId(receiveData
                        .getId()).setText(receiveData.getText()).setUid(receiveData.getUid()).build();
                if (!uidLogDataList.contains(converted)) {
                    uidLogDataList.add(converted);
                }
            }
        } catch (StatusRuntimeException ex) {
            logger.error("GRPC failed to talk to DB");
            return uidLogDataList;
        }

        channel.shutdown();
        return uidLogDataList;
    }

    public Result delete(Id id){
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        AccessDBGrpc.AccessDBBlockingStub stub = AccessDBGrpc.newBlockingStub(channel);

        Result dbResult = stub.delete(id);
        //logger.info(dbResult.toString());

        channel.shutdown();
        return dbResult;
    }
}
