/*
 * author Arya Permana - Kirin
 * created on 23-08-2025-11h-49m
 * github: https://github.com/KirinZero0
 * copyright 2025
*/

package com.jobseeker.cvms.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jobseeker.cvms.demo.dto.CandidateRankingDto;
import com.jobseeker.cvms.demo.model.Candidate;
import com.jobseeker.cvms.demo.model.Vacancy;
import com.jobseeker.cvms.demo.model.criteria.Criteria;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RankingService {
    
    private final CandidateService candidateService;
    private final VacancyService vacancyService;
    
    public List<CandidateRankingDto> rankCandidatesForVacancy(String vacancyId) {
        Vacancy vacancy = vacancyService.getVacancyById(vacancyId)
            .orElseThrow(() -> new IllegalArgumentException("Vacancy not found with id: " + vacancyId));
        
        List<Candidate> candidates = candidateService.getAllCandidates();
        
        return candidates.stream()
            .map(candidate -> {
                int score = calculateScore(candidate, vacancy.getCriteria());
                return CandidateRankingDto.builder()
                    .id(candidate.getId())
                    .name(candidate.getName())
                    .email(candidate.getEmail())
                    .score(score)
                    .build();
            })
            .sorted((c1, c2) -> Integer.compare(c2.getScore(), c1.getScore()))
            .collect(Collectors.toList());
    }
    
    private int calculateScore(Candidate candidate, List<Criteria> criteria) {
        return criteria.stream()
            .mapToInt(criterion -> criterion.matches(candidate) ? criterion.getWeight() : 0)
            .sum();
    }
}