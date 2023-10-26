package models;

import LinkedList.List;

public class Ocean {
    private String location;
    public List<Ship> shipsAtSea = new List();
    public Ocean(String location,List shipsAtSea){
        setLocation(location);
        setShipsAtSea(shipsAtSea);
    }

    public String getLocation() {
        return location;
    }

    public List<Ship> getShipsAtSea() {
        return shipsAtSea;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setShipsAtSea(List<Ship> shipsAtSea) {
        this.shipsAtSea = shipsAtSea;
    }
    @Override
    public String toString(){
        return "Location: " + location + ", Ships at sea: " + shipsAtSea;
    }
}
