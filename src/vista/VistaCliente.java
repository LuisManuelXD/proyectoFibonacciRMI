package vista;

import controlador.Fibonacci;
import controlador.ImplementacionClienteOperaciones;
import modelo.InterfaceServidor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VistaCliente extends JFrame {
    private String nombre;
    private String host;
    private int puerto;

    // instancias con clases para resolver
    private Fibonacci fibonacci;
    private int[] arregloTemp;

    // Objetos para conexion
    private Thread cliente;
    private InterfaceServidor servidor;
    private ImplementacionClienteOperaciones imp;

    // Objetos de ventana
    private JLabel lblIteraciones;
    private JTextField txtIteraciones;
    private JButton btnGenerarValores;
    private JLabel lblOriginal;
    private JLabel lblResultado;
    private JTextArea txtOriginal;
    private JScrollPane scrollPaneTxtOriginal;
    public JTextArea txtResultado;
    public JScrollPane scrollPaneTxtResultado;
    private JButton btnSecuencial;
    private JButton btnForkJoin;
    private JButton btnExecuteService;
    private JButton btnLimpiar;
    public JLabel lblTiempoSecuencial;
    public JLabel lblTiempoForkJoin;
    public JLabel lblTiempoExecuteService;

    public VistaCliente(String nombre, String host, int puerto) throws NotBoundException, RemoteException {
        vista();
        eventoBotones();

        this.nombre = nombre;
        this.host = host;
        this.puerto = puerto;

        conectar();
    }

    public void conectar() throws RemoteException, NotBoundException {
        Registry rmii = LocateRegistry.getRegistry(host, puerto);

        this.servidor = (InterfaceServidor) rmii.lookup("MergeRMI");
        JOptionPane.showMessageDialog(null, "Cliente " + nombre + " conectado a " + host);
//        ImplementacionClienteOperaciones imp = new ImplementacionClienteOperaciones("riostorres", servidor, this);
        this.imp = new ImplementacionClienteOperaciones("riostorres", servidor, this);
        cliente = new Thread(this.imp);
    }

    public void limpiar() {
        txtIteraciones.setText("");
        txtOriginal.setText("");
        txtResultado.setText("");
        lblTiempoSecuencial.setText("Tiempo: 0");
        lblTiempoForkJoin.setText("Tiempo: 0");
        lblTiempoExecuteService.setText("Tiempo: 0");
        arregloTemp = null;
    }

    public void eventoBotones() {
        btnGenerarValores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!txtIteraciones.getText().equals("")) {
                        if(servidor != null) {
                            int cantidad = Integer.parseInt(txtIteraciones.getText());
                            String mensaje = servidor.recepcionDatos(nombre, cantidad);
                            txtOriginal.setText(mensaje);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Ingrese el tamanio del arreglo");
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(VistaCliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        btnSecuencial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    imp.ejecutarMetodoSecuencial();
                } catch (RemoteException ex) {
                    Logger.getLogger(VistaCliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        btnForkJoin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    imp.ejecutarMetodoFork();
                } catch (RemoteException ex) {
                    Logger.getLogger(VistaCliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        btnExecuteService.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    imp.ejecutarMetodoExecutor();
                } catch (RemoteException ex) {
                    Logger.getLogger(VistaCliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiar();
            }
        });
    }

    public void vista() {
        lblIteraciones = new JLabel("Numero:");
        txtIteraciones = new JTextField();
        btnGenerarValores = new JButton("Generar");
        lblOriginal = new JLabel("Original");
        lblResultado = new JLabel("Resultado");
        txtOriginal = new JTextArea();
        scrollPaneTxtOriginal = new JScrollPane(txtOriginal);
        txtResultado = new JTextArea();
        scrollPaneTxtResultado = new JScrollPane(txtResultado);
        btnSecuencial = new JButton("Secuencial");
        btnForkJoin = new JButton("Fork Join");
        btnExecuteService = new JButton("Execute Service");
        btnLimpiar = new JButton("Limpiar");
        lblTiempoSecuencial = new JLabel("Tiempo: 0");
        lblTiempoForkJoin = new JLabel("Tiempo: 0");
        lblTiempoExecuteService = new JLabel("Tiempo: 0");

        lblIteraciones.setBounds(10, 6, 100, 50);
        txtIteraciones.setBounds(65, 10, 200,30);
        btnGenerarValores.setBounds(300, 10, 100, 30);
        lblOriginal.setBounds(10,50,50,20);
        txtOriginal.setBounds(10, 75, 250, 130);
        txtOriginal.setLineWrap(true);
        txtOriginal.setWrapStyleWord(true);
        scrollPaneTxtOriginal.setBounds(10, 75, 250, 130);
        lblResultado.setBounds(300,50,70,20);
        txtResultado.setBounds(300, 75, 360, 130);
        txtResultado.setLineWrap(true);
        scrollPaneTxtResultado.setBounds(300, 75, 360, 130);
        btnSecuencial.setBounds(10, 210, 100, 30);
        lblTiempoSecuencial.setBounds(10, 250, 100, 30);
        btnForkJoin.setBounds(120, 210, 140, 30);
        lblTiempoForkJoin.setBounds(120, 250, 100, 30);
        btnExecuteService.setBounds(270, 210, 140, 30);
        lblTiempoExecuteService.setBounds(270, 250, 150, 30);
        btnLimpiar.setBounds(420, 210, 80, 30);

        add(lblIteraciones);
        add(txtIteraciones);
        add(btnGenerarValores);
        add(lblOriginal);
        //add(txtOriginal);
        add(scrollPaneTxtOriginal);
        add(lblResultado);
        //add(txtResultado);
        add(scrollPaneTxtResultado);
        add(btnSecuencial);
        add(lblTiempoSecuencial);
        add(btnForkJoin);
        add(lblTiempoForkJoin);
        add(btnExecuteService);
        add(lblTiempoExecuteService);
        add(btnLimpiar);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
