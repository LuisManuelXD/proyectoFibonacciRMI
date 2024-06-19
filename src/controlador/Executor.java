package controlador;

import java.util.concurrent.*;

import static java.util.concurrent.ForkJoinTask.invokeAll;

//public class Executor implements Runnable {
//
//    private static final int UMBRAL_ORDENAMIENTO_SECUENCIAL = 1000;
//    public int[] arreglo;
//    int izq, der;
//
//    public Executor(int[] arreglo, int izq, int der) {
//        this.arreglo = arreglo;
//        this.izq = izq;
//        this.der = der;
//    }
//
//    @Override
//    public void run() {
//        if (der - izq < UMBRAL_ORDENAMIENTO_SECUENCIAL) {
//            calcular();
//        } else {
//            int m = (izq + der) / 2;
//            ForkJoin izquierda = new ForkJoin(arreglo, izq, m);
//            ForkJoin derecha = new ForkJoin(arreglo, m + 1, der);
//            invokeAll(izquierda, derecha);
//        }
//    }
//
//    private void calcular() {
//        for (int i = izq; i <= der; i++) {
//            arreglo[i] = calcularFibonacci(arreglo[i]);
//        }
//    }
//
//    private int calcularFibonacci(int n) {
//        if (n <= 0) {
//            return 0;
//        } else if (n == 1) {
//            return 1;
//        } else {
//            int a = 0;
//            int b = 1;
//            int result = 0;
//            for (int i = 2; i <= n; i++) {
//                result = a + b;
//                a = b;
//                b = result;
//            }
//            return result;
//        }
//    }
//}

public class Executor implements Callable<int[]> {

    private static final int UMBRAL_ORDENAMIENTO_SECUENCIAL = 20000;
    public int[] arreglo;
    int izq, der;

    public Executor(int[] arreglo, int izq, int der) {
        this.arreglo = arreglo;
        this.izq = izq;
        this.der = der;
    }

    @Override
    public int[] call() {
        if (der - izq < UMBRAL_ORDENAMIENTO_SECUENCIAL) {
            calcular();
        } else {
            int m = (izq + der) / 2;
            ExecutorService executor = Executors.newFixedThreadPool(2);
            Future<int[]> izquierda = executor.submit(new Executor(arreglo, izq, m));
            Future<int[]> derecha = executor.submit(new Executor(arreglo, m + 1, der));
            try {
                izquierda.get();
                derecha.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            executor.shutdown();
        }
        return arreglo;
    }

    private void calcular() {
        for (int i = izq; i <= der; i++) {
            arreglo[i] = calcularFibonacci(arreglo[i]);
        }
    }

    public int calcularFibonacci(int n) {
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
}