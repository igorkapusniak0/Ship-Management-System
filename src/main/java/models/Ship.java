package models;

import Controller.API;
import LinkedList.List;
import LinkedList.Node;
import utils.Utilities;

import java.io.Serializable;

public class Ship implements Serializable {
    public String shipName = "";
    public String shipCode = "";
    public String shipCountry = "";
    public String shipPicture;
    private double totalValue;
    private String location;
    public Port port;
    public List<Container> containers = new List<>();

    public Ship(String shipName, String shipCountry,String shipPicture,Port port){
        setShipName(shipName);
        setShipCountry(shipCountry);
        setShipPicture(shipPicture);
        setShipCode();
        setTotalValue();
        setLocation(port);
        setPort(port);
    }

    public void setShipName(String shipName){
        if (!Utilities.validStringlength(shipName, 20)) {
            Utilities.truncateString(shipName, 20);
        } this.shipName = shipName;
    }
    public String getShipName(){
        return shipName;
    }
    public void setShipCountry(String shipCountry){
        if (!Utilities.validStringlength(shipCountry, 20)) {
            Utilities.truncateString(shipCountry, 20);
        } this.shipCountry = shipCountry;
    }
    public void setLocation(Port port){
        if (port!=null){
            this.location=port.getPortCountry();
        }else {
            this.location="At Sea";
        }
    }
    public String getLocation(){
        return location;
    }
    public Port getPort(){
        return port;
    }
    public void setPort(Port port){
        this.port=port;
    }
    public String getShipCountry(){
        return shipCountry;
    }
    public void setShipCode(){
        this.shipCode=Utilities.truncateString(shipCountry,3)+"-"+API.uniqueShipCode(Utilities.uniqueCodeGenerator());
    }
    public void setTotalValue(){
        Node<Container> current = containers.head;
        double totalValue = 0;
        while (current!=null){
            totalValue+=current.data.getTotalValue();
            current=current.next;
        }
        this.totalValue=totalValue;
    }

    public double getTotalValue(){
        return totalValue;
    }
    public String getShipCode(){
        return shipCode;
    }
    public void setShipPicture(String shipPicture){
        this.shipPicture = shipPicture;
    }
    public String getShipPicture(){
        return shipPicture;
    }

    public void addContainer(Container container){
        containers.add(container);
        setTotalValue();
    }
    public List<Container> getAllContainers() {
        return containers;
    }
    public void removeContainer(Container container){
        containers.remove(container);
    }
    @Override
    public String toString() {
        return "Ship{Name " + shipName + ", Code: " + shipCode + ", Country: " + shipCountry+", Picture: "+shipPicture+"}";
    }
}