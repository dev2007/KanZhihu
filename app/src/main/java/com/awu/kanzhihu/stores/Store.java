package com.awu.kanzhihu.stores;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.awu.kanzhihu.actions.Action;
import com.awu.kanzhihu.app.KZHApp;
import com.squareup.otto.Bus;

import awu.com.awutil.LogUtil;

/**
 * Abstract Store class.
 * Created by yoyo on 2016/2/18.
 */
public abstract class Store {
    private static Bus bus = new Bus();
    protected RequestQueue mQueue = null;

    protected Store() {
        mQueue = Volley.newRequestQueue(KZHApp.getContext(),0);
    }

    /**
     * register Control-View.
     *
     * @param view Control-View object,such as Activity or Fragment.
     */
    public void register(final Object view) {
        this.bus.register(view);
    }

    /**
     * unregister Control-View.
     *
     * @param view Control-View object,such as Activity or Fragment.
     */
    public void unregister(final Object view) {
        this.bus.unregister(view);
    }

    /**
     * emit {@link com.awu.kanzhihu.stores.Store.StoreChangeEvent} object
     * to Control-View in {@link #onAction(Action)}.
     */
    void emitStoreChange() {
        this.bus.post(changeEvent());
    }

    /**
     * create {@link com.awu.kanzhihu.stores.Store.StoreChangeEvent} object.
     *
     * @return
     */
    public abstract StoreChangeEvent changeEvent();

    /**
     * Store's action function.
     *
     * @param action {@link Action} object.
     */
    public abstract void onAction(Action action);

    /**
     * StoreChangeEvent class.
     * Sub-class needs do itself functions.
     */
    public class StoreChangeEvent {

    }
}
