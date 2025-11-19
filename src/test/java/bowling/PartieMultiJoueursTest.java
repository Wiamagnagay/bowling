package bowling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PartieMultiJoueursTest {
    private PartieMultiJoueurs partie;

    @BeforeEach
    void setUp() {
        partie = new PartieMultiJoueurs();
    }

    @Test
    void testDemarreNouvellePartie() {
        String[] joueurs = {"Pierre", "Paul"};
        String resultat = partie.demarreNouvellePartie(joueurs);
        assertEquals("Prochain tir : joueur Pierre, tour n° 1, boule n° 1", resultat);
    }

    @Test
    void testDemarreNouvellePartieAvecJoueursNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            partie.demarreNouvellePartie(null);
        });
    }

    @Test
    void testDemarreNouvellePartieAvecListeVide() {
        assertThrows(IllegalArgumentException.class, () -> {
            partie.demarreNouvellePartie(new String[]{});
        });
    }

    @Test
    void testEnregistreLancerSequenceComplete() {
        String[] joueurs = {"Pierre", "Paul"};
        partie.demarreNouvellePartie(joueurs);
        
        // Pierre - tour 1, boule 1
        assertEquals("Prochain tir : joueur Pierre, tour n° 1, boule n° 2", 
                    partie.enregistreLancer(5));
        
        // Pierre - tour 1, boule 2
        assertEquals("Prochain tir : joueur Paul, tour n° 1, boule n° 1", 
                    partie.enregistreLancer(3));
        
        // Paul - tour 1, boule 1
        assertEquals("Prochain tir : joueur Pierre, tour n° 2, boule n° 1", 
                    partie.enregistreLancer(10));
        
        // Pierre - tour 2, boule 1
        assertEquals("Prochain tir : joueur Pierre, tour n° 2, boule n° 2", 
                    partie.enregistreLancer(7));
        
        // Pierre - tour 2, boule 2
        assertEquals("Prochain tir : joueur Paul, tour n° 2, boule n° 1", 
                    partie.enregistreLancer(3));
        
        // Paul - tour 2, boule 1
        assertEquals("Prochain tir : joueur Paul, tour n° 2, boule n° 2", 
                    partie.enregistreLancer(5));
    }

    @Test
    void testEnregistreLancerSansDemarrer() {
        assertThrows(IllegalStateException.class, () -> {
            partie.enregistreLancer(5);
        });
    }

   @Test
void testScores() {
    String[] joueurs = {"Pierre", "Paul"};
    partie.demarreNouvellePartie(joueurs);

    // Pierre: 5 + 3 = 8
    partie.enregistreLancer(5);
    partie.enregistreLancer(3);

    // Paul: Strike = 10 + bonus des 2 prochains lancers
    partie.enregistreLancer(10);

    // Pierre tour 2: 7 + 3 = spare (10) + bonus du prochain lancer
    partie.enregistreLancer(7);
    partie.enregistreLancer(3);

    // Paul tour 2: 5 + 2 = 7
    partie.enregistreLancer(5);
    partie.enregistreLancer(2);

    // VÉRIFICATION DES SCORES
    // Pierre: 8 (tour 1) + 10 + 5 (spare + bonus) = 23
    assertEquals(23, partie.scorePour("Pierre"));
    
    // Paul: 10 + 7 + 5 (strike + bonus) + 7 (tour 2) = 29
    assertEquals(27, partie.scorePour("Paul"));
}

    @Test
    void testScorePourJoueurInconnu() {
        partie.demarreNouvellePartie(new String[]{"Pierre", "Paul"});
        
        assertThrows(IllegalArgumentException.class, () -> {
            partie.scorePour("Jacques");
        });
    }

    @Test
    void testPartieTerminee() {
        partie.demarreNouvellePartie(new String[]{"Pierre"});
        
        // Jouer une partie complète pour un joueur (12 strikes)
        for (int i = 0; i < 12; i++) {
            partie.enregistreLancer(10);
        }
        
        assertEquals("Partie terminée", partie.enregistreLancer(10));
    }

    @Test
    void testAvecStrike() {
        String[] joueurs = {"Pierre", "Paul"};
        partie.demarreNouvellePartie(joueurs);
        
        // Pierre fait un strike
        assertEquals("Prochain tir : joueur Paul, tour n° 1, boule n° 1", 
                    partie.enregistreLancer(10));
        
        // Paul fait 5 + 4
        assertEquals("Prochain tir : joueur Paul, tour n° 1, boule n° 2", 
                    partie.enregistreLancer(5));
        assertEquals("Prochain tir : joueur Pierre, tour n° 2, boule n° 1", 
                    partie.enregistreLancer(4));
        
        // Retour à Pierre pour le tour 2
        assertEquals("Prochain tir : joueur Pierre, tour n° 2, boule n° 2", 
                    partie.enregistreLancer(3));
    }
}