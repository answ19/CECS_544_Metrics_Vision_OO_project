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
                if ("FP".equals(data.getMetricType())) {
                    for (FPType type : FPType.values()) {
                        FPEntry entry = data.getEntry(type);
                        out.println(p + type.name() + ".count=" + entry.getCount());
                        out.println(p + type.name() + ".complexity=" + entry.getComplexity().name());
                    }

                    for (int v = 0; v < data.getVaf().length; v++) {
                        out.println(p + "VAF." + v + "=" + data.getVaf()[v]);
                    }
                }
                if ("UCP".equals(data.getMetricType())) {
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
            }
        }
    }

    public List<ProjectData> loadAll(File file) throws IOException {
            List<String> lines = new ArrayList<>();
            int projectCount = 0;

            try (BufferedReader in = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = in.readLine()) != null) {
                    lines.add(line);

                    String[] parts = line.split("=", 2);
                    if (parts.length == 2 && parts[0].equals("projectCount")) {
                        projectCount = Integer.parseInt(parts[1]);
                    }
                }
            }

            List<ProjectData> projects = new ArrayList<>();
            for (int i = 0; i < projectCount; i++) {
                projects.add(new ProjectData());
            }

            for (String line : lines) {
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

                if (index < 0 || index >= projects.size()) continue;

                ProjectData data = projects.get(index);

                switch (field) {
                    case "projectName" -> data.setProjectName(value);
                    case "paneName" -> data.setPaneName(value);
                    case "creatorName" -> data.setCreatorName(value);
                    case "language" -> data.setLanguage(value);
                    case "metricType" -> data.setMetricType(value);
                    default -> {
                        if (field.startsWith("VAF.")) {
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

                        }else if (field.equals("simpleActors")) {
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
                            int tcfindex = Integer.parseInt(field.substring(4));
                            ensureUcpData(data).getTechnicalFactors()[index] = Integer.parseInt(value);
                        } else if (field.startsWith("ECF.")) {
                            int ecfindex = Integer.parseInt(field.substring(4));
                            ensureUcpData(data).getEnvironmentalFactors()[index] = Integer.parseInt(value);
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
    private String safe(String value) {
        return value == null ? "" : value;
    }
}