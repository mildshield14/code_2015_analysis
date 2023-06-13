// Date: 12 june 2023
// Authors; SOOBEN Vennila (20235256) and ABSALON Marion (20211423)


import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import java.lang.Math.*;


public class GestionCamion {

    private static Queue<Integer> queueBox;
    private static Queue<Double> queueDist;
    private static Queue<Coordinates> queueCoord;
    private int contentTruck;


    public void setqueueCoord(Queue<Coordinates> queueCoord) {
        this.queueCoord = queueCoord;
    }

    public void setContentTruck(int contentTruck) {
        this.contentTruck = contentTruck;
    }

    public  void setqueueBox(Queue<Integer> queueBox) {
        GestionCamion.queueBox = queueBox;
    }

    public static Queue<Integer> getqueueBox() {
        return queueBox;
    }


    public Queue<Coordinates> getQueueCoord() {
        return queueCoord;
    }

    public static Queue<Double> getQueueDist() {
        return queueDist;
    }

    public int getContentTruck() {
        return contentTruck;
    }

    public static void setqueueDist(Queue<Double> queueDist) {
        GestionCamion.queueDist = queueDist;
    }

    public static void main(String[] args) {
int y=0;

ArrayList<Double> arra1 = new ArrayList<Double>();
        ArrayList<Double> arra2 = new ArrayList<Double>();

        for (int w = 60; w < 50000; w=w+250) {

            DataSetGeneratorClass de = new DataSetGeneratorClass();
            if (w > 20 && w < 30){
                 y=y+1;
            }

            if (w > 50 && w < 70){
                y=y+3;
            }
            de.cal(w,y);


            long startTime = System.currentTimeMillis();
            int lengthArrBox = 0;
           // String fileNameIn = "/Users/vennilasooben/IFT2015_TP1/tp1Input/dataset.txt";
 String fileNameIn = "/Users/vennilasooben/IFT2015_TP1/sample.txt";
            int maxBoxIndex = 0;
            int maxBox = 0;

            GestionCamion truck = new GestionCamion();

            ManipulationFichier fileManip = new ManipulationFichier();

            fileManip.readFile(fileNameIn, "res.txt", truck);
            //  fileManip.readFile("test.txt",truck);

            maxBoxIndex = maxBox(fileManip.getArrayBox());
            maxBox = fileManip.getArrayBox().get(maxBoxIndex);


            Coordinates positions = fileManip.getArrayCoord().get(maxBoxIndex);

            fileManip.getArrayCoord().remove(maxBoxIndex);

            //First Position, where the truck will stay

           String outputstr = "Truck position: " + "(" + positions.getLatitude() + "," + positions.getLongitude() + ")";

            fileManip.writeFile("res.txt", outputstr);

           // System.out.println(outputstr);

            // check if truck capacity > max box available to be able to start moving else check is <= then done
            if (fileManip.getArrayBox().get(maxBoxIndex) < truck.getContentTruck()) {

            outputstr = "Distance:0\t\t\t" + "Number of boxes:0\t" + "\t" + "Position:" + "(" + positions.getLatitude() + "," + positions.getLongitude() + ")";

                //fetching content from file passes as parameter
                fileManip.writeFile("res.txt", outputstr);
               // System.out.println(outputstr);

                truck.setContentTruck(truck.getContentTruck() - (fileManip.getArrayBox().get(maxBoxIndex)));

                // we already took all boxes from 1st position so we remove it
                fileManip.getArrayBox().remove(maxBoxIndex);

                // output (pos initial)
                int i = 0;

                // arrays for calculating distance
                ArrayList<Double> arrayDistTemp = new ArrayList();
                // we need a copy to be able to remove from one and do several operation (see below)
                ArrayList<Double> arrayDistTempCopy = new ArrayList<>();


                double dist = 0;

                // add elements of the calculated distances to array
                for (int j = 0; j < fileManip.getArrayCoord().size(); j++) {
                    dist = calculeDistance(positions, fileManip.getArrayCoord().get(j));
                    arrayDistTemp.add(dist);
                    arrayDistTempCopy.add(dist);
                }

                lengthArrBox = fileManip.getArrayBox().size();

                Queue<Double> queue = new LinkedList<Double>();
                Queue<Integer> queueBox = new LinkedList<Integer>();
                Queue<Coordinates> queueCoord = new LinkedList<Coordinates>();

                //searching for the max so that when we see the minimum current distance we
                //can assign it to max so that it does not become min again
                int maxInd = maxmind("max", arrayDistTemp);
                double compa = arrayDistTemp.get(maxInd) + 1;


                // this will save a certain order on indices that will be used to populate our queues later on
                ArrayList<Integer> indexArrayList = new ArrayList<>();

                //sorting array of distances without actually sorting it; we are just using the indices here
                for (int j = 0; j < lengthArrBox; j++) {
                    int minIndex = maxmind("min", arrayDistTempCopy);
                    arrayDistTempCopy.set(minIndex, compa);
                    indexArrayList.add(minIndex);
                }

                //here we are considering the case where distances are similar then check latitude then check longitude
                for (int d = 0; d < lengthArrBox; d++) {
                    Double compar = arrayDistTemp.get(d);
                    for (int e = d + 1; e < lengthArrBox; e++) {
                        if (compar == arrayDistTemp.get(e)) {
                            if (fileManip.getArrayCoord().get(d).getLatitude() == fileManip.getArrayCoord().get(e).getLatitude()) {
                                if (fileManip.getArrayCoord().get(d).getLongitude() > fileManip.getArrayCoord().get(e).getLongitude()) {
                                    int temp = indexArrayList.get(e);
                                    indexArrayList.set(e, indexArrayList.get(d));
                                    indexArrayList.set(d, temp);
                                }
                            } else if (fileManip.getArrayCoord().get(d).getLatitude() > fileManip.getArrayCoord().get(e).getLatitude()) {
                                int temp = indexArrayList.get(e);
                                indexArrayList.set(e, indexArrayList.get(d));
                                indexArrayList.set(d, temp);
                            }
                        }
                    }
                }

                //populating the queues based on the indices we saved of sorted array of distances
                for (int k = 0; k < lengthArrBox; k++) {

                    int index = indexArrayList.get(k);

                    queue.add(arrayDistTemp.get(index));

                    queueBox.add(fileManip.getArrayBox().get(index));

                    queueCoord.add(fileManip.getArrayCoord().get(index));
                }
                truck.setqueueDist(queue);
                truck.setqueueBox(queueBox);
                truck.setqueueCoord(queueCoord);

                int sizequeue = truck.getQueueCoord().size();

                // here we are doing the operation of adding boxes to the truck and doing it
                // while truck is not yet filled and there are still boxes available at the warehouses
                for (i = 0; i < sizequeue; i++) {
                    if (truck.getContentTruck() > 0 && truck.getqueueBox().isEmpty() == false) {
                        positions = calculation(fileManip, "res.txt", truck, positions);
                    }
                }


            } else if (fileManip.getArrayBox().get(maxBoxIndex) >= truck.getContentTruck()) {
                // truck is already filled with boxes found at first warehouse
                lengthArrBox = fileManip.getArrayBox().size();
             //   outputstr = "Distance:0\t" + "\t" + "Number of boxes:\t" + ((fileManip.getArrayBox().get(maxBoxIndex)) - truck.getContentTruck()) + "\t" + "Position:" + "(" + positions.getLatitude() + "," + positions.getLatitude() + ")\n";
                fileManip.writeFile("res.txt", outputstr);
                System.out.println(outputstr);
            }
            long endTime = System.currentTimeMillis();
            System.out.println(endTime - startTime);

            arra1.add(Double.valueOf((endTime - startTime)));
            System.out.println(Integer.toString(lengthArrBox));
            arra2.add(Double.valueOf(lengthArrBox));
        }

GraphDrawer gd = new GraphDrawer();
        gd.draw(arra1, arra2);
    }
    //using Haversine formula t calculate distances between 2 points
    // (we decompose it for easier understanding even though not necessary)
    public static double calculeDistance(Coordinates posInit, Coordinates entrepot) {

        double latitudePos = Math.toRadians(posInit.getLatitude());
        double latitudeEntrepot = Math.toRadians(entrepot.getLatitude());
        double longitudePos = Math.toRadians(posInit.getLongitude());
        double longitudeEntrepot = Math.toRadians(entrepot.getLongitude());

        double phi=(latitudeEntrepot-latitudePos)/2;
        double lambda=(longitudeEntrepot-longitudePos)/2;

        double sincarre1= Math.sin(phi) * Math.sin(phi);

        double sincarre2= Math.sin(lambda) * Math.sin(lambda);
        double cossincarre2= Math.cos(latitudePos) * Math.cos(latitudeEntrepot) * sincarre2;
        double sincarre1ajoutcossincarre2 = sincarre1 + cossincarre2;

        double rootsincarre1ajoutcossincarre2=Math.sqrt(sincarre1ajoutcossincarre2);
        double distance= 2 * 6371000 * Math.asin(rootsincarre1ajoutcossincarre2);
        double scale = Math.pow(10,1);
        return Math.round(distance*scale)/scale;
    }

