package service;

import model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileService {

    public void saveAll(List<ProjectData> projects, File file) throws IOException {
        if (!file.getName().toLowerCase().endsWith(".ms")) {
            file = new File(file.getAbsolutePath() + ".ms");
        }

        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            out.println("projectCount=" + projects.size());

            for (int i = 0; i < projects.size(); i++) {
                ProjectData data = projects.get(i);
                String p = "project." + i + ".";

                out.println(p + "projectName=" + safe(data.getProjectName()));
                out.println(p + "paneName=" + safe(data.getPaneName()));
                out.println(p + "creatorName=" + safe(data.getCreatorName()));
                out.println(p + "language=" + safe(data.getLanguage()));
                out.println(p + "metricType=" + safe(data.getMetricType()));

                if ("FP".equalsIgnoreCase(data.getMetricType())) {
                    for (FPType type : FPType.values()) {
                        FPEntry entry = data.getEntry(type);
                        out.println(p + type.name() + ".count=" + entry.getCount());
                        out.println(p + type.name() + ".complexity=" + entry.getComplexity().name());
                    }

                    for (int v = 0; v < data.getVaf().length; v++) {
                        out.println(p + "VAF." + v + "=" + data.getVaf()[v]);
                    }
                }

                if ("UCP".equalsIgnoreCase(data.getMetricType())) {
                    UcpData ucp = data.getUcpData();

                    out.println(p + "simpleActors=" + ucp.getSimpleActors());
                    out.println(p + "averageActors=" + ucp.getAverageActors());
                    out.println(p + "complexActors=" + ucp.getComplexActors());

                    out.println(p + "simpleUseCases=" + ucp.getSimpleUseCases());
                    out.println(p + "averageUseCases=" + ucp.getAverageUseCases());
                    out.println(p + "complexUseCases=" + ucp.getComplexUseCases());

                    out.println(p + "productivityFactor=" + ucp.getProductivityFactor());
                    out.println(p + "locPerPm=" + ucp.getLocPerPm());
                    out.println(p + "locPerUcp=" + ucp.getLocPerUcp());

                    for (int t = 0; t < ucp.getTechnicalFactors().length; t++) {
                        out.println(p + "TCF." + t + "=" + ucp.getTechnicalFactors()[t]);
                    }

                    for (int e = 0; e < ucp.getEnvironmentalFactors().length; e++) {
                        out.println(p + "ECF." + e + "=" + ucp.getEnvironmentalFactors()[e]);
                    }
                }

                if ("SMI".equalsIgnoreCase(data.getMetricType())) {
                    SmiData smi = data.getSmiData();

                    if (smi != null) {
                        out.println(p + "SMI.rowCount=" + smi.getRows().size());

                        for (int r = 0; r < smi.getRows().size(); r++) {
                            SmiRow row = smi.getRows().get(r);

                            out.println(p + "SMI." + r + ".totalModules=" + row.getTotalModules());
                            out.println(p + "SMI." + r + ".addedModules=" + row.getAddedModules());
                            out.println(p + "SMI." + r + ".changedModules=" + row.getChangedModules());
                            out.println(p + "SMI." + r + ".deletedModules=" + row.getDeletedModules());
                            out.println(p + "SMI." + r + ".smi=" + row.getSmi());
                        }
                    } else {
                        out.println(p + "SMI.rowCount=0");
                    }
                }
            }
        }
    }

    public List<ProjectData> loadAll(File file) throws IOException {
        List<ProjectData> projects = new ArrayList<>();

        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = in.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length != 2) continue;

                String key = parts[0];
                String value = parts[1];

                if (!key.startsWith("project.")) continue;

                String rest = key.substring("project.".length());
                int dot = rest.indexOf(".");
                if (dot < 0) continue;

                int index = Integer.parseInt(rest.substring(0, dot));
                String field = rest.substring(dot + 1);

                while (projects.size() <= index) {
                    projects.add(new ProjectData());
                }

                ProjectData data = projects.get(index);

                switch (field) {
                    case "projectName" -> data.setProjectName(value);
                    case "paneName" -> data.setPaneName(value);
                    case "creatorName" -> data.setCreatorName(value);
                    case "language" -> data.setLanguage(value);
                    case "metricType" -> data.setMetricType(value);

                    default -> {
                        if (field.startsWith("SMI.")) {
                            SmiData smiData = ensureSmiData(data);

                            if (field.equals("SMI.rowCount")) {
                                int count = Integer.parseInt(value);

                                while (smiData.getRows().size() < count) {
                                    smiData.getRows().add(new SmiRow());
                                }

                            } else {
                                String smiRest = field.substring("SMI.".length());
                                String[] smiParts = smiRest.split("\\.", 2);

                                if (smiParts.length == 2) {
                                    int rowIndex = Integer.parseInt(smiParts[0]);
                                    String smiField = smiParts[1];

                                    while (smiData.getRows().size() <= rowIndex) {
                                        smiData.getRows().add(new SmiRow());
                                    }

                                    SmiRow row = smiData.getRows().get(rowIndex);

                                    if (smiField.equals("totalModules")) {
                                        row.setTotalModules(Integer.parseInt(value));
                                    } else if (smiField.equals("addedModules")) {
                                        row.setAddedModules(Integer.parseInt(value));
                                    } else if (smiField.equals("changedModules")) {
                                        row.setChangedModules(Integer.parseInt(value));
                                    } else if (smiField.equals("deletedModules")) {
                                        row.setDeletedModules(Integer.parseInt(value));
                                    } else if (smiField.equals("smi")) {
                                        row.setSmi(Double.parseDouble(value));
                                    }
                                }
                            }

                        } else if (field.startsWith("VAF.")) {
                            int vafIndex = Integer.parseInt(field.substring(4));
                            data.getVaf()[vafIndex] = Integer.parseInt(value);

                        } else if (field.endsWith(".count")) {
                            String typeName = field.substring(0, field.indexOf(".count"));
                            FPType type = FPType.valueOf(typeName);
                            data.getEntry(type).setCount(Integer.parseInt(value));

                        } else if (field.endsWith(".complexity")) {
                            String typeName = field.substring(0, field.indexOf(".complexity"));
                            FPType type = FPType.valueOf(typeName);
                            data.getEntry(type).setComplexity(Complexity.valueOf(value));

                        } else if (field.equals("simpleActors")) {
                            ensureUcpData(data).setSimpleActors(Integer.parseInt(value));
                        } else if (field.equals("averageActors")) {
                            ensureUcpData(data).setAverageActors(Integer.parseInt(value));
                        } else if (field.equals("complexActors")) {
                            ensureUcpData(data).setComplexActors(Integer.parseInt(value));
                        } else if (field.equals("simpleUseCases")) {
                            ensureUcpData(data).setSimpleUseCases(Integer.parseInt(value));
                        } else if (field.equals("averageUseCases")) {
                            ensureUcpData(data).setAverageUseCases(Integer.parseInt(value));
                        } else if (field.equals("complexUseCases")) {
                            ensureUcpData(data).setComplexUseCases(Integer.parseInt(value));
                        } else if (field.equals("productivityFactor")) {
                            ensureUcpData(data).setProductivityFactor(Double.parseDouble(value));
                        } else if (field.equals("locPerPm")) {
                            ensureUcpData(data).setLocPerPm(Double.parseDouble(value));
                        } else if (field.equals("locPerUcp")) {
                            ensureUcpData(data).setLocPerUcp(Double.parseDouble(value));
                        } else if (field.startsWith("TCF.")) {
                            int tcfIndex = Integer.parseInt(field.substring(4));
                            ensureUcpData(data).getTechnicalFactors()[tcfIndex] = Integer.parseInt(value);
                        } else if (field.startsWith("ECF.")) {
                            int ecfIndex = Integer.parseInt(field.substring(4));
                            ensureUcpData(data).getEnvironmentalFactors()[ecfIndex] = Integer.parseInt(value);
                        }
                    }
                }
            }
        }

        return projects;
    }

        public void save(ProjectData data, File file) throws IOException {
        List<ProjectData> list = new ArrayList<>();
        list.add(data);
        saveAll(list, file);
    }

    public ProjectData load(File file) throws IOException {
        List<ProjectData> list = loadAll(file);
        return list.isEmpty() ? new ProjectData() : list.get(0);
    }
    private UcpData ensureUcpData(ProjectData data) {
        if (data.getUcpData() == null) {
            data.setUcpData(new UcpData());
        }
        return data.getUcpData();
    }
    private SmiData ensureSmiData(ProjectData data) {
        if (data.getSmiData() == null) {
            data.setSmiData(new SmiData());
        }
        return data.getSmiData();
    }
    private String safe(String value) {
        return value == null ? "" : value;
    }
}