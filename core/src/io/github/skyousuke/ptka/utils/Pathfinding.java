package io.github.skyousuke.ptka.utils;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class Pathfinding {

    private TiledMapTileLayer mapLayer; // ค่าแผนที่
    private Node start; // โหนดของศัตรู
    private Node goal; // โหนดที่ศัตรูต้องการค้นหา หรือก็คือผู้เล่น

    private Queue<Node> frontiers; // ขอบเขตการค้นหา
    private ObjectMap<Node, Node> cameFrom; // โหนดที่ผ่านมา
    private Array<Node> path; // เส้นทางที่ใช้
    private Array<Node> neighbors; // โหนดข้างๆ
    private ObjectMap<Node, Integer> costSoFar; // ค่าคอสรวมของแต่ละโหนด

    private Node[][] nodes;

    public Pathfinding(TiledMapTileLayer mapLayer) {
        this.mapLayer = mapLayer;
        Comparator<Node> comparator = new Comparator<Node>() {
            @Override
            public int compare(Node n1, Node n2) {
                return (distanceToGoal(n1) + costSoFar.get(n1)) - (distanceToGoal(n2) + costSoFar.get(n2));
            }
        };
        frontiers = new PriorityQueue<Node>(1, comparator);
        cameFrom = new ObjectMap<Node, Node>();
        path = new Array<Node>();
        neighbors = new Array<Node>();
        costSoFar = new ObjectMap<Node, Integer>();
        nodes = new Node[mapLayer.getWidth()][mapLayer.getHeight()]; //สร้างโหนดโดยมีขนาดเท่ากับ tile

        createNode();
    }

    public void createNode() { // สร้างโหนดทุกโหนด
        for (int i = 0; i < mapLayer.getWidth(); i++) {
            for (int j = 0; j < mapLayer.getHeight(); j++) {
                nodes[i][j] = new Node(i, j, mapLayer.getCell(i, j).getTile().getProperties().containsKey("blocked"));
            }
        }
        for (int i = 0; i < mapLayer.getWidth(); i++) { // ใส่ค่าให้โหนดที่เป็น Blocked มีคอสสูงๆ จะได้ไม่เดินเข้าไปชน
            for (int j = 0; j < mapLayer.getHeight(); j++) {
                if (nodeNearBlocked(nodes[i][j]))
                    nodes[i][j].cost = 99;
            }
        }
    }

    private void init() {
        frontiers.clear();
        costSoFar.clear();
        cameFrom.clear();
        path.clear();
    }


    public Array<Node> findPath(float startX, float startY, float goalX, float goalY) {
        init();

        setStart(startX, startY); // ใส่ค่าตำแหน่งที่ศัตรูยืนอยู่
        setGoal(goalX, goalY); // ใส่ค่าตำแหน่งที่ผู้เล่นยืนอยู่ปัจจุบัน

        costSoFar.put(start, 0); // ใส่ค่าคอสรวมที่ใช้
        frontiers.add(start); // สร้าง frontiers ที่ตำแหน่ง start

        while (!frontiers.isEmpty()) { // ถ้า frontiers มีการรับค่าเข้ามา
            Node current = frontiers.poll(); // ดึงค่า frontier ตามคิวละเอาออก
            if (current.equals(goal)) //ถ้าโหนด current เป็น goal หยุดทำ
                break;

            for (Node neighbor : getNeighbors(current)) { // สร้างโหนด neighbor รอบ current
                int newCost = costSoFar.get(current) + neighbor.cost; // คอสตัวปัจจุบัน = cost รวมจนถึง current + cost ของโหนด
                if (!costSoFar.containsKey(neighbor) || newCost < costSoFar.get(neighbor)) {
                    if (!neighbor.blocked) { // ถ้า neighbor ไม่ได้อยู่ข้าง  blocked
                        costSoFar.put(neighbor, newCost); // เพิ่มอาเรย์ของ costSoFar
                        frontiers.add(neighbor); // เพิ่ม neighbor ให้เป็นหนึ่งใน frontiers
                        cameFrom.put(neighbor, current); // ใส่ค่าให้รู้ว่า neighbor มาจากโหนดไหน
                    }
                }
            }
        }

        Node current = goal; // ให้โหนด current = goal
        path.add(current); // ให้ current เป็นหนึ่งในเส้นทางที่ใช้เดิน

        while (current != null && !current.equals(start)) { // หากตำแหน่งที่ยืนอยู่ไม่ใช่ตำแหน่งเดียวกับ goal ให้สร้าง path
            current = cameFrom.get(current); // เรียกโหนดที่เคยผ่านมาเก็บไว้ใน current
            path.add(current); // เพิ่มเส้นทางที่จะใช้เดิน
        }

        path.pop(); // ลบจุด start ออก
        path.reverse(); //กลับเส้นทาง
        return path;
    }

    public Array<Node> getNeighbors(Node node) {

        neighbors.clear();

        for (int i = node.i - 1; i <= node.i + 1; ++i) {
            for (int j = node.j - 1; j <= node.j + 1; ++j) {
                if (i == node.i && j == node.j) continue;
                if (i >= 0 && i < mapLayer.getWidth() && j >= 0 && j < mapLayer.getHeight()) {
                    neighbors.add(nodes[i][j]);
                }
            }
        }

        return neighbors;
    }

    public boolean nodeNearBlocked(Node node) {
        for (Node neighbor : getNeighbors(node)) {
            if (neighbor.blocked)
                return true;
        }
        return false;
    }

    private void setGoal(float x, float y) {
        goal = nodes
                [(int) (x / mapLayer.getTileWidth())]
                [(int) (y / mapLayer.getTileHeight())];
    }

    private void setStart(float x, float y) {
        start = nodes
                [(int) (x / mapLayer.getTileWidth())]
                [(int) (y / mapLayer.getTileHeight())];
    }

    public class Node {
        public int i;
        public int j;
        public boolean blocked;
        public int cost;

        public Node(int i, int j, boolean blocked) {
            this.i = i;
            this.j = j;
            this.blocked = blocked;
            cost = 1;
        }

        public float getPositionX() {
            return i * mapLayer.getTileWidth() + mapLayer.getTileWidth() / 2;
        }

        public float getPositionY() {
            return j * mapLayer.getTileHeight() + mapLayer.getTileHeight() / 2;
        }

    }

    public int distanceToGoal(Node frontier) {
        return Math.abs(frontier.i - goal.i) + Math.abs(frontier.j - goal.j);
    }
}