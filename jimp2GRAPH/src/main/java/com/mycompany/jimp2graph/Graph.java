/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.jimp2graph;
import java.util.*;

public class Graph {
    public  List<List<Integer>> matrix = null;
    public  List<List<Integer>> neigh = null; // opisane we wspolrzednych X
    public  HashMap<Integer, Integer> color = null; // opisane we wspolrzednych X; 1 is blue 2 is red
    public  HashMap<Integer, Integer> position1 = null; // mapowanie z wspolrzednych macierzowych do X
    public  HashMap<Integer, Integer> position2 = null; // mapowanie z X do wspolrzednych macierzowych 
    public final int n;
    public final int m;
    
    // n w lewo i m w dol
    public Graph(int n, int m) {
        this.n = n;
        this.m = m;
    }

    // Setter for adjacencyList
    public void setNeighList(List<List<Integer>> adjacencyList) {
        this.neigh = adjacencyList;
    }

    // Setter for matrix
    public void setMatrix(List<List<Integer>> matrix) {
        this.matrix = matrix;
    }

    // Setter for color
    public void setColor(HashMap<Integer, Integer> color) {
        this.color = color;
    }

    // Setter for position1
    public void setPosition1(HashMap<Integer, Integer> position1) {
        this.position1 = position1;
    }

    // Setter for position2
    public void setPosition2(HashMap<Integer, Integer> position2) {
        this.position2 = position2;
    }
    
    public int getNumVertices() {
        return n*m;
    }
}
