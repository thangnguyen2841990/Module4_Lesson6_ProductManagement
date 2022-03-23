package com.codegym.service;

import java.util.List;

public interface IGeneralService<T> {
    List<T> findAll();

    void save(T t);

    T findById(Long id);

    List<T> findByName(String name);

    void delete(Long id);

}
