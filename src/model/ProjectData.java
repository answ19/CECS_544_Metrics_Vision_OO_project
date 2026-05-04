package model;

import java.util.EnumMap;
import java.util.Map;


public class ProjectData {
    private String projectName = "";
    private String creatorName = "";
    private String language = "Java";
    private String paneName = "";

    private Map<FPType, FPEntry> entries = new EnumMap<>(FPType.class);

    private int[] vaf = new int[14];
    private String metricType = "FP";
    private UcpData ucpData;

    public String getMetricType() {
        return metricType;
    }


    public void setMetricType(String metricType) {
        this.metricType = metricType;
    }
    public ProjectData() {
        for (FPType type : FPType.values()) {
            entries.put(type, new FPEntry());
        }
    }

    public UcpData getUcpData() {
        return ucpData;
    }

    public void setUcpData(UcpData ucpData) {
        this.ucpData = ucpData;
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
    public String getPaneName() {
        return paneName;
    }

    public void setPaneName(String paneName) {
        this.paneName = paneName;
    }
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

    public void resetEntries() {
        for (FPType type : FPType.values()) {
            entries.get(type).setCount(0);
            entries.get(type).setComplexity(Complexity.AVERAGE);
        }
    }

    public void resetVaf() {
        for (int i = 0; i < vaf.length; i++) {
            vaf[i] = 0;
        }
    }
    private SmiData smiData;

    public SmiData getSmiData() {
        return smiData;
    }

    public void setSmiData(SmiData smiData) {
        this.smiData = smiData;
    }
}