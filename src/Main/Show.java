package Main;

import java.util.List;

public class Show {
    public void showList(List<?> arr, String text) {
        System.out.println("Show " + text);
        for(var a: arr) {
            System.out.println(a);
        }

        System.out.println();
    }
}
