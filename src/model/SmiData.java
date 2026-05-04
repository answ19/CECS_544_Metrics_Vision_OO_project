package model;

import java.util.ArrayList;
import java.util.List;

public class SmiData {
    private String tabName = "SMI";
    private final List<SmiRow> rows = new ArrayList<>();

    public String getTabName() { return tabName; }
    public void setTabName(String tabName) { this.tabName = tabName; }

    public List<SmiRow> getRows() { return rows; }
}