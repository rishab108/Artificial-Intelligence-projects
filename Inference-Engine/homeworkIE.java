
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class Sentence {

    ArrayList<Predicate> predicatesList;

    Sentence() {
        predicatesList = new ArrayList<>();
    }

}

class Predicate {

    String predicateName;
    ArrayList<String> predicateArguments;

    Predicate() {
        predicateArguments = new ArrayList<>();
    }

}

public class homeworkIE {

    int queryNumber;
    int sentencesNumber;
    static ArrayList<String> queries = new ArrayList<>();
    static ArrayList<String> sentences = new ArrayList<>();
    static ArrayList<String> queryAnswers = new ArrayList<>();

    static ArrayList<Sentence> queriesList = new ArrayList<>();
    static ArrayList<Sentence> knowledgeBase = new ArrayList<>();
    static ArrayList<Sentence> copyofknowledgeBase = new ArrayList<>();

    static ArrayList<String> allConstants = new ArrayList<>();
    static ArrayList<String> allVaribales = new ArrayList<>();

    HashMap<String, ArrayList<Integer>> mapToKB = new HashMap<>();
    HashMap<String, ArrayList<Integer>> copyofmapToKB = new HashMap<>();

    void takeInput() {

        try {

            BufferedReader br = new BufferedReader(new FileReader(new File("/Users/rishabkumar/Desktop/Coding/homeworkIE/src/input.txt")));
            StringBuilder temp = new StringBuilder();
            String line;
            String finalinput;

            while ((line = br.readLine()) != null) {
                temp.append(line);
                temp.append("\n");
            }

            finalinput = temp.toString();
            String ss[] = finalinput.split("\n");

            queryNumber = Integer.parseInt(ss[0]);

            for (int i = 1; i <= queryNumber; i++) {

                queries.add(ss[i]);

            }

            sentencesNumber = Integer.parseInt(ss[queryNumber + 1]);

            for (int i = 1; i <= sentencesNumber; i++) {

                sentences.add(ss[i + queryNumber + 1]);

            }

        } catch (IOException e) {
        }

    }

    void checkCopy() {

        ArrayList<Sentence> KBcopy = new ArrayList<>();
        KBcopy = copyKBToNewKB(knowledgeBase);

        System.out.println("Original one :");
        printSentences(knowledgeBase);

        System.out.println();
        System.out.println();

        System.out.println("Copied one :");
        printSentences(copyofknowledgeBase);

    }

    void checkCopyMap() {

        HashMap<String, ArrayList<Integer>> mapcopy = new HashMap<String, ArrayList<Integer>>();
        mapcopy = copyHashMaptoNewHashMap(mapToKB);

        System.out.println("Original one :");
        System.out.println(mapToKB);

        System.out.println();
        System.out.println();

        System.out.println("Copied one :");
        System.out.println(copyofmapToKB);

    }

    ArrayList<Sentence> copyKBToNewKB(ArrayList<Sentence> kb) {

        ArrayList<Sentence> newKb = new ArrayList<>();

        for (int k = 0; k < kb.size(); k++) {

            Sentence sc = new Sentence();

            Sentence s = kb.get(k);

            for (int i = 0; i < s.predicatesList.size(); i++) {

                Predicate p = new Predicate();
                p.predicateName = new String(s.predicatesList.get(i).predicateName);

                for (int j = 0; j < s.predicatesList.get(i).predicateArguments.size(); j++) {

                    p.predicateArguments.add(new String(s.predicatesList.get(i).predicateArguments.get(j)));

                }
                sc.predicatesList.add(p);

            }
            newKb.add(sc);
        }

        return newKb;
    }

    HashMap<String, ArrayList<Integer>> copyHashMaptoNewHashMap(HashMap<String, ArrayList<Integer>> mapToKB) {

        HashMap<String, ArrayList<Integer>> copy = new HashMap<>();

        if (mapToKB == null) {
            return null;
        }

        for (Map.Entry<String, ArrayList<Integer>> entry : mapToKB.entrySet()) {

            copy.put(entry.getKey(), entry.getValue());

        }
        return copy;

    }

