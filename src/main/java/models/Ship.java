package models;

import LinkedList.List;
import utils.Utilities;

import java.net.URL;

public class Ship {
    private String shipName = "";
    private String shipCode = "";
    private String shipCountry = "";
    private URL shipPicture;
    private List<Container> container;

    public Ship(String shipName, String shipCountry, URL shipPicture, String shipCode){

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
    public String getShipCountry(){
        return shipCountry;
    }
    public void setShipCode(){
        this.shipCode = Utilities.truncateString(shipCountry,3)+"-"+Utilities.uniqueCodeGenerator();
    }
    public String getShipCode(){
        return shipCode;
    }
    public void setShipPicture(URL shipPicture){
        this.shipPicture = shipPicture;
    }
    public URL getShipPicture(){
        return shipPicture;
    }

    /*public void addContainer(Container container){
        containersInPort.add(container);
    }
    public void removeContainer(Container container){
        containersInPort.remove(container);
    }*/



}
