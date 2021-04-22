package filehandler.FileCopyManager;

import constants.UtilConfiguration;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.List;

/**
 * 用于执行文件拷贝的类
 */
public class FileCopyManager {
    private String directory; //父目录
    private List<String> fileLists; //父目录下的文件路径

    public FileCopyManager() {

    }


    public FileCopyManager(String directory, List<String> fileLists) {
        this.directory = directory;
        this.fileLists = fileLists;
    }

    /**
     * 复制文件
     * @throws IOException
     */
    public void copyFiles() throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        for (String fileName : fileLists) {
            File source = new File(UtilConfiguration.MiddleOutDirectory + "/" + directory + "/" + fileName);
            if (!source.exists()) continue;
            inputChannel = new FileInputStream(source).getChannel();
            File dest = new File(UtilConfiguration.RESULT_PATH + fileName);
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        }
        closeFileChannel(inputChannel);
        closeFileChannel(outputChannel);
    }

    public void closeFileChannel(FileChannel channel) throws IOException {
        if (channel != null && channel.isOpen()) {
            channel.close();
        }
    }


    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public List<String> getFileLists() {
        return fileLists;
    }

    public void setFileLists(List<String> fileLists) {
        this.fileLists = fileLists;
    }
}
