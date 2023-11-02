package models;

import utils.Utilities;

import java.io.Serializable;

public class Pallet implements Serializable {
    private String description = "";
    private int quantity = 0;
    private double value = 0;
    private double weight =0;
    private double volume =0;
    private double totalValue;
    private Container container;
    public Pallet(String description,int quantity,double value,double weight,double volume,Container container){
        setDescription(description);
        setQuantity(quantity);
        setValue(value);
        setWeight(weight);
        setVolume(volume);
        setContainer(container);
        setTotalValue();
    }

    public void setDescription(String description){
        if(Utilities.validStringlength(description,200)){
            this.description=description;
        }this.description=Utilities.truncateString(description,200);
    }
    public String getDescription(){
        return description;
    }
    public void setQuantity(int quantity){
        if(Utilities.validRange(quantity,0,10000)){
            this.quantity=quantity;
        }else{
            this.quantity=0;
        }
    }
    public int getQuantity(){
        return quantity;
    }
    public void setValue(double value){
        if(Utilities.validRange(value,0.1,10000000,1)){
            this.value=Utilities.toTwoDecimalPlaces(value);
        }else{
            this.value=0.1;
        }
    }
    public double getValue(){
        return value;
    }
    public void setWeight(double weight){
        if(Utilities.validRange(weight,0.1,10000,1)){
            this.weight=Utilities.toTwoDecimalPlaces(weight);
        }else{
            this.weight=0.1;
        }
    }
    public double getWeight(){
        return weight;
    }
    public void setVolume(double volume){
        if(Utilities.validRange(volume,0.1,640,1)){
            this.volume=Utilities.toTwoDecimalPlaces(volume);
        }else {
            this.volume=0.1;
        }
    }

    public double getVolume(){
        return volume;
    }

    public void setContainer(Container container){
        this.container = container;
    }
    public Container getContainer(){
        return container;
    }

    public double getTotalValue(){
        return totalValue;
    }
    public void setTotalValue(){
        this.totalValue=quantity*value;
    }

    @Override
    public String toString(){
        return "Pallet{Description: "+ description+", Quantity: " +quantity +", Value: "+ value + ", Volume: "+ volume + ", Weight: "+ weight + "}";
    }


}
