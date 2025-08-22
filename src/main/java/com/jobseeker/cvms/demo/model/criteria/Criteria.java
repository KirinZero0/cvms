/*
 * author Arya Permana - Kirin
 * created on 22-08-2025-22h-53m
 * github: https://github.com/KirinZero0
 * copyright 2025
*/

package com.jobseeker.cvms.demo.model.criteria;

import com.jobseeker.cvms.demo.model.Candidate;
import com.jobseeker.cvms.demo.model.CriteriaType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Criteria {
    protected CriteriaType type;
    protected int weight = 1;
    
    public abstract boolean matches(Candidate candidate);
}