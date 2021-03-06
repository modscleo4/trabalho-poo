package Util;

public class Pair<K, V> {
    private K first;
    private V second;

    public Pair(K first, V second) {
        this.first = first;
        this.second = second;
    }

    public K getFirst() {
        return this.first;
    }

    public void setFirst(K first) {
        if (this.getFirst() != null && this.getFirst().equals(first)) {
            return;
        }

        this.first = first;
    }

    public V getSecond() {
        return this.second;
    }

    public void setSecond(V second) {
        if (this.getSecond() != null && this.getSecond().equals(second)) {
            return;
        }

        this.second = second;
    }

}
