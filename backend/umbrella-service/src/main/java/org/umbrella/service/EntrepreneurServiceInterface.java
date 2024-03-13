package org.umbrella.service;

import org.umbrella.entity.EntrepreneurEntity;
import org.umbrella.dto.EntrepreneurDto;

import java.util.List;

public interface EntrepreneurServiceInterface {

    EntrepreneurDto createEntrepreneur(EntrepreneurDto entrepreneur);
    List<EntrepreneurDto> getEntrepreneurs();
    EntrepreneurDto getEntrepreneur(Long id);
    EntrepreneurDto updateEntrepreneur(EntrepreneurEntity entrepreneur);
    EntrepreneurEntity deleteEntrepreneur(Long id);


}
