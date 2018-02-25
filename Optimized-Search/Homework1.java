
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.Random;

class Positions {

    int row;
    int col;

    Positions(int r, int c) {
        row = r;
        col = c;
    }

}

class State {

    ArrayList<ArrayList<Integer>> currentMatrix;
    ArrayList<Positions> lizards;
    ArrayList<Positions> emptySpaces;

    public State() {
        currentMatrix = new ArrayList<>();
        lizards = new ArrayList<>();
        emptySpaces = new ArrayList<>();
    }
}

class LizardsPositions {                                                    //this class objects stores position of all placed lizards till now

    int row;
    int col;

    public LizardsPositions(int r, int c) {
        row = r;
        col = c;
    }
}

class Node implements Serializable {                                        //this class objects are actual bfs nodes and get stored in a queue

    Stack<LizardsPositions> lizardsPlaced;
    int lizardsLeft;

    Node() {
        lizardsPlaced = new Stack<>();
    }

    public void makeCopy(Node toCopy) {
        this.lizardsLeft = toCopy.lizardsLeft;
        this.lizardsPlaced = (Stack) toCopy.lizardsPlaced.clone();

    }

}

public class Homework1 {

    String algoUsed;                                                        //algorithm to be used taken from input
    int nurserySize;                                                        //given size of the input matrix
    int lizardsTotal;                                                       //total number of lizards to be placed    
    ArrayList<ArrayList<Integer>> inputMatrix = new ArrayList<>();          //given input matrix

    int totalTrees = 0;
    int totalSpaces = 0;
    String result;                                                          //solution of the placement. FAIL if failed and OK with final placements in output
    int flag = 1;                                                           //for result to check whether failed or passed

    Queue<Node> bfsQueue = new LinkedList<>();                              //queue to store all states in BFS
    Stack<Node> dfsStack = new Stack<>();
    Node rootNode;                                                          //root state which is the input matrix

    Random initrandom = new Random();
    double temperature = 1.0;
    double counter = 0;
    double counter2 = 10;
    long initialTime = System.currentTimeMillis();
    long finaltime;
    long totalTime = 0;

    State oldCopy;
    int oldCollisions = 0;
    int delta = 0;

    ArrayList<ArrayList<Integer>> outputMatrix = new ArrayList<>();

    public State replaceAll(State newone, State oldone) {

        for (int i = 0; i < newone.currentMatrix.size(); i++) {
            for (int j = 0; j < newone.currentMatrix.get(i).size(); j++) {
                oldone.currentMatrix.get(i).set(j, newone.currentMatrix.get(i).get(j));
            }
        }
        for (int i = 0; i < oldone.lizards.size(); i++) {
            oldone.lizards.set(i, newone.lizards.get(i));
        }

        return oldone;

    }

    public State copyAll(State oldone, State newone) {

        for (int i = 0; i < oldone.currentMatrix.size(); i++) {
            newone.currentMatrix.add(new ArrayList<>());
            for (int j = 0; j < oldone.currentMatrix.get(i).size(); j++) {
                newone.currentMatrix.get(i).add(oldone.currentMatrix.get(i).get(j));
            }
        }
        for (int i = 0; i < oldone.lizards.size(); i++) {
            newone.lizards.add(oldone.lizards.get(i));
        }
        for (int i = 0; i < oldone.emptySpaces.size(); i++) {
            newone.emptySpaces.add(oldone.emptySpaces.get(i));
        }
        return newone;

    }

    public void printBoard(State node) {

        for (int i = 0; i < node.currentMatrix.size(); i++) {
            for (int j = 0; j < node.currentMatrix.get(i).size(); j++) {
                System.out.print(node.currentMatrix.get(i).get(j));
            }
            System.out.println();
        }
    } //print whole board not used

