package model;

public class UcpResult {
    private final int uaw;
    private final int uucw;
    private final int uucp;
    private final double tcf;
    private final double ecf;
    private final double totalUcp;
    private final double estimatedHours;
    private final double estimatedLoc;
    private final double estimatedPm;

    public UcpResult(int uaw, int uucw, int uucp, double tcf, double ecf,
                     double totalUcp, double estimatedHours,
                     double estimatedLoc, double estimatedPm) {
        this.uaw = uaw;
        this.uucw = uucw;
        this.uucp = uucp;
        this.tcf = tcf;
        this.ecf = ecf;
        this.totalUcp = totalUcp;
        this.estimatedHours = estimatedHours;
        this.estimatedLoc = estimatedLoc;
        this.estimatedPm = estimatedPm;
    }

    public int getUaw() { return uaw; }
    public int getUucw() { return uucw; }
    public int getUucp() { return uucp; }
    public double getTcf() { return tcf; }
    public double getEcf() { return ecf; }
    public double getTotalUcp() { return totalUcp; }
    public double getEstimatedHours() { return estimatedHours; }
    public double getEstimatedLoc() { return estimatedLoc; }
    public double getEstimatedPm() { return estimatedPm; }
}

