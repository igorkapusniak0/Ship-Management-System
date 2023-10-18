package Controller;

import models.Port;

import java.util.LinkedList;

public class API {
    public LinkedList list;
    private Port port;

    public String showPorts(){
        String ports = "";
        while (list != null){
            ports += port.toString();
        }
        return ports;
    }


}
