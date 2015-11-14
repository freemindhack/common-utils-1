package com.kifile.android.utils.condition;

import java.util.regex.Pattern;

/**
 * 字符正则表达式条件.
 *
 * @author kifile
 */
public class RegexStringCondition implements ICondition<String> {

    private final Pattern mPatter;

    public RegexStringCondition(String regex) {
        mPatter = Pattern.compile(regex);
    }

    @Override
    public boolean match(String s) {
        return mPatter.matcher(s).matches();
    }
}
