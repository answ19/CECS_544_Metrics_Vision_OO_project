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

            for (int p = 0; p < projects.size(); p++) {
                ProjectData data = projects.get(p);
                String prefix = "project." + p + ".";

                out.println(prefix + "projectName=" + data.getProjectName());
                out.println(prefix + "creatorName=" + data.getCreatorName());
                out.println(prefix + "language=" + data.getLanguage());

                for (FPType type : FPType.values()) {
                    FPEntry entry = data.getEntry(type);
                    out.println(prefix + type.name() + ".count=" + entry.getCount());
                    out.println(prefix + type.name() + ".complexity=" + entry.getComplexity().name());
                }

                int[] vaf = data.getVaf();
                for (int i = 0; i < vaf.length; i++) {
                    out.println(prefix + "VAF." + i + "=" + vaf[i]);
                }

                FPService fpService = new FPService();
                int total = fpService.computeTotal(data);
                double fp = fpService.computeFinalFP(data);

                out.println(prefix + "totalCount=" + total);
                out.println(prefix + "computedFP=" + fp);
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

                if (!key.startsWith("project.")) {
                    continue;
                }

                String rest = key.substring("project.".length());
                int dot = rest.indexOf('.');
                if (dot < 0) continue;

                int projectIndex = Integer.parseInt(rest.substring(0, dot));
                String fieldKey = rest.substring(dot + 1);

                while (projects.size() <= projectIndex) {
                    projects.add(new ProjectData());
                }

                ProjectData data = projects.get(projectIndex);

                switch (fieldKey) {
                    case "projectName" -> data.setProjectName(value);
                    case "creatorName" -> data.setCreatorName(value);
                    case "language" -> data.setLanguage(value);
                    default -> {
                        if (fieldKey.startsWith("VAF.")) {
                            int index = Integer.parseInt(fieldKey.substring(4));
                            data.getVaf()[index] = Integer.parseInt(value);
                        } else if (fieldKey.endsWith(".count")) {
                            String typeName = fieldKey.substring(0, fieldKey.indexOf(".count"));
                            FPType type = FPType.valueOf(typeName);
                            data.getEntry(type).setCount(Integer.parseInt(value));
                        } else if (fieldKey.endsWith(".complexity")) {
                            String typeName = fieldKey.substring(0, fieldKey.indexOf(".complexity"));
                            FPType type = FPType.valueOf(typeName);
                            data.getEntry(type).setComplexity(Complexity.valueOf(value));
                        }
                    }
                }
            }
        }

        return projects;
    }

    // Backward compatibility: save/load one project if older code still calls these
    public void save(ProjectData data, File file) throws IOException {
        List<ProjectData> one = new ArrayList<>();
        one.add(data);
        saveAll(one, file);
    }

    public ProjectData load(File file) throws IOException {
        List<ProjectData> projects = loadAll(file);
        if (projects.isEmpty()) {
            return new ProjectData();
        }
        return projects.get(0);
    }
}