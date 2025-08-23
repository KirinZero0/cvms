/*
 * author Arya Permana - Kirin
 * created on 23-08-2025-11h-49m
 * github: https://github.com/KirinZero0
 * copyright 2025
*/

package com.jobseeker.cvms.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateRankingDto {
    private String id;
    private String name;
    private String email;
    private int score;
}