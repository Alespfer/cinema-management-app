package com.mycompany.cinema.view;

import com.mycompany.cinema.Client;
import com.mycompany.cinema.EvaluationClient;
import com.mycompany.cinema.Film;
import com.mycompany.cinema.Seance;
import com.mycompany.cinema.service.ClientService;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Image;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
// CORRECTION : L'import de 'Optional' a été supprimé. Il est interdit.
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;

public class FilmDetail extends javax.swing.JPanel {

    private final ClientService clientService;
    private final Frame parentFrame;
    private int clientId;
    private Film filmActuel;
    private LocalDate dateActuelle;
    private DefaultListModel<Seance> seanceListModel;
    private DefaultListModel<EvaluationClient> evaluationsListModel;
    private SeanceSelectionListener seanceSelectionListener;
    private RetourListener retourListener;

    public interface SeanceSelectionListener {

        void onSeanceSelected(Seance seance);
    }

    public interface RetourListener {

        void onRetourClicked();
    }

    public FilmDetail(ClientService clientService, Frame parentFrame) {
        this.clientService = clientService;
        this.parentFrame = parentFrame;
        initComponents();
        setupModelsAndRenderers();
    }

    private void setupModelsAndRenderers() {
        seanceListModel = new DefaultListModel<>();
        seanceJList.setModel(seanceListModel);
        evaluationsListModel = new DefaultListModel<>();
        evaluationsJList.setModel(evaluationsListModel);

        seanceJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Seance) {
                    Seance seance = (Seance) value;
                    setText("Horaire : " + seance.getDateHeureDebut().format(DateTimeFormatter.ofPattern("HH:mm")) + " - Salle " + seance.getIdSalle());
                }
                return this;
            }
        });

        evaluationsJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof EvaluationClient) {
                    EvaluationClient eval = (EvaluationClient) value;

                    // CORRECTION : Appel direct à la méthode et vérification de nullité.
                    Client client = clientService.getClientById(eval.getIdClient());
                    String nomClient = "Utilisateur anonyme";
                    if (client != null) {
                        nomClient = client.getNom();
                    }

                    String commentaire = eval.getCommentaire().isEmpty() ? "<i>(pas de commentaire)</i>" : eval.getCommentaire();
                    setText("<html><b>" + nomClient + "</b> (" + eval.getNote() + "/5 ★)<br>" + commentaire + "</html>");
                }
                return this;
            }
        });
    }

    public void displayFilmAndSeances(Film film, LocalDate date, int connectedClientId) {
        this.filmActuel = film;
        this.clientId = connectedClientId;
        this.dateActuelle = date;

        if (film != null) {
            titleLabel.setText(film.getTitre());
            infoLabel.setText("Durée: " + film.getDureeMinutes() + " min | Classification: " + film.getClassification());
            synopsisArea.setText(film.getSynopsis());
            ImageIcon posterIcon = new ImageIcon("images/" + film.getUrlAffiche());
            Image image = posterIcon.getImage().getScaledInstance(300, 450, Image.SCALE_SMOOTH);
            posterLabel.setIcon(new ImageIcon(image));
            notePresseLabel.setText("Presse: " + String.format("%.1f", film.getNotePresse()) + " / 5");
            double moyenneSpectateurs = clientService.getNoteMoyenneSpectateurs(film.getId());
            noteSpectateursLabel.setText("Spectateurs: " + (moyenneSpectateurs > 0 ? String.format("%.1f", moyenneSpectateurs) + " / 5" : "N/A"));

            evaluationsListModel.clear();
            List<EvaluationClient> evaluations = clientService.getEvaluationsByFilmId(film.getId());
            for (int i = evaluations.size() - 1; i >= 0; i--) {
                evaluationsListModel.addElement(evaluations.get(i));
            }

            boolean aEvalue = clientService.aDejaEvalue(this.clientId, film.getId());
            noterButton.setText(aEvalue ? "Modifier mon avis" : "Donner un avis");
            noterButton.setEnabled(true);

            seanceListModel.clear();
            List<Seance> seances = clientService.getSeancesPourFilmEtDate(film.getId(), date);
            for (Seance seance : seances) {
                seanceListModel.addElement(seance);
            }
        } else {
            clearPanel();
        }
    }

    public void clearPanel() {
        titleLabel.setText("Aucun film sélectionné");
        infoLabel.setText("");
        synopsisArea.setText("");
        posterLabel.setIcon(null);
        seanceListModel.clear();
        notePresseLabel.setText("Presse: N/A");
        noteSpectateursLabel.setText("Spectateurs: N/A");
        evaluationsListModel.clear();
        noterButton.setText("Donner un avis");
        noterButton.setEnabled(false);
    }

    public void setSeanceSelectionListener(SeanceSelectionListener listener) {
        this.seanceSelectionListener = listener;
    }

    public void setRetourListener(RetourListener listener) {
        this.retourListener = listener;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        posterLabel = new javax.swing.JLabel();
        retourButton = new javax.swing.JButton();
        northPanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        infoLabel = new javax.swing.JLabel();
        notesPanel = new javax.swing.JPanel();
        notePresseLabel = new javax.swing.JLabel();
        noteSpectateursLabel = new javax.swing.JLabel();
        middleContentPanel = new javax.swing.JPanel();
        synopsisScrollPane = new javax.swing.JScrollPane();
        synopsisArea = new javax.swing.JTextArea();
        seanceScrollPane = new javax.swing.JScrollPane();
        seanceJList = new javax.swing.JList<>();
        evaluationsPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        evaluationsJList = new javax.swing.JList<>();
        noterButton = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new java.awt.BorderLayout(15, 15));

        jPanel1.setPreferredSize(new java.awt.Dimension(300, 450));
        jPanel1.setLayout(new java.awt.BorderLayout(10, 10));

        posterLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(posterLabel, java.awt.BorderLayout.CENTER);

        retourButton.setText("<< Retour à la programmation");
        retourButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                retourButtonActionPerformed(evt);
            }
        });
        jPanel1.add(retourButton, java.awt.BorderLayout.PAGE_END);

        add(jPanel1, java.awt.BorderLayout.LINE_START);

        northPanel.setLayout(new java.awt.BorderLayout());

        titleLabel.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("Titre du Film");
        northPanel.add(titleLabel, java.awt.BorderLayout.PAGE_START);

        infoLabel.setFont(new java.awt.Font("Arial", 2, 14)); // NOI18N
        infoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        infoLabel.setText("Durée | Classification");
        northPanel.add(infoLabel, java.awt.BorderLayout.CENTER);

        notesPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 5));

        notePresseLabel.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        notePresseLabel.setText("Presse : N/A");
        notesPanel.add(notePresseLabel);

        noteSpectateursLabel.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        noteSpectateursLabel.setText("Spectateurs : N/A");
        notesPanel.add(noteSpectateursLabel);

        northPanel.add(notesPanel, java.awt.BorderLayout.PAGE_END);

        add(northPanel, java.awt.BorderLayout.PAGE_START);

        middleContentPanel.setLayout(new java.awt.BorderLayout());

        synopsisArea.setColumns(20);
        synopsisArea.setRows(5);
        synopsisArea.setText("La synopsis du film apparaîtra ici...");
        synopsisScrollPane.setViewportView(synopsisArea);

        middleContentPanel.add(synopsisScrollPane, java.awt.BorderLayout.PAGE_START);

        seanceScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Choisissez un horaire pour ce film"));

        seanceJList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                seanceJListValueChanged(evt);
            }
        });
        seanceScrollPane.setViewportView(seanceJList);

        middleContentPanel.add(seanceScrollPane, java.awt.BorderLayout.CENTER);

        evaluationsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Avis des spectateurs"));
        evaluationsPanel.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setViewportView(evaluationsJList);

        evaluationsPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        noterButton.setText("Donner une note");
        noterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noterButtonActionPerformed(evt);
            }
        });
        evaluationsPanel.add(noterButton, java.awt.BorderLayout.PAGE_END);

        middleContentPanel.add(evaluationsPanel, java.awt.BorderLayout.PAGE_END);

        add(middleContentPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void retourButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_retourButtonActionPerformed
        if (retourListener != null) {
            retourListener.onRetourClicked();
        } // TODO add your handling code here:
    }//GEN-LAST:event_retourButtonActionPerformed

    private void noterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noterButtonActionPerformed
        if (filmActuel != null) {
            // CORRECTION : Appel direct à la méthode et vérification de nullité.
            EvaluationClient eval = clientService.getEvaluation(clientId, filmActuel.getId());

            if (eval != null) { // Si une évaluation existe déjà, on la modifie.
                Evaluation dialog = new Evaluation(this.parentFrame, clientService, eval);
                dialog.setVisible(true);
            } else { // Sinon, on en crée une nouvelle.
                Evaluation dialog = new Evaluation(this.parentFrame, clientService, clientId, filmActuel.getId());
                dialog.setVisible(true);
            }
            // On rafraîchit l'affichage pour voir les changements.
            displayFilmAndSeances(filmActuel, dateActuelle, clientId);
        }
    }//GEN-LAST:event_noterButtonActionPerformed

    private void seanceJListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_seanceJListValueChanged
        if (!evt.getValueIsAdjusting() && seanceJList.getSelectedValue() != null && seanceSelectionListener != null) {
            seanceSelectionListener.onSeanceSelected(seanceJList.getSelectedValue());
        }// TODO add your handling code here:
    }//GEN-LAST:event_seanceJListValueChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<EvaluationClient> evaluationsJList;
    private javax.swing.JPanel evaluationsPanel;
    private javax.swing.JLabel infoLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel middleContentPanel;
    private javax.swing.JPanel northPanel;
    private javax.swing.JLabel notePresseLabel;
    private javax.swing.JLabel noteSpectateursLabel;
    private javax.swing.JButton noterButton;
    private javax.swing.JPanel notesPanel;
    private javax.swing.JLabel posterLabel;
    private javax.swing.JButton retourButton;
    private javax.swing.JList<Seance> seanceJList;
    private javax.swing.JScrollPane seanceScrollPane;
    private javax.swing.JTextArea synopsisArea;
    private javax.swing.JScrollPane synopsisScrollPane;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
