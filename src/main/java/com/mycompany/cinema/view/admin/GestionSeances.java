package com.mycompany.cinema.view.admin;

import com.mycompany.cinema.Film;
import com.mycompany.cinema.Salle;
import com.mycompany.cinema.Seance;
import com.mycompany.cinema.service.AdminService;
import java.awt.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;

public class GestionSeances extends javax.swing.JPanel {

    private final AdminService adminService;
    private DefaultListModel<Seance> seanceListModel;
    private DefaultComboBoxModel<Film> filmComboBoxModel;
    private DefaultComboBoxModel<Salle> salleComboBoxModel;
    private Seance seanceSelectionnee;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public GestionSeances(AdminService adminService) {
        this.adminService = adminService;

        initComponents();
        initModelsAndRenderers();

        rafraichirDonnees();
    }

    private void initModelsAndRenderers() {
        seanceListModel = new DefaultListModel<>();
        jListSeances.setModel(seanceListModel);

        filmComboBoxModel = new DefaultComboBoxModel<>();
        jComboBoxFilm.setModel(filmComboBoxModel);

        salleComboBoxModel = new DefaultComboBoxModel<>();
        jComboBoxSalle.setModel(salleComboBoxModel);

        jListSeances.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Seance) {
                    Seance seance = (Seance) value;
                    Film film = adminService.getFilmDetails(seance.getIdFilm());
                    String filmTitre = (film != null) ? film.getTitre() : "Film inconnu";
                    setText(filmTitre + " - " + seance.getDateHeureDebut().format(FORMATTER));
                }
                return this;
            }
        });

        jComboBoxFilm.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Film) {
                    setText(((Film) value).getTitre());
                }
                return this;
            }
        });

        jComboBoxSalle.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Salle) {
                    setText(((Salle) value).getNumero());
                }
                return this;
            }
        });
    }

    private void chargementSeances() {
        seanceListModel.clear();
        List<Seance> seances = adminService.getAllSeances();
        for (Seance seance : seances) {
            seanceListModel.addElement(seance);
        }
    }

    private void chargementFilmsEtSalles() {
        filmComboBoxModel.removeAllElements();
        List<Film> films = adminService.getFilmsAffiche();
        for (Film film : films) {
            filmComboBoxModel.addElement(film);
        }

        salleComboBoxModel.removeAllElements();
        List<Salle> salles = adminService.getAllSalles();
        for (Salle salle : salles) {
            salleComboBoxModel.addElement(salle);
        }
    }

    // Dans la classe GestionSeances (déjà existante)
    public void rafraichirDonnees() {
        chargementFilmsEtSalles();
        chargementSeances();
    }

    private void displaySeanceDetails(Seance seance) {
        if (seance != null) {
            jTextFieldDateHeure.setText(seance.getDateHeureDebut().format(FORMATTER));

            // --- DEBUT DE LA CORRECTION ---
            // Sélectionner le bon film
            for (int i = 0; i < filmComboBoxModel.getSize(); i++) {
                if (filmComboBoxModel.getElementAt(i).getId() == seance.getIdFilm()) {
                    jComboBoxFilm.setSelectedIndex(i);
                    break;
                }
            }

            // Sélectionner la bonne salle
            for (int i = 0; i < salleComboBoxModel.getSize(); i++) {
                if (salleComboBoxModel.getElementAt(i).getId() == seance.getIdSalle()) {
                    jComboBoxSalle.setSelectedIndex(i);
                    break;
                }
            }
            // --- FIN DE LA CORRECTION ---

            jButtonSupprimer.setEnabled(true);
        } else {
            clearForm();
        }
    }

    private void clearForm() {
        jListSeances.clearSelection();
        seanceSelectionnee = null;
        jTextFieldDateHeure.setText("");
        jComboBoxFilm.setSelectedIndex(-1);
        jComboBoxSalle.setSelectedIndex(-1);
        jButtonSupprimer.setEnabled(false);
    }

    private void saveSeance() {
        try {
            LocalDateTime dateHeure = LocalDateTime.parse(jTextFieldDateHeure.getText(), FORMATTER);
            Film filmSelectionne = (Film) jComboBoxFilm.getSelectedItem();
            Salle salleSelectionnee = (Salle) jComboBoxSalle.getSelectedItem();

            if (filmSelectionne == null || salleSelectionnee == null) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un film et une salle.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (seanceSelectionnee == null) {
                int nouvelId = com.mycompany.cinema.util.IdManager.getNextSeanceId();
                Seance nouvelleSeance = new Seance(nouvelId, dateHeure, salleSelectionnee.getId(), filmSelectionne.getId());
                adminService.ajouterSeance(nouvelleSeance);
                JOptionPane.showMessageDialog(this, "Séance ajoutée avec succès !");
            } else {
                seanceSelectionnee.setDateHeureDebut(dateHeure);
                seanceSelectionnee.setIdFilm(filmSelectionne.getId());
                seanceSelectionnee.setIdSalle(salleSelectionnee.getId());
                adminService.modifierSeance(seanceSelectionnee);
                JOptionPane.showMessageDialog(this, "Séance mise à jour avec succès !");
            }
            rafraichirDonnees();
            clearForm();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Format de date et heure invalide. Utilisez JJ/MM/AAAA HH:MM.", "Erreur de Format", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSeance() {
        if (seanceSelectionnee == null) {
            return;
        }
        int response = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer cette séance ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            try {
                adminService.supprimerSeance(seanceSelectionnee.getId());
                JOptionPane.showMessageDialog(this, "Séance supprimée avec succès !");
                rafraichirDonnees();
                clearForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jListSeances = new javax.swing.JList<>();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldDateHeure = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jComboBoxFilm = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jComboBoxSalle = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jButtonNouveau = new javax.swing.JButton();
        jButtonEnregistrer = new javax.swing.JButton();
        jButtonSupprimer = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout(10, 10));

        jScrollPane1.setPreferredSize(new java.awt.Dimension(300, 130));

        jListSeances.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListSeances.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListSeancesValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jListSeances);

        add(jScrollPane1, java.awt.BorderLayout.WEST);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Détails de la Séance"));
        jPanel2.setLayout(new java.awt.GridLayout(0, 2, 5, 5));

        jLabel1.setText("Date et Heure (JJ/MM/AAAA HH:MM) :");
        jPanel2.add(jLabel1);
        jPanel2.add(jTextFieldDateHeure);

        jLabel2.setText("Film :");
        jPanel2.add(jLabel2);
        jPanel2.add(jComboBoxFilm);

        jLabel3.setText("Salle :");
        jPanel2.add(jLabel3);
        jPanel2.add(jComboBoxSalle);

        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);

        jButtonNouveau.setText("Nouveau");
        jButtonNouveau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNouveauActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonNouveau);

        jButtonEnregistrer.setText("Enregistrer");
        jButtonEnregistrer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEnregistrerActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonEnregistrer);

        jButtonSupprimer.setText("Supprimer");
        jButtonSupprimer.setEnabled(false);
        jButtonSupprimer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupprimerActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonSupprimer);

        jPanel1.add(jPanel3, java.awt.BorderLayout.SOUTH);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>                        

    private void jListSeancesValueChanged(javax.swing.event.ListSelectionEvent evt) {
        if (!evt.getValueIsAdjusting()) {
            seanceSelectionnee = jListSeances.getSelectedValue();
            displaySeanceDetails(seanceSelectionnee);
        }
    }

    private void jButtonNouveauActionPerformed(java.awt.event.ActionEvent evt) {
        clearForm();
    }

    private void jButtonEnregistrerActionPerformed(java.awt.event.ActionEvent evt) {
        saveSeance();
    }

    private void jButtonSupprimerActionPerformed(java.awt.event.ActionEvent evt) {
        deleteSeance();
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton jButtonEnregistrer;
    private javax.swing.JButton jButtonNouveau;
    private javax.swing.JButton jButtonSupprimer;
    private javax.swing.JComboBox<Film> jComboBoxFilm;
    private javax.swing.JComboBox<Salle> jComboBoxSalle;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JList<com.mycompany.cinema.Seance> jListSeances;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextFieldDateHeure;
    // End of variables declaration                   
}
