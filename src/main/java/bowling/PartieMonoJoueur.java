package bowling;

import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe a pour but d'enregistrer le nombre de quilles abattues lors des
 * lancers successifs d'un seul et même joueur, et de calculer le score final de ce joueur
 */
public class PartieMonoJoueur {

    private final List<Integer> lancers = new ArrayList<>();
    private int tourCourant = 1;
    private int lancerCourant = 1;
    private boolean partieTerminee = false;

    public PartieMonoJoueur() {
        // constructeur vide
    }

    /**
     * Cette méthode doit être appelée à chaque lancer de boule
     *
     * @param nbQuillesAbattues le nombre de quilles abattues lors de ce lancer
     * @throws IllegalStateException si la partie est terminée
     * @return vrai si le joueur doit lancer à nouveau pour continuer son tour, faux sinon
     */
    public boolean enregistreLancer(int nbQuillesAbattues) {
        if (partieTerminee) throw new IllegalStateException("La partie est terminée");

        lancers.add(nbQuillesAbattues);

        if (tourCourant < 10) {
            // Tours 1 à 9
            if (lancerCourant == 1) {
                if (nbQuillesAbattues == 10) { // Strike
                    tourCourant++;
                    lancerCourant = 1;
                    return false;
                } else {
                    lancerCourant++;
                    return true;
                }
            } else { // deuxième lancer
                tourCourant++;
                lancerCourant = 1;
                return false;
            }
        } else {
            // 10ème tour
            if (lancerCourant == 1) {
                lancerCourant++;
                return true;
            } else if (lancerCourant == 2) {
                int firstLancer = lancers.get(lancers.size() - 2);
                if (firstLancer + nbQuillesAbattues >= 10) { // Spare ou Strike
                    lancerCourant++;
                    return true;
                } else {
                    partieTerminee = true;
                    lancerCourant = 0;
                    tourCourant = 0;
                    return false;
                }
            } else { // lancer 3 du dernier tour
                partieTerminee = true;
                lancerCourant = 0;
                tourCourant = 0;
                return false;
            }
        }
    }

    /**
     * Cette méthode donne le score du joueur.
     * Si la partie n'est pas terminée, on considère que les lancers restants abattent 0 quille.
     * @return Le score du joueur
     */
    public int score() {
        int score = 0;
        int index = 0;
        for (int tour = 0; tour < 10; tour++) {
            if (index >= lancers.size()) break;
            int b1 = lancers.get(index);
            int b2 = (index + 1 < lancers.size()) ? lancers.get(index + 1) : 0;

            if (b1 == 10) { // Strike
                int bonus1 = (index + 1 < lancers.size()) ? lancers.get(index + 1) : 0;
                int bonus2 = (index + 2 < lancers.size()) ? lancers.get(index + 2) : 0;
                score += 10 + bonus1 + bonus2;
                index++;
            } else if (b1 + b2 == 10) { // Spare
                int bonus = (index + 2 < lancers.size()) ? lancers.get(index + 2) : 0;
                score += 10 + bonus;
                index += 2;
            } else { // simple
                score += b1 + b2;
                index += 2;
            }
        }
        return score;
    }

    /**
     * @return vrai si la partie est terminée pour ce joueur, faux sinon
     */
    public boolean estTerminee() {
        return partieTerminee;
    }

    /**
     * @return Le numéro du tour courant [1..10], ou 0 si le jeu est fini
     */
    public int numeroTourCourant() {
        return partieTerminee ? 0 : tourCourant;
    }

    /**
     * @return Le numéro du prochain lancer pour tour courant [1..3], ou 0 si le jeu est fini
     */
    public int numeroProchainLancer() {
        return partieTerminee ? 0 : lancerCourant;
    }
}
