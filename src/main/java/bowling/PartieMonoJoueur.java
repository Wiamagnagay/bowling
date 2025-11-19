package bowling;

import java.util.ArrayList;
import java.util.List;

public class PartieMonoJoueur {

    private final List<Integer> lancers = new ArrayList<>();
    private int tourCourant = 1;
    private int lancerCourant = 1;
    private boolean partieTerminee = false;

    public PartieMonoJoueur() {
        // constructeur vide
    }

    public boolean enregistreLancer(int nbQuillesAbattues) {
        if (partieTerminee) throw new IllegalStateException("La partie est terminée");

        lancers.add(nbQuillesAbattues);

        if (tourCourant < 10) {
            if (lancerCourant == 1) {
                if (nbQuillesAbattues == 10) { // Strike
                    tourCourant++;
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
            if (lancerCourant == 1) {
                lancerCourant++;
                return true;
            } else if (lancerCourant == 2) {
                int firstLancer = lancers.get(lancers.size() - 2);
                if (firstLancer == 10 || firstLancer + nbQuillesAbattues == 10) {
                    lancerCourant++;
                    return true;
                } else {
                    partieTerminee = true;
                    return false;
                }
            } else { // lancer 3 du dernier tour
                partieTerminee = true;
                return false;
            }
        }
    }

    /**
     * CORRECTION : Méthode score complètement réécrite
     */
    public int score() {
        int score = 0;
        int index = 0;
        
        for (int tour = 0; tour < 10; tour++) {
            if (index >= lancers.size()) break;
            
            if (lancers.get(index) == 10) { // STRIKE
                score += 10;
                if (index + 1 < lancers.size()) score += lancers.get(index + 1);
                if (index + 2 < lancers.size()) score += lancers.get(index + 2);
                index += 1;
            } 
            else if (index + 1 < lancers.size() && lancers.get(index) + lancers.get(index + 1) == 10) { // SPARE
                score += 10;
                if (index + 2 < lancers.size()) score += lancers.get(index + 2);
                index += 2;
            } 
            else { // NORMAL
                score += lancers.get(index);
                if (index + 1 < lancers.size()) score += lancers.get(index + 1);
                index += 2;
            }
        }
        return score;
    }

    public boolean estTerminee() {
        return partieTerminee;
    }

    public int numeroTourCourant() {
        return partieTerminee ? 0 : tourCourant;
    }

    public int numeroProchainLancer() {
        return partieTerminee ? 0 : lancerCourant;
    }
}