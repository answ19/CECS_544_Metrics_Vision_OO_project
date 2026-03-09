package model;

import java.util.EnumMap;
import java.util.Map;

public class ProjectData {

    private String projectName = "";
    private String creatorName = "";
    private String language = "Java";

    private final Map<FPType, FPEntry> entries = new EnumMap<>(FPType.class);

    // Day 4: store 14 VAF values in the model (defaults to 0 automatically)
    private final int[] vaf = new int[14];

    public ProjectData() {
        for (FPType type : FPType.values()) {
            entries.put(type, new FPEntry());
        }
        // vaf[] is automatically all 0s, no need to set.
    }

    // --- FP Entries ---
    public FPEntry getEntry(FPType type) {
        return entries.get(type);
    }

    // --- VAF Support (Day 4) ---
    public int[] getVaf() {
        return vaf;
    }

    // index must be 0..13, value must be 0..5
    public void setVaf(int index, int value) {
        if (index < 0 || index >= vaf.length) {
            throw new IllegalArgumentException("VAF index must be 0..13");
        }
        if (value < 0 || value > 5) {
            throw new IllegalArgumentException("VAF value must be 0..5");
        }
        vaf[index] = value;
    }

    // Optional helper: replace all VAF values safely (useful when re-opening dialog)
    public void setVafAll(int[] arr) {
        if (arr == null || arr.length != 14) {
            throw new IllegalArgumentException("VAF array must have length 14");
        }
        for (int i = 0; i < 14; i++) {
            setVaf(i, arr[i]); // reuses validation
        }
    }

    // --- Project metadata ---
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = (projectName == null) ? "" : projectName;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = (creatorName == null) ? "" : creatorName;
    }

    // --- Language ---
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = (language == null || language.isBlank()) ? "Java" : language;
    }
}