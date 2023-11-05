package models;

import Controller.API;
import LinkedList.List;
import LinkedList.Node;
import utils.Utilities;

import java.io.Serializable;


public class Container implements Serializable{
    private String contCode = "";
    private int contSize = 0;
    public List<Pallet> pallets =new List<>();
    private double totalValue;
    private Port port;
    private Ship ship;
    private String location;
    public Container(int contSize, List<Pallet> pallets,Port port){
        setContCode();
        setContSize(contSize);
        setPallets(pallets);
        setPort(port);
        setLocation(port);
        setTotalValue();
    }
    public void setContCode(){
        this.contCode=API.uniqueContainerCode(Utilities.uniqueCodeGenerator());
    }

    public String getContCode(){
        return contCode;
    }
    public void setContSize(int contSize){
        int height = 8;
        int width = 8;
        if(!(contSize==10||contSize==20||contSize==40)){
            this.contSize=10*height*width;
        }else{
            this.contSize = contSize*height*width;
        }
    }
    public int getContSize(){
        return contSize;
    }
    public String getLocation(){
        return location;
    }
    public void setLocation(Port port){
        if (port!=null){
            this.location=port.getPortCountry();
        }else{
            this.location="At Sea";
        }
    }

    public void addPallet(Pallet pallet){
        if(getTotalPalletsVolume()<getContSize()){
            pallets.add(pallet);
            setTotalValue();
        }
    }
    public void setPallets(List<Pallet> pallets){
        this.pallets=pallets;
    }
    public Port getPort(){
        return port;
    }
    public void setPort(Port port){
        this.port = port;
        setLocation(port);
    }
    public Ship getShip(){
        return ship;
    }
    public void setShip(Ship ship){
        this.ship=ship;
    }
    public int getTotalPalletsVolume(){
        Node<Pallet> current = pallets.head;
        int totalVolume = 0;
        while (current!=null){
            totalVolume+= (int) current.data.getVolume();
            current=current.next;
        }
        return totalVolume;
    }
    public double percentageFull(){
        return (double) getTotalPalletsVolume() /getContSize()*100;
    }

    public void setTotalValue() {
        Node<Pallet> current = pallets.head;
        double totalValue = 0;
        while (current!=null){
            totalValue+=current.data.getTotalValue();
            current=current.next;
        }
        this.totalValue=totalValue;
    }

    public double getTotalValue(){
        return totalValue;
    }

    @Override
    public String toString() {
        return "Container{Code: "+  contCode + ", " + "Size: "+contSize+ "}";
    }

}
