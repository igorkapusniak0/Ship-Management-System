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





}
