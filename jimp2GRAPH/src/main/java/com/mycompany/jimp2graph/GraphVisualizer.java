/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.jimp2graph;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class GraphVisualizer extends JPanel {
    private List<VertexGroup> groups;
    private Graph graph;
    
    private final AtomicInteger globalValue;
    private final AtomicInteger position1;
    private final AtomicInteger position2;

    public GraphVisualizer(AtomicInteger globalValue, AtomicInteger position1, AtomicInteger position2) {
        this.groups = null;
        this.globalValue = globalValue;
        this.position1 = position1;
        this.position2 = position2;
        
        setPreferredSize(new Dimension(800, 600));
    }

    public void setGraph(Graph graph, List<VertexGroup> groups) {
        this.graph = graph;
        this.groups = groups;
        repaint();
    }
    
    void drawLine(Graphics g, int x1, int y1, int x2, int y2, int k1, int k2) {
        System.out.print(position1.get());
        x1 += position1.get();
        x2 += position1.get();
        y1 += position2.get();
        y2 += position2.get();
        g.setColor(Color.BLACK);
        
        g.drawLine((x1 + 1) * k1, (y1 + 1) * k2, (x2 + 1) * k1, (y2 + 1) * k2);
    }
    
    void drawCircle(Graphics g, int x, int y, int num, int k1, int k2, int numm) {
        x += position1.get();
        y += position2.get();
        var R = 40;
        if (num == 1) {
             g.setColor(Color.RED);
        }
        else if (num == 2)  g.setColor(Color.BLUE);
        else return;
        g.fillOval((1+x) * k1 - R / 2, (y + 1) * k2 - R / 2, R, R);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.setColor(Color.WHITE);
        g.drawString(numm + "", (1+x) * k1 - R / 4, (1+y)*k2 + R / 4);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.graph == null) return;
        Random rand = new Random();
        
        int k1 = (int)(this.getSize().width * 0.9) / graph.n;
        int k2 = (int)(this.getSize().height * 0.9) / graph.m;
        if (k1 == 0) k1 = 1;
        if (k2 == 0) k2 = 1;
        k1 *= globalValue.get();
        k2 *= globalValue.get();
        
        // Rysowanie krawędzi
        for (int i = 0; i < graph.neigh.size(); i++) {
            for (int o = 0; o < graph.neigh.get(i).size(); o++) {
                int a = i;
                int b = graph.neigh.get(i).get(o);
                int n = graph.n;
                int x1 = a % n;
                int y1 = a / n;
                int x2 = b % n;
                int y2 = b / n;
                drawLine(g, x1, y1, x2, y2, k1, k2);
            }
        }
        for (int i = 0; i < graph.n; i++) {
            for (int o = 0; o < graph.m; o++) {
                int pos = i + o * graph.n;
                if (!graph.position1.containsKey(pos)) continue;
                int x = graph.position1.get(pos);
                if (graph.color.containsKey(x)) {
                    drawCircle(g, i, o, graph.color.get(x), k1, k2, graph.position1.get(pos));
                }
            }
        }
        
        
        
//        int radius = 15;
//        int gap = 50;
//        int y = 100;
//
//        // Rysowanie wierzchołków i krawędzi
//        for (VertexGroup group : groups) {
//            Color color = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
//            g.setColor(color);
//
//            int x = 50;
//            // Rysowanie wierzchołków
//            for (int v : group.getVertices()) {
//                g.fillOval(x, y, radius * 2, radius * 2);
//                g.setColor(Color.BLACK);
//                g.drawString(String.valueOf(v), x + radius - 4, y + radius + 5);
//                g.setColor(color);
//                x += radius * 3;
//            }
//            y += gap * 2;
//        }

        
    }
}

