package tn.esprit.backend.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.backend.entity.Entreprise;
import tn.esprit.backend.repository.EntrepriseRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntrepriseServiceImplTest {

    @Mock
    private EntrepriseRepository entrepriseRepository;

    @InjectMocks
    private EntrepriseServiceImpl entrepriseService;

    private Entreprise entreprise;

    @BeforeEach
    void setUp() {
        entreprise = new Entreprise();
        entreprise.setId(1L);
        // Ajoute ici les autres champs de ton entité Entreprise si nécessaire,
        // ex : entreprise.setNom("Esprit Tech");
    }

    @Test
    void addEntreprise_shouldSaveAndReturnEntreprise() {
        when(entrepriseRepository.save(entreprise)).thenReturn(entreprise);

        Entreprise result = entrepriseService.addEntreprise(entreprise);

        assertNotNull(result);
        assertEquals(entreprise.getId(), result.getId());
        verify(entrepriseRepository, times(1)).save(entreprise);
    }

    @Test
    void updateEntreprise_shouldSaveAndReturnUpdatedEntreprise() {
        when(entrepriseRepository.save(entreprise)).thenReturn(entreprise);

        Entreprise result = entrepriseService.updateEntreprise(entreprise);

        assertNotNull(result);
        assertEquals(entreprise.getId(), result.getId());
        verify(entrepriseRepository, times(1)).save(entreprise);
    }

    @Test
    void deleteEntreprise_shouldCallRepositoryDeleteById() {
        Long id = 1L;
        doNothing().when(entrepriseRepository).deleteById(id);

        entrepriseService.deleteEntreprise(id);

        verify(entrepriseRepository, times(1)).deleteById(id);
    }

    @Test
    void getEntrepriseById_shouldReturnEntreprise_whenFound() {
        when(entrepriseRepository.findById(1L)).thenReturn(Optional.of(entreprise));

        Entreprise result = entrepriseService.getEntrepriseById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getEntrepriseById_shouldReturnNull_whenNotFound() {
        when(entrepriseRepository.findById(99L)).thenReturn(Optional.empty());

        Entreprise result = entrepriseService.getEntrepriseById(99L);

        assertNull(result);
    }

    @Test
    void getAllEntreprises_shouldReturnListOfEntreprises() {
        Entreprise entreprise2 = new Entreprise();
        entreprise2.setId(2L);
        List<Entreprise> entreprises = Arrays.asList(entreprise, entreprise2);

        when(entrepriseRepository.findAll()).thenReturn(entreprises);

        List<Entreprise> result = entrepriseService.getAllEntreprises();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(entrepriseRepository, times(1)).findAll();
    }
}
