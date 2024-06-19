package modelo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceCliente extends Remote {
    String envioDatos(int cantidadDatos) throws RemoteException;
    void recepcionDatos(String resultados, int numTiempo, String duracion) throws RemoteException;
}