    boolean compareTwoPredicates(Sentence one, Sentence two) {

        Predicate first = one.predicatesList.get(0);
        Predicate second = two.predicatesList.get(0);
        int flag = 1;

        if (first.predicateName.equals(second.predicateName)) {

            for (int i = 0; i < first.predicateArguments.size(); i++) {

                if (first.predicateArguments.get(i).equals(second.predicateArguments.get(i)) && flag == 1) {

                    continue;
                } else {
                    flag = 0;
                }

            }

        }
        if (flag == 0) {
            return false;
        } else {
            return true;
        }

    }

    void formatInput() {

        for (int i = 0; i < queries.size(); i++) {

            Sentence S = new Sentence();
            String ss[] = queries.get(i).split("\\|");

            for (int k = 0; k < ss.length; k++) {

                String q = ss[k].trim();
                Predicate T = new Predicate();
                String sss[] = q.split("\\(|\\,|\\)");
                T.predicateName = sss[0].trim();
                for (int j = 1; j < sss.length; j++) {
                    T.predicateArguments.add(sss[j].trim());
                }

                S.predicatesList.add(T);
            }

            queriesList.add(S);

        }

        for (int i = 0; i < sentences.size(); i++) {

            Sentence S = new Sentence();
            String ss[] = sentences.get(i).split("\\|");

            for (int k = 0; k < ss.length; k++) {

                String q = ss[k].trim();
                Predicate T = new Predicate();
                String sss[] = q.split("\\(|\\,|\\)");
                T.predicateName = sss[0].trim();
                fillHashMap(sss[0], i);
                for (int j = 1; j < sss.length; j++) {
                    if (isAVariable(sss[j].trim())) {
                        T.predicateArguments.add(sss[j].trim() + "" + i);
                    } else {
                        T.predicateArguments.add(sss[j].trim());
                    }
                    addConstantOrVaribale(sss[j].trim());
                }

                S.predicatesList.add(T);
            }

            knowledgeBase.add(S);

        }

    }

    void showInput() {

        System.out.println("Queries given are : ");
        System.out.println();

        for (int i = 0; i < queries.size(); i++) {

            System.out.println(queries.get(i));

        }

        System.out.println();
        System.out.println("Sentences given are : ");
        System.out.println();

        for (int i = 0; i < sentences.size(); i++) {

            System.out.println(sentences.get(i));

        }
        System.out.println();
        for (Map.Entry<String, ArrayList<Integer>> entry : mapToKB.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        System.out.println();

    }

    void addToKB(Sentence s) {

        Sentence newsn = new Sentence();
        newsn = copySentenceToNewSentence(s);
        knowledgeBase.add(newsn);

    }

    ArrayList<Integer> copyArrayList(ArrayList<Integer> input) {

        ArrayList<Integer> output = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            output.add(input.get(i));
        }
        return output;
    }

    void printSentences(ArrayList<Sentence> al) {

        for (int i = 0; i < al.size(); i++) {

            for (int j = 0; j < al.get(i).predicatesList.size(); j++) {

                System.out.print(al.get(i).predicatesList.get(j).predicateName + "(");
                for (int k = 0; k < al.get(i).predicatesList.get(j).predicateArguments.size(); k++) {
                    System.out.print(al.get(i).predicatesList.get(j).predicateArguments.get(k) + ".");
                }
                System.out.print(") ");
            }
            System.out.println();
        }
        System.out.println();
    }

    Sentence negateSentence(Sentence s) {

        StringBuilder originalPredicateName;
        StringBuilder negation = new StringBuilder("~");

        originalPredicateName = new StringBuilder(s.predicatesList.get(0).predicateName);

        if (originalPredicateName.charAt(0) == '~') {

            originalPredicateName.deleteCharAt(0);
            s.predicatesList.get(0).predicateName = originalPredicateName.toString();

        } else {
            originalPredicateName = negation.append(originalPredicateName);
            s.predicatesList.get(0).predicateName = originalPredicateName.toString();
        }
        return s;

    }