    public int collisions(ArrayList<ArrayList<Integer>> inp, ArrayList<Positions> liz) {

        int collision = 0;

        for (int i = 0; i < liz.size(); i++) {

            int row = liz.get(i).row;
            int col = liz.get(i).col;
            int k, l;

            for (k = row - 1; k >= 0; k--) {                //del all cells in up direction

                if (inp.get(k).get(col) == 2) {
                    break;
                } else if (inp.get(k).get(col) == 1) {
                    collision++;
                }
            }

            for (k = row + 1; k < nurserySize; k++) {        //del all cells in down direction

                if (inp.get(k).get(col) == 2) {
                    break;
                } else if (inp.get(k).get(col) == 1) {
                    collision++;
                }
            }

            for (k = col - 1; k >= 0; k--) {                //del all cells in left direction

                if (inp.get(row).get(k) == 2) {
                    break;
                } else if (inp.get(row).get(k) == 1) {
                    collision++;
                }
            }

            for (k = col + 1; k < nurserySize; k++) { //del all cells in right direction

                if (inp.get(row).get(k) == 2) {
                    break;
                } else if (inp.get(row).get(k) == 1) {
                    collision++;
                }
            }

            for (k = row - 1, l = col + 1; k >= 0 && l < nurserySize; k--, l++) { //del all cells in up-right direction

                if (inp.get(k).get(l) == 2) {
                    break;
                } else if (inp.get(k).get(l) == 1) {
                    collision++;
                }
            }

            for (k = row - 1, l = col - 1; k >= 0 && l >= 0; k--, l--) {        //del all cells in up-left direction

                if (inp.get(k).get(l) == 2) {
                    break;
                } else if (inp.get(k).get(l) == 1) {
                    collision++;
                }
            }

            for (k = row + 1, l = col + 1; k < nurserySize && l < nurserySize; k++, l++) { //del all cells in down-right direction

                if (inp.get(k).get(l) == 2) {
                    break;
                } else if (inp.get(k).get(l) == 1) {
                    collision++;;
                }
            }

            for (k = row + 1, l = col - 1; k < nurserySize && l >= 0; k++, l--) { //del all cells in down-left direction

                if (inp.get(k).get(l) == 2) {
                    break;
                } else if (inp.get(k).get(l) == 1) {
                    collision++;
                }
            }

        }

        return collision;

    }

    public boolean probabilityFunction(double temperature, int delta) {
        if (delta < 0) {
            return true;
        }

        double C = Math.exp(-delta / temperature);
        double R = Math.random();

        if (R < C) {

            return true;
        }

        return false;
    }

    public void initialPlacement() {  //randomly place all queens initially and goes to main annealing function

        oldCopy = new State();
        oldCopy.currentMatrix.addAll(inputMatrix);
        int lizardsTot = lizardsTotal;

        while (lizardsTot > 0) {

            int row = initrandom.nextInt(nurserySize);
            int col = initrandom.nextInt(nurserySize);

            if (oldCopy.currentMatrix.get(row).get(col) == 0) {
                oldCopy.currentMatrix.get(row).set(col, 1);
                oldCopy.lizards.add(new Positions(row, col));
                lizardsTot--;
            }

        }

        for (int row = 0; row < nurserySize; row++) {
            for (int col = 0; col < nurserySize; col++) {
                if (oldCopy.currentMatrix.get(row).get(col) == 0) {
                    oldCopy.emptySpaces.add(new Positions(row, col));
                }
            }
        }
        oldCollisions = collisions(oldCopy.currentMatrix, oldCopy.lizards);
        simulatedAnnealing();

    } //root state is made out of this

    public boolean canBeMoved(State s, int r, int c) {

        if (s.currentMatrix.get(r).get(c) == 0) {
            return true;
        }
        return false;

    }

