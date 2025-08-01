package com.mycompany.cinema.view;

import com.mycompany.cinema.ProduitSnack;
import com.mycompany.cinema.Siege;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Ceci est une classe "interne" simple pour transporter toutes les informations
 * nécessaires à l'affichage d'un billet/confirmation de commande.
 * Au lieu de passer 10 paramètres au constructeur de BilletDialog, on remplit
 * cet objet et on ne passe que lui. C'est plus propre.
 */
class BilletInfo {
    String filmTitre;
    String seanceDateHeure;
    String salleNumero;
    List<Siege> sieges;
    String clientNom;
    int reservationId;
    String tarifLibelle;
    String prixTotal;
    Map<ProduitSnack, Integer> panierSnacks;
}

/**
 * Mon binôme : C'est la fenêtre de dialogue (JDialog) qui s'affiche à la toute fin
 * du processus de réservation/commande pour montrer le récapitulatif.
 * Elle est "modale" (true), ce qui veut dire qu'elle bloque la fenêtre principale
 * tant qu'elle n'est pas fermée.
 */
public class BilletDialog extends JDialog {

    // Le texte formaté du billet, généré une seule fois dans le constructeur.
    private final String contenuBillet;

    /**
     * Constructeur de la boîte de dialogue.
     * @param owner La fenêtre parente (le ClientMainFrame).
     * @param infos L'objet contenant toutes les données à afficher.
     */
    public BilletDialog(Frame owner, BilletInfo infos) {
        super(owner, "Confirmation de Commande", true);
        // On construit le texte du billet à partir des informations reçues.
        this.contenuBillet = buildContenuBillet(infos);
        // On initialise les composants graphiques.
        initComponents();
    }

    /**
     * Construit et configure l'interface graphique de la fenêtre.
     */
    private void initComponents() {
        setSize(450, 600);
        setLocationRelativeTo(getOwner()); // Se centre sur la fenêtre principale.
        setLayout(new BorderLayout(10, 10)); // Layout principal avec des marges.

        // Une zone de texte pour afficher le billet. Elle n'est pas éditable.
        JTextArea billetTextArea = new JTextArea(contenuBillet);
        billetTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Police à espacement fixe pour un look "ticket".
        billetTextArea.setEditable(false);
        billetTextArea.setMargin(new Insets(10, 10, 10, 10)); // Marge intérieure.
        add(new JScrollPane(billetTextArea), BorderLayout.CENTER); // On l'ajoute dans un conteneur à barres de défilement.

        // Un panneau en bas pour les boutons, alignés à droite.
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton exportButton = new JButton("Exporter en .txt");
        JButton closeButton = new JButton("Fermer");
        
        buttonPanel.add(exportButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Écouteurs d'événements pour les boutons ---
        // Le bouton "Fermer" appelle simplement dispose() pour fermer cette fenêtre.
        closeButton.addActionListener(e -> dispose());

        // Le bouton "Exporter" appelle la méthode qui gère la sauvegarde en fichier texte.
        exportButton.addActionListener(e -> handleExport());
    }

    /**
     * Gère la logique d'exportation du billet en fichier texte.
     */
    private void handleExport() {
        // Ouvre une boîte de dialogue standard "Enregistrer sous...".
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Enregistrer la confirmation");
        fileChooser.setSelectedFile(new File("Confirmation_Commande.txt")); // Nom de fichier par défaut.
        
        // Si l'utilisateur clique sur "Enregistrer"...
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            // On écrit le contenu du billet dans le fichier choisi.
            // Le "try-with-resources" garantit que le fichier est bien fermé.
            try (FileWriter writer = new FileWriter(fileToSave)) {
                writer.write(contenuBillet);
                JOptionPane.showMessageDialog(this, "Confirmation exportée avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                // En cas d'erreur (ex: disque protégé en écriture), on affiche un message.
                JOptionPane.showMessageDialog(this, "Erreur lors de l'exportation : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Construit la chaîne de caractères formatée représentant le billet.
     * @param infos L'objet contenant les données de la commande.
     * @return Une chaîne de caractères prête à être affichée ou exportée.
     */
    private String buildContenuBillet(BilletInfo infos) {
    // On initialise une chaîne de caractères vide.
    String contenu = "";

    // On utilise l'opérateur += pour ajouter chaque nouvelle partie.
    contenu += "****************************************\n";
    contenu += "*             PISE CINEMA              *\n";
    contenu += "****************************************\n\n";
    contenu += "RÉCAPITULATIF DE COMMANDE\n";
    contenu += "----------------------------------------\n";
    contenu += "Client: " + infos.clientNom + "\n";
    contenu += "Réservation N°: " + infos.reservationId + "\n\n";
    contenu += "--- BILLETS ---\n";
    contenu += "Film: " + infos.filmTitre + "\n";
    contenu += "Séance: " + infos.seanceDateHeure + "\n";
    contenu += "Salle: " + infos.salleNumero + "\n";
    contenu += "Sièges Réservés:\n";
    
    // La concaténation se produit également à l'intérieur de la boucle.
    for (Siege siege : infos.sieges) {
        contenu += "  - Rangée " + siege.getNumeroRangee() + ", Siège " + siege.getNumeroSiege() + "\n";
    }
    
    contenu += "Tarif Appliqué: " + infos.tarifLibelle + "\n";

    if (infos.panierSnacks != null && !infos.panierSnacks.isEmpty()) {
        contenu += "\n--- SNACKS ---\n";
        for(Map.Entry<ProduitSnack, Integer> entry : infos.panierSnacks.entrySet()){
            contenu += "  - " + entry.getValue() + "x " + entry.getKey().getNomProduit() + "\n";
        }
    }

    contenu += "\n----------------------------------------\n";
    contenu += "PRIX TOTAL: " + infos.prixTotal + "\n";
    contenu += "----------------------------------------\n\n";
    contenu += "Merci de votre visite et bonne séance !\n";

    // On retourne la chaîne finale.
    return contenu;
}
}