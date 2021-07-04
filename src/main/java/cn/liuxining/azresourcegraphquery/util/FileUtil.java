package cn.liuxining.azresourcegraphquery.util;

import java.io.*;

/**
 * <p>Title: FileUtil </p >
 * <p>Description: FileUtil </p >
 *
 * @author: liuxining
 * @date: 2021/7/4 17:15
 * @version: v1.0
 */
public class FileUtil {

    public static String readFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            throw new RuntimeException(filename + "文件不存在");
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            StringBuilder content = new StringBuilder();
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                content.append(line);
            }
            bufferedReader.close();
            return content.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
