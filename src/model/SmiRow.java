package model;

public class SmiRow {
    private int totalModules;
    private int addedModules;
    private int changedModules;
    private int deletedModules;
    private double smi;

    public int getTotalModules() { return totalModules; }
    public void setTotalModules(int totalModules) { this.totalModules = totalModules; }

    public int getAddedModules() { return addedModules; }
    public void setAddedModules(int addedModules) { this.addedModules = addedModules; }

    public int getChangedModules() { return changedModules; }
    public void setChangedModules(int changedModules) { this.changedModules = changedModules; }

    public int getDeletedModules() { return deletedModules; }
    public void setDeletedModules(int deletedModules) { this.deletedModules = deletedModules; }

    public double getSmi() { return smi; }
    public void setSmi(double smi) { this.smi = smi; }
}