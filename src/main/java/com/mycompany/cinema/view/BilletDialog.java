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

class BilletInfo {
    String filmTitre;
    String seanceDateHeure;
    String salleNumero;
    List<Siege> sieges;
    String clientNom;
    int reservationId;
    String tarifLibelle;
    String prixTotal;
    Map<ProduitSnack, Integer> panierSnacks; // CHAMP AJOUTÉ
}

public class BilletDialog extends JDialog {

    private final String contenuBillet;

    public BilletDialog(Frame owner, BilletInfo infos) {
        super(owner, "Confirmation de Commande", true);
        this.contenuBillet = buildContenuBillet(infos);
        initComponents();
    }

    private void initComponents() {
        setSize(450, 600);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(10, 10));

        JTextArea billetTextArea = new JTextArea(contenuBillet);
        billetTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        billetTextArea.setEditable(false);
        billetTextArea.setMargin(new Insets(10, 10, 10, 10));
        add(new JScrollPane(billetTextArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton exportButton = new JButton("Exporter en .txt");
        JButton closeButton = new JButton("Fermer");
        
        buttonPanel.add(exportButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { dispose(); }
        });
        exportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { handleExport(); }
        });
    }

    private void handleExport() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Enregistrer la confirmation");
        fileChooser.setSelectedFile(new File("Confirmation_Commande.txt"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(fileToSave)) {
                writer.write(contenuBillet);
                JOptionPane.showMessageDialog(this, "Confirmation exportée avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'exportation : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String buildContenuBillet(BilletInfo infos) {
        StringBuilder sb = new StringBuilder();
        sb.append("****************************************\n");
        sb.append("*             PISE CINEMA              *\n");
        sb.append("****************************************\n\n");
        sb.append("RÉCAPITULATIF DE COMMANDE\n");
        sb.append("----------------------------------------\n");
        sb.append("Client: ").append(infos.clientNom).append("\n");
        sb.append("Réservation N°: ").append(infos.reservationId).append("\n\n");
        sb.append("--- BILLETS ---\n");
        sb.append("Film: ").append(infos.filmTitre).append("\n");
        sb.append("Séance: ").append(infos.seanceDateHeure).append("\n");
        sb.append("Salle: ").append(infos.salleNumero).append("\n");
        sb.append("Sièges Réservés:\n");
        for (Siege siege : infos.sieges) {
            sb.append("  - Rangée ").append(siege.getNumeroRangee())
              .append(", Siège ").append(siege.getNumeroSiege()).append("\n");
        }
        sb.append("Tarif Appliqué: ").append(infos.tarifLibelle).append("\n");

        if (infos.panierSnacks != null && !infos.panierSnacks.isEmpty()) {
            sb.append("\n--- SNACKS ---\n");
            for(Map.Entry<ProduitSnack, Integer> entry : infos.panierSnacks.entrySet()){
                sb.append("  - ").append(entry.getValue()).append("x ")
                  .append(entry.getKey().getNomProduit()).append("\n");
            }
        }

        sb.append("\n----------------------------------------\n");
        sb.append("PRIX TOTAL: ").append(infos.prixTotal).append("\n");
        sb.append("----------------------------------------\n\n");
        sb.append("Merci de votre visite et bonne séance !\n");

        return sb.toString();
    }
}