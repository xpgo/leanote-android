package org.houxg.leamonax.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static String notNullStr(String str) {
        return str == null ? "" : str;
    }

    public static void find(String content, String tagExp, String targetExp, Finder finder, Object... extraData) {
        Pattern tagPattern = Pattern.compile(tagExp);
        Pattern targetPattern = Pattern.compile(targetExp);
        Matcher tagMather = tagPattern.matcher(content);
        while (tagMather.find()) {
            String tag = tagMather.group();
            Matcher targetMatcher = targetPattern.matcher(tag);
            if (!targetMatcher.find()) {
                continue;
            }
            String original = targetMatcher.group();
            finder.onFound(original);
        }
    }

    public static String replace(String content, String tagExp, String targetExp, Replacer replacer, Object... extraData) {
        Pattern tagPattern = Pattern.compile(tagExp);
        Pattern targetPattern = Pattern.compile(targetExp);
        Matcher tagMather = tagPattern.matcher(content);
        StringBuilder contentBuilder = new StringBuilder(content);
        int offset = 0;
        while (tagMather.find()) {
            String tag = tagMather.group();
            Matcher targetMatcher = targetPattern.matcher(tag);
            if (!targetMatcher.find()) {
                continue;
            }
            String original = targetMatcher.group();
            int originalLen = original.length();
            String modified = replacer.replaceWith(original, extraData);
            contentBuilder.replace(tagMather.start() + targetMatcher.start() + offset,
                    tagMather.end() - (tag.length() - targetMatcher.end()) + offset,
                    modified);
            offset += modified.length() - originalLen;
        }
        return contentBuilder.toString();
    }

    public interface Finder {
        void onFound(String original, Object... extraData);
    }

    public interface Replacer {
        String replaceWith(String original, Object... extraData);
    }

    //转义正则表达式中的几个特殊符号
    public static String escapeRegex(String str) {
        int length = str.length();
        StringBuilder sbf = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = str.charAt(i);
            if (c == '*') {
                sbf.append('\\').append(c);
            } else if (c == '+') {
                sbf.append('\\').append(c);
            } else if (c == '?') {
                sbf.append('\\').append(c);
            }
        }
        return sbf.toString();
    }
}
