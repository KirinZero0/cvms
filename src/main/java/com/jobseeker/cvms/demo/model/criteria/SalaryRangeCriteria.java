/*
 * author Arya Permana - Kirin
 * created on 22-08-2025-22h-55m
 * github: https://github.com/KirinZero0
 * copyright 2025
*/

package com.jobseeker.cvms.demo.model.criteria;

import com.jobseeker.cvms.demo.model.Candidate;
import com.jobseeker.cvms.demo.model.CriteriaType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SalaryRangeCriteria extends Criteria {
    private long minSalary;
    private long maxSalary;
    
    public SalaryRangeCriteria(long minSalary, long maxSalary, int weight) {
        super(CriteriaType.SALARY_RANGE, weight);
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
    }
    
    @Override
    public boolean matches(Candidate candidate) {
        Long salary = candidate.getCurrentSalary();
        return salary != null && salary >= minSalary && salary <= maxSalary;
    }
}