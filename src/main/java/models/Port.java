package models;
import Controller.API;
import utils.*;
import LinkedList.List;



public class Port {
    private List list;
    public String portName;
    public String portCode;
    public String portCountry;
    public List<Container> containersInPort = new List<>();
    public List<Ship> ships = new List<>();

    public Port(String portName, String country, String portCode, List ships, List containersInPort){
        setPortName(portName);
        setPortCountry(country);
        setPortCode();
        setShips(ships);
        setContainersInPort(containersInPort);

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
    public List<Ship> getShips(){
        return ships;
    }

    public void setContainersInPort(List<Container> containersInPort) {
        this.containersInPort = containersInPort;
    }

    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }

    public void removeShip(Ship ship){
        ships.remove(ship);
    }

    @Override
    public String toString() {
        return "Port{Name: " + portName + ", Code: " + portCode + ", Country: " + portCountry+", Containers: "+containersInPort + ", Ships: " + ships + "}";
    }
}
