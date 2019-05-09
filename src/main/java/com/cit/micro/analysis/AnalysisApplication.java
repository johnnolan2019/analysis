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
    private static int port = 6567;
    private final static GrpcLoggerClient logger = new GrpcLoggerClient();

    public static void main(String[] args) {
        SpringApplication.run(AnalysisApplication.class, args);
        logger.info("Analysis Service now running");
        Server server = ServerBuilder
                .forPort(port)
                .addService(new GrpcAnalysis()).build();
        try{
            server.start();
            server.awaitTermination();
        }catch (IOException e){
            logger.error("Analysis Service threw IO exception");
        }catch (InterruptedException e){
            logger.error("Analysis Service threw Interrupted exception");
        }
    }

}
