package models;
import utils.*;
import LinkedList.List;



public class Port {
    public String portName;
    public String portCode;
    public String portCountry;
    public List<Container> containersInPort;
    public List<Ship> ships;

    public Port(String portName,String country, String portCode){
        setPortName(portName);
        setPortCountry(country);
        setPortCode();
    }

    public void setPortName(String portName){
        if (Utilities.validStringlength(portName,20)){
            this.portName = portName;
        }
    }
    public String getPortName(){
        return portName;
    }

    public void setPortCountry(String portCountry){
        if (Utilities.validStringlength(portCountry,15)){
            this.portCountry = portCountry;
        }
    }

    public String getPortCountry(){
        return portCountry;
    }
    public void setPortCode(){
        this.portCode= Utilities.truncateString(portCountry,3)+"-"+Utilities.uniqueCodeGenerator();
    }
    public String getPortCode(){
        return portCode;
    }

    public void addContainer(Container container){
        containersInPort.add(container);
    }
    public void removeContainer(Container container){
        containersInPort.remove(container);
    }

    public void addShip(Ship ship){
        ships.add(ship);
    }
    public void removeShip(Ship ship){
        ships.remove(ship);
    }




}
