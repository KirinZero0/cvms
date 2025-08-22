/*
 * author Arya Permana - Kirin
 * created on 22-08-2025-22h-54m
 * github: https://github.com/KirinZero0
 * copyright 2025
*/

package com.jobseeker.cvms.demo.model.criteria;

import com.jobseeker.cvms.demo.model.Candidate;
import com.jobseeker.cvms.demo.model.CriteriaType;
import com.jobseeker.cvms.demo.model.Gender;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class GenderCriteria extends Criteria {
    private Gender requiredGender;
    
    public GenderCriteria(Gender requiredGender, int weight) {
        super(CriteriaType.GENDER, weight);
        this.requiredGender = requiredGender;
    }
    
    @Override
    public boolean matches(Candidate candidate) {
        return requiredGender == Gender.ANY || candidate.getGender() == requiredGender;
    }
}