    void printSentence(Sentence s) {

        for (int i = 0; i < s.predicatesList.size(); i++) {

            System.out.print(s.predicatesList.get(i).predicateName);
            System.out.print(s.predicatesList.get(i).predicateArguments + " | ");

        }

    }

    void addConstantOrVaribale(String c) {

        if (c.charAt(0) >= 97 && c.charAt(0) <= 122 && !allVaribales.contains(c)) {
            allVaribales.add(c);
        } else if (c.charAt(0) >= 65 && c.charAt(0) <= 90 && !allConstants.contains(c)) {
            allConstants.add(c);
        }

    }

    boolean isAConstant(String string) {
        if (string.charAt(0) >= 65 && string.charAt(0) <= 90) {
            return true;
        }
        return false;
    }

    boolean isAVariable(String string) {
        if (string.charAt(0) >= 97 && string.charAt(0) <= 122) {
            return true;
        }
        return false;
    }

    void fillHashMap(String key, int row) {

        if (!mapToKB.containsKey(key)) {
            ArrayList<Integer> rowforkeys = new ArrayList<>();
            rowforkeys.add(row);
            mapToKB.put(key, rowforkeys);
        } else {
            mapToKB.get(key).add(row);

        }

    }

    Predicate negatePredicate(Predicate p) {

        Predicate newPredicate = new Predicate();
        newPredicate.predicateName = new String(p.predicateName);
        for (int i = 0; i < p.predicateArguments.size(); i++) {
            newPredicate.predicateArguments.add(new String(p.predicateArguments.get(i)));
        }

        StringBuilder originalPredicateName;
        StringBuilder negation = new StringBuilder("~");
        originalPredicateName = new StringBuilder(p.predicateName);
        if (originalPredicateName.charAt(0) == '~') {

            originalPredicateName.deleteCharAt(0);
            newPredicate.predicateName = originalPredicateName.toString();

        } else {
            originalPredicateName = negation.append(originalPredicateName);
            newPredicate.predicateName = originalPredicateName.toString();
        }
        return newPredicate;

    }

    HashMap<String, String> copyWholeHashMap(HashMap<String, String> c) {

        HashMap<String, String> cc = new HashMap<>();

        if (c == null) {
            return null;
        }

        for (Map.Entry<String, String> entry : c.entrySet()) {

            cc.put(entry.getKey(), entry.getValue());

        }
        return cc;

    }

    Sentence copySentenceToNewSentence(Sentence s) {

        Sentence sc = new Sentence();
        for (int i = 0; i < s.predicatesList.size(); i++) {

            Predicate p = new Predicate();
            p.predicateName = new String(s.predicatesList.get(i).predicateName);

            for (int j = 0; j < s.predicatesList.get(i).predicateArguments.size(); j++) {

                p.predicateArguments.add(new String(s.predicatesList.get(i).predicateArguments.get(j)));

            }
            sc.predicatesList.add(p);

        }
        return sc;
    }

    void printPredicate(Predicate p) {

        System.out.print(p.predicateName + " : ");
        for (int i = 0; i < p.predicateArguments.size(); i++) {
            System.out.print(p.predicateArguments.get(i) + " ");
        }
        System.out.println();

    }

    Predicate copyPredicateToNewPredicate(Predicate p) {

        Predicate newPredicate = new Predicate();

        newPredicate.predicateName = new String(p.predicateName);

        for (int j = 0; j < p.predicateArguments.size(); j++) {

            newPredicate.predicateArguments.add(new String(p.predicateArguments.get(j)));

        }

        return new Predicate();

    }

