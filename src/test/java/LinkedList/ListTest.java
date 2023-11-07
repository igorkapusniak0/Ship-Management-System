package LinkedList;

import LinkedList.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ListTest {
    private List<Integer> list;

    @BeforeEach
    public void setUp() {
        list = new List<>();
    }

    @Test
    public void testAdd() {
        list.add(1);
        list.add(2);
        list.add(3);

        assertTrue(list.contains(1));
        assertTrue(list.contains(2));
        assertTrue(list.contains(3));
    }

    @Test
    public void testRemove() {
        list.add(1);
        list.add(2);
        list.add(3);

        list.remove(2);

        assertTrue(list.contains(1));
        assertFalse(list.contains(2));
        assertTrue(list.contains(3));
    }

    @Test
    public void testIsEmpty() {
        assertTrue(list.isEmpty());

        list.add(1);
        assertFalse(list.isEmpty());
    }

    @Test
    public void testContains() {
        list.add(1);
        list.add(2);
        list.add(3);

        assertTrue(list.contains(2));
        assertFalse(list.contains(4));
    }
}