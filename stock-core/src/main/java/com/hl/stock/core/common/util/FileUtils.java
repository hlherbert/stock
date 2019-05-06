package com.hl.stock.core.common.util;

/**
 * 文件工具
 */
public class FileUtils {

    /**
     * 获取resources目录下,资源文件的绝对路径
     *
     * @param filename 文件名
     * @return 文件绝对名
     */
    public static String getResourceFilePath(String filename) {
        return FileUtils.class.getResource(filename).getFile();
    }
}
