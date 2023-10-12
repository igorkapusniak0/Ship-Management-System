package models;

import LinkedList.List;
import utils.Utilities;

public class Container {
    private String contCode = "";
    private int contSize = 0;
    //private Pallet pallet;
    public Container(String contCode, int contSize){

    }
    public void setContCode(){
        this.contCode = "Container-"+Utilities.uniqueCodeGenerator();
    }
    public String getContCode(){
        return contCode;
    }
    public void setContSize(int contSize){
        if(!(contSize==10||contSize==20||contSize==40)){
            this.contSize=10;
        }this.contSize=contSize;
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

}
