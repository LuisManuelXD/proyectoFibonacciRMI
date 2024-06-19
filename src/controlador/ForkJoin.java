package controlador;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class ForkJoin extends RecursiveAction {

    private static final int UMBRAL_ORDENAMIENTO_SECUENCIAL = 3000;
    private int[] arreglo;
    private int izq, der;
//    private int[] fibCache;

    public ForkJoin(int[] arreglo, int izq, int der) {
        this.arreglo = arreglo;
        this.izq = izq;
        this.der = der;
//        this.fibCache = new int[arreglo.length];
    }

    @Override
    protected void compute() {
        if (der - izq < UMBRAL_ORDENAMIENTO_SECUENCIAL) {
            calcular();
        } else {
            int m = (izq + der) / 2;
            ForkJoin izquierda = new ForkJoin(arreglo, izq, m);
            ForkJoin derecha = new ForkJoin(arreglo, m + 1, der);
            invokeAll(izquierda, derecha);
        }
    }

    private void calcular() {
        for (int i = izq; i <= der; i++) {
            arreglo[i] = calcularFibonacci(arreglo[i]);
        }
    }

    private int calcularFibonacci(int n) {
        if (n <= 0) {
            return 0;
        } else if (n == 1) {
            return 1;
        } else {
            int a = 0;
            int b = 1;
            int result = 0;
            for (int i = 2; i <= n; i++) {
                result = a + b;
                a = b;
                b = result;
            }
            return result;
        }
    }
//    private int calcularFibonacci(int n) {
//        if (n <= 0) {
//            return 0;
//        } else if (n == 1) {
//            return 1;
//        } else if (fibCache[n] != 0) {
//            return fibCache[n];
//        } else {
//            int a = 0;
//            int b = 1;
//            int result = 0;
//            for (int i = 2; i <= n; i++) {
//                result = a + b;
//                a = b;
//                b = result;
//            }
//            fibCache[n] = result;
//            return result;
//        }
//    }
}