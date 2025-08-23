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

import com.jobseeker.cvms.demo.model.Candidate;
import com.jobseeker.cvms.demo.repository.CandidateRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CandidateService {
    
    private final CandidateRepository candidateRepository;
    
    public Candidate createCandidate(Candidate candidate) {
        if (candidateRepository.existsByEmail(candidate.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + candidate.getEmail());
        }
        return candidateRepository.save(candidate);
    }
    
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }
    
    public Optional<Candidate> getCandidateById(String id) {
        return candidateRepository.findById(id);
    }
    
    public Candidate updateCandidate(String id, Candidate updatedCandidate) {
        return candidateRepository.findById(id)
            .map(candidate -> {
                if (!candidate.getEmail().equals(updatedCandidate.getEmail()) &&
                    candidateRepository.existsByEmail(updatedCandidate.getEmail())) {
                    throw new IllegalArgumentException("Email already exists: " + updatedCandidate.getEmail());
                }
                
                candidate.setName(updatedCandidate.getName());
                candidate.setEmail(updatedCandidate.getEmail());
                candidate.setBirthdate(updatedCandidate.getBirthdate());
                candidate.setGender(updatedCandidate.getGender());
                candidate.setCurrentSalary(updatedCandidate.getCurrentSalary());
                
                return candidateRepository.save(candidate);
            })
            .orElseThrow(() -> new IllegalArgumentException("Candidate not found with id: " + id));
    }
    
    public void deleteCandidate(String id) {
        if (!candidateRepository.existsById(id)) {
            throw new IllegalArgumentException("Candidate not found with id: " + id);
        }
        candidateRepository.deleteById(id);
    }
}