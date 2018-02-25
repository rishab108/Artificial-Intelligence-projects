
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.lang.management.*;
import java.util.Comparator;

class Location {

    int row;
    int col;

    public Location(int r, int c) {
        row = r;
        col = c;
    }

}

class Moves {

    ArrayList<Location> movesTillNow;
    int currentMoveScore;

    public Moves(int s) {

        currentMoveScore = s;
        movesTillNow = new ArrayList<>();

    }
}

public class homework {

    static int boardSize;
    static int fruitsType;
    static double timeLeft;

    static ArrayList<ArrayList<Character>> inputBoard = new ArrayList<>();
    static ArrayList<ArrayList<Boolean>> fruitVisted = new ArrayList<>();
    static Moves root = new Moves(0);
    static Moves finalMove;
    static ArrayList<ArrayList<Character>> finalBoard = new ArrayList<>();
    static int count = 0;
    static int maxDepth;
    static double possibleMoves;

    public ArrayList<ArrayList<Character>> copyInputBoard(ArrayList<ArrayList<Character>> input) {

        ArrayList<ArrayList<Character>> copy = new ArrayList<>();
        for (int i = 0; i < boardSize; i++) {

            copy.add(new ArrayList<>());

            for (int j = 0; j < boardSize; j++) {

                copy.get(i).add(input.get(i).get(j));
            }

        }
        return copy;

    } //copy input board

    public ArrayList<ArrayList<Boolean>> copyInputBoardBoolean(ArrayList<ArrayList<Boolean>> input) {

        ArrayList<ArrayList<Boolean>> copy = new ArrayList<>();
        for (int i = 0; i < boardSize; i++) {

            copy.add(new ArrayList<>());

            for (int j = 0; j < boardSize; j++) {

                copy.get(i).add(input.get(i).get(j));
            }

        }
        return copy;

    } //copy input board boolean

    public ArrayList<ArrayList<Boolean>> resetBoolean(ArrayList<ArrayList<Character>> input) {

        ArrayList<ArrayList<Boolean>> booleaninput = copyInputBoardBoolean(fruitVisted);
        for (int i = 0; i < boardSize; i++) {

            for (int j = 0; j < boardSize; j++) {

                if (input.get(i).get(j) == '*') {
                    booleaninput.get(i).set(j, Boolean.TRUE);

                } else {
                    booleaninput.get(i).set(j, Boolean.FALSE);
                }
            }
        }
        return booleaninput;

    }// to regenerate the boolean matrix

    public void tempDisplay(ArrayList<ArrayList<Character>> input) {

        for (int i = 0; i < boardSize; i++) {

            for (int j = 0; j < boardSize; j++) {

                System.out.print(input.get(i).get(j));
            }
            System.out.println();

        }
    } // display function not used or useful just utility

    public void tempDisplayBoolean(ArrayList<ArrayList<Boolean>> input) {

        for (int i = 0; i < boardSize; i++) {

            for (int j = 0; j < boardSize; j++) {

                System.out.print(input.get(i).get(j));
            }
            System.out.println();

        }
        System.out.println();
    } // display function not used or useful just utility

    public ArrayList<Location> copyAllLocations(ArrayList<Location> previousone) {

        ArrayList<Location> newone = new ArrayList<>();

        for (int i = 0; i < previousone.size(); i++) {
            newone.add(previousone.get(i));
        }

        return newone;

    } //copy all previous locations

    public void displaymoves(ArrayList<Moves> move) {

        for (int i = 0; i < move.size(); i++) {

            for (int j = 0; j < move.get(i).movesTillNow.size(); j++) {

                System.out.print("(" + move.get(i).movesTillNow.get(j).row + "," + move.get(i).movesTillNow.get(j).col + ")");

            }
            System.out.println();
            //tempDisplay(generateMatrix(move.get(i).movesTillNow));
            System.out.println(move.get(i).currentMoveScore);
            System.out.println();
        }

    } //display all results

