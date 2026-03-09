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
}
