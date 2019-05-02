package com.hl.stock.core.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 种子url
 */
public class InputSeedData {
    private static Logger logger = LoggerFactory.getLogger(InputSeedData.class);
    protected static InputSeedData instance = new InputSeedData();
    private List<String> seeds;

    public static InputSeedData getInstance() {
        return instance;
    }

    protected InputSeedData() {
        load();
    }

    private void load() {
        InputStreamReader ir = null;
        BufferedReader br = null;
        try {
            InputStream in = new FileInputStream(new File(System.getProperty("user.dir") + File.separator + "conf" + File.separator + "input_seed.txt"));
            ir = new InputStreamReader(in);
            br = new BufferedReader(ir);
            seeds = br.lines().filter(s -> !s.startsWith("#")).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("请在当前目录下创建并配置conf/input_seed.txt文件.", e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<String> getSeeds() {
        return seeds;
    }

    public void setSeeds(List<String> seeds) {
        this.seeds = seeds;
    }
}