    ArrayList<Integer> sentencesIndexForGivenPredicate(String predicateName) {

        ArrayList<Integer> al = new ArrayList<>();
        al = copyofmapToKB.get(predicateName);
        return al;

    }

    Predicate findPredicateInSentence(Sentence s, String predicatename) {

        for (int i = 0; i < s.predicatesList.size(); i++) {
            if (s.predicatesList.get(i).predicateName.equals(predicatename)) {

                return s.predicatesList.get(i);
            }
        }
        return null;
    }

    ArrayList<Predicate> findAllPredicatesInASentence(Sentence s, String predicateName) {

        ArrayList<Predicate> list = new ArrayList<>();

        for (int i = 0; i < s.predicatesList.size(); i++) {
            if (s.predicatesList.get(i).predicateName.equals(predicateName)) {

                list.add(s.predicatesList.get(i));
            }
        }
        if (list.size() > 0) {
            return list;
        } else {
            return null;
        }
    }

    Sentence substituteVaribalesFromMap(Sentence s, HashMap<String, String> hm) {

        for (int i = 0; i < s.predicatesList.size(); i++) {

            for (int j = 0; j < s.predicatesList.get(i).predicateArguments.size(); j++) {

                if (hm.containsKey(s.predicatesList.get(i).predicateArguments.get(j))) {

                    s.predicatesList.get(i).predicateArguments.set(j, hm.get(s.predicatesList.get(i).predicateArguments.get(j)));

                }

            }

        }

        return s;
    }

    Sentence mergeTwoSentences(Sentence s1, Sentence s2) {

        for (int i = 0; i < s2.predicatesList.size(); i++) {
            s1.predicatesList.add(s2.predicatesList.get(i));
        }
        return s1;
    }

    Sentence deleteSamePredicate(Sentence s) {

        for (int i = 0; i < s.predicatesList.size();) {

            int flag = 0;

            Predicate firstwala = s.predicatesList.get(i);

            for (int j = i + 1; j < s.predicatesList.size(); j++) {

                Predicate dusrawala = s.predicatesList.get(j);

                if (firstwala.predicateName.equals(negatePredicate(dusrawala).predicateName)) {

                    for (int k = 0; k < firstwala.predicateArguments.size(); k++) {

                        if (firstwala.predicateArguments.get(k).equals(dusrawala.predicateArguments.get(k))) {
                            flag = 1;
//                            continue;

                        } else {
                            flag = 0;
                            break;
                        }

                    }

                }
                if (flag == 1) {

                    s.predicatesList.remove(firstwala);
                    s.predicatesList.remove(dusrawala);

                }
            }

            if (flag == 0) {
                i++;
            }
        }

        return s;
    }

    void performResolution() {

        for (int i = 0; i < queriesList.size(); i++) {

            copyofknowledgeBase = copyKBToNewKB(knowledgeBase);
            copyofmapToKB = copyHashMaptoNewHashMap(mapToKB);

            ArrayList<Integer> termUsage = new ArrayList<>();
            for (int k = 0; k <= knowledgeBase.size(); k++) {
                termUsage.add(k, 0);

            }

            Sentence negatedQuery = negateSentence(queriesList.get(i));
            Sentence negatedOne = copySentenceToNewSentence(negatedQuery);
            copyofknowledgeBase.add(negatedOne);

            if (!copyofmapToKB.containsKey(negatedOne.predicatesList.get(0).predicateName)) {
                copyofmapToKB.put(negatedOne.predicatesList.get(0).predicateName, new ArrayList<>(Arrays.asList(copyofknowledgeBase.size() - 1)));
            } else {

                ArrayList<Integer> value = copyofmapToKB.get(negatedOne.predicatesList.get(0).predicateName);
                value.add(copyofknowledgeBase.size() - 1);
                copyofmapToKB.put(negatedOne.predicatesList.get(0).predicateName, value);

            }

            boolean result = Resolution(negatedQuery, termUsage);
            if (result == true) {
                queryAnswers.add("TRUE");

            } else {
                queryAnswers.add("FALSE");

            }
        }

    }

