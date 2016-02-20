package com.awu.kanzhihu.dispatcher;

import com.awu.kanzhihu.actions.Action;
import com.awu.kanzhihu.stores.Store;

import java.util.ArrayList;
import java.util.List;

/**
 * Dispatcher class.
 * Dispatch actions to stores.
 * Created by yoyo on 2016/2/18.
 */
public class Dispatcher {
    private static Dispatcher _instance;
    private List<Store> storeList = new ArrayList<>();

    public static Dispatcher instance() {
        if (_instance == null)
            _instance = new Dispatcher();
        return _instance;
    }

    Dispatcher() {

    }

    /**
     * register store.
     * @param store {@link Store} object.
     */
    public void register(final Store store) {
        if (!storeList.contains(store))
            storeList.add(store);
    }

    /**
     * unregister store.
     * @param store {@link Store} object.
     */
    public void unregister(final Store store) {
        storeList.remove(store);
    }

    /**
     * dispatch action to store.
     * @param action {@link Action} object.
     */
    public void dispatch(Action action) {
        for (Store store : storeList) {
            store.onAction(action);
        }
    }
}
