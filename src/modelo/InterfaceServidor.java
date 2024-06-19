package modelo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceServidor extends Remote {
    void registro(InterfaceCliente cliente) throws RemoteException;
    String recepcionDatos(String nombre, int cantidadDatos) throws RemoteException;
    int[] obtenerArregloConcatenado() throws RemoteException;
//    void ejecutarMetodoSecuencial() throws RemoteException;
//    void ejecutarMetodoExecutor() throws RemoteException;
//    void ejecutarMetodoFork() throws RemoteException;

    void ejecutarMetodoSecuencial(int[] arregloConcatenado) throws RemoteException;
    void ejecutarMetodoFork(int[] arregloConcatenado) throws RemoteException;
    void ejecutarMetodoExecutor(int[] arregloConcatenado) throws RemoteException;
}
