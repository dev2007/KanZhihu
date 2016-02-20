package com.awu.kanzhihu.actions;

/**
 * Action POJO class.
 * Created by yoyo on 2016/2/18.
 */
public class Action<T> {
    private final String type;
    private final T data;

    Action(String type,T data){
        this.type = type;
        this.data = data;
    }

    /**
     * get action's {@link #type}.
     * @return action's type.
     */
    public String getType(){
        return type;
    }

    /**
     * get action's {@link #data}
     * @return action's data.
     */
    public T getData(){
        return data;
    }
}
