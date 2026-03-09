package model;

public class FPEntry {
    private int count;
    private Complexity complexity;

    public FPEntry() {
        this.count = 0;
        this.complexity = Complexity.AVERAGE;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Complexity getComplexity() {
        return complexity;
    }

    public void setComplexity(Complexity complexity) {
        this.complexity = complexity;
    }
}
