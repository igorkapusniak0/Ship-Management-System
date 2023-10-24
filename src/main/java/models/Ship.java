package models;

import LinkedList.List;
import utils.Utilities;

import java.net.URL;

public class Ship {
    public String shipName = "";
    public String shipCode = "";
    public String shipCountry = "";
    public String shipPicture;
    public List<Container> container;

    public Ship(String shipName, String shipCountry, String shipPicture, String shipCode){
        setShipName(shipName);
        setShipCountry(shipCountry);
        setShipPicture(shipPicture);
        setShipCode();

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
    public void setShipPicture(String shipPicture){
        this.shipPicture = shipPicture;
    }
    public String getShipPicture(){
        return shipPicture;
    }

    /*public void addContainer(Container container){
        containersInPort.add(container);
    }
    public void removeContainer(Container container){
        containersInPort.remove(container);
    }*/
    @Override
    public String toString() {
        return "Ship{Name " + shipName + ", Code: " + shipCode + ", Country: " + shipCountry+", Picture: "+shipPicture+"}";
    }



}
