package models;

import LinkedList.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PortTest {
    private Port port;
    private List<Container> containersInPort;
    private List<Ship> ships;

    @BeforeEach
    public void setUp() {
        containersInPort = new List<>();
        ships = new List<>();
        port = new Port("PortName", "SampleCountry", ships, containersInPort);
    }

    @Test
    public void testSetPortName() {
        port.setPortName("NewPortName");
        assertEquals("NewPortName", port.getPortName());
    }

    @Test
    public void testSetPortCountry() {
        port.setPortCountry("NewCountry");
        assertEquals("NewCountry", port.getPortCountry());
    }

    @Test
    public void testSetPortCode() {
        port.setPortCode();
        assertNotNull(port.getPortCode());
    }

    @Test
    public void testAddContainer() {
        Container container = new Container(20, new List<>(), port, null);
        port.addContainer(container);
        assertTrue(containersInPort.contains(container));
    }

    @Test
    public void testRemoveContainer() {
        Container container = new Container(20, new List<>(), port, null);
        port.addContainer(container);
        port.removeContainer(container);
        assertFalse(containersInPort.contains(container));
    }

    @Test
    public void testSetTotalValue() {
        assertTrue(port.getTotalValue() >= 0);
    }

    @Test
    public void testAddShip() {
        Ship ship = new Ship("ShipName", "ShipCountry", "ShipPicture", port);
        port.addShip(ship);
        assertTrue(ships.contains(ship));
    }

    @Test
    public void testRemoveShip() {
        Ship ship = new Ship("ShipName", "ShipCountry", "ShipPicture", port);
        port.addShip(ship);
        port.removeShip(ship);
        assertFalse(ships.contains(ship));
    }


    @Test
    public void testToString() {
        assertNotNull(port.toString());
    }
}
