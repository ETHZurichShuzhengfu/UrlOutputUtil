package apktool;

/**
 * author:szf
 * desc:apktool命令
 * date:2021/2/19
 */
public class CommandManager {
    public static final String DEFAULT_DECOMPILE_COMMAND = "cmd /c apktool -sf -o out d ";

    //public static final String OUTPUT_PATH_DECOMPILE_COMMAND="cmd /c apktool -sf -o out";
    public static String getDefaultCommand(String inputPath) {
        return DEFAULT_DECOMPILE_COMMAND + inputPath;
    }
}
