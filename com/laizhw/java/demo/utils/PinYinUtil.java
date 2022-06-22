package demo.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinYinUtil {

    /**
     * 获取全拼
     *
     * @param s (字符串 汉字)
     * @return 汉字转拼音 其它字符不变
     */
    public static String getPinyin(String s) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        char[] chars = s.trim().toCharArray();
        StringBuilder result = new StringBuilder();
        try {
            for (char c : chars) {
                if (Character.toString(c).matches("[\\u4e00-\\u9fa5]")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(c, format);
                    result.append(temp[0]);
                } else {
                    result.append(c);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    /**
     * 获取字符串拼音首字母
     *
     * @param s (字符串 汉字)
     * @return 汉字转拼音 其它字符不变
     */
    public static String getFirstPinyin(String s) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        char[] chars = s.trim().toCharArray();
        StringBuilder result = new StringBuilder();
        try {
            for (char c : chars) {
                if (Character.toString(c).matches("[\\u4e00-\\u9fa5]")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(c, format);
                    result.append(temp[0].charAt(0));
                } else {
                    result.append(c);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }

        return result.toString();
    }
}
