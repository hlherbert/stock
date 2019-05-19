package com.hl.stock.core.server.task.download;

import java.io.*;

public class Ttt {
    public static void main(String[] a) {
        split();
    }

    public static void split() {
        System.out.println("begin");
        String file = "D:\\mysql-dump\\Dump20190508.sql";
        String distfile = "D:\\musql-dump\\data.txt";
        long bytes = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file));
             BufferedWriter bw = new BufferedWriter(new FileWriter(distfile))) {
            char[] buf = new char[1024 * 1024];
            int len = 0;
            while ((len = br.read(buf)) > 0) {
                for (int i = 0; i < len; i++) {
                    if (buf[i] == '(') {
                        buf[i] = ' ';
                    } else if (i > 0 && buf[i - 1] == ')' && buf[i] == ',') {
                        buf[i - 1] = ';';
                        buf[i] = ' ';
                    }
                }
                bw.write(buf, 0, len);
                bytes += len;
                System.out.println(bytes / 1024 / 1024 + " MB");
            }
            bw.flush();
            bw.close();
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("end");
    }
}
