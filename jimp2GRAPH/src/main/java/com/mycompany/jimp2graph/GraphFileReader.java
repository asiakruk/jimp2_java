/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.jimp2graph;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class GraphFileReader {

    public static void main(String[] args) {
        // GUI: Przyciski do wyboru pliku
        JFrame frame = new JFrame("Wczytywanie grafu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Przyciski w GUI
        JButton loadCsrrgButton = new JButton("Wczytaj plik .csrrg");
        JButton loadTextButton = new JButton("Wczytaj plik .txt");
        JButton loadBinaryButton = new JButton("Wczytaj plik .bin");

        // Panel i układ
        JPanel panel = new JPanel();
        panel.add(loadCsrrgButton);
        panel.add(loadTextButton);
        panel.add(loadBinaryButton);

        frame.add(panel);
        frame.setSize(400, 150);
        frame.setVisible(true);

        // Listener do wczytania pliku .csrrg
        loadCsrrgButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Wybierz plik .csrrg");
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String filePath = file.getAbsolutePath();
                try {
                    Graph graph = readCsrrgFile(filePath);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Listener do wczytania pliku tekstowego .txt
        loadTextButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Wybierz plik .txt");
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String filePath = file.getAbsolutePath();
                try {
                    Graph graph = readTextFile(filePath);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Listener do wczytania pliku binarnego .bin
        loadBinaryButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Wybierz plik .bin");
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String filePath = file.getAbsolutePath();
                try {
                    Graph graph = readBinaryFile(filePath);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    // Odczyt pliku .csrrg
    public static Graph readCsrrgFile(String filePath) throws IOException {
        Scanner scanner = new Scanner(new File(filePath));
        String line = scanner.nextLine().trim();
        int n = Integer.parseInt(line);
        int m;
        
        List<Integer> line1 = new ArrayList();
        List<Integer> line2 = new ArrayList();
        List<Integer> line3 = new ArrayList();
        List<Integer> line4 = new ArrayList();
        var linee = scanner.nextLine().trim().split(";");
        for (int i = 0; i < linee.length; i++) {
            line1.add(Integer.parseInt(linee[i]));
        }
        linee = scanner.nextLine().trim().split(";");
        for (int i = 0; i < linee.length; i++) {
            line2.add(Integer.parseInt(linee[i]));
        }
        linee = scanner.nextLine().trim().split(";");
        for (int i = 0; i < linee.length; i++) {
            line3.add(Integer.parseInt(linee[i]));
        }
        linee = scanner.nextLine().trim().split(";");
        for (int i = 0; i < linee.length; i++) {
            line4.add(Integer.parseInt(linee[i]));
        }
        m = line2.size();
        
        
        int[][] arr = new int[n][m];
        int last = 0;
        int posInFirst = 0;
        for (int i = 1; i < line2.size(); i++) {
            while(posInFirst < line2.get(i)) {
                arr[i - 1][line1.get(posInFirst)] = 1;
                posInFirst++;
            }
        }
        File file = new File("temp.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Zapisz tablicę do pliku
            for (int i = 0; i < n; i++) {
                writer.write("[");
                for (int o = 0; o < m; o++) {
                    writer.write(arr[i][o] + ". ");
                }
                writer.write("]\n");
            }

            Random rand = new Random();
            int[] color = new int[n * m];
            for (int i = 0; i < n * m; i++) {
                color[i] = rand.nextInt(2); // Losowanie kolorów
            }

            // Zapisz wyniki dla kolorów 0
            for (int z = 1; z < line4.size(); z++) {
                int a = line3.get(line4.get(z - 1));
                for (int o = line4.get(z - 1); o < line4.get(z); o++) {
                    int b = line3.get(o);
                    if (color[a] == color[b] && color[a] == 0 && a != b) {
                        writer.write(a + " - " + b + "\n");
                    }
                }
            }

            writer.write("\n");

            // Zapisz wyniki dla kolorów 1
            for (int z = 1; z < line4.size(); z++) {
                int a = line3.get(line4.get(z - 1));
                for (int o = line4.get(z - 1); o < line4.get(z); o++) {
                    int b = line3.get(o);
                    if (color[a] == color[b] && color[a] == 1 && a != b) {
                        writer.write(a + " - " + b + "\n");
                    }
                }
            }
        }

        // Odczytaj zawartość pliku temp.txt
        var graphh = readTextFile("temp.txt");

        // Usuń plik temp.txt po odczytaniu
        File tempFile = new File("temp.txt");
        if (tempFile.exists()) {
            tempFile.delete();
        }

        return graphh; // Zwróć zawartość pliku
    }

    // Odczyt pliku tekstowego .txt
    public static Graph readTextFile(String filePath) throws IOException {
        
        
        List<List<Integer>> matrix = null;
        List<List<Integer>> neigh = null; // opisane we wspolrzednych X
        HashMap<Integer, Integer> color = new HashMap<>(); // opisane we wspolrzednych X
        HashMap<Integer,Integer> position1 = new HashMap<>(); // mapowanie z wspolrzednych macierzowych do X
        HashMap<Integer,Integer> position2 = new HashMap<>();; // mapowanie z X do wspolrzednych macierzowych 
        
        Graph g = null;
        int ind = 0;
        int onesInd=0;
        int numVertices;
        int lines = 0;
        Scanner scanner2 = new Scanner(new File(filePath));
        while (scanner2.hasNextLine()) {
             var a = scanner2.nextLine();
             if (a.length() < 3 || a.charAt(0) != '[') break;
             lines++;
        }
        scanner2.close();
        
        Scanner scanner = new Scanner(new File(filePath));
        int state = 0;

        while (scanner.hasNextLine()) {
             String line = scanner.nextLine().trim();
             if (line.isBlank()) {
                 state = 2;
                 continue;
             }
             if (state == 0 && line.charAt(0) != '[') {
                 state = 1;
             }
             else 
             line = line.replaceAll("[\\[\\]\\.]", "");
             if (state == 0) {
                 String[] indices = line.split(" ");
                 List<Integer> row = new ArrayList<>();
                for (String index : indices) {
                    row.add(Integer.parseInt(index));
                }
                 if (g == null) {
//                     System.out.println(line);
//                     System.out.println(indices);
//                     System.out.println(row);
                     numVertices = row.size();
                     g = new Graph(numVertices, lines);
                      neigh = new ArrayList<>();
                      for (int i = 0; i < numVertices * lines + 3; i++) {
                          neigh.add(new ArrayList<>());
                      }
                    matrix = new ArrayList<>(numVertices);
                 }
                 for (int i = 0; i <row.size(); i++) {
                     if (row.get(i) == 1) {
                         position1.put(ind, onesInd);
                         position2.put(onesInd, ind);
                         onesInd++;
                     }
                     ind++;
                 }
                matrix.add(row);
             }
             if (state == 1) {
                 List<Integer> row = new ArrayList<>();
                   String[] parts = line.split(" - ");
                int vertex1 = Integer.parseInt(parts[0]);
                String secondPart = parts[1];
                color.put(vertex1, 1);
                if (secondPart.equals("-1")) {
//                    edges.add(new Edge(vertex1, -1));
                } else {
                    int vertex2 = Integer.parseInt(secondPart);
                    color.put(vertex2, 1);
                    vertex1 = position2.get(vertex1);
                    vertex2 = position2.get(vertex2);
                    
                    int x = Math.min(vertex1, vertex2);
                    int y = Math.max(vertex1, vertex2);
                    neigh.get(x).add(y);
                }
             }
             if (state == 2) {
                List<Integer> row = new ArrayList<>();
                   String[] parts = line.split(" - ");
                int vertex1 = Integer.parseInt(parts[0]);
                String secondPart = parts[1];
                color.put(vertex1, 2);
                if (secondPart.equals("-1")) {
//                    edges.add(new Edge(vertex1, -1));
                } else {
                    int vertex2 = Integer.parseInt(secondPart);
                    color.put(vertex2, 2);
                    vertex1 = position2.get(vertex1);
                    vertex2 = position2.get(vertex2);
                    
                    int x = Math.min(vertex1, vertex2);
                    int y = Math.max(vertex1, vertex2);
                    neigh.get(x).add(y);
                }
             }
        }

        scanner.close();
        g.setColor(color);
        g.setNeighList(neigh);
        g.setMatrix(matrix);
        g.setPosition1(position1);
        g.setPosition2(position2);
        return g;
    }

    // Odczyt pliku binarnego .bin
    public static Graph readBinaryFile(String filePath) throws IOException {
        DataInputStream dis = new DataInputStream(new FileInputStream(filePath));

        List<List<Integer>> matrix = null;
        List<List<Integer>> neigh = null; // opisane we wspolrzednych X
        HashMap<Integer, Integer> color = new HashMap<>(); // opisane we wspolrzednych X
        HashMap<Integer,Integer> position1 = new HashMap<>(); // mapowanie z wspolrzednych macierzowych do X
        HashMap<Integer,Integer> position2 = new HashMap<>();; // mapowanie z X do wspolrzednych macierzowych 
        
        Graph g = null;
        int ind = 0;
        int onesInd=0;
        int numVertices;
        int lines = 0;
        
        int n = Integer.reverseBytes(dis.readInt());
        int m = Integer.reverseBytes(dis.readInt());
        int k;
        
        neigh = new ArrayList<>();
                      for (int i = 0; i < n * m + 3; i++) {
                          neigh.add(new ArrayList<>());
                      }
                    matrix = new ArrayList<>(n);
        
        for (int i1 = 0; i1 < n; i1++) {
            List<Integer> row = new ArrayList<>();
            for (int o = 0; o < m; o++) {
                k = Integer.reverseBytes(dis.readInt());
                row.add(k);
            }
            for (int i = 0; i <row.size(); i++) {
                     if (row.get(i) == 1) {
                         position1.put(ind, onesInd);
                         position2.put(onesInd, ind);
                         onesInd++;
                     }
                     ind++;
                 }
                matrix.add(row);
        }
        int z = Integer.reverseBytes(dis.readInt()); // ilosc podgrafow
        z = Integer.reverseBytes(dis.readInt());
        for (int i = 0; i < z; i++) {
            int a = Integer.reverseBytes(dis.readInt());
            int b = Integer.reverseBytes(dis.readInt());
            color.put(a, 1);
                if (b== -1) {
//                    edges.add(new Edge(vertex1, -1));
                } else {
                    color.put(b, 1);
                    a = position2.get(a);
                    b = position2.get(b);
                    
                    int x = Math.min(a, b);
                    int y = Math.max(a, b);
                    neigh.get(x).add(y);
                }
        }
        
        z = Integer.reverseBytes(dis.readInt());
        for (int i = 0; i < z; i++) {
            int a = Integer.reverseBytes(dis.readInt());
            int b = Integer.reverseBytes(dis.readInt());
            color.put(a, 2);
                if (b== -1) {
//                    edges.add(new Edge(vertex1, -1));
                } else {
                    color.put(b, 2);
                    a = position2.get(a);
                    b = position2.get(b);
                    
                    int x = Math.min(a, b);
                    int y = Math.max(a, b);
                    neigh.get(x).add(y);
                }
        }
        
        
        dis.close();
        Graph graph = new Graph(n, m);
                graph.setColor(color);
        graph.setNeighList(neigh);
        graph.setMatrix(matrix);
        graph.setPosition1(position1);
        graph.setPosition2(position2);
        return graph;
    }
}


