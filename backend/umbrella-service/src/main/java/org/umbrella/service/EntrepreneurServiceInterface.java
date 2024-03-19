package org.umbrella.service;

import org.umbrella.entity.EntrepreneurEntity;

import java.util.List;

public interface EntrepreneurServiceInterface {

    EntrepreneurEntity persistEntrepreneurEntity(EntrepreneurEntity entrepreneur);
    List<EntrepreneurEntity> getAllEntrepreneurs();
    EntrepreneurEntity getEntrepreneur(Long id);
    EntrepreneurEntity updateEntrepreneur(EntrepreneurEntity entrepreneur);
    void deleteEntrepreneur(Long id);


}
