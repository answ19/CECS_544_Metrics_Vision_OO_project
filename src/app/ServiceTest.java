package app;



import model.*;

import service.FileService;



import java.io.File;



public class ServiceTest {

    public static void main(String[] args) throws Exception {

        ProjectData d = new ProjectData();



        d.setProjectName("DemoProject");

        d.setCreatorName("Team");

        d.setLanguage("Python");



        d.getEntry(FPType.EI).setCount(5);

        d.getEntry(FPType.EI).setComplexity(Complexity.COMPLEX);



        d.getEntry(FPType.EO).setCount(3);

        d.getEntry(FPType.EO).setComplexity(Complexity.SIMPLE);



        d.getVaf()[0] = 4;

        d.getVaf()[1] = 2;



        FileService fs = new FileService();

        File file = new File("test.ms");



        fs.save(d, file);



        ProjectData loaded = fs.load(file);



        System.out.println("Project Name: " + loaded.getProjectName());

        System.out.println("Creator: " + loaded.getCreatorName());

        System.out.println("Language: " + loaded.getLanguage());

        System.out.println("EI Count: " + loaded.getEntry(FPType.EI).getCount());

        System.out.println("EI Complexity: " + loaded.getEntry(FPType.EI).getComplexity());

        System.out.println("VAF[0]: " + loaded.getVaf()[0]);

        System.out.println("VAF[1]: " + loaded.getVaf()[1]);

    }

}
