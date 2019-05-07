package com.hl.stock.funny;

import com.hl.stock.core.common.util.SoundUtils;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FunnyTest {

    @Test
    public void beep() {
        //警报声
        //Toolkit.getDefaultToolkit().beep(); //耳机发声
        //System.out.println("\u0007"); //蜂鸣器发声
        SoundUtils.beep();
    }
}
