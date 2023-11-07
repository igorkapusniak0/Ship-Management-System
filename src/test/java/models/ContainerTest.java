package models;

import LinkedList.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ContainerTest {
    private Container container;
    private Port port;
    private Ship ship;

    @BeforeEach
    public void setUp() {
        port = new Port("PortName", "SampleCountry", new List<>(), new List<>());
        ship = new Ship("ShipName", "ShipCountry", "ShipPicture", port);

        List<Pallet> pallets = new List<>();

        container = new Container(20, pallets, port, ship);

        Pallet pallet1 = new Pallet("Pallet1", 1, 1, 1, 1, container);
        Pallet pallet2 = new Pallet("Pallet2", 2, 2, 2, 2, container);

        // Add the pallets to the list
        pallets.add(pallet1);
        pallets.add(pallet2);

        System.out.println(container);
    }


    @Test
    public void testSetContCode() {
        System.out.println(container);
        assertNotNull(container.getContCode());
    }

    @Test
    public void testSetContSize() {
        assertEquals(20 * 8 * 8, container.getContSize());
    }

    @Test
    public void testSetLocation() {
        assertEquals(port.getPortCountry(), container.getLocation());
    }

    @Test
    public void testSetLocationAtSea() {
        container.setPort(null);
        assertEquals("At Sea", container.getLocation());
    }

    @Test
    public void testAddPallet() {
        Pallet newPallet = new Pallet("Pallet2",2,2,2,2,container);
        container.addPallet(newPallet);

        assertTrue(container.pallets.contains(newPallet));
    }

    @Test
    public void testSetTotalValue() {
        assertTrue(container.getTotalValue() >= 0);
    }

    @Test
    public void testGetTotalPalletsVolume() {
        assertTrue(container.getTotalPalletsVolume() >= 0);
    }

    @Test
    public void testPercentageFull() {
        double percentage = container.percentageFull();
        assertTrue(percentage >= 0 && percentage <= 100);
    }

    @Test
    public void testToString() {
        assertNotNull(container.toString());
    }
}
