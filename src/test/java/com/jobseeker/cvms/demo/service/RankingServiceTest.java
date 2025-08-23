package com.jobseeker.cvms.demo.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jobseeker.cvms.demo.dto.CandidateRankingDto;
import com.jobseeker.cvms.demo.model.Candidate;
import com.jobseeker.cvms.demo.model.Gender;
import com.jobseeker.cvms.demo.model.Vacancy;
import com.jobseeker.cvms.demo.model.criteria.AgeCriteria;
import com.jobseeker.cvms.demo.model.criteria.Criteria;
import com.jobseeker.cvms.demo.model.criteria.GenderCriteria;

@ExtendWith(MockitoExtension.class)
class RankingServiceTest {

    @Mock
    private CandidateService candidateService;

    @Mock
    private VacancyService vacancyService;

    @InjectMocks
    private RankingService rankingService;

    private Vacancy testVacancy;
    private List<Candidate> testCandidates;

    @BeforeEach
    void setUp() {
        AgeCriteria ageCriteria = new AgeCriteria();
        ageCriteria.setMinAge(25);
        ageCriteria.setMaxAge(35);
        ageCriteria.setWeight(10);

        GenderCriteria genderCriteria = new GenderCriteria();
        genderCriteria.setRequiredGender(Gender.MALE);
        genderCriteria.setWeight(5);

        List<Criteria> criteria = Arrays.asList(ageCriteria, genderCriteria);

        testVacancy = new Vacancy();
        testVacancy.setId("1");
        testVacancy.setName("Software Developer");
        testVacancy.setCriteria(criteria);

        Candidate candidate1 = new Candidate();
        candidate1.setId("1");
        candidate1.setName("John Doe");
        candidate1.setEmail("john@example.com");
        candidate1.setBirthdate(LocalDate.of(1990, 1, 1));
        candidate1.setGender(Gender.MALE);
        candidate1.setCurrentSalary(50000L);

        Candidate candidate2 = new Candidate();
        candidate2.setId("2");
        candidate2.setName("Jane Smith");
        candidate2.setEmail("jane@example.com");
        candidate2.setBirthdate(LocalDate.of(1985, 1, 1));
        candidate2.setGender(Gender.FEMALE);
        candidate2.setCurrentSalary(60000L);

        Candidate candidate3 = new Candidate();
        candidate3.setId("3");
        candidate3.setName("Bob Wilson");
        candidate3.setEmail("bob@example.com");
        candidate3.setBirthdate(LocalDate.of(1992, 1, 1));
        candidate3.setGender(Gender.MALE);
        candidate3.setCurrentSalary(55000L);

        testCandidates = Arrays.asList(candidate1, candidate2, candidate3);
    }

    @Test
    void rankCandidatesForVacancy_Success() {
        when(vacancyService.getVacancyById("1")).thenReturn(Optional.of(testVacancy));
        when(candidateService.getAllCandidates()).thenReturn(testCandidates);

        List<CandidateRankingDto> result = rankingService.rankCandidatesForVacancy("1", 0, 10);

        assertNotNull(result);
        assertEquals(3, result.size());
        
        assertTrue(result.get(0).getScore() >= result.get(1).getScore());
        assertTrue(result.get(1).getScore() >= result.get(2).getScore());
        
        CandidateRankingDto johnDoe = result.stream()
            .filter(c -> "John Doe".equals(c.getName()))
            .findFirst().orElse(null);
        assertNotNull(johnDoe);
        assertEquals(15, johnDoe.getScore());

        CandidateRankingDto bobWilson = result.stream()
            .filter(c -> "Bob Wilson".equals(c.getName()))
            .findFirst().orElse(null);
        assertNotNull(bobWilson);
        assertEquals(15, bobWilson.getScore());

        CandidateRankingDto janeSmith = result.stream()
            .filter(c -> "Jane Smith".equals(c.getName()))
            .findFirst().orElse(null);
        assertNotNull(janeSmith);
        assertEquals(0, janeSmith.getScore());

        verify(vacancyService).getVacancyById("1");
        verify(candidateService).getAllCandidates();
    }

    @Test
    void rankCandidatesForVacancy_VacancyNotFound_ThrowsException() {
        when(vacancyService.getVacancyById("999")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> rankingService.rankCandidatesForVacancy("999", 0, 10)
        );

        assertEquals("Vacancy not found with id: 999", exception.getMessage());
        verify(vacancyService).getVacancyById("999");
    }

    @Test
    void rankCandidatesForVacancy_WithPagination() {
        when(vacancyService.getVacancyById("1")).thenReturn(Optional.of(testVacancy));
        when(candidateService.getAllCandidates()).thenReturn(testCandidates);

        List<CandidateRankingDto> result = rankingService.rankCandidatesForVacancy("1", 0, 2);

        assertNotNull(result);
        assertEquals(2, result.size());

        List<CandidateRankingDto> result2 = rankingService.rankCandidatesForVacancy("1", 1, 2);

        assertNotNull(result2);
        assertEquals(1, result2.size());

        verify(vacancyService, times(2)).getVacancyById("1");
        verify(candidateService, times(2)).getAllCandidates();
    }

    @Test
    void rankCandidatesForVacancy_EmptyCandidateList() {
        when(vacancyService.getVacancyById("1")).thenReturn(Optional.of(testVacancy));
        when(candidateService.getAllCandidates()).thenReturn(Arrays.asList());

        List<CandidateRankingDto> result = rankingService.rankCandidatesForVacancy("1", 0, 10);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(vacancyService).getVacancyById("1");
        verify(candidateService).getAllCandidates();
    }
}