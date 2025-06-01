package org.example;


import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.example.GrpcClient;
/**
 * Server that manages startup/shutdown of a {@code Greeter} server.
 */
public class GrpcServer {
    private static final Logger logger = Logger.getLogger(GrpcServer.class.getName());

    private Server gRPCServer;
    public void start() throws IOException {
        /* The port on which the server should run */
        int port = 50051;
        /*
         * By default gRPC uses a global, shared Executor.newCachedThreadPool() for gRPC callbacks into
         * your application. This is convenient, but can cause an excessive number of threads to be
         * created if there are many RPCs. It is often better to limit the number of threads your
         * application uses for processing and let RPCs queue when the CPU is saturated.
         * The appropriate number of threads varies heavily between applications.
         * Async application code generally does not need more threads than CPU cores.
         */
        ExecutorService executor = Executors.newFixedThreadPool(2);
        gRPCServer = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
                .executor(executor)
                .addService(new GreeterImpl())
                .build()
                .start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                try {
                    GrpcServer.this.stop();
                } catch (InterruptedException e) {
                    if (gRPCServer != null) {
                        gRPCServer.shutdownNow();
                    }
                    e.printStackTrace(System.err);
                } finally {
                    executor.shutdown();
                }
                System.err.println("*** server shut down");
            }
        });
    }

    public void stop() throws InterruptedException {
        if (gRPCServer != null) {
            gRPCServer.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    public void blockUntilShutdown() throws InterruptedException {
        if (gRPCServer != null) {
            gRPCServer.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.println("Hello and welcome!");

        final GrpcServer server = new GrpcServer();
        server.start();
        server.blockUntilShutdown();
    }

}