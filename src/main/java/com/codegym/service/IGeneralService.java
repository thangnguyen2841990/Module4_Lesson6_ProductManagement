package com.codegym.service;

import java.util.List;

public interface IGeneralService<T> {
    List<T> findAll();

    void save(T t);

    T findById(Long id);


    void delete(Long id);

}
