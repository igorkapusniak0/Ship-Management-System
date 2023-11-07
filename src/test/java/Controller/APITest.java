package Controller;

import LinkedList.List;
import models.Container;
import models.Port;
import models.Ship;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class APITest {
    private API api;

    @BeforeEach
    public void setUp() {
        api = new API();
    }

    @Test
    public void testAddPort() {
        Port port = new Port("PortName", "SampleCountry", new List<>(), new List<>());
        api.addPort(port);

        assertTrue(API.list.contains(port));
    }

    @Test
    public void testGetPort() {
        Port port = new Port("PortName", "SampleCountry", new List<>(), new List<>());
        api.addPort(port);

        Port retrievedPort = api.getPort(port);
        assertEquals(port, retrievedPort);
    }

    @Test
    public void testMoveShip() {
        Port sourcePort = new Port("SourcePort", "SourceCountry", new List<>(), new List<>());
        Port destinationPort = new Port("DestinationPort", "DestinationCountry", new List<>(), new List<>());
        Ship ship = new Ship("ShipName", "ShipCountry", "ShipPicture", sourcePort);
        sourcePort.addShip(ship);

        api.moveShip(sourcePort, destinationPort, ship);

        assertEquals(destinationPort, ship.getPort());
        assertTrue(destinationPort.ships.contains(ship));
        assertFalse(sourcePort.ships.contains(ship));
    }

    @Test
    public void testUnloadContainer() {
        Ship ship = new Ship("ShipName", "ShipCountry", "ShipPicture", null);
        Container container = new Container(20, new List<>(), null, ship);
        ship.addContainer(container);
        Port destinationPort = new Port("DestinationPort", "DestinationCountry", new List<>(), new List<>());

        api.unloadContainer(ship, destinationPort, container);

        assertEquals(destinationPort, container.getPort());
        assertTrue(destinationPort.containersInPort.contains(container));
        assertFalse(ship.containers.contains(container));
    }

    @Test
    public void testLoadContainer() {
        Port sourcePort = new Port("SourcePort", "SourceCountry", new List<>(), new List<>());
        Container container = new Container(20, new List<>(), sourcePort, null);
        sourcePort.addContainer(container);
        Ship ship = new Ship("ShipName", "ShipCountry", "ShipPicture", null);

        api.loadContainer(sourcePort, ship, container);

        assertEquals(ship, container.getShip());
        assertEquals(sourcePort, container.getPort());
        assertTrue(ship.containers.contains(container));
        assertFalse(sourcePort.containersInPort.contains(container));
    }

    @Test
    public void testMoveShipFromSea() {
        Ship ship = new Ship("ShipName", "ShipCountry", "ShipPicture", null);
        Port destinationPort = new Port("DestinationPort", "DestinationCountry", new List<>(), new List<>());
        API.shipsAtSea.add(ship);

        api.moveShipFromSea(destinationPort, ship);

        assertEquals(destinationPort, ship.getPort());
        assertTrue(destinationPort.ships.contains(ship));
        assertFalse(API.shipsAtSea.contains(ship));
    }

    @Test
    public void testMoveShipToSea() {
        Ship ship = new Ship("ShipName", "ShipCountry", "ShipPicture", null);
        Port sourcePort = new Port("SourcePort", "SourceCountry", new List<>(), new List<>());
        sourcePort.ships.add(ship);

        api.moveShipToSea(sourcePort, ship);

        assertNull(ship.getPort());
        assertTrue(API.shipsAtSea.contains(ship));
        assertFalse(sourcePort.ships.contains(ship));
    }

    @Test
    public void testMoveContainerToShip() {
        Ship sourceShip = new Ship("SourceShip", "SourceCountry", "ShipPicture", null);
        Ship destinationShip = new Ship("DestinationShip", "DestinationCountry", "ShipPicture", null);
        Container container = new Container(20, new List<>(), null, sourceShip);
        sourceShip.addContainer(container);

        api.moveContainerToShip(sourceShip, destinationShip, container);

        assertEquals(destinationShip, container.getShip());
        assertTrue(destinationShip.containers.contains(container));
        assertFalse(sourceShip.containers.contains(container));
    }




    @Test
    public void testResetFacility() {
        Port port = new Port("Port1", "Country1", new List<>(), new List<>());
        Ship ship = new Ship("Ship1", "Country1", "Picture1", port);

        API.shipsAtSea.add(ship);
        API.list.add(port);

        api.resetFacility();

        assertTrue(API.list.isEmpty());
        assertTrue(API.shipsAtSea.isEmpty());
    }


}
