package com.cit.micro.analysis;

import com.cit.micro.analysis.client.GrpcLoggerClient;
import com.cit.micro.analysis.service.GrpcAnalysis;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class AnalysisApplication {

    public static void main(String[] args) {
        final GrpcLoggerClient logger = new GrpcLoggerClient();
        SpringApplication.run(AnalysisApplication.class, args);
        logger.info("Analysis Service now running");
        Server server = ServerBuilder
                .forPort(6567)
                .addService(new GrpcAnalysis()).build();
        try{
            server.start();
            server.awaitTermination();
        }catch (IOException e){
            logger.info("bad");
        }catch (InterruptedException e){
            logger.error("Not as bad, but not good");
        }
    }

}
