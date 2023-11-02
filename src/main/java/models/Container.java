package models;

import LinkedList.List;
import LinkedList.Node;
import utils.Utilities;

public class Container {
    private String contCode = "";
    private int contSize = 0;
    public List<Pallet> pallets =new List();
    private double totalValue;
    private Port port;
    private Ship ship;
    public Container(String contCode, int contSize, List pallets,Port port,Ship ship){
        setContCode();
        setContSize(contSize);
        setPallets(pallets);
        setPort(port);
        setShip(ship);
        setTotalValue();
    }
    public void setContCode(){
        this.contCode = "Container-"+Utilities.uniqueCodeGenerator();
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

    public int contSize(){
        int height = 8;
        int width = 8;
        int volume = 0;
        volume = contSize*height*width;
        return volume;
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
            totalVolume+=current.data.getVolume();
            current=current.next;
        }
        return totalVolume;
    }
    public double percentageFull(){
        return getTotalPalletsVolume()/getContSize()*100;
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
