package com.hl.stock.funny;

import com.hl.stock.core.common.util.SoundUtils;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@SpringBootTest
public class FunnyTest {

    @Test
    public void beep() {
        //警报声
        //Toolkit.getDefaultToolkit().beep(); //耳机发声
        //System.out.println("\u0007"); //蜂鸣器发声
        SoundUtils.beep();
    }

    // 处理股票导出的数据，替换符号
    @Test
    public void splitStockData() {
        System.out.println("begin");
        String file = "D:\\MySQLData\\Data\\dump\\data.txt";
        String distfile = "D:\\MySQLData\\Data\\dump\\data_out.txt";
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
                    } else if (buf[i] == '\'') {
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

    public void testFuture() {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        List<Integer> ints = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            ints.set(i, i);
        }

        for (Integer i : ints) {
            CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
                return null;
            });

            futures.add(future);
        }


        // 等待所有任务完成
        CompletableFuture<Void> allResult = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allResult.join();

    }
}