    public ArrayList<ArrayList<Character>> makeOutput(Location l) {

        ArrayList<ArrayList<Character>> copyboard = copyInputBoard(inputBoard);
        ArrayList<ArrayList<Boolean>> copyboardboolean = copyInputBoardBoolean(fruitVisted);

        Queue<Location> tempQueue = new LinkedList<>();

        tempQueue.add(l);
        char ch = copyboard.get(l.row).get(l.col);
        int score = 0;

        while (tempQueue.size() > 0) { //make that loc *

            int r = tempQueue.peek().row;
            int c = tempQueue.peek().col;
            copyboardboolean.get(r).set(c, true);
            copyboard.get(r).set(c, '*');
            tempQueue.poll();
            score++;

            if (r - 1 >= 0 && copyboard.get(r - 1).get(c) == ch && copyboardboolean.get(r - 1).get(c) == false) {
                tempQueue.add(new Location(r - 1, c));
                copyboardboolean.get(r - 1).set(c, true);
            }
            if (r + 1 < boardSize && copyboard.get(r + 1).get(c) == ch && copyboardboolean.get(r + 1).get(c) == false) {
                tempQueue.add(new Location(r + 1, c));
                copyboardboolean.get(r + 1).set(c, true);
            }
            if (c - 1 >= 0 && copyboard.get(r).get(c - 1) == ch && copyboardboolean.get(r).get(c - 1) == false) {
                tempQueue.add(new Location(r, c - 1));
                copyboardboolean.get(r).set(c - 1, true);
            }
            if (c + 1 < boardSize && copyboard.get(r).get(c + 1) == ch && copyboardboolean.get(r).get(c + 1) == false) {
                tempQueue.add(new Location(r, c + 1));
                copyboardboolean.get(r).set(c + 1, true);
            }

        }

        for (int col = 0; col < boardSize; col++) { //gravity

            int count = 0;
            for (int row = boardSize - 1; row >= 0; row--) {

                if (copyboard.get(row).get(col) != '*') {

                    copyboard.get(boardSize - 1 - count).set(col, copyboard.get(row).get(col));
                    count++;

                }
            }
            while (count < boardSize) {
                copyboard.get(boardSize - 1 - count).set(col, '*');
                count++;
            }

        }

        for (int k = 0; k < boardSize; k++) { //reset boolean for next loc in list

            for (int j = 0; j < boardSize; j++) {

                if (copyboard.get(k).get(j) == '*') {
                    copyboardboolean.get(k).set(j, Boolean.TRUE);

                } else {
                    copyboardboolean.get(k).set(j, Boolean.FALSE);
                }
            }

        }

        System.out.println("Fruits Selected: " + (score));
        return copyboard;

    }

    public boolean checkExitCondition(ArrayList<Location> locations) {

        ArrayList<ArrayList<Character>> copyboard = copyInputBoard(inputBoard);
        ArrayList<ArrayList<Boolean>> copyboardboolean = copyInputBoardBoolean(fruitVisted);

        for (int i = 0; i < locations.size(); i++) {

            Queue<Location> tempQueue = new LinkedList<>();
            Location l = locations.get(i);
            tempQueue.add(l);
            char ch = copyboard.get(l.row).get(l.col);

            while (tempQueue.size() > 0) { //make that loc *

                int r = tempQueue.peek().row;
                int c = tempQueue.peek().col;
                copyboardboolean.get(r).set(c, true);
                copyboard.get(r).set(c, '*');
                tempQueue.poll();

                if (r - 1 >= 0 && copyboard.get(r - 1).get(c) == ch && copyboardboolean.get(r - 1).get(c) == false) {
                    tempQueue.add(new Location(r - 1, c));
                    copyboardboolean.get(r - 1).set(c, true);
                }
                if (r + 1 < boardSize && copyboard.get(r + 1).get(c) == ch && copyboardboolean.get(r + 1).get(c) == false) {
                    tempQueue.add(new Location(r + 1, c));
                    copyboardboolean.get(r + 1).set(c, true);
                }
                if (c - 1 >= 0 && copyboard.get(r).get(c - 1) == ch && copyboardboolean.get(r).get(c - 1) == false) {
                    tempQueue.add(new Location(r, c - 1));
                    copyboardboolean.get(r).set(c - 1, true);
                }
                if (c + 1 < boardSize && copyboard.get(r).get(c + 1) == ch && copyboardboolean.get(r).get(c + 1) == false) {
                    tempQueue.add(new Location(r, c + 1));
                    copyboardboolean.get(r).set(c + 1, true);
                }

            }

            for (int col = 0; col < boardSize; col++) { //gravity

                int count = 0;
                for (int row = boardSize - 1; row >= 0; row--) {

                    if (copyboard.get(row).get(col) != '*') {

                        copyboard.get(boardSize - 1 - count).set(col, copyboard.get(row).get(col));
                        count++;

                    }
                }
                while (count < boardSize) {
                    copyboard.get(boardSize - 1 - count).set(col, '*');
                    count++;
                }

            }

            for (int k = 0; k < boardSize; k++) { //reset boolean for next loc in list

                for (int j = 0; j < boardSize; j++) {

                    if (copyboard.get(k).get(j) == '*') {
                        copyboardboolean.get(k).set(j, Boolean.TRUE);

                    } else {
                        copyboardboolean.get(k).set(j, Boolean.FALSE);
                    }
                }

            }

        }
        int starcount = 0;
        for (int k = 0; k < boardSize; k++) { //reset boolean for next loc in list

            for (int j = 0; j < boardSize; j++) {

                if (copyboardboolean.get(k).get(j) == Boolean.FALSE) {
                    starcount++;
                }

            }
        }
        if (starcount == 0) {
            return true;
        }
        return false;
    } //generate matrix from given location set 

