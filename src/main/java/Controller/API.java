package Controller;

import LinkedList.Node;
import models.Port;

import LinkedList.List;

public class API {
    public List<Port> list = new List<>();

    public boolean addPort(Port port){
        if(port.getPortCode()!=null){
            list.add(port);
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





}
