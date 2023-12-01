package airhacks;

import java.io.IOException;
import java.nio.file.Files;

public interface FunctionZip {
    
    public static String createEmptyFunctionZip() throws IOException{
        var path = Files.createTempDirectory("function");
        var functionZip = path.resolve("function.zip");
        var existingFile = Files.createFile(functionZip);
        System.out.println("existingFile = " + existingFile);
        return existingFile.toString();
    }
}
