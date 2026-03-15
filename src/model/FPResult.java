
package model;

public class FPResult {
    private final int totalCount;
    private final int vafSum;
    private final double finalFp;

    public FPResult(int totalCount, int vafSum, double finalFp) {
        this.totalCount = totalCount;
        this.vafSum = vafSum;
        this.finalFp = finalFp;
    }

    public int getTotalCount() { return totalCount; }
    public int getVafSum() { return vafSum; }
    public double getFinalFp() { return finalFp; }
}