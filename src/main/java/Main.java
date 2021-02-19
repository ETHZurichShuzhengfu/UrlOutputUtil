import Apktool.CommandManager;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * author:szf
 * desc:主启动类
 * date:2021/2/19
 */
public class Main {
    private static final String INPUT_PARAM = "-in";
    private static final String OUTPUT_PARAM = "-out";

    public static void main(String[] args) {
        List<String> params = Arrays.asList(args);
        /**
         * 输入参数-in不许为空
         */
        int inputParamIndex = params.indexOf(INPUT_PARAM);
        if (inputParamIndex == -1) {
            System.out.println("Param -in Must Not Be Null");
            return;
        }
        int inputFileIndex = inputParamIndex + 1;
        String apkPath = params.get(inputFileIndex);

        /**
         * 查询apk是否存在
         */
        File file = new File(apkPath);
        if (!file.exists()) {
            System.out.println("APK Not Found!");
            return;
        }
        Process process;
        try {
            int outputParamIndex = params.indexOf(OUTPUT_PARAM);
            /**
             * 是否指定输出路径
             */
            if (outputParamIndex == -1) {
                System.out.println("输出路径未指定，将默认输出到当前路径");
                process = Runtime.getRuntime().exec(CommandManager.getDefaultCommand(apkPath));
            } else {
                int outputPathIndex = outputParamIndex + 1;
                String outputPath = params.get(outputPathIndex);
                process = Runtime.getRuntime().exec(CommandManager.getCommandWithOutputPath(apkPath, outputPath));
            }
            InputStream ins = process.getInputStream();
            InputStream eos = process.getErrorStream();
            new Thread(new InputStreamThread(ins)).start();

            /**
             * 等待反编译执行完成
             */
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
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
            byte[] b = new byte[1024];
            try {
                while (ins.read(b) != -1) {
                    System.out.println(new String(b));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
