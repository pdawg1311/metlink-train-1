package comp261.assig1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import java.util.Random;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;

import javafx.scene.Parent;
import javafx.scene.Scene;



import java.util.ArrayList;
import java.awt.*;
import java.util.*;

import javax.lang.model.util.Elements.Origin;

import apple.laf.JRSUIConstants.Size;
import javafx.event.*;

public class GraphController{

    // names from the items defined in the FXML file
    @FXML private TextField searchText; 
    @FXML private Label search; 
    @FXML private Label node; 
    @FXML private Button handleMi;
    @FXML private Button handleGer;
    @FXML private Button load;
    @FXML private Button quit;
    @FXML private Button up;
    @FXML private Button down;
    @FXML private Button left;
    @FXML private Button right;
    @FXML private Canvas mapCanvas;
    @FXML private Label nodeDisplay;
    @FXML private TextArea tripText;
  

    // These are use to map the nodes to the location on screen
    private Double scale = 5000.0; //5000 gives 1 pixel ~ 2 meter
    private static final double ratioLatLon = 0.73; // in Wellington ratio of latitude to longitude
    private GisPoint mapOrigin = new GisPoint(174.77, -41.3); // Lon Lat for Wellington
 
    private static int stopSize = 5; // drawing size of stops
    private static int moveDistance = 100; // 100 pixels
    private static double zoomFactor = 1.1; // zoom in/out factor

    private ArrayList<Stop> highlightNodes = new ArrayList<Stop>();
    private ArrayList<ArrayList<Edge>> drawedges=new ArrayList<>();
   
    // map model to screen using scale and origin
    private Point2D model2Screen (GisPoint modelPoint) {
        return new Point2D(model2ScreenX(modelPoint.lon), model2ScreenY(modelPoint.lat));
    }
    private double model2ScreenX (double modelLon) {
      return (modelLon - mapOrigin.lon) * (scale*ratioLatLon) + mapCanvas.getWidth()/2; 
    }
    // the getHeight at the start is to flip the Y axis for drawing as JavaFX draws from the top left with Y down.
    private double model2ScreenY (double modelLat) {
      return mapCanvas.getHeight()-((modelLat - mapOrigin.lat)* scale + mapCanvas.getHeight()/2);
    }

    // map screen to model using scale and origin
    private double getScreen2ModelX(Point2D screenPoint) {
        return (((screenPoint.getX()-mapCanvas.getWidth()/2)/(scale*ratioLatLon)) + mapOrigin.lon);
    }
    private double getScreen2ModelY(Point2D screenPoint) {
        return ((((mapCanvas.getHeight()-screenPoint.getY())-mapCanvas.getHeight()/2)/scale) + mapOrigin.lat);
    }
    
    private GisPoint getScreen2Model(Point2D screenPoint) {
        return new GisPoint(getScreen2ModelX(screenPoint), getScreen2ModelY(screenPoint));
    }   



    public void handleMi(ActionEvent event){
        Locale locale = new Locale("mi","NZ");
       

        ResourceBundle bundle = ResourceBundle.getBundle("comp261/assig1/resources/strings", locale);
        initialize(bundle);

    }
    public void handleGer(ActionEvent event){
        Locale locale = new Locale("Ge");

        ResourceBundle bundle = ResourceBundle.getBundle("comp261/assig1/resources/strings", locale);
        initialize(bundle);

    }


    


   
    public void initialize(ResourceBundle r ) {
       // currently blank

       load.setText(r.getString("load"));
       up.setText(r.getString("up"));
       quit.setText(r.getString("quit"));
       
       node.setText(r.getString("node"));
       down.setText(r.getString("down"));
       left.setText(r.getString("left"));
       
       right.setText(r.getString("right"));

       search.setText(r.getString("search"));

            
       Parent root = up.getScene().getRoot();
       Stage stage = new Stage();

       stage.setTitle(r.getString("title")); // set the title of the window from the bundle
       stage.setScene(new Scene(root, 800, 700));
       stage.show();
       stage.setOnCloseRequest(e -> {
           System.exit(0);
       });

       

        

    }

