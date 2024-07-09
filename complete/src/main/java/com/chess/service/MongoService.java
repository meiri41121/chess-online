package com.chess.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

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
public class MongoService {

    private MongoDatabase database;
    private MongoCollection<Document> gameCollection;
    private MongoCollection<Document> moveCollection;

    MongoService(){
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
        boolean collectionExists = database.listCollectionNames()
        .into(new ArrayList<String>()).contains("exampleCollection");
        if(collectionExists) database.getCollection("exampleCollection").drop();

        if(database.listCollectionNames().into(new ArrayList<String>()).contains("Games")) database.getCollection("Games").drop();
        database.createCollection("Games");
        if(database.listCollectionNames().into(new ArrayList<String>()).contains("Moves")) database.getCollection("Moves").drop();
        database.createCollection("Moves");

        gameCollection = database.getCollection("Games");
        moveCollection = database.getCollection("Moves");

        //csvToMongoFull("/Users/meiri/Documents/csvToMongo.csv","name");
        // Vector<Boolean> need = new Vector<Boolean>();
        // need.add(true);need.add(true);need.add(false);need.add(true);
        // Vector<Vector<String>> change = new Vector<Vector<String>>();
        // Vector<String> v = new Vector<String>();v.add("mail");v.add("gmail");
        // change.add(v);
        // v = new Vector<String>();v.add("_id");v.add("inde");
        // change.add(v);
        // sqlToMongo("/Users/meiri/Documents/csvToMongo.csv","name",need,change);
    }
    
    MongoCollection<Document> getMoveCollection(){return moveCollection;}
    MongoCollection<Document> getGameCollection(){return gameCollection;}

    //needChanges[0] = 1 if we need to change names of culmuns => changes[0] contain array where arr[0] = 'from name' arr[1] = 'to name', arr[2] = 'from name' arr[3] = 'to name'..
    //needChanges[1] = 1 if we need to change column's type to int => changes[1] contain the names of those columns.
    //needChanges[2] = 1 if we want to add to columns to one => changes[2] contain array where arr[0] = the column that I sum there his value + the val of arr[1]..
    //needChanges[3] = 1 if I prefer to move the data from csv instead of sql, there sqlName will contain the csv file url, if I want to add Bulk I will put in changes[3][0].
    void sqlToMongo(String sqlName, String mongoName, Vector<Boolean> needChanges, Vector<Vector<String>> changes){

        if(needChanges.size()>3 && needChanges.get(3)) {
            if(changes.size()>3 && changes.get(3).size()>0) csvToMongoFull(sqlName,mongoName,needChanges,changes,Integer.parseInt(changes.get(3).get(0)));
            else csvToMongoFull(sqlName,mongoName,needChanges,changes);  
            return;
        }

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
                boolean flag = false;
                if(needChanges.size()>0 && needChanges.get(0) && changes.size()>0) flag = true;
                columnsNames.add(flag ? changeColumnName(rsmd.getColumnName(i),changes.get(0)):rsmd.getColumnName(i));
                sum += rsmd.getColumnType(i);
            }
            int block = 10000/sum + 1;
            List<Document> docs = new ArrayList<Document>();
            while(rs.next()){
                int n=0;
                while(n++<block){
                    Document d = new Document();
                    for(int i=0;i<column_count;i++){
                        Object o = (needChanges.size()>1 && needChanges.get(1) && changes.size()>1 && changes.get(1).contains(columnsNames.get(i))) ? Integer.parseInt((String) rs.getObject(columnsNames.get(i))): rs.getObject(columnsNames.get(i));
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

    String changeColumnName(String column, Vector<String> changes){
        for(int i=0;i<changes.size()-1;i++)
            if(changes.get(i).compareTo(column)==0 && i%2==0) return changes.get(i+1);
        return column;
    }
    
    void csvToMongoFull(String csvurl, String mongoName, Vector<Boolean> needChanges, Vector<Vector<String>> changes, int... blockk){

        //create the table in mongo
        if(database.listCollectionNames().into(new ArrayList<String>()).contains(mongoName)) 
        database.getCollection(mongoName).drop();
        database.createCollection(mongoName);
        MongoCollection<Document> newCollection=database.getCollection(mongoName);

        try{  
            File file=new File(csvurl);    //creates a new file instance  
            FileReader fr=new FileReader(file);   //reads the file  
            BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream  
            String line = br.readLine();
            int column_count = line.length() - line.replaceAll(",","").length() + 1;
            int block = blockk.length>0? blockk[0] : 1000;
            String []columnsName = line.split(",");
            ArrayList<String> columnsNames = (ArrayList<String>) Arrays.stream(columnsName).map(s -> (needChanges.size()>0 && needChanges.get(0) && changes.size()>0) ? changeColumnName(s,changes.get(0)): s).collect(Collectors.toList());
            List<Document> docs = new ArrayList<Document>();
            while((line=br.readLine())!=null){
                int n=0;
                while(n++ < block){
                    Document d = new Document();
                    for(int i=0;i<column_count;i++){
                        Object o = (needChanges.size()>1 && needChanges.get(1) && changes.size()>1 && changes.get(1).contains(columnsNames.get(i))) ? Integer.parseInt(line.split(",")[i]): line.split(",")[i];
                        d.append(columnsNames.get(i), o);
                        // Object o = line.split(",")[i];
                        // d.append(columnsNames.get(i), o);
                    }
                    docs.add(d);
                    if((line=br.readLine())==null)break;
                }
                newCollection.insertMany(docs);
            }
            fr.close();    //closes the stream and release the resources  
        }  
        catch(IOException e){  
            e.printStackTrace();  
        }  
    } 

}
