import Apktool.CommandManager;
import Constants.UtilConfiguration;
import Dict.Dict;
import OutputConfiguration.ConfigurationReader;
import ThreadPool.UrlUtilThreadPool;
import XmlParse.XmlParser;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * author:szf
 * desc:主启动类
 * date:2021/2/19
 */
public class UrlOutputUtil {
    public static void main(String[] args) throws IOException, InterruptedException {
        List<String> params = Arrays.asList(args);
        File middleFiles = new File(UtilConfiguration.MiddleOutDirectory);
        /**
         * 输入参数-in不许为空
         */
        int inputParamIndex = params.indexOf(UtilConfiguration.INPUT_PARAM);
        if (inputParamIndex == -1) {
            System.out.println(UtilConfiguration.PARAM_IN_EMPTY_INFO);
            return;
        }

        /**
         * 验证apk是否存在
         */
        int inputFileIndex = inputParamIndex + 1;
        if (inputFileIndex >= args.length) {
            System.out.println(UtilConfiguration.INPUT_ERROR);
            return;
        }
        String apkName = params.get(inputFileIndex);
        String apkPath = UtilConfiguration.APK_FILEPATH + apkName;
        File apkFile = new File(apkPath);
        if (!apkFile.exists()) {
            System.out.println(UtilConfiguration.INPUT_ERROR);
            return;
        }

        /**
         * 输入参数-conf不许为空
         */
        int confParamIndex = params.indexOf(UtilConfiguration.CONFIGURATION_PARAM);
        if (confParamIndex == -1) {
            System.out.println(UtilConfiguration.PARAM_CONF_EMPTY_INFO);
            return;
        }

        /**
         * 验证配置文件是否存在
         */
        int confFileIndex = confParamIndex + 1;
        if (confFileIndex >= args.length) {
            System.out.println(UtilConfiguration.CONF_ERROR);
            return;
        }
        String confName = params.get(confFileIndex);
        String confPath = UtilConfiguration.CONFIGURATION_PATH + confName;
        Map<String, List<String>> configuration = ConfigurationReader.readConfiguration(confPath);
        if (configuration.size() == 0) {
            System.out.println(UtilConfiguration.CONF_EMPTY_INFO);
            return;
        }

        /**
         * 字典功能是否开启
         */
        boolean isDictOn = params.contains(UtilConfiguration.DICT_PARAM);
        Process process;
        try {
            if (isDictOn) {
                Dict.loadDict(UtilConfiguration.DICT_PATH);
            }
            process = Runtime.getRuntime().exec(CommandManager.getDefaultCommand(apkPath));
            InputStream ins = process.getInputStream();
            InputStream eos = process.getErrorStream();
            new Thread(new InputStreamThread(ins)).start();
            /**
             * 等待反编译执行完成
             */
            process.waitFor();
            XmlParser.parseXml(apkName, configuration, isDictOn);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            UrlUtilThreadPool.getThreadPool().shutdown();
            if (middleFiles.exists()) {
                deleteDir(middleFiles);
                System.out.println(UtilConfiguration.PROGRAM_END_INFO);
            }
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

    static class InputStreamThread implements Runnable {
        private InputStream ins;
        private BufferedReader reader;

        public InputStreamThread(InputStream inputStream) {
            this.ins = inputStream;
            this.reader = new BufferedReader(new InputStreamReader(inputStream));
        }

        @Override
        public void run() {
            try {
                String line;
                while ((line=reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    ins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
