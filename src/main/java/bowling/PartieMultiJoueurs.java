package bowling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartieMultiJoueurs implements IPartieMultiJoueurs {
    private String[] joueurs;
    private Map<String, PartieMonoJoueur> parties;
    private int joueurCourant;
    private boolean partieDemarree;
    private boolean partieTerminee;
    
    // Stocker tous les lancers dans l'ordre pour calculer les bonus
    private List<Lancer> tousLesLancers;

    public PartieMultiJoueurs() {
        this.parties = new HashMap<>();
        this.partieDemarree = false;
        this.partieTerminee = false;
        this.joueurCourant = 0;
        this.tousLesLancers = new ArrayList<>();
    }

    // CLASSE INTERNE pour stocker les lancers avec le joueur
    private static class Lancer {
        final String joueur;
        final int quilles;

        Lancer(String joueur, int quilles) {
            this.joueur = joueur;
            this.quilles = quilles;
        }
    }

    @Override
    public String demarreNouvellePartie(String[] nomsDesJoueurs) throws IllegalArgumentException {
        if (nomsDesJoueurs == null || nomsDesJoueurs.length == 0) {
            throw new IllegalArgumentException("La liste des joueurs ne peut pas être vide");
        }

        this.joueurs = nomsDesJoueurs;
        this.parties.clear();
        this.tousLesLancers.clear();
        
        for (String joueur : joueurs) {
            parties.put(joueur, new PartieMonoJoueur());
        }

        this.joueurCourant = 0;
        this.partieDemarree = true;
        this.partieTerminee = false;

        return formaterProchainTir();
    }

    @Override
    public String enregistreLancer(int nombreDeQuillesAbattues) throws IllegalStateException {
        if (!partieDemarree) {
            throw new IllegalStateException("La partie n'est pas démarrée");
        }
        
        if (partieTerminee) {
            return "Partie terminée";
        }

        String joueurActuel = joueurs[joueurCourant];
        PartieMonoJoueur partieJoueur = parties.get(joueurActuel);
        
        // Enregistre dans la partie mono-joueur
        boolean doitRelancer = partieJoueur.enregistreLancer(nombreDeQuillesAbattues);
        
        // Enregistre aussi dans la liste globale pour les bonus
        tousLesLancers.add(new Lancer(joueurActuel, nombreDeQuillesAbattues));
        
        // CORRECTION : Vérifier si la partie est terminée pour CE JOUEUR
        if (partieJoueur.estTerminee()) {
            boolean tousTermines = true;
            for (String joueur : joueurs) {
                if (!parties.get(joueur).estTerminee()) {
                    tousTermines = false;
                    break;
                }
            }
            if (tousTermines) {
                partieTerminee = true;
                return "Partie terminée";
            }
        }
        
        // CORRECTION : Passer au prochain joueur seulement si le joueur courant a fini son tour
        // ET que la partie n'est pas terminée
        if (!doitRelancer && !partieTerminee) {
            joueurCourant = (joueurCourant + 1) % joueurs.length;
        }

        return formaterProchainTir();
    }

    @Override
    public int scorePour(String nomDuJoueur) throws IllegalArgumentException {
        if (!parties.containsKey(nomDuJoueur)) {
            throw new IllegalArgumentException("Joueur inconnu : " + nomDuJoueur);
        }
        
        // Utiliser le calcul de score avec bonus multi-joueurs
        return calculerScoreAvecBonus(nomDuJoueur);
    }

    /**
     * Calcul du score avec prise en compte des bonus entre joueurs
     */
    private int calculerScoreAvecBonus(String nomDuJoueur) {
        int score = 0;
        int index = 0;
        
        // Filtrer les lancers du joueur
        List<Integer> lancersJoueur = new ArrayList<>();
        for (Lancer lancer : tousLesLancers) {
            if (lancer.joueur.equals(nomDuJoueur)) {
                lancersJoueur.add(lancer.quilles);
            }
        }
        
        // Calcul du score selon les règles du bowling avec bonus
        for (int tour = 0; tour < 10; tour++) {
            if (index >= lancersJoueur.size()) break;
            
            if (lancersJoueur.get(index) == 10) { // STRIKE
                score += 10 + bonusStrike(nomDuJoueur, index);
                index += 1;
            } 
            else if (index + 1 < lancersJoueur.size() && 
                     lancersJoueur.get(index) + lancersJoueur.get(index + 1) == 10) { // SPARE
                score += 10 + bonusSpare(nomDuJoueur, index);
                index += 2;
            } 
            else { // NORMAL
                score += lancersJoueur.get(index);
                if (index + 1 < lancersJoueur.size()) {
                    score += lancersJoueur.get(index + 1);
                }
                index += 2;
            }
        }
        return score;
    }

    /**
     * Bonus pour un strike : les 2 prochains lancers (peu importe le joueur)
     */
    private int bonusStrike(String nomDuJoueur, int indexLancer) {
        int bonus = 0;
        int count = 0;
        
        // Trouver la position de ce lancer dans la liste globale
        int position = trouverPositionLancer(nomDuJoueur, indexLancer);
        
        // Prendre les 2 lancers suivants (peu importe le joueur)
        for (int i = position + 1; i < tousLesLancers.size() && count < 2; i++) {
            bonus += tousLesLancers.get(i).quilles;
            count++;
        }
        
        return bonus;
    }

    /**
     * Bonus pour un spare : le prochain lancer (peu importe le joueur)
     */
    private int bonusSpare(String nomDuJoueur, int indexLancer) {
        // Trouver la position de ce spare dans la liste globale
        int position = trouverPositionLancer(nomDuJoueur, indexLancer);
        
        // Prendre le lancer suivant (peu importe le joueur)
        if (position + 2 < tousLesLancers.size()) {
            return tousLesLancers.get(position + 2).quilles;
        }
        return 0;
    }

    /**
     * Trouve la position dans la liste globale d'un lancer d'un joueur
     */
    private int trouverPositionLancer(String nomDuJoueur, int indexLancer) {
        int count = -1;
        for (int i = 0; i < tousLesLancers.size(); i++) {
            if (tousLesLancers.get(i).joueur.equals(nomDuJoueur)) {
                count++;
                if (count == indexLancer) {
                    return i;
                }
            }
        }
        return -1;
    }

    private String formaterProchainTir() {
        if (partieTerminee) {
            return "Partie terminée";
        }

        String joueur = joueurs[joueurCourant];
        PartieMonoJoueur partieJoueur = parties.get(joueur);
        
        int tour = partieJoueur.numeroTourCourant();
        int boule = partieJoueur.numeroProchainLancer();

        return String.format("Prochain tir : joueur %s, tour n° %d, boule n° %d", 
                           joueur, tour, boule);
    }
}