package com.jobseeker.cvms.demo.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.jobseeker.cvms.demo.model.Vacancy;
import com.jobseeker.cvms.demo.model.criteria.AgeCriteria;
import com.jobseeker.cvms.demo.model.criteria.Criteria;
import com.jobseeker.cvms.demo.repository.VacancyRepository;

@ExtendWith(MockitoExtension.class)
class VacancyServiceTest {

    @Mock
    private VacancyRepository vacancyRepository;

    @InjectMocks
    private VacancyService vacancyService;

    private Vacancy testVacancy;
    private List<Criteria> validCriteria;

    @BeforeEach
    void setUp() {
        AgeCriteria ageCriteria = new AgeCriteria();
        ageCriteria.setMinAge(25);
        ageCriteria.setMaxAge(40);
        ageCriteria.setWeight(10);
        
        validCriteria = Arrays.asList(ageCriteria);

        testVacancy = new Vacancy();
        testVacancy.setId("1");
        testVacancy.setName("Software Developer");
        testVacancy.setCriteria(validCriteria);
    }

    @Test
    void createVacancy_Success() {
        when(vacancyRepository.save(any(Vacancy.class))).thenReturn(testVacancy);

        Vacancy result = vacancyService.createVacancy(testVacancy);

        assertNotNull(result);
        assertEquals(testVacancy.getName(), result.getName());
        assertEquals(testVacancy.getCriteria().size(), result.getCriteria().size());
        verify(vacancyRepository).save(testVacancy);
    }

    @Test
    void createVacancy_NoCriteria_ThrowsException() {
        testVacancy.setCriteria(Collections.emptyList());

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> vacancyService.createVacancy(testVacancy)
        );

        assertEquals("Vacancy must have at least one criterion", exception.getMessage());
        verify(vacancyRepository, never()).save(any());
    }

    @Test
    void createVacancy_NullCriteria_ThrowsException() {
        testVacancy.setCriteria(null);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> vacancyService.createVacancy(testVacancy)
        );

        assertEquals("Vacancy must have at least one criterion", exception.getMessage());
        verify(vacancyRepository, never()).save(any());
    }

    @Test
    void getVacancies_WithoutPagination() {
        List<Vacancy> vacancies = Arrays.asList(testVacancy);
        when(vacancyRepository.findAll()).thenReturn(vacancies);

        List<Vacancy> result = vacancyService.getVacancies();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testVacancy.getName(), result.get(0).getName());
        verify(vacancyRepository).findAll();
    }

    @Test
    void getVacancies_WithPagination() {
        List<Vacancy> vacancies = Arrays.asList(testVacancy);
        Page<Vacancy> page = new PageImpl<>(vacancies);
        
        when(vacancyRepository.findAll(any(Pageable.class))).thenReturn(page);

        List<Vacancy> result = vacancyService.getVacancies(0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testVacancy.getName(), result.get(0).getName());
        verify(vacancyRepository).findAll(any(Pageable.class));
    }

    @Test
    void getVacancyById_Found() {
        when(vacancyRepository.findById("1")).thenReturn(Optional.of(testVacancy));

        Optional<Vacancy> result = vacancyService.getVacancyById("1");

        assertTrue(result.isPresent());
        assertEquals(testVacancy.getName(), result.get().getName());
        verify(vacancyRepository).findById("1");
    }

    @Test
    void getVacancyById_NotFound() {
        when(vacancyRepository.findById("999")).thenReturn(Optional.empty());

        Optional<Vacancy> result = vacancyService.getVacancyById("999");

        assertFalse(result.isPresent());
        verify(vacancyRepository).findById("999");
    }

    @Test
    void updateVacancy_Success() {
        Vacancy updatedVacancy = new Vacancy();
        updatedVacancy.setName("Senior Software Developer");
        updatedVacancy.setCriteria(validCriteria);

        when(vacancyRepository.findById("1")).thenReturn(Optional.of(testVacancy));
        when(vacancyRepository.save(any(Vacancy.class))).thenReturn(testVacancy);

        Vacancy result = vacancyService.updateVacancy("1", updatedVacancy);

        assertNotNull(result);
        verify(vacancyRepository).findById("1");
        verify(vacancyRepository).save(any(Vacancy.class));
    }

    @Test
    void updateVacancy_NotFound_ThrowsException() {
        when(vacancyRepository.findById("999")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> vacancyService.updateVacancy("999", testVacancy)
        );

        assertEquals("Vacancy not found with id: 999", exception.getMessage());
        verify(vacancyRepository).findById("999");
        verify(vacancyRepository, never()).save(any());
    }

    @Test
    void updateVacancy_InvalidCriteria_ThrowsException() {
        testVacancy.setCriteria(Collections.emptyList());
        when(vacancyRepository.findById("1")).thenReturn(Optional.of(testVacancy));

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> vacancyService.updateVacancy("1", testVacancy)
        );

        assertEquals("Vacancy must have at least one criterion", exception.getMessage());
        verify(vacancyRepository).findById("1");
        verify(vacancyRepository, never()).save(any());
    }

    @Test
    void deleteVacancy_Success() {
        when(vacancyRepository.existsById("1")).thenReturn(true);
        doNothing().when(vacancyRepository).deleteById("1");

        assertDoesNotThrow(() -> vacancyService.deleteVacancy("1"));

        verify(vacancyRepository).existsById("1");
        verify(vacancyRepository).deleteById("1");
    }

    @Test
    void deleteVacancy_NotFound_ThrowsException() {
        when(vacancyRepository.existsById("999")).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> vacancyService.deleteVacancy("999")
        );

        assertEquals("Vacancy not found with id999", exception.getMessage());
        verify(vacancyRepository).existsById("999");
        verify(vacancyRepository, never()).deleteById(anyString());
    }
}