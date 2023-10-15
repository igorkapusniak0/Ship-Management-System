package models;

import utils.Utilities;

public class Pallet {
    private String description = "";
    private int quantity = 0;
    private double value = 0;
    private double weight =0;
    private double volume =0;
    public Pallet(String description,int quantity,double value,double weight,double volume){
        setDescription(description);
        setQuantity(quantity);
        setValue(value);
        setWeight(weight);
        setVolume(volume);
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


}
