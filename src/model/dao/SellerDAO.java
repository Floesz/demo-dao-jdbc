package model.dao;

import java.util.List;

public interface Seller {

    void insert(Seller seller);
    void update(Seller seller);
    void deleteById(Seller seller);
    Seller findById(Integer id);
    List<Seller> findAll();

}
