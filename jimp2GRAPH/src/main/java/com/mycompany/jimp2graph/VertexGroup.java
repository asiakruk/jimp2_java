/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.jimp2graph;

import java.util.*;

public class VertexGroup {
    private final int id;
    private final List<Integer> vertices;

    public VertexGroup(int id) {
        this.id = id;
        this.vertices = new ArrayList<>();
    }

    public void addVertex(int v) {
        vertices.add(v);
    }

    public List<Integer> getVertices() {
        return vertices;
    }

    public int getId() {
        return id;
    }
}
