package com.chess.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
/**
 * mongoService
 */
@Service
public class mongoService {

    private MongoDatabase database;
    private MongoCollection<Document> gameCollection;
    private MongoCollection<Document> moveCollection;

    mongoService(){
        ConnectionString connectionString = new ConnectionString("mongodb+srv://meiri:12342332@cluster0.u4esz.mongodb.net/?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder()
        .applyConnectionString(connectionString)
        .build();
        MongoClient mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase("chess");

        try {
                    Bson command = new BsonDocument("ping", new BsonInt64(1));
                    Document commandResult = database.runCommand(command);
                    System.out.println("Connected successfully to server.");
                } catch (MongoException me) {
                    System.err.println("An error occurred while attempting to run a command: " + me);
        }
        //database.createCollection("exampleCollection");
        boolean collectionExists = database.listCollectionNames()
        .into(new ArrayList<String>()).contains("exampleCollection");
        if(collectionExists) database.getCollection("exampleCollection").drop();

        if(database.listCollectionNames().into(new ArrayList<String>()).contains("Games")) database.getCollection("Games").drop();
        database.createCollection("Games");
        if(database.listCollectionNames().into(new ArrayList<String>()).contains("Moves")) database.getCollection("Moves").drop();
        database.createCollection("Moves");

        gameCollection = database.getCollection("Games");
        moveCollection = database.getCollection("Moves");
    }
    
    MongoCollection<Document> getMoveCollection(){return moveCollection;}
    MongoCollection<Document> getGameCollection(){return gameCollection;}
}