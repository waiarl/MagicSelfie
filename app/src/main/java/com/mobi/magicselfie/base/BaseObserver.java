package com.mobi.magicselfie.base;


import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by waiarl on 2018/6/27.
 */

public abstract class BaseObserver<T> implements Observer<T> {
    private Disposable disposable;
    private CompositeDisposable compositeDisposable;

    public BaseObserver() {

    }

    public BaseObserver(@Nullable CompositeDisposable compositeDisposable) {
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    @CallSuper
    public void onSubscribe(Disposable d) {
        disposable = d;
        if (compositeDisposable != null) {
            compositeDisposable.add(disposable);
        }
    }

    @Override
    public void onNext(T t) {

    }

    @Override
    @CallSuper
    public void onError(Throwable e) {
        onComplete();
    }

    @Override
    @CallSuper
    public void onComplete() {
        if (compositeDisposable != null && disposable != null) {
            compositeDisposable.remove(disposable);
            compositeDisposable = null;
        } else if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public Disposable getDisposable() {
        return disposable;
    }



}
