package com.mycompany.jimp2graph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MainFrame extends JFrame {
    private Graph graph;
    private GraphVisualizer visualizer;
    
    // Zmienna do zmiany przy scrollowaniu
    private AtomicInteger globalValue = new AtomicInteger(1);
    private AtomicInteger position1 = new AtomicInteger(0);
    private AtomicInteger position2 = new AtomicInteger(0);

    // Zmienna do zapamiętywania pozycji kliknięcia
    private Point initialClick = null;

    public MainFrame() {
        super("Graph Partitioning");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 700);
        setLayout(new BorderLayout());
        
        visualizer = new GraphVisualizer(globalValue, position1, position2);
        add(visualizer, BorderLayout.CENTER);

        JPanel controls = new JPanel();
        // Przyciski do wczytania różnych formatów grafu
        JButton loadCsrrgBtn = new JButton("Wczytaj plik .csrrg");
        loadCsrrgBtn.addActionListener(new LoadGraphActionListener("csrrg"));
        JButton loadTextBtn = new JButton("Wczytaj plik .txt");
        loadTextBtn.addActionListener(new LoadGraphActionListener("txt"));
        JButton loadBinaryBtn = new JButton("Wczytaj plik .bin");
        loadBinaryBtn.addActionListener(new LoadGraphActionListener("bin"));

        // Przyciski do dzielenia grafu
        JTextField groupsField = new JTextField("2", 3);
        JTextField marginField = new JTextField("0.1", 3);
        JButton partitionBtn = new JButton("Partition Graph");
        partitionBtn.addActionListener(e -> {
            if (graph == null) {
                JOptionPane.showMessageDialog(this, "Load a graph first!");
                return;
            }
            try {
                int numGroups = Integer.parseInt(groupsField.getText());
                double margin = Double.parseDouble(marginField.getText());
                if (numGroups <= 0 || numGroups > graph.getNumVertices()) {
                    JOptionPane.showMessageDialog(this, "Invalid number of groups.");
                    return;
                }
                // Logika dzielenia grafu na grupy (do zaimplementowania)
                visualizer.setGraph(graph, null);  // Zaktualizuj wizualizację z nowymi danymi
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter valid numbers.");
            }
        });
        
        controls.add(loadCsrrgBtn);
        controls.add(loadTextBtn);
        controls.add(loadBinaryBtn);
        controls.add(new JLabel("Groups:"));
        controls.add(groupsField);
        controls.add(new JLabel("Margin:"));
        controls.add(marginField);
        controls.add(partitionBtn);
        add(controls, BorderLayout.SOUTH);

        // Obsługa scrolla (do zmiany globalValue)
        this.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() < 0) {
                    // Scroll w górę (powiększ)
                    globalValue.getAndIncrement();
                } else {
                    // Scroll w dół (pomniejsz)
                    globalValue.getAndDecrement();
                }
                // Aktualizuj wizualizację
                visualizer.repaint();
            }
        });

        // Obsługa myszy
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    // Zapisz pozycję kliknięcia
                    initialClick = e.getPoint();  // Zapamiętaj początkową pozycję kliknięcia
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    // Resetuj pozycję kliknięcia po zwolnieniu przycisku myszy
                    initialClick = null;
                }
            }
        });

        // Obsługa ruchu myszy
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (initialClick != null) {
                    // Oblicz wektor przesunięcia
                    int deltaX = e.getX() - initialClick.x;
                    int deltaY = e.getY() - initialClick.y;
                    
                    // Przesuń pozycję o wektor przesunięcia
                    position1.set(deltaX);
                    position2.set(deltaY);
                    
                    // Zaktualizuj wizualizację
                    visualizer.repaint();
                    
                    // Zaktualizuj początkową pozycję kliknięcia
                    initialClick = e.getPoint();
                }
            }
        });

        this.setFocusable(true); // Umożliwia odbieranie zdarzeń klawiatury
        setVisible(true);
    }

    // Klasa do obsługi przycisków wczytujących różne formaty grafu
    private class LoadGraphActionListener implements ActionListener {
        private String fileType;

        public LoadGraphActionListener(String fileType) {
            this.fileType = fileType;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser();
            int result = fc.showOpenDialog(MainFrame.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    var file = fc.getSelectedFile();
                    String filePath = file.getAbsolutePath();
                    switch (fileType) {
                        case "csrrg":
                            graph = GraphFileReader.readCsrrgFile(filePath);
                            break;
                        case "txt":
                            graph = GraphFileReader.readTextFile(filePath);
                            break;
                        case "bin":
                            graph = GraphFileReader.readBinaryFile(filePath);
                            break;
                    }
                    // Komunikat, gdy graf został pomyślnie załadowany
                    JOptionPane.showMessageDialog(MainFrame.this, "Graph loaded: " + graph.getNumVertices() + " vertices.");
                    
                    // Zaktualizowanie wizualizacji grafu
                    visualizer.setGraph(graph, null);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Error loading file: " + ex.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
