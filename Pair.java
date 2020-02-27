package bgu.spl.mics;

public class Pair<T, F> {
    private T first;
    private F second;

    public Object getFirst() {
        return first;
    }

    public Object getSecond() {
        return second;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public void setSecond(F second) {
        this.second = second;
    }

    public Pair(T first, F second) {
        this.first = first;
        this.second = second;
    }
}
