package com.mycompany.cinema.view;

import com.mycompany.cinema.Siege;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Fenêtre de dialogue affichant un billet de cinéma après une réservation réussie.
 * Fournit une représentation visuelle de la confirmation d'achat et permet
 * d'exporter ces informations dans un fichier texte.
 */
public class BilletDialog extends JDialog {

    // Le contenu textuel du billet, conservé pour l'exportation.
    private final String contenuBillet;

    /**
     * Constructeur du dialogue de billet.
     * @param owner La fenêtre parente (pour le positionnement et le comportement modal).
     * @param infos Un objet contenant toutes les informations formatées à afficher.
     */
    public BilletDialog(Frame owner, BilletInfo infos) {
        super(owner, "Confirmation de Réservation", true);
        
        // Construction du contenu textuel du billet.
        this.contenuBillet = buildContenuBillet(infos);
        
        // Initialisation de l'interface graphique.
        initComponents();
    }

    /**
     * Construit et organise les composants graphiques du dialogue.
     */
    private void initComponents() {
        setSize(450, 500);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(10, 10));

        // Le JTextArea est utilisé pour afficher le billet. Il est non-éditable.
        JTextArea billetTextArea = new JTextArea(contenuBillet);
        billetTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Police à espacement fixe pour un bon alignement.
        billetTextArea.setEditable(false);
        billetTextArea.setMargin(new Insets(10, 10, 10, 10));
        add(new JScrollPane(billetTextArea), BorderLayout.CENTER);

        // Panneau inférieur pour les boutons d'action.
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton exportButton = new JButton("Exporter en .txt");
        JButton closeButton = new JButton("Fermer");
        
        buttonPanel.add(exportButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Association des actions aux boutons (sans lambdas).
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Ferme simplement la fenêtre de dialogue.
            }
        });

        exportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleExport();
            }
        });
    }

    /**
     * Gère la logique d'exportation du contenu du billet dans un fichier texte.
     */
    private void handleExport() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Enregistrer le billet");
        fileChooser.setSelectedFile(new File("Billet_Reservation.txt"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            // Utilisation d'un try-with-resources pour garantir la fermeture du FileWriter.
            try (FileWriter writer = new FileWriter(fileToSave)) {
                writer.write(contenuBillet);
                JOptionPane.showMessageDialog(this, "Billet exporté avec succès !", "Exportation réussie", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'exportation du fichier : " + ex.getMessage(), "Erreur d'écriture", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Méthode utilitaire qui assemble la chaîne de caractères formatée du billet.
     * @param infos Les données de la réservation.
     * @return Une chaîne de caractères représentant le billet.
     */
    private String buildContenuBillet(BilletInfo infos) {
        StringBuilder sb = new StringBuilder();
        sb.append("****************************************\n");
        sb.append("*             PISE CINEMA              *\n");
        sb.append("****************************************\n\n");
        sb.append("BILLET DE RESERVATION\n");
        sb.append("----------------------------------------\n");
        sb.append("Client: ").append(infos.clientNom).append("\n");
        sb.append("Réservation N°: ").append(infos.reservationId).append("\n\n");
        sb.append("Film: ").append(infos.filmTitre).append("\n");
        sb.append("Séance: ").append(infos.seanceDateHeure).append("\n");
        sb.append("Salle: ").append(infos.salleNumero).append("\n\n");
        sb.append("Sièges Réservés:\n");
        for (Siege siege : infos.sieges) {
            sb.append("  - Rangée ").append(siege.getNumeroRangee())
              .append(", Siège ").append(siege.getNumeroSiege()).append("\n");
        }
        sb.append("\n----------------------------------------\n");
        sb.append("Tarif Appliqué: ").append(infos.tarifLibelle).append("\n");
        sb.append("PRIX TOTAL: ").append(infos.prixTotal).append("\n");
        sb.append("----------------------------------------\n\n");
        sb.append("Merci de votre visite et bonne séance !\n");

        return sb.toString();
    }
}

/**
 * Classe de support (DTO - Data Transfer Object) pour passer de manière propre
 * toutes les informations nécessaires à la création du billet.
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
}