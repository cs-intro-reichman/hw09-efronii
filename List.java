public class List {

    private Node first;
    private int size;

    public List() {
        first = null;
        size = 0;
    }

    public int getSize() {
        return size;
    }

    public CharData getFirst() {
        return first == null ? null : first.cp;
    }

    public void addFirst(char chr) {
        first = new Node(new CharData(chr), first);
        size++;
    }

    public int indexOf(char chr) {
        Node curr = first;
        int index = 0;
        while (curr != null) {
            if (curr.cp.chr == chr) return index;
            curr = curr.next;
            index++;
        }
        return -1;
    }

    public void update(char chr) {
        Node curr = first;
        while (curr != null) {
            if (curr.cp.chr == chr) {
                curr.cp.count++;
                return;
            }
            curr = curr.next;
        }
        addFirst(chr);
    }

    public boolean remove(char chr) {
        if (first == null) return false;

        if (first.cp.chr == chr) {
            first = first.next;
            size--;
            return true;
        }

        Node prev = first;
        Node curr = first.next;

        while (curr != null) {
            if (curr.cp.chr == chr) {
                prev.next = curr.next;
                size--;
                return true;
            }
            prev = curr;
            curr = curr.next;
        }
        return false;
    }

    public CharData get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();

        Node curr = first;
        for (int i = 0; i < index; i++) {
            curr = curr.next;
        }
        return curr.cp;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        Node curr = first;
        while (curr != null) {
            sb.append(curr.cp);
            if (curr.next != null) sb.append(" ");
            curr = curr.next;
        }
        sb.append(")");
        return sb.toString();
    }

    public CharData[] toArray() {
        CharData[] arr = new CharData[size];
        Node curr = first;
        int i = 0;
        while (curr != null) {
            arr[i++] = curr.cp;
            curr = curr.next;
        }
        return arr;
    }

    public ListIterator listIterator(int index) {
        if (size == 0) return null;
        Node curr = first;
        for (int i = 0; i < index; i++) {
            curr = curr.next;
        }
        return new ListIterator(curr);
    }
}
