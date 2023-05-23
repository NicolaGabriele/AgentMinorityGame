package minoritiGame;

import java.util.Arrays;
import java.util.HashMap;

public class Observer {

    private int[] A = new int[Utility.TIME_LIMIT];
    private double FS = 0,Nsc = 0;

    public void notifyA(int time, int a){
        A[time] = a;
    }

    public void updateFS(int a, int b){
        double winner = (a<b)?a:b;
        FS+= winner/Utility.N;
    }

    public double FS(){
        return FS/Utility.TIME_LIMIT;
    }

    public void updateNsc(int nsc){
        Nsc+=((double)nsc)/Utility.N;
    }

    public double Nsc(){
        return Nsc/Utility.TIME_LIMIT;
    }

    public void printA(){
        for(int i = 0; i<A.length; i++)
            System.out.println(A[i]);
    }

}
