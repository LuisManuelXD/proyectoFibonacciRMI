package mains;//import vista.VistaServidor;

import controlador.ImplementacionOperaciones;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MainServidor {
    static int PUERTO = 3000;
    public static void main(String[] args) throws RemoteException {
        Registry rmi = LocateRegistry.createRegistry(PUERTO);
        rmi.rebind("MergeRMI", (Remote) new ImplementacionOperaciones() { });
        System.out.println("Servidor Activo");
    }
}
