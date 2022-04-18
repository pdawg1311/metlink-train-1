package comp261.assig1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


// decide the data structure for stops
public class Stop {
    //probably always have these three    
    private GisPoint loc;
    private String name;
    private String id;
   private Stop incoming;
   private Stop outgoing;
  // private HashMap<String,Stop> routes = new HashMap<>();

    //Todo: add additional data structures


        
    // Constructor
        public Stop(String name, String id,GisPoint loc) {
            this.name=name;
            this.id=id;
            this.loc=loc;
        }
       public void addIncoming(Stop stop){this.incoming=stop;}
        public Stop getIncoming(){return this.incoming;}
    public void addOutgoing(Stop stop){this.outgoing =stop;}
    public Stop getOutgoing(){return this.outgoing;}

    public String getId() {
        return id;
    }
    public String getName(){
            return name;
    }

    public GisPoint getLoc() {
        return loc;
    }
    

    // add getters and setters etc
        
    }
