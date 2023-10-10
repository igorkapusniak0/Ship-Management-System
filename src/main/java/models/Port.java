package models;
import LinkedList.*;
import utils.*;



public class Port {
    private String portName;
    private String portCode;
    private String country;
    private List<Container> containersInPort;
    private List<Ship> ships;

    public Port(String portName, String portCode, String country){
        setPortName(portName);
    }

    public void setPortName(String portName){
        if (Utilities.validStringlength(portName,20)){
            this.portName = portName;
        }
    }
    public String getPortName(){
        return portName;
    }

    public void setCountry(String country){
        if (Utilities.validStringlength(country,15)){
            this.country = country;
        }
    }

    public String getCountry(){
        return country;
    }
    public void setPortCode(String portCode){
        String code = Utilities.uniqueCodeGenerator();
        this.portCode = Utilities.truncateString(country,3)+"-"+code;

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
