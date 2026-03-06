package app;

import model.*;
import service.FPService;

public class ServiceTest {
    public static void main(String[] args) {

        ProjectData data = new ProjectData();

        data.getEntry(FPType.EI).setCount(5);
        data.getEntry(FPType.EI).setComplexity(Complexity.AVERAGE);

        data.setVaf(0, 3);
        data.setVaf(1, 2);

        FPService service = new FPService();

        System.out.println("Total: " + service.computeTotal(data));
        System.out.println("VAF Sum: " + service.computeVafSum(data));
        System.out.println("Final FP: " + service.computeFinalFP(data));
    }
}
