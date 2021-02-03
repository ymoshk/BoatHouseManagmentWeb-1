package engine.model;


import engine.api.EngineInterface;

import java.io.Serializable;
import java.util.function.Consumer;


public abstract class Modifier<T> implements AutoCloseable, Serializable {

    private final EngineInterface engine;
    protected T objectToEdit;
    private Runnable autoInvokeMethod;
    private boolean modified;
    private Consumer<String> callback;
    private Consumer<String> callbackBackup;

    public Modifier(EngineInterface engineContext, T objectToEdit, Runnable collectionSaveFunc, Consumer<String> callback) {
        this.objectToEdit = objectToEdit;
        this.modified = false;
        this.autoInvokeMethod = collectionSaveFunc;
        this.callback = callback;
        this.callbackBackup = null;
        this.engine = engineContext;
    }

    protected void markModify() {
        this.modified = true;
        close();
    }

    @Override
    public void close() {
        if (this.modified && this.autoInvokeMethod != null) {
            this.autoInvokeMethod.run();
        }
    }

    protected void invokeCallBack(String result) {
        if (this.callback != null) {
            this.callback.accept(result);
        }
    }

    protected EngineInterface getEngine() {
        return this.engine;
    }

    protected void disableCallback() {
        if (this.callback != null) {
            this.callbackBackup = this.callback;
            this.callback = null;
        }
    }

    protected void enableCallback() {
        if (this.callbackBackup != null) {
            this.callback = callbackBackup;
            this.callbackBackup = null;
        }
    }

    public T getObjectToEdit() {
        return this.objectToEdit;
    }

    public void setMethodToInvoke(Runnable method) {
        this.autoInvokeMethod = method;
    }
}
