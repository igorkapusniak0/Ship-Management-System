package Controller;

import LinkedList.Node;
import javafx.application.Platform;
import javafx.scene.control.TableView;
import models.Container;
import models.Pallet;
import models.Port;
import LinkedList.List;
import models.Ship;
import utils.Utilities;


import java.io.*;
import java.util.function.Consumer;

public class API {
    public static List<Port> list = new List<>();
    public static List<Ship> shipsAtSea = new List<>();
    public double totalValue;


    public static void save(String fileName) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(list);
            objectOutputStream.writeObject(shipsAtSea);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void clear(String fileName) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(null);
            objectOutputStream.writeObject(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void load(String fileName) {
        try (FileInputStream fileInputStream = new FileInputStream(fileName);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            list = (List<Port>) objectInputStream.readObject();
            shipsAtSea = (List<Ship>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addPort(Port port){
        list.add(port);
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
            string.setLength(string.length() - 2);
            return string.toString();
        } else {
            return "No ports found.";
        }
    }

    public String listAllContainers() {
        Node<Container> current = list.head.data.ships.head.data.containers.head;
        StringBuilder string = new StringBuilder();

        while (current != null) {
            string.append(current.data.getContCode()).append(", ");
            current = current.next;
        }

        if (!string.isEmpty()) {
            string.setLength(string.length() - 2);
            return string.toString();
        } else {
            return "No containers found.";
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
        Node<Container> current= ship.containers.head;
        while (current!=null){
            current.data.setPort(destination);
            Node<Pallet> currentPallet= current.data.pallets.head;
            while (currentPallet!=null){
                currentPallet.data.setPalletLocation(current.data);
                currentPallet=currentPallet.next;
            }
            current=current.next;
        }
        destination.addShip(ship);
        source.removeShip(ship);
    }
    public void unloadContainer(Ship source, Port destination,Container container){
        Node<Pallet> current = container.pallets.head;
        while (current!=null){
            current.data.setPalletLocation(container);
            current=current.next;
        }
        container.setPort(destination);
        destination.containersInPort.add(container);
        source.removeContainer(container);
    }
    public void loadContainer(Port source, Ship destination,Container container){
        destination.addContainer(container);
        source.containersInPort.remove(container);
    }
    public void moveShipFromSea(Port destination,Ship ship){
        Node<Container> current= ship.containers.head;
        while (current!=null){
            current.data.setPort(destination);
            Node<Pallet> currentPallet= current.data.pallets.head;
            while (currentPallet!=null){
                currentPallet.data.setPalletLocation(current.data);
                currentPallet=currentPallet.next;
            }
            current=current.next;
        }
        destination.addShip(ship);
        shipsAtSea.remove(ship);

    }

    public void moveShipToSea(Port source,Ship ship){
        Node<Container> current= ship.containers.head;
        while (current!=null){
            current.data.setPort(null);
            Node<Pallet> currentPallet= current.data.pallets.head;
            while (currentPallet!=null){
                currentPallet.data.setPalletLocation(current.data);
                currentPallet=currentPallet.next;
            }
            current=current.next;
        }
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
            } else {
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


    public <T> void updateListView(String filter, TableView tableView, Node<T> head, Consumer setTotalValue) {
            Platform.runLater(() -> {
                tableView.getItems().clear();
                tableView.getItems().removeIf(container1 -> !container1.toString().contains(filter));
                Node<T> current = head;
                while (current != null){
                    setTotalValue.accept(current.data);
                    if (current.data.toString().contains(filter)) {
                        if (!tableView.getItems().contains(current.data)) {
                            tableView.getItems().add(current.data);
                        }
                    }
                    current = current.next;
                }
            });
    }

    public static String uniquePortCode(String code){
        boolean unique = false;
        String uniqueCode = "";
        while (!unique) {
            unique = true;
            Node<Port> current = list.head;
            while (current != null) {
                if (current.data.getPortCode().equals(code)) {
                    code = Utilities.uniqueCodeGenerator();
                    unique = false;
                    break;
                }
                current = current.next;
            }
        }
        if (unique) {
            uniqueCode = code;
        }
        return uniqueCode;
    }
    public static String uniqueShipCode(String code){
        String uniqueCode = "";
        boolean unique = false;
        while (!unique){
            unique = true;
            Node<Port> current = list.head;
            while (current!=null){
                Node<Ship> currentShip =current.data.ships.head;
                while (currentShip!=null){
                    if (currentShip.data.getShipCode().equals(code)){
                        code = Utilities.uniqueCodeGenerator();
                        unique = false;
                        break;
                    }
                    currentShip=currentShip.next;
                }
                current=current.next;
            }
            Node<Ship> currentShip = shipsAtSea.head;
            while (currentShip!=null){
                if (currentShip.data.getShipCode().equals(code)){
                    code = Utilities.uniqueCodeGenerator();
                    unique = false;
                    break;
                }
                currentShip=currentShip.next;
            }
        }
        if (unique) {
            uniqueCode = code;
        }
        return uniqueCode;
    }

    public static String uniqueContainerCode(String code){
        String uniqueCode = "";
        boolean unique = false;
        while (!unique){
            unique = true;
            Node<Port> current = list.head;
            while (current!=null){
                Node<Ship> currentShip =current.data.ships.head;
                while (currentShip!=null){
                    Node<Container> currentContainer= currentShip.data.containers.head;
                    while (currentContainer!=null){
                        if (currentContainer.data.getContCode().equals(code)){
                            code = Utilities.uniqueCodeGenerator();
                            unique=false;
                            break;
                        }
                        currentContainer=currentContainer.next;
                    }
                    currentShip=currentShip.next;

                }
                Node<Container> currentContainer = current.data.containersInPort.head;
                while (currentContainer!=null){
                    if (currentContainer.data.getContCode().equals(code)){
                        code = Utilities.uniqueCodeGenerator();
                        unique=false;
                        break;
                    }
                    currentContainer=currentContainer.next;
                }
                current= current.next;
            }
            Node<Ship> currentShip = shipsAtSea.head;
            while (currentShip!=null){
                Node<Container> currentContainer = currentShip.data.containers.head;
                while (currentContainer!=null){
                    if (currentContainer.data.getContCode().equals(code)){
                        code = Utilities.uniqueCodeGenerator();
                        unique=false;
                        break;
                    }
                    currentContainer=currentContainer.next;
                }
                currentShip=currentShip.next;
            }
        }
        if (unique){
            uniqueCode = code;
        }
        return uniqueCode;
    }



}
