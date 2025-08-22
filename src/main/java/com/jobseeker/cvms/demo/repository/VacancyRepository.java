/*
 * author Arya Permana - Kirin
 * created on 22-08-2025-23h-55m
 * github: https://github.com/KirinZero0
 * copyright 2025
*/

package com.jobseeker.cvms.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.jobseeker.cvms.demo.model.Vacancy;

@Repository
public interface VacancyRepository extends MongoRepository<Vacancy, String> {
}