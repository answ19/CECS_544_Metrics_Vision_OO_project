package service;

import model.*;

import java.io.*;

public class FileService {

    public void save(ProjectData data, File file) throws IOException {
        if (!file.getName().toLowerCase().endsWith(".ms")) {
            file = new File(file.getAbsolutePath() + ".ms");
        }
        PrintWriter out = new PrintWriter(new FileWriter(file));

        out.println("projectName=" + data.getProjectName());
        out.println("creatorName=" + data.getCreatorName());
        out.println("language=" + data.getLanguage());

        for (FPType type : FPType.values()) {
            FPEntry entry = data.getEntry(type);
            out.println(type.name() + ".count=" + entry.getCount());
            out.println(type.name() + ".complexity=" + entry.getComplexity().name());
        }

        int[] vaf = data.getVaf();
        for (int i = 0; i < vaf.length; i++) {
            out.println("VAF." + i + "=" + vaf[i]);
        }

        FPService fpService = new FPService();
        int total = fpService.computeTotal(data);
        double fp = fpService.computeFinalFP(data);

        out.println("totalCount=" + total);
        out.println("computedFP=" + fp);

        out.close();
    }

    public ProjectData load(File file) throws IOException {
        ProjectData data = new ProjectData();

        BufferedReader in = new BufferedReader(new FileReader(file));
        String line;

        while ((line = in.readLine()) != null) {
            String[] parts = line.split("=", 2);
            if (parts.length != 2) continue;

            String key = parts[0];
            String value = parts[1];

            switch (key) {
                case "projectName" -> data.setProjectName(value);
                case "creatorName" -> data.setCreatorName(value);
                case "language" -> data.setLanguage(value);
                default -> {
                    if (key.startsWith("VAF.")) {
                        int index = Integer.parseInt(key.substring(4));
                        data.getVaf()[index] = Integer.parseInt(value);
                    } else if (key.endsWith(".count")) {
                        String typeName = key.substring(0, key.indexOf(".count"));
                        FPType type = FPType.valueOf(typeName);
                        data.getEntry(type).setCount(Integer.parseInt(value));
                    } else if (key.endsWith(".complexity")) {
                        String typeName = key.substring(0, key.indexOf(".complexity"));
                        FPType type = FPType.valueOf(typeName);
                        data.getEntry(type).setComplexity(Complexity.valueOf(value));
                    }
                }
            }
        }

        in.close();
        return data;
    }
}