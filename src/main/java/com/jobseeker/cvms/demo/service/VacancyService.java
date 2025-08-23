/*
 * author Arya Permana - Kirin
 * created on 23-08-2025-11h-41m
 * github: https://github.com/KirinZero0
 * copyright 2025
*/

/*
 * author Arya Permana - Kirin
 * created on 23-08-2025-11h-39m
 * github: https://github.com/KirinZero0
 * copyright 2025
*/

package com.jobseeker.cvms.demo.service;

import java.util.List;
import java.util.Optional;


import org.springframework.stereotype.Service;

import com.jobseeker.cvms.demo.model.Vacancy;
import com.jobseeker.cvms.demo.repository.VacancyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VacancyService {
    
    private final VacancyRepository vacancyRepository;

    private void validateVacancy(Vacancy vacancy) {
        if (vacancy.getCriteria() == null || vacancy.getCriteria().isEmpty()) {
            throw new IllegalArgumentException("Vacancy must have at least one criterion");
        }
    }
    
    public Vacancy createVacancy(Vacancy vacancy) {
        validateVacancy(vacancy);
        return vacancyRepository.save(vacancy);
    }

    public List<Vacancy> getVacancies() {
        return vacancyRepository.findAll();
    }

    public Optional<Vacancy> getVacancyById(String id) {
        return vacancyRepository.findById(id);
    }

    public Vacancy updateVacancy(String id, Vacancy updatedVacancy) {
        return vacancyRepository.findById(id)
            .map(vacancy -> {
                validateVacancy(updatedVacancy);
                vacancy.setName(updatedVacancy.getName());
                vacancy.setCriteria(updatedVacancy.getCriteria());
                return vacancyRepository.save(vacancy);
            })
             .orElseThrow(() -> new IllegalArgumentException("Vacancy not found with id: " + id));
    }

    public void deleteVacancy(String id) {
        if(!vacancyRepository.existsById(id)) {
            throw new IllegalArgumentException("Vacancy not found with id" + id);
        }
        vacancyRepository.deleteById(id);
    }
}