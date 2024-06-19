package controlador;

import modelo.InterfaceCliente;
import modelo.InterfaceServidor;
import vista.VistaCliente;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ImplementacionClienteOperaciones extends UnicastRemoteObject implements InterfaceCliente, Runnable {
    private InterfaceServidor servidor;
    private VistaCliente vistaCliente;
    public String nombre;

    public ImplementacionClienteOperaciones(String nombre, InterfaceServidor servidor, VistaCliente vistaCliente) throws RemoteException {
        this.nombre = nombre;
        this.servidor = servidor;
        this.vistaCliente = vistaCliente;
        servidor.registro(this);
    }

    @Override
    public String envioDatos(int cantidadDatos) throws RemoteException {
        return servidor.recepcionDatos(nombre, cantidadDatos);
    }

    @Override
    public void recepcionDatos(String resultados, int numTiempo, String duracion) throws RemoteException {
        vistaCliente.txtResultado.setText(resultados);
        switch(numTiempo) {
            case 1 -> vistaCliente.lblTiempoSecuencial.setText(duracion);
            case 2 -> vistaCliente.lblTiempoForkJoin.setText(duracion);
            case 3 -> vistaCliente.lblTiempoExecuteService.setText(duracion);
        }
    }

    @Override
    public void run() {

    }

    public void ejecutarMetodoSecuencial() throws RemoteException {
        int[] arregloConcatenado = servidor.obtenerArregloConcatenado();
        servidor.ejecutarMetodoSecuencial(arregloConcatenado);
    }

    public void ejecutarMetodoFork() throws RemoteException {
        int[] arregloConcatenado = servidor.obtenerArregloConcatenado();
        servidor.ejecutarMetodoFork(arregloConcatenado);
    }

    public void ejecutarMetodoExecutor() throws RemoteException {
        int[] arregloConcatenado = servidor.obtenerArregloConcatenado();
        servidor.ejecutarMetodoExecutor(arregloConcatenado);
    }
}
