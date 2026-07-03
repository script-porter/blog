package com.example.demo.common;

import java.util.concurrent.ThreadLocalRandom;

public class CommonUtils {

    private static final String[] ADJECTIVES = {
            "快乐的", "安静的", "温暖的", "明亮的", "可爱的",
            "勇敢的", "聪明的", "活泼的", "温柔的", "帅气的",
            "酷酷的", "甜甜的", "萌萌的", "呆呆的", "调皮的",
            "认真的", "淡定的", "洒脱的", "文艺的", "热血的"
    };

    private static final String[] NOUNS = {
            "小蜜蜂", "大鲨鱼", "小猫咪", "大熊猫", "小松鼠",
            "小兔子", "小海豚", "小老虎", "小狐狸", "小鹿",
            "小星星", "大太阳", "小月亮", "小花花", "小草草",
            "程序员", "行者", "梦想家", "探险家", "艺术家"
    };

    /**
     * 生成随机中文昵称
     * 格式: 形容词 + 名词 + 随机数字(可选)
     */
    public static String randomNickname() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        String adj = ADJECTIVES[random.nextInt(ADJECTIVES.length)];
        String noun = NOUNS[random.nextInt(NOUNS.length)];
        // 20% 概率追加 2~4 位随机数字，降低重名概率
        if (random.nextInt(100) < 20) {
            int num = random.nextInt(100, 9999);
            return adj + noun + num;
        }
        return adj + noun;
    }
}
