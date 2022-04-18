package comp261.assig1;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Graph {

    //Todo add your data structures here
    ArrayList<Stop> stops = new ArrayList<>();
    ArrayList<Trip> trips= new ArrayList<>();
    ArrayList<Edge> edges = new ArrayList<>();


    
// constructor post parsing
    public Graph() {

    }

    // constructor with parsing
    public Graph(File stopFile, File tripFile) {
        //Todo: instantiate your data structures here

        //Then you could parse them using the Parser
        this.stops = Parser.parseStops(stopFile);
        this.trips = Parser.parseTrips(tripFile);
        buildStopList();
        int count=0;

     /**    for(Stop s:stops){
            if(s.getIncoming()==null){
                System.out.println("incoming null");
                System.out.println(s.getId());
                count++;
            }
        } */
     
       // edgeMaker();
       
    }
    public ArrayList<Stop> getStops(){
        return this.stops;
    }
    public ArrayList<Trip> getTrips(){
        return this.trips;
    }
    public ArrayList<Edge> getEdges(){
        return this.edges;
    }


    // buildStoplist from other data structures
    private void buildStopList() {
        Stop incoming=null;
        Stop outgoing=null;
       
       // Todo: you could use this sort of method to create additional data structures
        for(int p=1;p<trips.size();p++){
            if(trips.get(p).getStop_pattern_id().equals(trips.get(p-1).getStop_pattern_id())){
                for(Stop i:stops){
                   
                    if(i.getId().equals(trips.get(p-1).getTripId())){
                       incoming=i; 
                       i.addOutgoing(outgoing);
                    }}

                       for(Stop s:stops){
                        if(s.getId().equals(trips.get(p).getTripId())){
                            outgoing=s;        
                            s.addIncoming(incoming);         
                    }
                    }
                    edges.add(new Edge(incoming, outgoing, trips.get(p-1).getStop_pattern_id()));                      
                        }
                    }

                }
    // buildTripData into stops
   /**   private void edgeMaker(){
         //Todo: this could be used for trips
        for(int i=0;i<stops.size();i++){
            if(stops.get(i).getIncoming()!=null){this.edges.add(new Edge(stops.get(i).getIncoming(),stops.get(i).getOutgoing(),stops.get(i).getId()));}
        }
    } */
    // Todo: getters and setters 
    }


