/*
 * author Arya Permana - Kirin
 * created on 22-08-2025-23h-54m
 * github: https://github.com/KirinZero0
 * copyright 2025
*/

package com.jobseeker.cvms.demo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.jobseeker.cvms.demo.model.Candidate;

@Repository
public interface CandidateRepository extends MongoRepository<Candidate, String> {
    Optional<Candidate> findByEmail(String email);
    boolean existsByEmail(String email);
}