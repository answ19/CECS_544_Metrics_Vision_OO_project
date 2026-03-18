package service;

public class CodeSizeService {

    public int computeCodeSize(String language, double fpValue) {
        double factor = switch (language) {
            case "Java" -> 53;
            case "C++" -> 55;
            case "C#" -> 58;
            case "Python" -> 42;
            case "Ruby" -> 50;
            case "Objective C" -> 60;
            default -> 50;
        };

        return (int) Math.round(fpValue * factor);
    }
}
