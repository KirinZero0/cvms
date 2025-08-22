/*
 * author Arya Permana - Kirin
 * created on 22-08-2025-22h-53m
 * github: https://github.com/KirinZero0
 * copyright 2025
*/

package com.jobseeker.cvms.demo.model.criteria;

import com.jobseeker.cvms.demo.model.Candidate;
import com.jobseeker.cvms.demo.model.CriteriaType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AgeCriteria extends Criteria {
    private int minAge;
    private int maxAge;
    
    public AgeCriteria(int minAge, int maxAge, int weight) {
        super(CriteriaType.AGE, weight);
        this.minAge = minAge;
        this.maxAge = maxAge;
    }
    
    @Override
    public boolean matches(Candidate candidate) {
        int candidateAge = Period.between(candidate.getBirthdate(), LocalDate.now()).getYears();
        return candidateAge >= minAge && candidateAge <= maxAge;
    }
}