package bowling;

/**
 * Interface pour une partie de bowling pour plusieurs joueurs
 * @author rbastide
 */
public interface IPartieMultiJoueurs {
	
	/**
	 * Démarre une nouvelle partie pour un groupe de joueurs
	 * @param nomsDesJoueurs un tableau des noms de joueurs (il faut au moins un joueur)
	 * @return une chaîne de caractères indiquant le prochain joueur,
	 * de la forme "Prochain tir : joueur Bastide, tour n° 1, boule n° 1"
	 * @throws java.lang.IllegalArgumentException si le tableau est vide ou null
	 */
	public String demarreNouvellePartie(String[] nomsDesJoueurs) throws IllegalArgumentException ;
	
	/**
	 * Enregistre le nombre de quilles abattues pour le joueur courant, dans le tour courant, pour la boule courante
	 * @param nombreDeQuillesAbattues : nombre de quilles abattue à ce lancer
	 * @return une chaîne de caractères indiquant le prochain joueur,
	 * de la forme "Prochain tir : joueur Bastide, tour n° 5, boule n° 2",
	 * ou bien "Partie terminée" si la partie est terminée.
	 * @throws java.lang.IllegalStateException si la partie n'est pas démarrée.
	 */
	public String enregistreLancer(int nombreDeQuillesAbattues) throws IllegalStateException;
	
	/**
	 * Donne le score pour le joueur playerName
	 * @param nomDuJoueur le nom du joueur recherché
	 * @return le score pour ce joueur
	 * @throws IllegalArgumentException si nomDuJoueur ne joue pas dans cette partie
	 */
	public int scorePour(String nomDuJoueur) throws IllegalArgumentException;
	
	
}
