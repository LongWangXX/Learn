package com.lw.io;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 目录监控数据提取器
 */
public class DirectoryMonitoring {

    private String directoryPath;
    private String positionMapPath;
    private String fireNameSuffix;
    private String fireNamePrefix;
    private Long monitorTime;


    /**
     * @param directoryPath   //目录路径
     * @param positionMapPath //positionMap位置
     * @param fireNameSuffix  //监控的文件名以fireNameSuffix文件开头的文件
     * @param fireNamePrefix  //监控的文件名以fireNamePrefix结束的文件文件
     */
    public DirectoryMonitoring(String directoryPath, String positionMapPath, String fireNameSuffix, String fireNamePrefix, Long monitorTime) {
        this.directoryPath = directoryPath;
        this.positionMapPath = positionMapPath;
        this.fireNameSuffix = fireNameSuffix;
        this.fireNamePrefix = fireNamePrefix;
        this.monitorTime = monitorTime;
    }

    public void start() {
        IOFileFilter directories = FileFilterUtils.and(
                FileFilterUtils.directoryFileFilter(),
                HiddenFileFilter.VISIBLE);

        IOFileFilter files = null;
        IOFileFilter filter = null;
        //如果该字段不为null那么采集以fileSuffix结尾的数据

        if (fireNameSuffix != null) {
            files = FileFilterUtils.and(
                    FileFilterUtils.fileFileFilter(),
                    FileFilterUtils.suffixFileFilter(fireNameSuffix));
        }
        //如果该字段不为null那么采集以fileSuffix开始的数据
        if (fireNamePrefix != null) {
            if (files != null) {
                files = FileFilterUtils.and(
                        files,
                        FileFilterUtils.prefixFileFilter(fireNamePrefix));
            } else {
                files = FileFilterUtils.and(
                        FileFilterUtils.fileFileFilter(),
                        FileFilterUtils.prefixFileFilter(fireNamePrefix));
            }
        }

        if (files != null) {
            filter = FileFilterUtils.or(directories, files);
        } else {
            filter = FileFilterUtils.or(directories);
        }

        // 使用过滤器
        FileAlterationObserver observer = new FileAlterationObserver(new File(directoryPath), filter);
        observer.addListener(new FileListener(positionMapPath));
        FileAlterationMonitor monitor = new FileAlterationMonitor(1000, observer);
        // 开始监控
        try {
            monitor.start();
            Thread.sleep(monitorTime);
            monitor.stop();
        } catch (
                Exception e) {

            e.printStackTrace();
        }
    }
}

/**
 * 文件监听器
 */
class FileListener extends FileAlterationListenerAdaptor {
    private String positionMapPath;
    private Map<String, Long> positionMap;

    public FileListener(String positionMapPath) {
        this.positionMapPath = positionMapPath;
    }

    // 文件创建
    @Override

    public void onFileCreate(File file) {
        System.out.println("[新建]:" + file.getAbsolutePath());
    }


    // 文件修改

    @Override

    public void onFileChange(File file) {
        File pfile = new File(positionMapPath);
        if (pfile.exists()) {
            //map key是文件名，value是位置
            positionMap = (Map<String, Long>) SerilizeUtil.deSerilize(positionMapPath);
        }
        //如果为空那么自己创建一个
        if (positionMap == null) {
            positionMap = new HashMap<>();
        }
        //获取position文件
        Long position = positionMap.get(file.getAbsolutePath());
        if (position == null) position = 0L;
        RandomAccessFile r = null;
        try {
            //创建流
            r = new RandomAccessFile(file, "r");
            //设置位置到之前读取的位置
            r.seek(position);
            String lineTxt = null;
            //开始读取数据
            while ((lineTxt = r.readLine()) != null) {
                //获取当前指针指向的位置
                position = r.getFilePointer();
                System.out.println(position);
                System.out.println(lineTxt);
                //判断是否有属性要切割
            }

            if (r != null)
                r.close();
            //更新该文件的位置
            positionMap.put(file.getAbsolutePath(), position);
            //再次序列化
            SerilizeUtil.serilize(positionMap, positionMapPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    // 文件删除

    @Override

    public void onFileDelete(File file) {
    }


    // 目录创建

    @Override

    public void onDirectoryCreate(File directory) {

    }


    // 目录修改

    @Override

    public void onDirectoryChange(File directory) {
    }


    // 目录删除

    @Override

    public void onDirectoryDelete(File directory) {

    }

}