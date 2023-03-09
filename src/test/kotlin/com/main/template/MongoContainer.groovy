/*
 * Copyright (c) 2019 ZTE, Corp. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.main.template

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MongoDBContainer
import spock.lang.Specification;

import static org.testcontainers.containers.Network.newNetwork;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,properties = ["spring.main.allow-bean-definition-overriding=true"])
 class MongoContainer extends Specification {
    public static MongoContainer INSTANCE = new MongoContainer();
    private static final int MONGO_PORT = 27018;
    private static final String MONGO_IMAGE = "mongo:4.0.8";


    static String endpointURI;

    static MongoDBContainer m1 = new MongoDBContainer(MONGO_IMAGE)
            .withNetwork(Network.SHARED)
            .withNetworkAliases("M1")
           // .withExposedPorts(MONGO_PORT)
            .withCommand("--replSet rs0 --bind_ip localhost,M1");

    static {
        m1.start();
        // Thread.sleep(20000)
        def result= m1.execInContainer("/bin/bash", "-c", "mongo --eval 'printjson(rs.initiate())' --quiet");
        println(result.stdout)
        endpointURI = "mongodb://" + m1.getContainerIpAddress() + ":" + m1.getFirstMappedPort();
        println(endpointURI);
        def replica=m1.getReplicaSetUrl()
        println("Replica is"+replica);
    }
@DynamicPropertySource
static void fileGenProps(DynamicPropertyRegistry registry){

    registry.add("spring.data.mongodb.uri",{x -> m1.getReplicaSetUrl()})
}

  /*  public MongoClient getMongoClient() {
        return mongoClient;
    }

    public com.mongodb.reactivestreams.client.MongoClient getRSMongoClient() {
        return rsMongoClient;
    }*/
}