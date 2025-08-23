package com.jobseeker.cvms.demo.service;

import java.time.LocalDate;
import java.util.Arrays;
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

import com.jobseeker.cvms.demo.model.Candidate;
import com.jobseeker.cvms.demo.model.Gender;
import com.jobseeker.cvms.demo.repository.CandidateRepository;

@ExtendWith(MockitoExtension.class)
class CandidateServiceTest {

    @Mock
    private CandidateRepository candidateRepository;

    @InjectMocks
    private CandidateService candidateService;

    private Candidate testCandidate;

    @BeforeEach
    void setUp() {
        testCandidate = new Candidate();
        testCandidate.setId("1");
        testCandidate.setName("John Doe");
        testCandidate.setEmail("john@example.com");
        testCandidate.setBirthdate(LocalDate.of(1990, 1, 1));
        testCandidate.setGender(Gender.MALE);
        testCandidate.setCurrentSalary(50000L);
    }

    @Test
    void createCandidate_Success() {
        when(candidateRepository.existsByEmail(testCandidate.getEmail())).thenReturn(false);
        when(candidateRepository.save(any(Candidate.class))).thenReturn(testCandidate);

        Candidate result = candidateService.createCandidate(testCandidate);

        assertNotNull(result);
        assertEquals(testCandidate.getName(), result.getName());
        assertEquals(testCandidate.getEmail(), result.getEmail());
        verify(candidateRepository).existsByEmail(testCandidate.getEmail());
        verify(candidateRepository).save(testCandidate);
    }

    @Test
    void createCandidate_EmailAlreadyExists_ThrowsException() {
        when(candidateRepository.existsByEmail(testCandidate.getEmail())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> candidateService.createCandidate(testCandidate)
        );

        assertEquals("Email already exists: " + testCandidate.getEmail(), exception.getMessage());
        verify(candidateRepository).existsByEmail(testCandidate.getEmail());
        verify(candidateRepository, never()).save(any());
    }

    @Test
    void getAllCandidates_WithoutPagination() {
        List<Candidate> candidates = Arrays.asList(testCandidate);
        when(candidateRepository.findAll()).thenReturn(candidates);

        List<Candidate> result = candidateService.getAllCandidates();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testCandidate.getName(), result.get(0).getName());
        verify(candidateRepository).findAll();
    }

    @Test
    void getAllCandidates_WithPagination() {
        List<Candidate> candidates = Arrays.asList(testCandidate);
        Page<Candidate> page = new PageImpl<>(candidates);
        
        when(candidateRepository.findAll(any(Pageable.class))).thenReturn(page);

        List<Candidate> result = candidateService.getAllCandidates(0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testCandidate.getName(), result.get(0).getName());
        verify(candidateRepository).findAll(any(Pageable.class));
    }

    @Test
    void getCandidateById_Found() {
        when(candidateRepository.findById("1")).thenReturn(Optional.of(testCandidate));

        Optional<Candidate> result = candidateService.getCandidateById("1");

        assertTrue(result.isPresent());
        assertEquals(testCandidate.getName(), result.get().getName());
        verify(candidateRepository).findById("1");
    }

    @Test
    void getCandidateById_NotFound() {
        when(candidateRepository.findById("999")).thenReturn(Optional.empty());

        Optional<Candidate> result = candidateService.getCandidateById("999");

        assertFalse(result.isPresent());
        verify(candidateRepository).findById("999");
    }

    @Test
    void updateCandidate_Success() {
        Candidate updatedCandidate = new Candidate();
        updatedCandidate.setName("Jane Doe");
        updatedCandidate.setEmail("jane@example.com");
        updatedCandidate.setBirthdate(LocalDate.of(1995, 5, 5));
        updatedCandidate.setGender(Gender.FEMALE);
        updatedCandidate.setCurrentSalary(60000L);

        when(candidateRepository.findById("1")).thenReturn(Optional.of(testCandidate));
        when(candidateRepository.existsByEmail(updatedCandidate.getEmail())).thenReturn(false);
        when(candidateRepository.save(any(Candidate.class))).thenReturn(testCandidate);

        Candidate result = candidateService.updateCandidate("1", updatedCandidate);

        assertNotNull(result);
        verify(candidateRepository).findById("1");
        verify(candidateRepository).save(any(Candidate.class));
    }

    @Test
    void updateCandidate_NotFound_ThrowsException() {
        when(candidateRepository.findById("999")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> candidateService.updateCandidate("999", testCandidate)
        );

        assertEquals("Candidate not found with id: 999", exception.getMessage());
        verify(candidateRepository).findById("999");
        verify(candidateRepository, never()).save(any());
    }

    @Test
    void deleteCandidate_Success() {
        when(candidateRepository.existsById("1")).thenReturn(true);
        doNothing().when(candidateRepository).deleteById("1");

        assertDoesNotThrow(() -> candidateService.deleteCandidate("1"));

        verify(candidateRepository).existsById("1");
        verify(candidateRepository).deleteById("1");
    }

    @Test
    void deleteCandidate_NotFound_ThrowsException() {
        when(candidateRepository.existsById("999")).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> candidateService.deleteCandidate("999")
        );

        assertEquals("Candidate not found with id: 999", exception.getMessage());
        verify(candidateRepository).existsById("999");
        verify(candidateRepository, never()).deleteById(anyString());
    }
}