    public ArrayList<Integer> orderMoves(ArrayList<Moves> oldmovesorder) {

        ArrayList<Integer> newmovesorder = new ArrayList<>();
        ArrayList<Boolean> accessedornot = new ArrayList<>();
        ArrayList<Integer> sortedscore = new ArrayList<>();

        for (int i = 0; i < oldmovesorder.size(); i++) {
            accessedornot.add(Boolean.FALSE);
        }

        for (int i = 0; i < oldmovesorder.size(); i++) {
            sortedscore.add(oldmovesorder.get(i).currentMoveScore);
        }
        //System.out.println(sortedscore);
        Collections.sort(sortedscore);
        //System.out.println(sortedscore);

        for (int i = 0; i < sortedscore.size(); i++) {

            for (int j = 0; j < oldmovesorder.size(); j++) {

                if ((sortedscore.get(i) == oldmovesorder.get(j).currentMoveScore) && (accessedornot.get(j) == Boolean.FALSE)) {

                    newmovesorder.add(j);
                    accessedornot.set(j, Boolean.TRUE);
                    break;

                }

            }

        }
        //System.out.println(newmovesorder);
        return newmovesorder;

    }

    public void takeInput() {

        try {

            BufferedReader br = new BufferedReader(new FileReader(new File("/Users/rishabkumar/My Own/Coding/homworkMinimax/src/input.txt")));
            StringBuilder temp = new StringBuilder();
            String line;
            String finalinput;

            while ((line = br.readLine()) != null) {
                temp.append(line);
                temp.append("\n");
            }

            finalinput = temp.toString();
            String ss[] = finalinput.split("\n");

            boardSize = Integer.parseInt(ss[0]);
            fruitsType = Integer.parseInt(ss[1]);
            timeLeft = Double.parseDouble(ss[2]);

            for (int i = 3; i < ss.length; i++) {

                inputBoard.add(new ArrayList<>());

                for (char c : ss[i].toCharArray()) {
                    inputBoard.get(i - 3).add(c);

                }
            }

            for (int i = 0; i < boardSize; i++) {

                fruitVisted.add(new ArrayList<>());

                for (int j = 0; j < boardSize; j++) {

                    if (inputBoard.get(i).get(j) == '*') {
                        fruitVisted.get(i).add(Boolean.TRUE);

                    } else {
                        fruitVisted.get(i).add(Boolean.FALSE);
                    }
                }

            }

        } catch (IOException e) {
        }
    } //input taken

