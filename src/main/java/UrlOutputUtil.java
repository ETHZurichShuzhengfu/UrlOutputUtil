import apktool.CommandManager;
import constants.UtilConfiguration;
import dict.Dict;
import configuration.ConfigurationReader;
import threadpool.UrlUtilThreadPool;
import filehandler.ConfigurationHandler;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * author:szf
 * desc:主启动类
 * date:2021/2/19
 */
public class UrlOutputUtil {
    public static void main(String[] args) throws IOException {
        List<String> params = Arrays.asList(args);
        File middleFiles = new File(UtilConfiguration.MiddleOutDirectory);

        /**
         * 验证apk是否存在
         */
        String apkName = params.get(0);
        String apkPath = UtilConfiguration.APK_FILEPATH + apkName;
        File apkFile = new File(apkPath);
        if (!apkFile.exists()) {
            System.out.println(UtilConfiguration.INPUT_ERROR);
            return;
        }

        /**
         * 验证配置文件是否存在
         */
        String confName = params.get(1);
        String confPath = UtilConfiguration.CONFIGURATION_PATH + confName;
        Map<String, List<String>> configuration = ConfigurationReader.readConfiguration(confPath);
        if (configuration.size() == 0) {
            System.out.println(UtilConfiguration.CONF_EMPTY_INFO);
            return;
        }

        /**
         * 字典功能是否开启
         */
        boolean isDictOn = params.size()==3;
        Process process;
        try {
            if (isDictOn) {
                Dict.loadDict(UtilConfiguration.DICT_PATH);
            }
//            process = Runtime.getRuntime().exec(CommandManager.getDefaultCommand(apkPath));
//            InputStream ins = process.getInputStream();
//            InputStream eos = process.getErrorStream();
//            new Thread(new InputStreamThread(ins)).start();
//            /**
//             * 等待反编译执行完成
//             */
//            process.waitFor();
            File file=new File(UtilConfiguration.RESULT_PATH);
            if(!file.exists())
                file.mkdir();
            ConfigurationHandler.execute(apkName, configuration, isDictOn);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            UrlUtilThreadPool.getThreadPool().shutdown();
//            if (middleFiles.exists()) {
//                deleteDir(middleFiles);
//                System.out.println(UtilConfiguration.PROGRAM_END_INFO);
//            }
        }
    }

    /**
     * 删除中间目录
     */
    public static void deleteDir(File file) {
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile != null) {
                for (File f : childFile) {
                    deleteDir(f);
                }
            }
        }
        file.delete();
    }
}
