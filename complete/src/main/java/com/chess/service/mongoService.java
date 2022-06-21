package com.chess.service;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
                    database.runCommand(command);
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

    void sqlToMongo(String sqlName, String mongoName){

        //create the table in mongo
        if(database.listCollectionNames().into(new ArrayList<String>()).contains(mongoName)) 
            database.getCollection(mongoName).drop();
        database.createCollection(mongoName);
        MongoCollection<Document> newCollection=database.getCollection(mongoName);

        try{
            Connection con=DriverManager.getConnection(  
            "jdbc:mysql://localhost:3306/db_example","meir","meir");  
            Statement stmt=con.createStatement();  
            ResultSet rs=stmt.executeQuery("SELECT * FROM " + sqlName); 
            ResultSetMetaData rsmd = rs.getMetaData();
            int column_count = rsmd.getColumnCount(), sum=0;
            ArrayList<String> columnsNames = new ArrayList<String>();
            for(int i=1;i<=column_count;i++){
                columnsNames.add(rsmd.getColumnName(i));
                sum += rsmd.getColumnType(i);
            }
            int block = 10000/sum + 1;
            List<Document> docs = new ArrayList<Document>();
            while(rs.next()){
                int n=0;
                while(n++<block){
                    Document d = new Document();
                    for(int i=0;i<column_count;i++){
                        Object o = rs.getObject(columnsNames.get(i));
                        d.append(columnsNames.get(i), o);
                    }
                    docs.add(d);
                    if(!rs.next())break;
                }
                newCollection.insertMany(docs);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        } 
    }
}