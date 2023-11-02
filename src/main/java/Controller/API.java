package Controller;

import LinkedList.Node;
import models.Container;
import models.Pallet;
import models.Port;

import LinkedList.List;
import models.Ship;



import java.io.*;

public class API {
    private Port port;
    public List<Port> list = new List<>();
    public List<Ship> shipsAtSea = new List<>();
    public double totalValue;


    public boolean addPort(Port port){
        if(port.getPortCode()!=null){
            list.add(port);
            return true;
        }else{
            return false;
        }
    }
    public boolean addShip(Ship ship){
        if(ship.getShipCode()!=null){
            port.ships.add(ship);
            return true;
        }else{
            return false;
        }
    }

    public Port deletePort(Port port){
        if(port!=null && list.contains(port)){
            list.remove(port);
            return port;
        }else{
            return null;
        }

    }
    public Port getPort(Port port) {
        Node<Port> current = list.head;
        while (current != null) {
            if (current.data.equals(port)) {
                return current.data;
            }
            current = current.next;
        }
        return null;
    }
    public String getPortAtIndex(int index){
        Node<Port> current = list.head;
        String string = "";
        int i = 0;
        while (current != null && i < index){
            current = current.next;
            i+=1;
        }
        if (current == null){
            return null;
        }
        return current.data.portName;
    }

    public String listAllPorts() {
        Node<Port> current = list.head;
        StringBuilder string = new StringBuilder();

        while (current != null) {
            string.append(current.data.getPortName()).append(", ");
            current = current.next;
        }

        if (!string.isEmpty()) {
            // Remove the trailing ", " from the string
            string.setLength(string.length() - 2);
            return string.toString();
        } else {
            return "No ports found.";
        }
    }

    public String listAlConts() {
        Node<Container> current = list.head.data.ships.head.data.containers.head;
        StringBuilder string = new StringBuilder();

        while (current != null) {
            string.append(current.data.getContCode()).append(", ");
            current = current.next;
        }

        if (!string.isEmpty()) {
            // Remove the trailing ", " from the string
            string.setLength(string.length() - 2);
            return string.toString();
        } else {
            return "No cotns found.";
        }
    }
    public Port searchPort(String port){
        Node<Port> current = list.head;
        while (current!=null){
            if(current.data.toString().contains(port)){
                return current.data;
            }
            current= current.next;
        }return null;
    }

    public void moveShip(Port source, Port destination,Ship ship){
        destination.addShip(ship);
        source.removeShip(ship);
    }
    public void unloadContainer(Ship source, Port destination,Container container){
        destination.containersInPort.add(container);
        source.removeContainer(container);
        container.setPort(destination);
    }
    public void loadContainer(Port source, Ship destination,Container container){
        destination.addContainer(container);
        source.containersInPort.remove(container);
        container.setShip(destination);
    }
    public void moveShipFromSea(Port destination,Ship ship){
        destination.addShip(ship);
        shipsAtSea.remove(ship);
    }

    public void moveShipToSea(Port source,Ship ship){
        shipsAtSea.add(ship);
        source.ships.remove(ship);
    }
    public String getContainerLocation(Container container){
        if(container!=null){
            System.out.println(container.getPort());
            if (container.getPort() != null){
                return "Port: " + container.getPort().getPortCountry();
            } else if (container.getShip()!=null) {
                return "Ship at Sea";
            }
            else {
                return "Location Unknown";
            }


        }else {
            return "Invalid Container";
        }
    }
    public double getTotalValue(){
        Node<Port> currentPort = list.head;
        Node<Ship> currentShip = shipsAtSea.head;
        double totalValue = 0;

        while (currentShip!=null){
            totalValue+=currentShip.data.getTotalValue();
            currentShip=currentShip.next;
        }
        while (currentPort!=null){
            totalValue+=currentPort.data.getTotalValue();
            currentPort=currentPort.next;
        }
        this.totalValue=totalValue;
        return totalValue;
    }

}
