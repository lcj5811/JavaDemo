package com.lee;

import java.util.ArrayList;
import java.util.List;
/**
 * 利用数字二进制遍历 实现java全组合算法
 * @author 郭文梁
 *
 */
public class fullComb {
    public static String[] source = { "1", "2", "3" };//源数据
    public static int len = source.length;    //源数据大小
    public static int blen = 1 << len;    //全组合数量为2的len次方

    public static void main(String[] args) {
        fullComb();
    }
    /**
     * 计算全组合
     */
    public static void fullComb() {
        for (int i = 0; i < blen; i++) {    //遍历所有对应的二进制数字
            System.out.println(getComb(source, i)); //打印当前数字对应的组合
        }
    }
    /**
     * 遍历数字 计算出对应的组合
     * @param source
     * @param index
     * @return
     */
    public static List<String> getComb(String[] source, int index) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < len; i++) {    //逐个遍历为禁止的位
            int tmp = index << i;
            if ((tmp & blen>>1) != 0) {    //遇到1就将数据加入组合中
                list.add(source[i]);
            }
        }
        return list;
    }
}
