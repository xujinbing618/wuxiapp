 
package com.magus.magusutils;


/**
 * 数据格式验证类
 * @author 徐健君
 */
public class Validator {
	 
    public static final String PATTERN_ALPHA_NUM="[a-z0-9A-Z]+";
    public static final String PATTERN_EMAIL="([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)";
    public static final String PATTERN_PHONE_NO="\\d+\\-?\\d+";

    /**
     * input完全匹配模式regExp
     * @param input
     * @param regExp
     * @return
     */
    public static boolean isMatches(String input, String regExp){
        if(input==null){
            return false;
        }
        return input.matches(regExp);
    }
    /**
     * 是否是电话号码
     * @param input
     * @return
     */
    public static boolean isPhoneNo(String input) {
        return isMatches(input, PATTERN_PHONE_NO);
    }
    /**
     * 是否是字母或数字
     * @param input
     * @return
     */
    public static boolean isAlphaNumber(String input){
        return isMatches(input, PATTERN_ALPHA_NUM);
    }


    /**
     * 是否是email格式
     * @param input
     * @return
     */
    public static boolean isEmail(String input){
        return isMatches(input, PATTERN_EMAIL);
    }

    /**
     * 是否为空。input会被自动trim
     * @param input
     * @return
     */
    public static boolean isEmpty(String input){
        return isEmptyNotTrim(input==null?input:input.trim());
    }
    /**
     * 是否为空。input会被自动trim
     * @param input
     * @return
     */
    public static boolean isEmptyNotTrim(String input){
        if(input == null || "".equals(input)){
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String str){
        return !Validator.isEmpty(str);
    }
}

