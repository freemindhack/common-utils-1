package com.kifile.android.utils.condition;

import java.util.ArrayList;
import java.util.List;

/**
 * And条件,需要所有条件都满足时才返回true.
 *
 * @author kifile
 */
public class AndCondition<TARGET> implements ICondition<TARGET> {

    private List<ICondition<TARGET>> mConditions = new ArrayList<>();

    public void add(ICondition<TARGET> condition) {
        mConditions.add(condition);
    }

    /**
     * 当所有条件都满足时,返回true.
     *
     * @param o
     * @return
     */
    @Override
    public boolean match(TARGET o) {
        for (ICondition<TARGET> condition : mConditions) {
            if (!condition.match(o)) {
                return false;
            }
        }
        return true;
    }
}
