package com.cfirmo33.moviesapp.ui.contract;

public interface IBaseView<T> {
    void setPresenter(T presenter);
    T getPresenter();
}