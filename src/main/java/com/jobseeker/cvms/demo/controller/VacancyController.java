/*
 * author Arya Permana - Kirin
 * created on 23-08-2025-12h-07m
 * github: https://github.com/KirinZero0
 * copyright 2025
*/

/*
 * author Arya Permana - Kirin
 * created on 23-08-2025-12h-03m
 * github: https://github.com/KirinZero0
 * copyright 2025
*/

package com.jobseeker.cvms.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobseeker.cvms.demo.dto.CandidateRankingDto;
import com.jobseeker.cvms.demo.model.Vacancy;
import com.jobseeker.cvms.demo.service.RankingService;
import com.jobseeker.cvms.demo.service.VacancyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vacancies")
@RequiredArgsConstructor
public class VacancyController {
    
    private final VacancyService vacancyService;
    private final RankingService rankingService;
    
    @PostMapping
    public ResponseEntity<Vacancy> createVacancy(@Valid @RequestBody Vacancy vacancy) {
        Vacancy created = vacancyService.createVacancy(vacancy);
        return ResponseEntity.ok(created);
    }
    
    @GetMapping
    public ResponseEntity<List<Vacancy>> getAllVacancies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Vacancy> vacancies = vacancyService.getVacancies(page, size);
        return ResponseEntity.ok(vacancies);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Vacancy> getCandidateById(@PathVariable String id) {
        return vacancyService.getVacancyById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/ranking")
    public ResponseEntity<List<CandidateRankingDto>> getRankedCandidates(
            @PathVariable String id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<CandidateRankingDto> rankings = rankingService.rankCandidatesForVacancy(id, page, size);
            return ResponseEntity.ok(rankings);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Vacancy> updateVacancy(@PathVariable String id, @Valid @RequestBody Vacancy vacancy) {
        try {
            Vacancy updated = vacancyService.updateVacancy(id, vacancy);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVacancy(@PathVariable String id) {
        try {
            vacancyService.deleteVacancy(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}