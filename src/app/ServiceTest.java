package app;

import model.*;          // includes ProjectData, FPType, Complexity, FPResult
import service.FPService;

public class ServiceTest {
    public static void main(String[] args) {

        ProjectData data = new ProjectData();

        data.getEntry(FPType.EI).setCount(5);
        data.getEntry(FPType.EI).setComplexity(Complexity.AVERAGE);

        data.setVaf(0, 3);
        data.setVaf(1, 2);

        FPService service = new FPService();


        FPResult r = service.compute(data);

        System.out.println("Total: " + r.getTotalCount());
        System.out.println("VAF: " + r.getVafSum());
        System.out.println("Final: " + r.getFinalFp());

        // System.out.println("Total (old): " + service.computeTotal(data));
        // System.out.println("VAF Sum (old): " + service.computeVafSum(data));
        // System.out.println("Final FP (old): " + service.computeFinalFP(data));
    }
}