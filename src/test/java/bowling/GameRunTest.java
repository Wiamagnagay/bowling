package bowling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameRunTest {

	private PartieMonoJoueur partie;

	@BeforeEach
	void setUp() {
		partie = new PartieMonoJoueur();
	}

	@Test
	void testBeginGame() {
		assertEquals(1, partie.numeroTourCourant(),
				"On doit commencer au tour n°1");
		assertEquals(1, partie.numeroProchainLancer(),
				"On doit commencer à la boule n°1");
	}

	@Test
	void passeAuTourSuivant() {
		assertTrue(partie.enregistreLancer(1), "Premier lancer, le tour continue");
		assertFalse(partie.enregistreLancer(1), "Deuxième lancer, le tour est fini");
		assertEquals(2, partie.numeroTourCourant(), "On doit être au tour n°2");
		assertEquals(1, partie.numeroProchainLancer(), "On doit être à la boule n°1");
	}

	@Test
	void testAllOnes() {
		lancerPlusieurs(20, 1);
		assertEquals(0, partie.numeroTourCourant(),
				"On a fini, le n° de tour doit être 0");
		assertTrue(partie.estTerminee(),
				"Le jeu doit être terminé");
	}

	@Test
	void testOneSpare() {
		faireUnSpare();
		assertEquals(2, partie.numeroTourCourant(),
				"On a fini le premier tour, le n° de tour doit être 2");
		assertEquals(1, partie.numeroProchainLancer(),
				"On doit commencer le tour à la boule n°1");
	}

	@Test
	void testOneStrike() {
		faireUnStrike();

		assertEquals(2, partie.numeroTourCourant(),
				"On a fini le premier tour, le n° de tour doit être 2");
		assertEquals(1, partie.numeroProchainLancer(),
				"On doit commencer le tour à la boule n°1");
	}

	/**
	 * Pas plus de 20 boules dans un jeu
	 */
	@Test
	void unLancerDeTrop() {
		lancerPlusieurs(20, 0); // Le jeu est fini

		assertThrows(IllegalStateException.class, () -> {
			// On doit avoir une exception
			partie.enregistreLancer(0);
		}, "Le jeu est fini, on doit avoir une exception");
	}

	// Quelques methodes utilitaires pour faciliter l'écriture des tests
	private boolean lancerPlusieurs(int n, int quilles) {
		boolean leTourcontinue = false;
		for (int i = 0; i < n; i++) {
			leTourcontinue = partie.enregistreLancer(quilles);
		}
		return leTourcontinue;
	}

	private void faireUnSpare() {
		partie.enregistreLancer(7);
		partie.enregistreLancer(3);
	}

	private void faireUnStrike() {
		partie.enregistreLancer(10);
	}

}
