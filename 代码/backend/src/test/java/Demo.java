import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

public class Demo {
    public static void main(String[] args) {
        Object[] a = {};
        Object[] b = {};
        String c = "";
        String d = "";
        System.out.println(a==b);
        System.out.println(c==d);
        ArrayList<Integer> list = new ArrayList<>();
        // 1 2 3 4 5 7 8 9 10 11
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
            if (i == 2) {
                list.remove(i);
            }
        }
        Object[] abab = new Object[list.size()];
        Deque<String> deque = new LinkedList<>();
        String.join(" ", deque);
    }
}
