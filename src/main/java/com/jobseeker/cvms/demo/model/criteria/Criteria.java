/*
 * author Arya Permana - Kirin
 * created on 22-08-2025-22h-53m
 * github: https://github.com/KirinZero0
 * copyright 2025
*/

package com.jobseeker.cvms.demo.model.criteria;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.jobseeker.cvms.demo.model.Candidate;
import com.jobseeker.cvms.demo.model.CriteriaType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type",
    visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = AgeCriteria.class, name = "AGE"),
    @JsonSubTypes.Type(value = GenderCriteria.class, name = "GENDER"),
    @JsonSubTypes.Type(value = SalaryRangeCriteria.class, name = "SALARY_RANGE")
})

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Criteria {
    protected CriteriaType type;
    protected int weight = 1;
    
    public abstract boolean matches(Candidate candidate);
}