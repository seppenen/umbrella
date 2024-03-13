package org.umbrella.umbrella.service;

import org.umbrella.umbrella.dto.EntrepreneurDto;
import org.umbrella.umbrella.entity.EntrepreneurEntity;

import java.util.List;

public interface EntrepreneurServiceInterface {

    EntrepreneurDto createEntrepreneur(EntrepreneurDto entrepreneur);
    List<EntrepreneurDto> getEntrepreneurs();
    EntrepreneurDto getEntrepreneur(Long id);
    EntrepreneurDto updateEntrepreneur(EntrepreneurEntity entrepreneur);
    EntrepreneurEntity deleteEntrepreneur(Long id);


}