    boolean Resolution(Sentence inputSentence, ArrayList<Integer> termUsage) {

        if (inputSentence == null || inputSentence.predicatesList.isEmpty()) {
            return true;
        }

        //printSentence(inputSentence);
        //System.out.println();
        //System.out.println();
        for (int i = 0; i < inputSentence.predicatesList.size(); i++) {

            Predicate originalPredicate = inputSentence.predicatesList.get(i);
            Predicate negatedPredicate = negatePredicate(originalPredicate);
            ArrayList<Integer> presentPredicateNegationList = sentencesIndexForGivenPredicate(negatedPredicate.predicateName);

            if (presentPredicateNegationList == null || presentPredicateNegationList.isEmpty()) {
                continue;
            }

            for (int j = 0; j < presentPredicateNegationList.size(); j++) {

                termUsage.set(presentPredicateNegationList.get(j), termUsage.get(presentPredicateNegationList.get(j)) + 1);
                if (termUsage.get(presentPredicateNegationList.get(j)) > 5) {

                    continue;
                }

                Sentence sentenceFromKB = copyofknowledgeBase.get(presentPredicateNegationList.get(j));
                Sentence copyInputSentence = copySentenceToNewSentence(inputSentence);
                Sentence copySentenceFromKB = copySentenceToNewSentence(sentenceFromKB);

                HashMap<String, String> newResult = new HashMap<>();

                newResult = unification(copyInputSentence, copySentenceFromKB);

                if (newResult != null) {

                    copyInputSentence = substituteVaribalesFromMap(copyInputSentence, newResult);
                    copySentenceFromKB = substituteVaribalesFromMap(copySentenceFromKB, newResult);
                    Sentence newSentence = mergeTwoSentences(copyInputSentence, copySentenceFromKB);
                    newSentence = deleteSamePredicate(newSentence);

                    if (newSentence.predicatesList.isEmpty()) {
                        return true;
                    }

                    if (!Resolution(copySentenceToNewSentence(newSentence), copyArrayList(termUsage))) {

                        continue;
                    } else {
                        return true;
                    }

                }

            }
        }
        return false;

    }

