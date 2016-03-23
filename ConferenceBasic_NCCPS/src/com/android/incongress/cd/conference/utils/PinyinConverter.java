package com.android.incongress.cd.conference.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 中文转拼音,不显示音标，不需要声调，全部大写，方便保存数据库，进行后续查询使用
 *
 * @author Administrator
 */
public class PinyinConverter {
    /**
     * @param str
     *            中文
     *
     * @return 中文拼音，不是中文的则以原字符输出
     * 
     * Jacky_Chen 乱码被判断为中文，为出现异常，因此try{}catch中进行了返回
     */
    public static String getPinyin(String str) {
        HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
        // 不需要声调
        outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        // 遇到“ü” 显示成V
        outputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
        // 所有输出小写
        outputFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);

        char[] charArray = str.toCharArray();
        String result = "";
        for (int i = 0; i < charArray.length; i++) {
            try {
                if (isCn(charArray[i])) {
                    // 返回的是数组，因为会有多音字
                    String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(
                            charArray[i], outputFormat);
                    result += pinyinArray[0];
                } else {
                    //  字符不是中文则直接放入结果中
                    result += charArray[i];
                }
            } catch (Exception e) {
                // can not happen
                e.printStackTrace();
                return "";
            }
        }
        return result;
    }

    /**
     * @param str
     *            待验证字符串
     *
     * @return true if is cn, false if not cn
     *
     */
    public static boolean isCn(char ch) {
        Pattern p=Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m=p.matcher(String.valueOf(ch));
        if(m.matches()){
         return true;
        }
        return false;
    }



}
