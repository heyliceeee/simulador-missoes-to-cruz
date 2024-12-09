package org.example.test;

import org.example.collections.implementation.LinkedList;
import org.junit.Test;

import static org.junit.Assert.*;

public class LinkedListTest {

    @Test
    public void testAddAndGetElement() {
        LinkedList<String> list = new LinkedList<>();
        list.add("Heliporto");
        list.add("Laboratório");

        assertEquals(2, list.getSize());
        assertEquals("Heliporto", list.getElementAt(0));
        assertEquals("Laboratório", list.getElementAt(1));
    }

    @Test
    public void testRemoveElement() {
        LinkedList<String> list = new LinkedList<>();
        list.add("Heliporto");
        list.add("Laboratório");

        list.remove("Heliporto");

        assertEquals(1, list.getSize());
        assertEquals("Laboratório", list.getElementAt(0));
    }

    @Test
    public void testIsEmpty() {
        LinkedList<String> list = new LinkedList<>();

        assertTrue(list.isEmpty());

        list.add("Heliporto");
        assertFalse(list.isEmpty());
    }
}