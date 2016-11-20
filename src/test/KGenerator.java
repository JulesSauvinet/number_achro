package test;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by jules on 20/11/2016.
 */
public class KGenerator {
    public static void main (String[] args) throws IOException {
        try{
            for (int i = 7 ; i <= 30; i++){
                PrintWriter writer = new PrintWriter("k" +i, "UTF-8");
                writer.println(i);
                for (int v=1; v<=i ; v++){
                    writer.println("v"+v);
                }
                writer.println((i*(i-1))/2);
                for (int v1=1; v1<=i-1 ; v1++){
                    for (int v2=v1+1; v2<=i; v2++){
                        writer.println("v"+v1+" v"+v2);
                    }
                }
                writer.close();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
