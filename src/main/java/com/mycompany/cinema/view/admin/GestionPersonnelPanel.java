package com.mycompany.cinema.view.admin;

import com.mycompany.cinema.Personnel;
import com.mycompany.cinema.Role;
import com.mycompany.cinema.service.AdminService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Panneau de gestion (CRUD) pour le Personnel.
 * Respecte le modèle de conception des panneaux d'administration existants.
 * La logique métier est entièrement déléguée à l'AdminService.
 */
public class GestionPersonnelPanel extends JPanel {

    private final AdminService adminService;

    private JList<Personnel> listePersonnel;
    private DefaultListModel<Personnel> listModel;

    private JTextField idField;
    private JTextField nomField;
    private JTextField prenomField;
    private JPasswordField motDePasseField;
    private JComboBox<Role> roleComboBox;

    private JButton nouveauBouton;
    private JButton enregistrerBouton;
    private JButton supprimerBouton;

    private Personnel personnelSelectionne;

    public GestionPersonnelPanel(AdminService adminService) {
        this.adminService = adminService;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initComponents();
        initListeners();

        chargerRoles();
        chargerListePersonnel();
    }

    private void initComponents() {
        // --- Panneau de gauche : Liste des membres du personnel ---
        JPanel panneauGauche = new JPanel(new BorderLayout());
        panneauGauche.setBorder(BorderFactory.createTitledBorder("Membres du personnel"));

        listModel = new DefaultListModel<Personnel>();
        listePersonnel = new JList<Personnel>(listModel);
        listePersonnel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Renderer pour afficher le nom et prénom dans la liste
        listePersonnel.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Personnel) {
                    Personnel p = (Personnel) value;
                    setText(p.getPrenom() + " " + p.getNom());
                }
                return this;
            }
        });
        panneauGauche.add(new JScrollPane(listePersonnel), BorderLayout.CENTER);

        // --- Panneau de droite : Détails ---
        JPanel panneauDroite = new JPanel(new BorderLayout());
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Détails du membre"));

        idField = new JTextField();
        idField.setEditable(false);
        nomField = new JTextField();
        prenomField = new JTextField();
        motDePasseField = new JPasswordField();
        roleComboBox = new JComboBox<Role>();

        // Renderer pour afficher le libellé du rôle dans le ComboBox
        roleComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Role) {
                    setText(((Role) value).getLibelle());
                }
                return this;
            }
        });

        formPanel.add(new JLabel("ID :"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Nom :"));
        formPanel.add(nomField);
        formPanel.add(new JLabel("Prénom :"));
        formPanel.add(prenomField);
        formPanel.add(new JLabel("Mot de passe :"));
        formPanel.add(motDePasseField);
        formPanel.add(new JLabel("Rôle :"));
        formPanel.add(roleComboBox);

        panneauDroite.add(formPanel, BorderLayout.NORTH);

        // Panneau des boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        nouveauBouton = new JButton("Nouveau");
        enregistrerBouton = new JButton("Enregistrer");
        supprimerBouton = new JButton("Supprimer");
        buttonPanel.add(nouveauBouton);
        buttonPanel.add(enregistrerBouton);
        buttonPanel.add(supprimerBouton);
        panneauDroite.add(buttonPanel, BorderLayout.SOUTH);

        add(panneauGauche, BorderLayout.CENTER);
        add(panneauDroite, BorderLayout.EAST);
    }

    private void initListeners() {
        listePersonnel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    personnelSelectionne = listePersonnel.getSelectedValue();
                    mettreAJourChamps(personnelSelectionne);
                }
            }
        });

        nouveauBouton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionNouveau();
            }
        });

        enregistrerBouton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionEnregistrer();
            }
        });
        
        supprimerBouton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionSupprimer();
            }
        });
    }

    private void chargerListePersonnel() {
        try {
            listModel.clear();
            List<Personnel> personnelList = adminService.getAllPersonnel();
            for (Personnel p : personnelList) {
                listModel.addElement(p);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement du personnel: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void chargerRoles() {
        try {
            List<Role> roles = adminService.getAllRoles();
            for (Role role : roles) {
                roleComboBox.addItem(role);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des rôles: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mettreAJourChamps(Personnel p) {
        if (p != null) {
            idField.setText(String.valueOf(p.getId()));
            nomField.setText(p.getNom());
            prenomField.setText(p.getPrenom());
            motDePasseField.setText(p.getMotDePasse());
            supprimerBouton.setEnabled(true);

            // Sélectionner le bon rôle dans le ComboBox
            Role roleAssigne = null;
            for (int i = 0; i < roleComboBox.getItemCount(); i++) {
                if (roleComboBox.getItemAt(i).getId() == p.getIdRole()) {
                    roleAssigne = roleComboBox.getItemAt(i);
                    break;
                }
            }
            roleComboBox.setSelectedItem(roleAssigne);
            
        } else {
            idField.setText("");
            nomField.setText("");
            prenomField.setText("");
            motDePasseField.setText("");
            roleComboBox.setSelectedIndex(-1);
            supprimerBouton.setEnabled(false);
        }
    }
    
    private void actionNouveau() {
        personnelSelectionne = null;
        listePersonnel.clearSelection();
        mettreAJourChamps(null);
    }

    private void actionEnregistrer() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String motDePasse = new String(motDePasseField.getPassword());
        Role roleSelectionne = (Role) roleComboBox.getSelectedItem();

        if (nom == null || nom.trim().isEmpty() || prenom == null || prenom.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le nom et le prénom ne peuvent pas être vides.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (roleSelectionne == null) {
             JOptionPane.showMessageDialog(this, "Veuillez sélectionner un rôle.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (personnelSelectionne == null) { // Création
                Personnel nouveau = new Personnel();
                nouveau.setNom(nom);
                nouveau.setPrenom(prenom);
                nouveau.setMotDePasse(motDePasse);
                nouveau.setIdRole(roleSelectionne.getId());
                adminService.ajouterPersonnel(nouveau);
                JOptionPane.showMessageDialog(this, "Membre du personnel créé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else { // Modification
                personnelSelectionne.setNom(nom);
                personnelSelectionne.setPrenom(prenom);
                personnelSelectionne.setMotDePasse(motDePasse);
                personnelSelectionne.setIdRole(roleSelectionne.getId());
                adminService.modifierPersonnel(personnelSelectionne);
                JOptionPane.showMessageDialog(this, "Membre du personnel modifié avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        chargerListePersonnel();
    }
    
    private void actionSupprimer() {
        if (personnelSelectionne == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un membre à supprimer.", "Action impossible", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmation = JOptionPane.showConfirmDialog(this, 
                "Êtes-vous sûr de vouloir supprimer '" + personnelSelectionne.getPrenom() + " " + personnelSelectionne.getNom() + "' ?", 
                "Confirmation de suppression", 
                JOptionPane.YES_NO_OPTION);
                
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                adminService.supprimerPersonnel(personnelSelectionne.getId());
                JOptionPane.showMessageDialog(this, "Membre supprimé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                actionNouveau();
                chargerListePersonnel();
            } catch (Exception ex) {
                 JOptionPane.showMessageDialog(this, "Erreur lors de la suppression : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}