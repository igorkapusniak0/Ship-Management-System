package LinkedList;

public class List<T> {
    private Node<T> head;
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


    public String listAll(){
        String list = "";
        int index = 0;
        Node<T> current = head;
        while (current != null) {
            list += index + ": "+ current+"\n";
            current = current.next;
            index++;
        }
        return list;
    }

}
