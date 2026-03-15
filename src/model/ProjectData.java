package model;

import java.util.EnumMap;
import java.util.Map;

public class ProjectData {
    private String projectName = "";
    private String creatorName = "";
    private String language = "Java";

    private Map<FPType, FPEntry> entries = new EnumMap<>(FPType.class);

    private int[] vaf = new int[14];

    public ProjectData() {
        for (FPType type : FPType.values()) {
            entries.put(type, new FPEntry());
        }
    }

    public FPEntry getEntry(FPType type) {
        return entries.get(type);
    }

    public int[] getVaf() {
        return vaf;
    }

    public void setVaf(int index, int value) {
        vaf[index] = value;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    // --------- Add these convenience accessors ---------
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    // optional helper to reset all VAFs to zero
    public void resetVaf() {
        for (int i = 0; i < vaf.length; i++) vaf[i] = 0;
    }

    // optional helper to reset all FP entries to defaults
    public void resetEntries() {
        for (FPType t : FPType.values()) {
            FPEntry e = entries.get(t);
            if (e != null) {
                e.setCount(0);
                e.setComplexity(Complexity.AVERAGE);
            } else {
                entries.put(t, new FPEntry());
            }
        }
    }
}