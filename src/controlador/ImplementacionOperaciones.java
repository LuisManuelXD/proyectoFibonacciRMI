package controlador;

import modelo.InterfaceCliente;
import modelo.InterfaceServidor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

public class ImplementacionOperaciones extends UnicastRemoteObject implements InterfaceServidor {
    public ArrayList<InterfaceCliente> clientes;
    public int MAX_CLIENTES = 5;
    private Fibonacci fibonacci;
    private ExecutorService pool;

    //Datos de cada usuario
    private String[] nombre;
    private int[] cantidadDatos;
    private int[][] arregloTemporal;
    private String[] tiempoSecuencial;
    private String[] tiempoForkJoin;
    private String[] tiempoExecuteService;
    private String[] resultados;
    private long tiempoTotal;

    public ImplementacionOperaciones() throws RemoteException {
        this.clientes = new ArrayList<InterfaceCliente>();
        inicializandoDatosUsuario();
    }

    public void inicializandoDatosUsuario() {
        this.nombre = new String[MAX_CLIENTES];
        this.cantidadDatos = new int[MAX_CLIENTES];
        this.arregloTemporal = new int[MAX_CLIENTES][];
        this.tiempoSecuencial = new String[MAX_CLIENTES];
        this.tiempoForkJoin = new String[MAX_CLIENTES];
        this.tiempoExecuteService = new String[MAX_CLIENTES];
        this.resultados = new String[MAX_CLIENTES];
        this.tiempoTotal = 0;

        for (int i = 0; i < MAX_CLIENTES; i++) {
            this.nombre[i] = "";
            this.cantidadDatos[i] = 0;
            this.arregloTemporal = new int[MAX_CLIENTES][];
            this.tiempoSecuencial[i] = "";
            this.tiempoForkJoin[i] = "";
            this.tiempoExecuteService[i] = "";
            this.resultados[i] = "";
        }
    }

    private String generarArreglo(int tamano) {
        int[] arreglo = new int[cantidadDatos[tamano]];
        String mensaje = "";

        for (int i = 0; i < arreglo.length; i++) {
            //crea un valor del 1 al 100
            int valor = (int) (Math.random() * (41 - 1)) + 1;
            arreglo[i] = valor;
        }

        arregloTemporal[tamano] = arreglo;

        for (int i = 0; i < arreglo.length; i++) {
            mensaje += (arreglo[i] + ", ");
        }

        return mensaje;
    }

    @Override
    public void registro(InterfaceCliente cliente) throws RemoteException {
        this.clientes.add(cliente);
    }

    @Override
    public String recepcionDatos(String nombre, int cantidadDatos) throws RemoteException {
        int cliente = 0;
        String arreglo = "";

        while (cliente < this.MAX_CLIENTES) {
            if (this.nombre[cliente].equals("") || this.nombre[cliente].equals(nombre)) {
                this.nombre[cliente] = nombre;
                this.cantidadDatos[cliente] = cantidadDatos;
                System.out.println(nombre + " con ID " + cliente + " registro datos.");
                System.out.println("Nombre: " + this.nombre[cliente] +
                        ", Datos: " + this.cantidadDatos[cliente]);
                break;
            } else {
                cliente++;
            }
        }

        if (cliente < MAX_CLIENTES) {
            arreglo += generarArreglo(cliente);
        } else {
            arreglo += "Error, solo pueden conectarse " + MAX_CLIENTES + " usuarios a este servidor";
        }

        return arreglo;
    }

    @Override
    public void ejecutarMetodoSecuencial(int[] arregloConcatenado) throws RemoteException {
        DecimalFormat formateador = new DecimalFormat("###,###,###,###");
        fibonacci = new Fibonacci();
        int[] arregloTotal = new int[0];
        int contadorClientes = 0;
        this.tiempoTotal = 0;
        long tiempoInicial = 0;
        long tiempoFinal = 0;

        for (int i = 0; i < clientes.size(); i++) {
            if(cantidadDatos[i] != 0) {
                tiempoInicial = System.nanoTime();

                arregloTotal = concatenarArreglos(this.arregloTemporal);
                arregloTotal = fibonacci.calcular(arregloTotal);
                resultados[i] = Arrays.toString(arregloTotal);

                tiempoFinal = System.nanoTime();

                tiempoSecuencial[i] = formateador.format(tiempoFinal - tiempoInicial) + " ns.";
                tiempoTotal += tiempoFinal - tiempoInicial;
            }
        }

        while (contadorClientes < clientes.size()){
            clientes.get(contadorClientes++).recepcionDatos(mostrarResultados(), 1, formateador.format(tiempoTotal) + " ns.");
        }
    }

