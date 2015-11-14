package com.kifile.android.utils.condition;

/**
 * Created by kifile on 15/11/14.
 */
public interface ICondition<OBJECT> {

    boolean match(OBJECT object);
}
