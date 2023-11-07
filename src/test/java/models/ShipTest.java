package models;

import LinkedList.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShipTest {
    private Ship ship;
    private Port port;

    @BeforeEach
    public void setUp() {
        port = new Port("SamplePort", "SampleCountry", new List<>(), new List<>());
        ship = new Ship("SampleShip", "ShipCountry", "ShipPicture", port);
    }

    @Test
    public void testSetShipName() {
        ship.setShipName("NewShipName");
        assertEquals("NewShipName", ship.getShipName());
    }

    @Test
    public void testSetShipCountry() {
        ship.setShipCountry("NewShipCountry");
        assertEquals("NewShipCountry", ship.getShipCountry());
    }

    @Test
    public void testSetShipCode() {
        ship.setShipCode();
        assertNotNull(ship.getShipCode());
    }

    @Test
    public void testSetShipPicture() {
        ship.setShipPicture("NewShipPicture");
        assertEquals("NewShipPicture", ship.getShipPicture());
    }

    @Test
    public void testSetLocationWithPort() {
        ship.setLocation(port);
        assertEquals(port.getPortCountry(), ship.getLocation());
    }

    @Test
    public void testSetLocationAtSea() {
        ship.setPort(null);
        ship.setLocation(null);
        assertEquals("At Sea", ship.getLocation());
    }

    @Test
    public void testAddContainer() {
        Container container = new Container(20, new List<>(), port, ship);
        ship.addContainer(container);
        assertTrue(ship.getAllContainers().contains(container));
    }

    @Test
    public void testRemoveContainer() {
        Container container = new Container(20, new List<>(), port, ship);
        ship.addContainer(container);
        ship.removeContainer(container);
        assertFalse(ship.getAllContainers().contains(container));
    }

    @Test
    public void testSetTotalValue() {
        assertTrue(ship.getTotalValue() >= 0);
    }

    @Test
    public void testToString() {
        assertNotNull(ship.toString());
    }
}
