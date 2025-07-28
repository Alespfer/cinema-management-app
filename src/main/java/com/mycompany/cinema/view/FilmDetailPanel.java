package com.mycompany.cinema.view;

import com.mycompany.cinema.EvaluationClient;
import com.mycompany.cinema.Film;
import com.mycompany.cinema.Seance;
import com.mycompany.cinema.service.ClientService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class FilmDetailPanel extends JPanel {

    private final ClientService clientService;
    private int clientId;
    private Film filmActuel;
    private LocalDate dateActuelle;

    private JLabel titleLabel, posterLabel, infoLabel;
    private JTextArea synopsisArea;
    private JList<Seance> seanceJList;
    private DefaultListModel<Seance> seanceListModel;
    
    private JLabel notePresseLabel;
    private JLabel noteSpectateursLabel;
    private JButton noterButton;
    private JList<EvaluationClient> evaluationsJList;
    private DefaultListModel<EvaluationClient> evaluationsListModel;
    private static final DecimalFormat NOTE_FORMATTER = new DecimalFormat("0.0 / 5");

    public interface SeanceSelectionListener { void onSeanceSelected(Seance seance); }
    private SeanceSelectionListener seanceSelectionListener;
    public interface RetourListener { void onRetourClicked(); }
    private RetourListener retourListener;

    public FilmDetailPanel(ClientService clientService) {
        this.clientService = clientService;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        posterLabel = new JLabel();
        posterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JButton retourButton = new JButton("<< Retour à la programmation");
        leftPanel.add(posterLabel, BorderLayout.CENTER);
        leftPanel.add(retourButton, BorderLayout.SOUTH);
        add(leftPanel, BorderLayout.WEST);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        
        JPanel northPanel = new JPanel(new BorderLayout());
        titleLabel = new JLabel("Titre du film", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        infoLabel = new JLabel("Durée | Classification", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        
        JPanel notesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        notePresseLabel = new JLabel("Presse: N/A");
        noteSpectateursLabel = new JLabel("Spectateurs: N/A");
        notePresseLabel.setFont(new Font("Arial", Font.BOLD, 16));
        noteSpectateursLabel.setFont(new Font("Arial", Font.BOLD, 16));
        notesPanel.add(notePresseLabel);
        notesPanel.add(noteSpectateursLabel);
        
        northPanel.add(titleLabel, BorderLayout.NORTH);
        northPanel.add(infoLabel, BorderLayout.CENTER);
        northPanel.add(notesPanel, BorderLayout.SOUTH);

        synopsisArea = new JTextArea("Synopsis...");
        synopsisArea.setWrapStyleWord(true);
        synopsisArea.setLineWrap(true);
        synopsisArea.setEditable(false);
        synopsisArea.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane synopsisScrollPane = new JScrollPane(synopsisArea);
        synopsisScrollPane.setPreferredSize(new Dimension(100, 120));

        JPanel middleContentPanel = new JPanel(new BorderLayout(10, 10));
        seanceListModel = new DefaultListModel<Seance>();
        seanceJList = new JList<Seance>(seanceListModel);
        JScrollPane seanceScrollPane = new JScrollPane(seanceJList);
        seanceScrollPane.setBorder(BorderFactory.createTitledBorder("Choisissez un horaire pour ce film"));
        middleContentPanel.add(synopsisScrollPane, BorderLayout.NORTH);
        middleContentPanel.add(seanceScrollPane, BorderLayout.CENTER);
        
        JPanel evaluationsPanel = new JPanel(new BorderLayout(5, 5));
        evaluationsPanel.setBorder(BorderFactory.createTitledBorder("Avis des spectateurs"));
        evaluationsListModel = new DefaultListModel<EvaluationClient>();
        evaluationsJList = new JList<EvaluationClient>(evaluationsListModel);
        
        evaluationsJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof EvaluationClient) {
                    EvaluationClient eval = (EvaluationClient) value;
                    String commentaire = eval.getCommentaire().isEmpty() ? "<i>(pas de commentaire)</i>" : eval.getCommentaire();
                    setText("<html><b>" + eval.getNote() + "/5 ★</b> - " + commentaire + "</html>");
                }
                return this;
            }
        });
        evaluationsPanel.add(new JScrollPane(evaluationsJList), BorderLayout.CENTER);
        
        noterButton = new JButton("Donner une note");
        evaluationsPanel.add(noterButton, BorderLayout.SOUTH);

        centerPanel.add(northPanel, BorderLayout.NORTH);
        centerPanel.add(middleContentPanel, BorderLayout.CENTER);
        centerPanel.add(evaluationsPanel, BorderLayout.SOUTH);
        
        add(centerPanel, BorderLayout.CENTER);

        retourButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (retourListener != null) {
                    retourListener.onRetourClicked();
                }
            }
        });

        seanceJList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && seanceJList.getSelectedValue() != null && seanceSelectionListener != null) {
                    seanceSelectionListener.onSeanceSelected(seanceJList.getSelectedValue());
                }
            }
        });

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
        
        noterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (filmActuel != null) {
                    EvaluationDialog dialog = new EvaluationDialog(
                        (Frame) SwingUtilities.getWindowAncestor(FilmDetailPanel.this), 
                        clientService, 
                        clientId, 
                        filmActuel.getId()
                    );
                    dialog.setVisible(true);
                    displayFilmAndSeances(filmActuel, dateActuelle, clientId);
                }
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
            
            notePresseLabel.setText("Presse: " + NOTE_FORMATTER.format(film.getNotePresse()));
            double moyenneSpectateurs = clientService.getNoteMoyenneSpectateurs(film.getId());
            noteSpectateursLabel.setText("Spectateurs: " + (moyenneSpectateurs > 0 ? NOTE_FORMATTER.format(moyenneSpectateurs) : "N/A"));

            evaluationsListModel.clear();
            // CET APPEL EST MAINTENANT VALIDE
            List<EvaluationClient> evaluations = clientService.getEvaluationsByFilmId(film.getId());
            Collections.reverse(evaluations);
            for (EvaluationClient eval : evaluations) {
                evaluationsListModel.addElement(eval);
            }

            try {
                boolean aEvalue = clientService.aDejaEvalue(this.clientId, film.getId());
                noterButton.setEnabled(!aEvalue);
                if (aEvalue) {
                    noterButton.setToolTipText("Vous avez déjà noté ce film.");
                } else {
                    noterButton.setToolTipText("Donnez votre avis sur ce film.");
                }
            } catch (Exception e) {
                noterButton.setEnabled(false);
                e.printStackTrace();
            }
            
            seanceListModel.clear();
            List<Seance> seances = clientService.getSeancesPourFilmEtDate(film.getId(), date);
            for(Seance seance : seances) {
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
        noterButton.setEnabled(false);
    }

    public void setSeanceSelectionListener(SeanceSelectionListener listener) {
        this.seanceSelectionListener = listener;
    }

    public void setRetourListener(RetourListener listener) {
        this.retourListener = listener;
    }
}