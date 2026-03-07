package service;

import model.*;
import model.FPResult;

public class FPService {
    private static final int[] EI_W = {3,4,6};
    private static final int[] EO_W = {4,5,7};
    private static final int[] EQ_W = {3,4,6};
    private static final int[] ILF_W = {7,10,15};
    private static final int[] EIF_W = {5,7,10};

    private int getWeight(FPType type, Complexity c) {
        int index = switch (c) {
            case SIMPLE -> 0;
            case AVERAGE -> 1;
            case COMPLEX -> 2;
        };

        return switch (type) {
            case EI -> EI_W[index];
            case EO -> EO_W[index];
            case EQ -> EQ_W[index];
            case ILF -> ILF_W[index];
            case EIF -> EIF_W[index];
        };
    }

    public int computeTotal(ProjectData data) {
        int total = 0;

        for (FPType type : FPType.values()) {
            FPEntry entry = data.getEntry(type);
            total += entry.getCount() * getWeight(type, entry.getComplexity());
        }

        return total;
    }

    public int computeVafSum(ProjectData data) {
        int sum = 0;
        for (int v : data.getVaf()) {
            sum += v;
        }
        return sum;
    }

    public double computeFinalFP(ProjectData data) {
        int total = computeTotal(data);
        int vafSum = computeVafSum(data);
        return total * (0.65 + (0.01 * vafSum));
    }

    public FPResult compute(model.ProjectData data) {
        int total = computeTotal(data);
        int vaf = computeVafSum(data);
        double finalFp = computeFinalFP(data);
        return new FPResult(total, vaf, finalFp);
    }

    public double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
