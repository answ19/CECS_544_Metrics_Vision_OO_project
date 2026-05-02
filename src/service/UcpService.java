package service;

import model.UcpData;
import model.UcpResult;

public class UcpService {

    // Technical factor weights (13)
    private static final double[] T_WEIGHTS = {
            2, 1, 1, 1, 0.6, 0.5, 0.5, 2, 1, 1, 1, 1, 1
    };

    // Environmental factor weights (8)
    // E1..E8: 1.5,0.5,1,0.5,1,2,-1,-1
    private static final double[] E_WEIGHTS = {
            1.5, 0.5, 1, 0.5, 1, 2, -1, -1
    };

    public int computeUaw(UcpData d) {
        return d.getSimpleActors() * 1
                + d.getAverageActors() * 2
                + d.getComplexActors() * 3;
    }

    public int computeUucw(UcpData d) {
        return d.getSimpleUseCases() * 5
                + d.getAverageUseCases() * 10
                + d.getComplexUseCases() * 15;
    }

    public int computeUucp(UcpData d) {
        return computeUaw(d) + computeUucw(d);
    }

    public double computeTcf(UcpData d) {
        double sum = 0;
        int[] vals = d.getTechnicalFactors();
        for (int i = 0; i < T_WEIGHTS.length; i++) {
            sum += vals[i] * T_WEIGHTS[i];
        }
        return 0.6 + (0.01 * sum);
    }

    public double computeEcf(UcpData d) {
        double sum = 0;
        int[] vals = d.getEnvironmentalFactors();
        for (int i = 0; i < E_WEIGHTS.length; i++) {
            sum += vals[i] * E_WEIGHTS[i];
        }
        return 1.4 + (-0.03 * sum);
    }

    public UcpResult compute(UcpData d) {
        int uaw = computeUaw(d);
        int uucw = computeUucw(d);
        int uucp = uaw + uucw;
        double tcf = computeTcf(d);
        double ecf = computeEcf(d);
        double totalUcp = uucp * tcf * ecf;

        double estimatedHours = totalUcp * d.getProductivityFactor();
        double estimatedLoc = totalUcp * d.getLocPerUcp();
        double estimatedPm = d.getLocPerPm() == 0 ? 0 : estimatedLoc / d.getLocPerPm();

        return new UcpResult(
                uaw, uucw, uucp, tcf, ecf,
                totalUcp, estimatedHours, estimatedLoc, estimatedPm
        );
    }
}