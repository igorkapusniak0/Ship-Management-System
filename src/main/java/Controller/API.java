package Controller;

import LinkedList.Node;
import Scenes.WelcomeScene;
import javafx.application.Platform;
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


    public void save(String fileName) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(list); // Save the list of ports
            objectOutputStream.writeObject(shipsAtSea); // Save the list of ships at sea
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void clear(String fileName) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(null); // Save the list of ports
            objectOutputStream.writeObject(null); // Save the list of ships at sea
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load data from a file using Java serialization
    public void load(String fileName) {
        try (FileInputStream fileInputStream = new FileInputStream(fileName);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            list = (List<Port>) objectInputStream.readObject(); // Load the list of ports
            shipsAtSea = (List<Ship>) objectInputStream.readObject(); // Load the list of ships at sea
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

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

    public Container suitableContainer(){
        Node<Port> currentPort = list.head;
        Node<Ship> currentShipAtSea = shipsAtSea.head;
        double lowestPercentage=Double.MAX_VALUE;
        Container lowestPercentageContainer = null;

        while (currentPort!=null){
            Node<Container> currentContainer = currentPort.data.containersInPort.head;
            while (currentContainer!=null){
                Container container = currentContainer.data;
                double percentage = container.percentageFull();

                if (percentage<lowestPercentage){
                    lowestPercentage = percentage;
                    lowestPercentageContainer= container;
                }
                currentContainer = currentContainer.next;
            }

            Node<Ship> currentShip = currentPort.data.ships.head;
            while (currentShip!=null){
                Node<Container> containerOnShip = currentShip.data.containers.head;
                while (containerOnShip!=null){
                    Container container = currentContainer.data;
                    double percentage = container.percentageFull();

                    if (percentage<lowestPercentage){
                        lowestPercentage = percentage;
                        lowestPercentageContainer = container;
                    }
                    currentContainer = currentContainer.next;
                }
                currentShip = currentShip.next;
            }
            currentPort = currentPort.next;
        }

        while (currentShipAtSea!=null){
            Node<Container> containerAtSea = currentShipAtSea.data.containers.head;
            while (containerAtSea!=null){
                Container container = containerAtSea.data;
                double percentage = container.percentageFull();

                if (percentage<lowestPercentage){
                    lowestPercentage = percentage;
                    lowestPercentageContainer = container;
                }
                containerAtSea = containerAtSea.next;
            }
            currentShipAtSea = currentShipAtSea.next;
        }
        return lowestPercentageContainer;
    }

    public void resetFacility(){
        Node<Port> current = list.head;
        while (current!=null){
            list.remove(current.data);
            current.data=null;
            current= current.next;
        }
        Node<Ship> currentShip = shipsAtSea.head;
        while (currentShip!=null){
            shipsAtSea.remove(currentShip.data);
            currentShip.data=null;
            currentShip=currentShip.next;
        }
    }

    public void resetFacility2(){
        Node<Port> current = list.head;
        while (current!=null){
            Port port = current.data;
            Node<Ship> currentShip =current.data.ships.head;
            while (currentShip!=null){
                Ship ship = currentShip.data;
                Node<Container> currentContainer= currentShip.data.containers.head;
                while (currentContainer!=null){
                    Container container = currentContainer.data;
                    Node<Pallet> currentPallet = currentContainer.data.pallets.head;
                    while (currentPallet!=null){
                        Pallet pallet = currentPallet.data;
                        pallet=null;
                        currentPallet=currentPallet.next;
                    }
                    container=null;
                    currentContainer=currentContainer.next;
                }
                ship=null;
                currentShip=currentShip.next;

            }
            Node<Container> currentContainer = current.data.containersInPort.head;
            while (currentContainer!=null){
                Container container = currentContainer.data;
                Node<Pallet> currentPallet = currentContainer.data.pallets.head;
                while (currentPallet!=null){
                    Pallet pallet = currentPallet.data;
                    pallet=null;
                    currentPallet=currentPallet.next;
                }
                container=null;
                currentContainer=currentContainer.next;
            }
            port=null;
            current= current.next;

        }

        Node<Ship> currentShip = shipsAtSea.head;
        while (currentShip!=null){
            Ship ship = currentShip.data;
            Node<Container> currentContainer = currentShip.data.containers.head;
            while (currentContainer!=null){
                Container container = currentContainer.data;
                Node<Pallet> currentPallet = currentContainer.data.pallets.head;
                while (currentPallet!=null){
                    Pallet pallet = currentPallet.data;
                    pallet=null;
                    currentPallet=currentPallet.next;
                }
                container=null;
                currentContainer=currentContainer.next;
            }
            ship=null;
            currentShip=currentShip.next;
        }
    }




}