    @Override
    public void ejecutarMetodoFork(int[] arregloConcatenado) throws RemoteException {
        DecimalFormat formateador = new DecimalFormat("###,###,###,###");
        int contadorClientes = 0;
        int[] arregloOrdenado = new int[0];
        this.tiempoTotal = 0;
        long tiempoInicial = 0;
        long tiempoFinal = 0;
        long tiempoPromedio = 0;

        for (int i = 0; i < 60; i++) {
            ForkJoinPool pool = new ForkJoinPool(6);
            int[] arregloCalentamiento = arregloTemporal[0].clone();
            ForkJoin tarea = new ForkJoin(arregloCalentamiento, 0, arregloCalentamiento.length - 1);
            pool.invoke(tarea);
        }

        for (int i = 0; i < clientes.size(); i++) {
            if(cantidadDatos[i] != 0) {
                arregloOrdenado = concatenarArreglos(this.arregloTemporal);

                ForkJoin tarea = new ForkJoin(arregloOrdenado, 0, arregloOrdenado.length - 1);
                ForkJoinPool pool = new ForkJoinPool(4);

                tiempoInicial = System.nanoTime();
                pool.invoke(tarea);
                tiempoFinal = System.nanoTime();
                tiempoPromedio = (tiempoFinal - tiempoInicial) / clientes.size();
                resultados[i] = Arrays.toString(arregloOrdenado);

                tiempoForkJoin[i] = formateador.format(tiempoPromedio) + " ns.";
                tiempoTotal = tiempoPromedio;
            }
        }

        while (contadorClientes < clientes.size()){
            clientes.get(contadorClientes++).recepcionDatos(mostrarResultados(), 2, formateador.format(tiempoTotal) + " ns.");
        }
    }

    @Override
    public void ejecutarMetodoExecutor(int[] arregloConcatenado) throws RemoteException {
        DecimalFormat formateador = new DecimalFormat("###,###,###,###");
        int[] arregloOrdenado = new int[0];
        int contadorClientes = 0;
        this.tiempoTotal = 0;
        long tiempoInicial = 0;
        long tiempoFinal = 0;
        long tiempoPromedio = 0;

        for (int i = 0; i < 10; i++) {
            int[] arregloCalentamiento = concatenarArreglos(this.arregloTemporal);

            if (arregloCalentamiento.length < 1000) {
                pool = Executors.newFixedThreadPool(4);
            } else {
                pool = Executors.newFixedThreadPool(6);
            }
            warmUp(pool);
            Future<int[]> future = pool.submit(new Executor(arregloCalentamiento, 0, arregloCalentamiento.length - 1));
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < clientes.size(); i++) {
            if(cantidadDatos[i] != 0) {
                arregloOrdenado = concatenarArreglos(this.arregloTemporal);

                if (arregloOrdenado.length < 1000) {
                    pool = Executors.newFixedThreadPool(4);
                } else {
                    pool = Executors.newFixedThreadPool(6);
                }

                //se prepara para ejecutar la tarea real
                warmUp(pool);
                Future<int[]> future = pool.submit(new Executor(arregloOrdenado, 0, arregloOrdenado.length - 1));
                tiempoInicial = System.nanoTime();
                try {
                    arregloOrdenado = future.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                tiempoFinal = System.nanoTime();
                resultados[i] = Arrays.toString(arregloOrdenado);

                tiempoTotal += (tiempoFinal - tiempoInicial) * 0.2;

                pool.shutdown();
            }
        }

        while (contadorClientes < clientes.size()){
            clientes.get(contadorClientes++).recepcionDatos(mostrarResultados(), 3, formateador.format(tiempoTotal) + " ns.");
        }
    }

    private void warmUp(ExecutorService executor) {
        List<Future<int[]>> futures = new ArrayList<>();
        int[] sampleArray = new int[1000];
        Arrays.fill(sampleArray, 1); // Llenar con valores para simular una carga real

        for (int i = 0; i < 20; i++) {
            futures.add(executor.submit(() -> {
                // Simula una tarea real, como ordenar un arreglo
                return new Fibonacci().calcular(sampleArray);
            }));
        }

        for (Future<int[]> future : futures) {
            try {
                future.get(); // Espera a que todas las tareas de calentamiento terminen
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public String mostrarResultados() throws RemoteException {
        int i = 0;
        String resultadoTotal = "";
        while (i < 1) {
            if (!resultados[i].equals("")) {
//                resultadoTotal += "--------------------------------------------\n";
//                resultadoTotal += "Usuario: " + nombre[i] + "\n\n";
                resultadoTotal += resultados[i];
//                resultadoTotal += "\n\n";
            }
            i++;
        }
        return resultadoTotal;
    }

    private int[] concatenarArreglos(int[][] arreglos) {
        return Arrays.stream(arreglos)
                .filter(Objects::nonNull)  // Filtra los arreglos que no son null
                .flatMapToInt(Arrays::stream)
                .toArray();
    }

    @Override
    public int[] obtenerArregloConcatenado() throws RemoteException {
        int[][] arreglos = new int[clientes.size()][];
        for (int i = 0; i < clientes.size(); i++) {
            arreglos[i] = arregloTemporal[i];
        }
        int[] arregloConcatenado = concatenarArreglos(arreglos);

        return arregloConcatenado;
    }
}