    /* handle the load button being pressed connected using FXML*/
    public void handleLoad(ActionEvent event) {
        Stage stage = (Stage) mapCanvas.getScene().getWindow();
        System.out.println("Handling event " + event.getEventType());
        FileChooser fileChooser = new FileChooser();
        //Set to user directory or go to default if cannot access

        
        File defaultNodePath = new File("data");
        System.out.println(defaultNodePath.getPath());
        if(!defaultNodePath.canRead()) {
            defaultNodePath = new File("C:/");
        }
        fileChooser.setInitialDirectory(defaultNodePath);
        FileChooser.ExtensionFilter extentionFilter = new FileChooser.ExtensionFilter("txt files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extentionFilter);

        fileChooser.setTitle("Open Stop File");
        File stopFile = fileChooser.showOpenDialog(stage);

        fileChooser.setTitle("Open Stop Pattern File");
        File tripFile = fileChooser.showOpenDialog(stage);

        Main.graph=new Graph(stopFile,tripFile);
        drawGraph();
        event.consume(); // this prevents other handlers from being called
    }

    public void handleQuit(ActionEvent event) {
        System.out.println("Quitting with event " + event.getEventType()); 
        event.consume();  
        System.exit(0); 
    }

    public void handleZoomin(ActionEvent event) {
        System.out.println("Zoom in event " + event.getEventType()); 
// Todo: zoom in
    scale*=zoomFactor;
        drawGraph();
        event.consume();  
    }

    public void handleZoomout(ActionEvent event) {
        System.out.println("Zoom out event " + event.getEventType()); 
// Todo: zoom out
    scale *= 1.0/zoomFactor;
    drawGraph();
        event.consume();  
    }

    public void handleUp(ActionEvent event) {
        System.out.println("Move up event " + event.getEventType()); 
 // Todo: move up
        mapOrigin.add(0, moveDistance/scale);
        drawGraph();
        event.consume();  
    }

    public void handleDown(ActionEvent event) {
        System.out.println("Move Down event " + event.getEventType()); 
// Todo: move down
        mapOrigin.subtract(0,moveDistance/scale);
        drawGraph();
        event.consume();  
    }

    public void handleLeft(ActionEvent event) {
        System.out.println("Move Left event " + event.getEventType()); 
// Todo: move left
        mapOrigin.add(moveDistance/scale,0);
        drawGraph();
        event.consume();  
    }

    public void handleRight(ActionEvent event) {
        System.out.println("Move Right event " + event.getEventType()); 
// Todo: move right
        mapOrigin.subtract(moveDistance/scale,0);
        drawGraph();
        event.consume();  
    }

    public void handleSearch(ActionEvent event) {
        System.out.println("Look up event " + event.getEventType()+ "  "+searchText.getText()); 
        String search = searchText.getText();
        drawedges.clear();
 // Todo: figure out how to add searching and by text
 // This is were a Trie would be used potentially
        highlightNodes.clear();
        for(Stop stop : Main.graph.getStops()){
            if(search.length()>0&&stop.getName().equals(search)){
                highlightClosestStop(stop.getLoc().lon, stop.getLoc().lat);
                nodeDisplay.setText(stop.getName());
                
            }

        }
        /**ArrayList<Stop> trips= new ArrayList<>();
        for(Trip t:Main.graph.getTrips()){
            if(t.getTripId().equals(closestStop.getId())){
                trips.add(t.getStop_pattern_id());
            } */

        drawGraph();
        
        event.consume();  
    }  

    public void handleSearchKey(KeyEvent event) {
        System.out.println("Look up event " + event.getEventType()+ "  "+searchText.getText()); 
        String search = searchText.getText();
// Todo: figure out how to add searching and by text after each keypress
// This is were a Trie would be used potentially

        System.out.println("PREFIX"+search);
        Trie trie = new Trie();
        
        

        for(Stop stop:Main.graph.getStops()){
            trie.add(stop);
        }

        //trie.print(trie.root,"");

       highlightNodes.clear();

       System.out.println("SEARCH"+search);
       for(Stop s:trie.getAll(search)) {
           highlightNodes.add(s);
       }

       for(int i=0;i<highlightNodes.size();i++){
            System.out.println("Highlighted nodes list: \n"+highlightNodes.get(i).getName());
       }

       String matchStops = "Matched Stops: "+System.lineSeparator();


       for(Stop i:highlightNodes){
           matchStops += i.getName()+System.lineSeparator();
       }

       tripText.setText(matchStops);


       if(highlightNodes.size()==1){
           highlightClosestStop(highlightNodes.get(0).getLoc().lon,highlightNodes.get(0).getLoc().lat );
       }

       System.out.println("HIGHLIGHT NODES COUNT"+highlightNodes.size());
       
      
       drawGraph();
        event.consume();  

    }  


/* handle mouse clicks on the canvas
   select the node closest to the click */
    public void handleMouseClick(MouseEvent event) {
        System.out.println("Mouse click event " + event.getEventType());
// Todo: find node closed to mouse click
// event.getX(), event.getY() are the mouse coordinates
       Point2D screenPoint=new Point2D(event.getX(), event.getY());
       double x = getScreen2ModelX(screenPoint);
       double y=getScreen2ModelY(screenPoint);
       highlightClosestStop(x,y);

        event.consume();
    }

    public ArrayList<String> stopgetter(Trip t){
       ArrayList<Trip> tripiana= new ArrayList<>();//for all trips with stop
       ArrayList<String> stopiana=new ArrayList<>();//for all stops on the trips 
       for(Trip trip: Main.graph.getTrips()) {
            if(t.getStop_pattern_id().equals(trip.getStop_pattern_id())){
                tripiana.add(trip);
            }
       }
       for(Stop s:Main.graph.getStops()){
           for(Trip trip:tripiana){
                if(s.getId().equals(trip.getTripId())){
                        stopiana.add(s.getId());
                }
           }
       }


       return stopiana;
    }

    public ArrayList<Edge> edgegetter(ArrayList<String> t){
        ArrayList<Edge> stuff=new ArrayList<>();
        for(Edge e:Main.graph.getEdges()){
            for(String s:t){
                if(e.getFromStop().getId().equals(s)||e.getToStop().equals(s)){
                    stuff.add(e);
                }


            }
        }
        return stuff;
    }

   


    //find the Closest stop to the lon,lat postion
    public void highlightClosestStop(double lon, double lat) {
        double minDist = Double.MAX_VALUE;
        Stop closestStop = null;
//Todo: find closest stop and work out how to highlight it
//Todo: Work out highlighting the trips through this node
        for(Stop stop:Main.graph.getStops()){
            double dist=stop.getLoc().distance(lon,lat);
            if(dist<minDist){
                minDist=dist;
                closestStop=stop;
            }
        }
        
        ArrayList<Trip> trips= new ArrayList<>();
        for(Trip t:Main.graph.getTrips()){
            if(t.getTripId().equals(closestStop.getId())){
                trips.add(t);
            }
        }
        drawedges.clear();



        
        String s="";
        for(Trip t:trips){
            
            s+="Trip_id: "+ t.getStop_pattern_id()+" stops:["+stopgetter(t)+" ]"+System.lineSeparator();
            drawedges.add(edgegetter(stopgetter(t)));
            drawGraph();
        }

       
        tripText.setText(s);
        System.out.println("Connected Trips"+trips);
        if(closestStop!=null){
            highlightNodes.clear();
            highlightNodes.add(closestStop);
            nodeDisplay.setText(closestStop.getName());
            drawGraph();
        }


    }



/*
Drawing the graph on the canvas
*/
    public void drawCircle(double x, double y, int radius) {
        GraphicsContext gc = mapCanvas.getGraphicsContext2D();
        gc.fillOval(x-radius/2, y-radius/2, radius, radius);
    }

    public void drawLine(double x1, double y1, double x2, double y2) {
        mapCanvas.getGraphicsContext2D().strokeLine(x1, y1, x2, y2);
    }
    

    // This will draw the graph in the graphics context of the mapcanvas
    public void drawGraph() {
        Graph graph = Main.graph;
        if(graph == null) {
            return;
        }
        GraphicsContext gc = mapCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());

        gc.setFill(Color.BLUE);
       // drawCircle(0, 0, 10000);

        // Todo:  store node list so we can use nodes to find edge end points.
        ArrayList<Stop> stopList = graph.getStops();
       

        ArrayList<Edge> edgeList = graph.getEdges();

        
            
         for(ArrayList<Edge>e:drawedges){
            Random rand = new Random();
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            Color randomColor = new Color(r,g,b,1);
            for(Edge edge:e){
                mapCanvas.getGraphicsContext2D().setLineWidth(5);
                mapCanvas.getGraphicsContext2D().setStroke(randomColor);
               if(edge.getFromStop()!=null&&edge.getToStop()!=null){
                        
                 Point2D start = model2Screen(edge.getToStop().getLoc());
                 Point2D end = model2Screen(edge.getFromStop().getLoc());
                
                drawLine(start.getX(), start.getY(), end.getX(), end.getY());  
                }  }

         }
         
       
        

                //174.9769631
                //-41.11019647
                //drawCircle(100, 200,stopSize);

        // Todo: use the nodes form the data in graph to draw the graph
        // probably use something like this
        for(Stop stop:stopList){
            int size=stopSize;
            if(highlightNodes.contains(stop)){
                gc.setFill(Color.RED);
                size=stopSize*2;
            }else{gc.setFill(Color.BLUE);}
            Point2D screenPoint = model2Screen(stop.getLoc());
            drawCircle(screenPoint.getX(), screenPoint.getY(), size);
           
        }



        //draw edges
        for(Edge edge:edgeList){
           gc.setLineWidth(1);
           gc.setStroke(Color.BLACK);
           if(edge.getFromStop()!=null&&edge.getToStop()!=null){

            Point2D start = model2Screen(edge.getFromStop().getLoc());
            Point2D end = model2Screen(edge.getToStop().getLoc());

           gc.strokeLine(start.getX(),start.getY(),end.getX(),end.getY());  
           }  

           
       }

       


                //Todo: use the edge form the data in graph to draw the graph


            
            //Todo: step through the edges and draw them with something like
                 //   Point2D startPoint = model2Screen(fromStop.getPoint());
                   // Point2D endPoint = model2Screen(toStop.getPoint());
                    //drawLine(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
                
    }


    private void drawTrip(Trip trip, GraphicsContext gc, Color color) {
        gc.setStroke(color);
        gc.setLineWidth(2);
//Todo: step through a trip to highlight it in a different colour
               // Point2D startPoint = model2Screen(fromStop.getPoint());
                //Point2D endPoint = model2Screen(toStop.getPoint());
                //drawLine(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());

}}
