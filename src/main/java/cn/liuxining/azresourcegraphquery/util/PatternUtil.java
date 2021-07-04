package cn.liuxining.azresourcegraphquery.util;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Title: PatternUtil </p >
 * <p>Description: PatternUtil </p >
 *
 * @author: liuxining
 * @date: 2021/7/4 01:28
 * @version: v1.0
 */
public class PatternUtil {

    public static List<String> getStrList(String str, String regex, int resultCount) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        List<String> result = null;
        if (matcher.find()) {
            result = new ArrayList<>(resultCount);
            for (int i = 0;i < resultCount;i++) {
                result.add(matcher.group(i + 1));
            }
        }

        return result;
    }

}
