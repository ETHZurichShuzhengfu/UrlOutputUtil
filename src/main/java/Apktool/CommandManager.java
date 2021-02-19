package Apktool;

public class CommandManager {
    public static final String DEFAULT_DECOMPILE_COMMAND="cmd /c apktool -sf d ";
    public static final String OUTPUT_PATH_DECOMPILE_COMMAND="cmd /c apktool -sf -o ";
    public static String getCommandWithOutputPath(String inputPath,String outputPath){
        return OUTPUT_PATH_DECOMPILE_COMMAND+outputPath+" d "+inputPath;
    }

    public static String getDefaultCommand(String inputPath){
        return DEFAULT_DECOMPILE_COMMAND+inputPath;
    }
}