    public void giveOutput(ArrayList<ArrayList<Character>> finalBoard, Location l) {

        try {
            File f = new File("/Users/rishabkumar/My Own/Coding/homworkMinimax/src/output.txt");
            if (f.exists()) {
                f.delete();
            } else {
                f.createNewFile();
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));

            bw.write((char) (l.col + 65) + "" + (int) (l.row + 1) + "\n");

            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    bw.write(finalBoard.get(i).get(j));
                }
                bw.write("\n");
            }
            bw.flush();
            bw.flush();
            bw.close();

        } catch (IOException e) {
        }

    } // gives output

    public ArrayList<ArrayList<Character>> generateMatrix(ArrayList<Location> locations) {

        ArrayList<ArrayList<Character>> copyboard = copyInputBoard(inputBoard);
        ArrayList<ArrayList<Boolean>> copyboardboolean = copyInputBoardBoolean(fruitVisted);

        for (int i = 0; i < locations.size(); i++) {

            Queue<Location> tempQueue = new LinkedList<>();
            Location l = locations.get(i);
            tempQueue.add(l);
            char ch = copyboard.get(l.row).get(l.col);

            while (tempQueue.size() > 0) { //make that loc *

                int r = tempQueue.peek().row;
                int c = tempQueue.peek().col;
                copyboardboolean.get(r).set(c, true);
                copyboard.get(r).set(c, '*');
                tempQueue.poll();

                if (r - 1 >= 0 && copyboard.get(r - 1).get(c) == ch && copyboardboolean.get(r - 1).get(c) == false) {
                    tempQueue.add(new Location(r - 1, c));
                    copyboardboolean.get(r - 1).set(c, true);
                }
                if (r + 1 < boardSize && copyboard.get(r + 1).get(c) == ch && copyboardboolean.get(r + 1).get(c) == false) {
                    tempQueue.add(new Location(r + 1, c));
                    copyboardboolean.get(r + 1).set(c, true);
                }
                if (c - 1 >= 0 && copyboard.get(r).get(c - 1) == ch && copyboardboolean.get(r).get(c - 1) == false) {
                    tempQueue.add(new Location(r, c - 1));
                    copyboardboolean.get(r).set(c - 1, true);
                }
                if (c + 1 < boardSize && copyboard.get(r).get(c + 1) == ch && copyboardboolean.get(r).get(c + 1) == false) {
                    tempQueue.add(new Location(r, c + 1));
                    copyboardboolean.get(r).set(c + 1, true);
                }

            }

            for (int col = 0; col < boardSize; col++) { //gravity

                int count = 0;
                for (int row = boardSize - 1; row >= 0; row--) {

                    if (copyboard.get(row).get(col) != '*') {

                        copyboard.get(boardSize - 1 - count).set(col, copyboard.get(row).get(col));
                        count++;

                    }
                }
                while (count < boardSize) {
                    copyboard.get(boardSize - 1 - count).set(col, '*');
                    count++;
                }

            }

            for (int k = 0; k < boardSize; k++) { //reset boolean for next loc in list

                for (int j = 0; j < boardSize; j++) {

                    if (copyboard.get(k).get(j) == '*') {
                        copyboardboolean.get(k).set(j, Boolean.TRUE);

                    } else {
                        copyboardboolean.get(k).set(j, Boolean.FALSE);
                    }
                }

            }
        }
        return copyboard;
    } //generate matrix from given location set

    public ArrayList<Moves> allMovesOnCurrentBoard(Moves parent, String player) {

        ArrayList<ArrayList<Character>> currentBoard = generateMatrix(parent.movesTillNow);
        ArrayList<ArrayList<Boolean>> currentBoardBoolean = resetBoolean(currentBoard);
        ArrayList<Moves> all = new ArrayList<>();

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {

                if (currentBoardBoolean.get(i).get(j) == false) {

                    char ch = currentBoard.get(i).get(j);
                    Queue<Location> tempQueue = new LinkedList<>();
                    int score = 0;
                    int finalscore = 0;
                    tempQueue.add(new Location(i, j));

                    while (tempQueue.size() > 0) {

                        int r = tempQueue.peek().row;
                        int c = tempQueue.peek().col;
                        currentBoardBoolean.get(i).set(j, true);
                        score++;
                        tempQueue.poll();

                        if (r - 1 >= 0 && currentBoard.get(r - 1).get(c) == ch && currentBoardBoolean.get(r - 1).get(c) == false) {
                            tempQueue.add(new Location(r - 1, c));
                            currentBoardBoolean.get(r - 1).set(c, true);
                        }
                        if (r + 1 < boardSize && currentBoard.get(r + 1).get(c) == ch && currentBoardBoolean.get(r + 1).get(c) == false) {
                            tempQueue.add(new Location(r + 1, c));
                            currentBoardBoolean.get(r + 1).set(c, true);
                        }
                        if (c - 1 >= 0 && currentBoard.get(r).get(c - 1) == ch && currentBoardBoolean.get(r).get(c - 1) == false) {
                            tempQueue.add(new Location(r, c - 1));
                            currentBoardBoolean.get(r).set(c - 1, true);
                        }
                        if (c + 1 < boardSize && currentBoard.get(r).get(c + 1) == ch && currentBoardBoolean.get(r).get(c + 1) == false) {
                            tempQueue.add(new Location(r, c + 1));
                            currentBoardBoolean.get(r).set(c + 1, true);
                        }
                    }
                    score = (int) Math.pow(score, 2);
                    if (player.equals("MAX")) {
                        finalscore = (int) (parent.currentMoveScore + (score));
                        //finalscore = (int) Math.pow((int) Math.sqrt(parent.currentMoveScore) + score, 2);

                    } else {
                        finalscore = (int) (parent.currentMoveScore - (score));
                        //finalscore = (int) Math.pow((int) Math.sqrt(parent.currentMoveScore) - score, 2);
                    }
                    Moves newmove = new Moves(finalscore);
                    newmove.movesTillNow = copyAllLocations(parent.movesTillNow);
                    newmove.movesTillNow.add(new Location(i, j));
                    all.add(newmove);
                }

            }
        }
        return all;

    }

    public void orderMovesComapartorMin(ArrayList<Moves> oldmovesorder) {

        Collections.sort(oldmovesorder, new Comparator<Moves>() {
            public int compare(Moves m1, Moves m2) {
                if (m1.currentMoveScore == m2.currentMoveScore) {
                    return 0;
                } else if (m1.currentMoveScore > m2.currentMoveScore) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

    }

    public void orderMovesComapartorMax(ArrayList<Moves> oldmovesorder) {

        Collections.sort(oldmovesorder, new Comparator<Moves>() {
            public int compare(Moves m1, Moves m2) {
                if (m1.currentMoveScore == m2.currentMoveScore) {
                    return 0;
                } else if (m1.currentMoveScore > m2.currentMoveScore) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });

    }

    public int minimax(int depth, Moves parent, String player, int alpha, int beta) {

        count++;
        if (depth == 0) {

            return parent.currentMoveScore;
        }

        ArrayList<Moves> allmoves = allMovesOnCurrentBoard(parent, player);

        if (player.equals("MIN")) {
            orderMovesComapartorMin(allmoves);
        } else {
            orderMovesComapartorMax(allmoves);
        }

        int size = allmoves.size() - 1;

        if (allmoves.isEmpty()) {
            return parent.currentMoveScore;
        }

        int currentScore = 0;

        for (int i = 0; i < allmoves.size(); i++) {

            if (player.equals("MAX")) {
                currentScore = minimax(depth - 1, allmoves.get(i), "MIN", alpha, beta);

                if (currentScore > alpha) {
                    alpha = currentScore;
                    if (depth == maxDepth) {
                        finalMove = allmoves.get(i);
                    }
                }
                if (alpha >= beta) {
                    break;
                }
            } else {
                currentScore = minimax(depth - 1, allmoves.get(i), "MAX", alpha, beta);
                beta = Math.min(currentScore, beta);

                if (alpha >= beta) {
                    break;
                }
            }
        }
        if ("MAX".equals(player)) {
            return alpha;
        } else {
            return beta;
        }

    } //main minimax function with pruning

    public void minimax_test(int depth, Moves parent, String player, int alpha, int beta) {

        ArrayList<Moves> allmoves = allMovesOnCurrentBoard(parent, player);
        possibleMoves = allmoves.size();

    } //main minimax function with pruning

    public int calculateDepth() {

        int depth = 3;

        if (timeLeft < 1.00) {
            depth = 1;
        } else if (timeLeft < 2.00) {
            depth = 2;
        } else {
            if (boardSize < 6) {
                depth = 5;
            } else if (boardSize < 17) {
                if (possibleMoves <= 100) {
                    depth = 5;
                } else if (possibleMoves <= 200) {
                    depth = 4;
                }
            } else if (boardSize < 22) {
                if (possibleMoves < 60) {
                    depth = 5;
                } else if (possibleMoves < 100) {
                    depth = 4;
                }
            } else if (boardSize < 27) {
                if (possibleMoves < 40) {
                    depth = 5;
                } else if (possibleMoves < 80) {
                    depth = 4;
                }
            }
        }
        return depth;
    }

    public static void main(String args[]) {

        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        double ini = bean.isCurrentThreadCpuTimeSupported() ? bean.getCurrentThreadCpuTime() : 0L;

        homework hw = new homework();
        hw.takeInput();
        hw.minimax_test(1, root, "MAX", Integer.MIN_VALUE, Integer.MAX_VALUE);
        System.out.println(possibleMoves);
        maxDepth = hw.calculateDepth();
        System.out.println("max depth is :" + maxDepth);
        int best = hw.minimax(maxDepth, root, "MAX", Integer.MIN_VALUE, Integer.MAX_VALUE);
        finalBoard = hw.makeOutput(finalMove.movesTillNow.get(0));
        hw.giveOutput(finalBoard, finalMove.movesTillNow.get(0));

        double fin = bean.isCurrentThreadCpuTimeSupported() ? bean.getCurrentThreadCpuTime() : 0L;
        System.out.println("time is : " + (fin - ini) / 1000000000);
        System.out.println("final Move : " + finalMove.movesTillNow.get(0).row + "," + finalMove.movesTillNow.get(0).col + " \nScore : " + best);
        System.out.println("Total Count : " + count);

    }

}