    public void simulatedAnnealing() {

        State newCopy;
        int newCollisions;
        int flag = 1;

        if (nurserySize >= 0 && nurserySize <= 5) {
            totalTime = 5000;
        } else if (nurserySize > 5 && nurserySize <= 10) {
            totalTime = 20000;
        } else if (nurserySize > 10 && nurserySize <= 25) {
            totalTime = 30000;
        } else if (nurserySize > 25 && nurserySize <= 40) {
            totalTime = 90000;
        } else if (nurserySize > 40 && nurserySize <= 50) {
            totalTime = 120000;
        } else if (nurserySize > 50 && nurserySize <= 60) {
            totalTime = 160000;
        } else if (nurserySize > 60 && nurserySize <= 70) {
            totalTime = 180000;
        } else {
            totalTime = 210000;
        }

        if (oldCollisions == 0) {
            printBoard(oldCopy);
            giveOutput(oldCopy.currentMatrix, 1);
        }

        while (true && oldCollisions > 0) {

            if (System.currentTimeMillis() - initialTime > 280000) {
                flag = 0;
                break;
            }

            counter2++;
            newCopy = new State();
            newCopy = copyAll(oldCopy, newCopy);

            int lizrow, lizcol, emptyrow, emptycol, lizindex, newemptyspace;

            lizindex = initrandom.nextInt(oldCopy.lizards.size());//randomly select any lizard from old matrix

            lizrow = oldCopy.lizards.get(lizindex).row;
            lizcol = oldCopy.lizards.get(lizindex).col;

            newemptyspace = initrandom.nextInt(oldCopy.emptySpaces.size()); //select any empty space

            emptyrow = oldCopy.emptySpaces.get(newemptyspace).row;
            emptycol = oldCopy.emptySpaces.get(newemptyspace).col;

            if (canBeMoved(oldCopy, emptyrow, emptycol)) {

                newCopy.currentMatrix.get(lizrow).set(lizcol, 0);
                newCopy.currentMatrix.get(emptyrow).set(emptycol, 1);

                newCopy.lizards.remove(lizindex);
                newCopy.lizards.add(new Positions(emptyrow, emptycol));

                newCopy.emptySpaces.remove(newemptyspace);
                newCopy.emptySpaces.add(new Positions(lizrow, lizcol));

            } else if (!canBeMoved(oldCopy, emptyrow, emptycol)) {
                continue;
            }

            newCollisions = collisions(newCopy.currentMatrix, newCopy.lizards);

            delta = newCollisions - oldCollisions;

            if (probabilityFunction(temperature, delta)) {

                temperature = (1 / Math.log(counter2));
                oldCopy = newCopy;
                oldCollisions = newCollisions;
                counter++;

            }

            if (oldCollisions == 0) {

                //System.out.println("Solution found with total counter " + counter2);
                //System.out.println("Solution found with counter " + counter);
                //printBoard(oldCopy);
                giveOutput(oldCopy.currentMatrix, 1);
                break;
            }
        }

        if (flag == 0) {
            System.out.println("Failed with counter " + counter);
            giveOutput(null, 0);
        }

    }

    public void maxQueens(ArrayList<ArrayList<Integer>> ar) {               //calculate max number of queens for each row but not required at the present moment

        ArrayList<Integer> ans = new ArrayList<>();

        int flag = 0;
        int counter = 0;

        for (int i = 0; i < ar.size(); i++) {
            for (int j = 0; j < ar.get(i).size(); j++) {

                if (ar.get(i).get(j) == 0 && flag == 0) {
                    counter++;
                    flag = 1;
                } else if (ar.get(i).get(j) == 2 && flag == 1) {
                    flag = 0;
                }
            }
            ans.add(counter);
            counter = 0;
            flag = 0;

        }
    }

