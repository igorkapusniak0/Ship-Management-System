package LinkedList;

import models.Container;

import java.io.Serializable;

public class List<T> implements Serializable {
    public Node<T> head;
    public List(){
        this.head = null;
    }
    public void add(T data){
        Node<T> newNode = new Node<>(data);

        if (head == null){
            head = newNode;
        }
        else {
            Node<T> current = head;
            while (current.next != null){
                current=current.next;
            }
            current.next = newNode;
        }
    }

    public void remove(T data) {
        if (head == null){
            return;
        }

        if (head.data.equals(data)) {
            head=head.next;
            return;
        }
        Node<T> current = head;
        Node<T> previous = null;

        while (current != null && !current.data.equals(data)){
            previous = current;
            current = current.next;
        }
        if (current == null){
            return;
        }
        previous.next=current.next;
    }


    public String display() {
        String show = "";
        Node<T> current = head;
        while (current != null) {
            show += current.next + "\n";
            current = current.next;
        }
        return show;
    }
    public String getDataAtIndex(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index cannot be negative: " + index);
        }
        Node<T> current = head;
        int i = 0;
        while (current != null && i < index) {
            current = current.next;
            i++;
        }
        if (current == null) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }

        if (current.data == null) {
            return null;
        }

        return current.data.toString();
    }

    public boolean isEmpty(){
        return head ==null;
    }
    public boolean contains(T data) {
        Node<T> current = head;
        while (current != null) {
            if (current.data.equals(data)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }
    public int listSize(){
        int size = 0;
        Node<T> current = head;
        while (current != null){
            size+=1;
        }
        return size;
    }
    public Container getContainer(Container container){
        Node<T> current = head;
        while (current!=null){
            if (current.equals(container)){
                return container;
            }
            current=current.next;
        }
        return null;
    }



}
