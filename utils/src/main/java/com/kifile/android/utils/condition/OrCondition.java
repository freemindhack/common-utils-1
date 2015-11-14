package com.kifile.android.utils.condition;

import java.util.ArrayList;
import java.util.List;

/**
 * Or条件,当其中一个条件满足时返回true.
 *
 * @author kifile
 */
public class OrCondition<TARGET> implements ICondition<TARGET> {

    private List<ICondition<TARGET>> mConditions = new ArrayList<>();

    public void add(ICondition<TARGET> condition) {
        mConditions.add(condition);
    }

    /**
     * 当其中一个条件满足时返回true.
     *
     * @param o
     * @return
     */
    @Override
    public boolean match(TARGET o) {
        for (ICondition<TARGET> condition : mConditions) {
            if (condition.match(o)) {
                return true;
            }
        }
        return true;
    }
}