    public static Object deepClone(Object object) {                         //Cloning of the parent object is done here via this method
        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();

        } catch (IOException | ClassNotFoundException e) {

            return null;
        }
    }

    public void takeInput() {                                               //takes the input from file and save it into global variables

        try {

            BufferedReader br = new BufferedReader(new FileReader(new File("input.txt")));
            StringBuilder temp = new StringBuilder();
            String line;
            String finalinput;

            while ((line = br.readLine()) != null) {
                temp.append(line);
                temp.append("\n");
            }

            finalinput = temp.toString();
            String ss[] = finalinput.split("\n");

            algoUsed = ss[0];
            nurserySize = Integer.parseInt(ss[1]);
            lizardsTotal = Integer.parseInt(ss[2]);

            for (int i = 3; i < ss.length; i++) {

                inputMatrix.add(new ArrayList<>());

                for (char c : ss[i].toCharArray()) {
                    inputMatrix.get(i - 3).add(c - '0');

                }
            }
            for (int i = 0; i < inputMatrix.size(); i++) {
                for (int j = 0; j < inputMatrix.get(i).size(); j++) {
                    if (inputMatrix.get(i).get(j) == 2) {
                        totalTrees++;
                    }
                    if (inputMatrix.get(i).get(j) == 0) {
                        totalSpaces++;
                    }
                }
            }
            if (totalTrees == 0 && (lizardsTotal > nurserySize)) {
                giveOutput(null, 0);
                return;
            }
            if (totalSpaces < lizardsTotal) {
                giveOutput(null, 0);
                return;
            }

            //maxQueens(inputMatrix);
            makeRootQueue();

        } catch (IOException e) {
        }

    }

    public void giveOutput(ArrayList<ArrayList<Integer>> ar, int flag) {     //gives the output after getting result for both bfs and dfs

        try {
            File f = new File("output.txt");
            if (f.exists()) {
                f.delete();
            } else {
                f.createNewFile();
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));

            if (flag == 0) {
                bw.write("FAIL" + "\n");
                bw.flush();

            } else {
                bw.write("OK" + "\n");
                bw.flush();
                for (int i = 0; i < ar.size(); i++) {
                    for (int j = 0; j < ar.get(i).size(); j++) {

                        bw.write(ar.get(i).get(j) + "");

                    }
                    bw.write("\n");
                }
                bw.flush();
                bw.close();
            }

        } catch (IOException e) {
        }

    }

    public void makeRootQueue() {                                           //initial root node is made in this and inserted into main queue

        rootNode = new Node();
        rootNode.lizardsLeft = lizardsTotal;

        if (algoUsed.equals("BFS")) {

            bfsQueue.add(rootNode);
            BFS();

        } else if (algoUsed.equals("DFS")) {

            dfsStack.add(rootNode);
            DFS();

        } else if (algoUsed.equals("SA")) {
            initialPlacement();
        }

    }

    public ArrayList<ArrayList<Integer>> makeParentMatrix(Stack<LizardsPositions> lizardsPlaced) {

        ArrayList<ArrayList<Integer>> parentMatrix = (ArrayList<ArrayList<Integer>>) deepClone(inputMatrix);
        for (int i = 0; i < lizardsPlaced.size(); i++) {
            parentMatrix.get(lizardsPlaced.get(i).row).set(lizardsPlaced.get(i).col, 1);
        }
        return parentMatrix;

    }

    public void makeChildrenBFS(Node queueHead) {

        ArrayList<ArrayList<Integer>> parent = makeParentMatrix(queueHead.lizardsPlaced); //makes clone of input matrix and place respective queens in it using stack
        Boolean placed = false;
        Node child;

        if (queueHead.lizardsLeft == lizardsTotal) {                             // handles case when atleast one queen is placed
            parent = makeParentMatrix(queueHead.lizardsPlaced);

            for (int row = 0; row < nurserySize; row++) {
                placed = false;
                for (int col = 0; col < nurserySize; col++) {

                    if (parent.get(row).get(col) == 0 && lizardSafe(parent, row, col)) {

                        child = new Node();
                        child.makeCopy(queueHead);
                        child.lizardsPlaced.add(new LizardsPositions(row, col));
                        child.lizardsLeft = child.lizardsLeft - 1;
                        bfsQueue.add(child);
                        placed = true;
                    }

                }
                if (placed == true) {
                    break;
                }
            }

        } else {                                                              //handles case when no queen is placed till now and stack is empty

            int rowlast = queueHead.lizardsPlaced.peek().row;
            int collast = queueHead.lizardsPlaced.peek().col;
            int row, col;
            parent = makeParentMatrix(queueHead.lizardsPlaced);

            for (row = rowlast; row < nurserySize; row++) {
                placed = false;
                for (col = collast; col < nurserySize; col++) {

                    if (parent.get(row).get(col) == 0 && lizardSafe(parent, row, col)) {
                        child = new Node();
                        child.makeCopy(queueHead);
                        child.lizardsPlaced.add(new LizardsPositions(row, col));
                        child.lizardsLeft = child.lizardsLeft - 1;
                        bfsQueue.add(child);
                        placed = true;
                    }

                }
                collast = 0;
                if (placed == true) {
                    break;
                }
            }

        }

    }

    public void makeChildrenDFS(Node queueHead) {

        ArrayList<ArrayList<Integer>> parent = makeParentMatrix(queueHead.lizardsPlaced); //makes clone of input matrix and place respective queens in it using stack
        Boolean placed = false;
        Node child;

        if (queueHead.lizardsLeft == lizardsTotal) {                             // handles case when atleast one queen is placed

            for (int row = 0; row < nurserySize; row++) {
                placed = false;
                for (int col = 0; col < nurserySize; col++) {
                    parent = makeParentMatrix(queueHead.lizardsPlaced);
                    if (parent.get(row).get(col) == 0 && lizardSafe(parent, row, col)) {

                        child = new Node();
                        child.makeCopy(queueHead);
                        child.lizardsPlaced.add(new LizardsPositions(row, col));
                        child.lizardsLeft = child.lizardsLeft - 1;
                        dfsStack.add(child);
                        placed = true;
                    }

                }
                if (placed == true) {
                    break;
                }
            }

        } else {                                                              //handles case when no queen is placed till now and stack is empty

            int rowlast = queueHead.lizardsPlaced.peek().row;
            int collast = queueHead.lizardsPlaced.peek().col;
            int row, col;

            for (row = rowlast; row < nurserySize; row++) {
                placed = false;
                for (col = collast; col < nurserySize; col++) {

                    parent = makeParentMatrix(queueHead.lizardsPlaced);
                    if (parent.get(row).get(col) == 0 && lizardSafe(parent, row, col)) {

                        child = new Node();
                        child.makeCopy(queueHead);
                        child.lizardsPlaced.add(new LizardsPositions(row, col));
                        child.lizardsLeft = child.lizardsLeft - 1;
                        dfsStack.add(child);
                        placed = true;
                    }

                }
                collast = 0;
                if (placed == true) {
                    break;
                }
            }

        }

    }

    private boolean lizardSafe(ArrayList<ArrayList<Integer>> child_matrix, int row, int col) {

        int k, l;

        for (k = row - 1; k >= 0; k--) {                //del all cells in up direction

            if (child_matrix.get(k).get(col) == 2) {
                break;
            } else if (child_matrix.get(k).get(col) == 1) {
                return false;
            }
        }

        for (k = row + 1; k < nurserySize; k++) {        //del all cells in down direction

            if (child_matrix.get(k).get(col) == 2) {
                break;
            } else if (child_matrix.get(k).get(col) == 1) {
                return false;
            }
        }

        for (k = col - 1; k >= 0; k--) {                //del all cells in left direction

            if (child_matrix.get(row).get(k) == 2) {
                break;
            } else if (child_matrix.get(row).get(k) == 1) {
                return false;
            }
        }

        for (k = col + 1; k < nurserySize; k++) { //del all cells in right direction

            if (child_matrix.get(row).get(k) == 2) {
                break;
            } else if (child_matrix.get(row).get(k) == 1) {
                return false;
            }
        }

        for (k = row - 1, l = col + 1; k >= 0 && l < nurserySize; k--, l++) { //del all cells in up-right direction

            if (child_matrix.get(k).get(l) == 2) {
                break;
            } else if (child_matrix.get(k).get(l) == 1) {
                return false;
            }
        }

        for (k = row - 1, l = col - 1; k >= 0 && l >= 0; k--, l--) {        //del all cells in up-left direction

            if (child_matrix.get(k).get(l) == 2) {
                break;
            } else if (child_matrix.get(k).get(l) == 1) {
                return false;
            }
        }

        for (k = row + 1, l = col + 1; k < nurserySize && l < nurserySize; k++, l++) { //del all cells in down-right direction

            if (child_matrix.get(k).get(l) == 2) {
                break;
            } else if (child_matrix.get(k).get(l) == 1) {
                return false;
            }
        }

        for (k = row + 1, l = col - 1; k < nurserySize && l >= 0; k++, l--) { //del all cells in down-left direction

            if (child_matrix.get(k).get(l) == 2) {
                break;
            } else if (child_matrix.get(k).get(l) == 1) {
                return false;
            }
        }

        return true;
    }

    private void BFS() {                                                    //implememnts BFS method and gives output

        Node queueHead = new Node();
        int f = 0;
        int counter = 0;
        while (bfsQueue.size() > 0) {
            counter++;

            queueHead = bfsQueue.poll();

            if (queueHead.lizardsLeft == 0) {                               //all are placed so show result               
                f = 1;
                //System.out.println("c=" + counter);
                outputMatrix = makeParentMatrix(queueHead.lizardsPlaced);
                System.out.println(outputMatrix);
                giveOutput(outputMatrix, f);
                break;

            } else {
                makeChildrenBFS(queueHead);
            }
        }

        if (bfsQueue.size() == 0 && f == 0) {
            System.out.println("fAILED");
            giveOutput(null, 0);
        }

    }

    private void DFS() {                                                    //implements DFS method and gives output

        Node queueHead = new Node();
        int f = 0;
        int counter = 0;
        while (dfsStack.size() > 0) {

            counter++;

            queueHead = dfsStack.pop();

            if (queueHead.lizardsLeft == 0) {                               //all are placed so show result               
                f = 1;
                //System.out.println("c=" + counter);
                outputMatrix = makeParentMatrix(queueHead.lizardsPlaced);
                System.out.println("ok");
                System.out.println(outputMatrix);
                giveOutput(outputMatrix, f);
                break;

            } else {
                makeChildrenDFS(queueHead);
            }
        }

        if (dfsStack.size() == 0 && f == 0) {
            System.out.println("FAILED");
            giveOutput(null, 0);
        }

    }

    public static void main(String args[]) {                                 //main method runs from here

        Homework1 hw1 = new Homework1();
        hw1.takeInput();

    }
}
