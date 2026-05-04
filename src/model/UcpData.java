package model;

public class UcpData {

    // actors
    private int simpleActors = 0;
    private int averageActors = 0;
    private int complexActors = 0;

    // use cases
    private int simpleUseCases = 0;
    private int averageUseCases = 0;
    private int complexUseCases = 0;

    // factors
    private final int[] technicalFactors = new int[13];
    private final int[] environmentalFactors = new int[8];

    // editable defaults required by assignment
    private double productivityFactor = 20.0;
    private double locPerPm = 700.0;
    private double locPerUcp = 120.0;

    private String tabName = "UCP";

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public int getSimpleActors() { return simpleActors; }
    public void setSimpleActors(int v) { simpleActors = v; }

    public int getAverageActors() { return averageActors; }
    public void setAverageActors(int v) { averageActors = v; }

    public int getComplexActors() { return complexActors; }
    public void setComplexActors(int v) { complexActors = v; }

    public int getSimpleUseCases() { return simpleUseCases; }
    public void setSimpleUseCases(int v) { simpleUseCases = v; }

    public int getAverageUseCases() { return averageUseCases; }
    public void setAverageUseCases(int v) { averageUseCases = v; }

    public int getComplexUseCases() { return complexUseCases; }
    public void setComplexUseCases(int v) { complexUseCases = v; }

    public int[] getTechnicalFactors() { return technicalFactors; }
    public int[] getEnvironmentalFactors() { return environmentalFactors; }

    public double getProductivityFactor() { return productivityFactor; }
    public void setProductivityFactor(double v) { productivityFactor = v; }

    public double getLocPerPm() { return locPerPm; }
    public void setLocPerPm(double v) { locPerPm = v; }

    public double getLocPerUcp() { return locPerUcp; }
    public void setLocPerUcp(double v) { locPerUcp = v; }
}
