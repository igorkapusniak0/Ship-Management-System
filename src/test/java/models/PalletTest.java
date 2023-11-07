package models;

import LinkedList.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PalletTest {
    private Pallet pallet;
    private Container container;
    @BeforeEach
    public void setUp() {
        container = new Container(20, new List<>(), null, null);
        pallet = new Pallet("Pallet Description", 5, 10.0, 2.0, 3.0, container);
    }

    @Test
    public void testSetDescription() {
        pallet.setDescription("NewPalletDescription");
        assertEquals("NewPalletDescription", pallet.getDescription());
    }

    @Test
    public void testSetQuantity() {
        pallet.setQuantity(8);
        assertEquals(8, pallet.getQuantity());
    }

    @Test
    public void testSetValue() {
        pallet.setValue(15.0);
        assertEquals(15.0, pallet.getValue(), 0.001);
    }

    @Test
    public void testSetPalletLocation() {
        container.setLocation(new Port("Port", "Ireland", new List<>(), new List<>()));
        pallet.setPalletLocation(container);
        assertEquals("Ireland", pallet.getPalletLocation());
    }

    @Test
    public void testSetWeight() {
        pallet.setWeight(4.0);
        assertEquals(4.0, pallet.getWeight(), 0.001);
    }

    @Test
    public void testSetVolume() {
        pallet.setVolume(5.0);
        assertEquals(5.0, pallet.getVolume(), 0.001);
    }

    @Test
    public void testSetContainer() {
        Container newContainer = new Container(40, new List<>(), null, null);
        pallet.setContainer(newContainer);
        assertEquals(newContainer, pallet.getContainer());
    }

    @Test
    public void testSetShip() {
        Ship ship = new Ship("Ship", "Ireland", "ShipPicture", null);
        container.setShip(ship);
        pallet.setShip();
        assertEquals(ship, pallet.getShip());
    }

    @Test
    public void testSetPort() {
        Port port = new Port("Port", "Ireland", new List<>(), new List<>());
        container.setPort(port);
        pallet.setPort();
        assertEquals(port, pallet.getPort());
    }

    @Test
    public void testSetTotalValue() {
        assertEquals(5 * 10.0, pallet.getTotalValue(), 0.001);
    }

    @Test
    public void testToString() {
        assertNotNull(pallet.toString());
    }
}