    HashMap<String, String> unification(Sentence inputSentence, Sentence sentenceFromKB) {

        HashMap<String, String> argValue = null;

        for (int k = 0; k < inputSentence.predicatesList.size(); k++) {

            Predicate originalPredicate = inputSentence.predicatesList.get(k);

            ArrayList<Predicate> neagtedPredicateList = findAllPredicatesInASentence(sentenceFromKB, negatePredicate(originalPredicate).predicateName);

            if (neagtedPredicateList == null || neagtedPredicateList.isEmpty()) {
                continue;
            }

            if (argValue == null) {

                argValue = new HashMap<>();
            }

            for (int j = 0; j < neagtedPredicateList.size(); j++) {

                int flag = 0;

                if (argValue == null) {

                    argValue = new HashMap<>();
                }

                for (int i = 0; i < originalPredicate.predicateArguments.size(); i++) {

                    String arg1 = originalPredicate.predicateArguments.get(i);
                    String arg2 = neagtedPredicateList.get(j).predicateArguments.get(i);

                    if (isAConstant(arg1) && isAConstant(arg2)) {
                        if (!arg1.equals(arg2)) {
                            argValue = null;
                            break;
                        } else {
                            if (i == (originalPredicate.predicateArguments.size() - 1)) {
                                flag = 1;
                            }
                            continue;
                        }

                    } else if (isAConstant(arg1) && isAVariable(arg2)) {
                        if (!argValue.containsKey(arg1)) {
                            argValue.put(arg2, arg1);
                        } else if (argValue.containsKey(arg2)) {

                            if (isAConstant(argValue.get(arg2)) && arg2.equals(argValue.get(arg2))) {

                                if (i == (originalPredicate.predicateArguments.size() - 1)) {
                                    flag = 1;
                                }

                                continue;

                            } else if (isAConstant(argValue.get(arg2)) && !arg2.equals(argValue.get(arg2))) {
                                argValue = null;
                                break;
                            } else if (isAVariable(argValue.get(arg2))) {

                                argValue.put(argValue.get(arg2), arg1);
                            }
                        } else {
                            argValue = null;
                            break;
                        }

                    } else if (isAVariable(arg1) && isAConstant(arg2)) {
                        if (!argValue.containsKey(arg1)) {
                            argValue.put(arg1, arg2);
                        } else if (argValue.containsKey(arg1)) {
                            if (isAConstant(argValue.get(arg1)) && arg2.equals(argValue.get(arg1))) {
                                if (i == (originalPredicate.predicateArguments.size() - 1)) {
                                    flag = 1;
                                }
                                continue;
                            } else if (isAConstant(argValue.get(arg1)) && !arg2.equals(argValue.get(arg1))) {
                                argValue = null;
                                break;
                            } else if (isAVariable(argValue.get(arg1))) {

                                argValue.put(argValue.get(arg1), arg2);
                            }
                        } else {
                            argValue = null;
                            break;
                        }

                    } else if (isAVariable(arg1) && isAVariable(arg2)) {

                        if (!argValue.containsKey(arg1) && !argValue.containsKey(arg2)) {
                            argValue.put(arg1, arg2);

                        } else if (argValue.containsKey(arg1) && argValue.containsKey(arg2)) {

                            if (argValue.get(arg1).equals(argValue.get(arg2))) {

                                if (i == (originalPredicate.predicateArguments.size() - 1)) {
                                    flag = 1;
                                }
                                continue;

                            } else {

                                if (isAConstant(argValue.get(arg1)) && isAConstant(argValue.get(arg2)) && !argValue.get(arg1).equals(argValue.get(arg2))) {
                                    argValue = null;
                                    break;
                                } else if (isAConstant(argValue.get(arg1)) && isAConstant(argValue.get(arg2)) && argValue.get(arg1).equals(argValue.get(arg2))) {
                                    continue;
                                } else if (isAConstant(argValue.get(arg1)) && isAVariable(argValue.get(arg2))) {
                                    //argValue.put(argValue.get(arg2), argValue.get(arg1));
                                    argValue.put(arg2, argValue.get(arg1));
                                } else if (isAVariable(arg1) && isAConstant(arg2)) {
                                    //argValue.put(argValue.get(arg1), argValue.get(arg2));
                                    argValue.put(arg1, argValue.get(arg2));

                                } else {
                                    //argValue.put(argValue.get(arg1), argValue.get(arg2));
                                    argValue.put(arg1, arg2);
                                    argValue.put(arg2, arg2);
                                }

                            }
                        } else if (!argValue.containsKey(arg1) && argValue.containsKey(arg2)) {
                            argValue.put(arg1, argValue.get(arg2));

                        } else if (argValue.containsKey(arg1) && !argValue.containsKey(arg2)) {
                            argValue.put(arg2, argValue.get(arg1));
                        }

                    }

                }
                if (flag == 1) {
                    break;
                }
            }
        }

        return argValue;
    }

    void giveOutput(ArrayList<String> ans) {

        try {
            File f = new File("/Users/rishabkumar/Desktop/Coding/homeworkIE/src/output.txt");
            if (f.exists()) {
                f.delete();
            } else {
                f.createNewFile();
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));

            for (int i = 0; i < ans.size(); i++) {

                bw.write(ans.get(i) + "\n");

            }
            bw.flush();
            bw.close();

        } catch (IOException e) {
        }

    }

    public static void main(String[] args) {

        homeworkIE ie = new homeworkIE();
        ie.takeInput();
        ie.formatInput();
        ie.performResolution();
        ie.giveOutput(queryAnswers);

    }

}