    //maxBox here is used to to return index of maximum of boxes
    public static int maxBox(ArrayList<Integer> tab){

        int comp=tab.get(0);
        int ind=0;

        for (int i=1; i<tab.size(); i++){

            if (comp<tab.get(i)){
                comp=tab.get(i);
                ind=i;
            }

        }



        return ind;

    }

    // maxmind is used to calculate either max or min element of an array of type Double
    // it returns the index of the selected element (max/min)
    public static int maxmind(String maxOrMin, ArrayList<Double> tab){

        double comp=tab.get(0);
        int ind=0;

        for (int i=1; i<tab.size(); i++){

            if (maxOrMin=="max"){
                if (comp<tab.get(i)){


                    comp = tab.get(i);
                    ind = i;

                }
            }else if (maxOrMin=="min"){

                if (comp>tab.get(i)){
                    comp=tab.get(i);
                    ind=i;
                }

            }

        }

        return ind;

    }

    //calculation is used to fill the truck with boxes and empty the warehouses
    public static Coordinates calculation(ManipulationFichier fileManip, String namefile, GestionCamion ob, Coordinates positions ){

        int minDistIndex=0;

        // we will now be done with that warehouse so we remove its data from the queues

        double minDist=ob.getQueueDist().remove();

        int box= ob.getqueueBox().remove();

        Coordinates pos = ob.getQueueCoord().remove();


        while (ob.getContentTruck() >0 && box > 0) {
            ob.setContentTruck((ob.getContentTruck() - 1));
            box=box-1;
        }

        String outputstr="Distance:"+ minDist + "\t\t" + "Number of boxes:" + box + "\t\t" + "Position:" + "(" + pos.getLatitude() + "," + pos.getLongitude() + ")";
        fileManip.writeFile(namefile,outputstr);
      //  System.out.println("Distance:" + minDist + "\t\t" + "Number of boxes:" + box + "\t\t" + "Position:" + "(" + pos.getLatitude() +"," + pos.getLongitude() + ")");

        //this is the next warehouse to visit
        positions = pos;


        return positions;
    }

}