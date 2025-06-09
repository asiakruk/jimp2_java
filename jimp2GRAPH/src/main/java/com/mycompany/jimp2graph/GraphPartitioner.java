/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.jimp2graph;

/**
 *
 * @author AsiaK
 */
import java.util.*;

public class GraphPartitioner {
    
    // Metoda do podziału grafu z uwzględnieniem marginesu wielkości grup
    public static List<VertexGroup> partition(Graph graph, int numGroups, double margin) {
        int n = graph.getNumVertices();
        int baseSize = n / numGroups; // Podstawowy rozmiar grupy
        int remainder = n % numGroups; // Reszta wierzchołków, które nie pasują do równych grup

        // Lista grup
        List<VertexGroup> groups = new ArrayList<>();
        for (int i = 0; i < numGroups; i++) {
            groups.add(new VertexGroup(i));
        }

        // Rozdzielanie wierzchołków
        int currentVertex = 0;
        for (int i = 0; i < numGroups; i++) {
            // Obliczamy, ile wierzchołków dodać do tej grupy
            int groupSize = baseSize + (i < remainder ? 1 : 0);

            // Dodajemy wierzchołki do grupy
            for (int j = 0; j < groupSize; j++) {
                groups.get(i).addVertex(currentVertex++);
            }
        }

        return groups;
    }
}

