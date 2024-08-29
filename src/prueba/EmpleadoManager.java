/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prueba;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EmpleadoManager {
    private RandomAccessFile rcods, remps;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public EmpleadoManager() {
        try {
            File mf = new File("Company");
            mf.mkdir();
            rcods = new RandomAccessFile("company/codigo.emp", "rw");
            remps = new RandomAccessFile("company/empleado.emp", "rw");
            initCodes();
        } catch (IOException e) {
        }
    }

    private void initCodes() throws IOException {
        if (rcods.length() == 0) {
            rcods.writeInt(1); 
        }
    }

    private int getCode() throws IOException {
        rcods.seek(0);
        int codigo = rcods.readInt();
        rcods.seek(0);
        rcods.writeInt(codigo + 1);
        return codigo;
    }

    public void addEmployee(String name, double monto) throws IOException {
        remps.seek(remps.length());
        int code = getCode();
        remps.writeInt(code);
        remps.writeUTF(name);
        remps.writeDouble(monto);
        remps.writeLong(Calendar.getInstance().getTimeInMillis());
        remps.writeLong(0); // No despedido
        createEmployeeFolders(code);
    }

    private String employeeFolder(int code) {
        return "company/empleado" + code;
    }

    private void createEmployeeFolders(int code) throws IOException {
        File edir = new File(employeeFolder(code));
        edir.mkdir();
        this.createYearSalesFile(code);
    }

    private RandomAccessFile salesFileFor(int code) throws IOException {
        String dirPadre = employeeFolder(code);
        int yearActual = Calendar.getInstance().get(Calendar.YEAR);
        String path = dirPadre + "/ventas" + yearActual + ".emp";
        return new RandomAccessFile(path, "rw");
    }

    private void createYearSalesFile(int code) throws IOException {
        RandomAccessFile raf = salesFileFor(code);
        if (raf.length() == 0) {
            for (int mes = 0; mes < 12; mes++) {
                raf.writeDouble(0);
                raf.writeBoolean(false);
            }
        }
    }

    public void printActiveEmployees() throws IOException {
        remps.seek(0);
        System.out.println("**** LISTA DE EMPLEADOS ****");
        while (remps.getFilePointer() < remps.length()) {
            int code = remps.readInt();
            String name = remps.readUTF();
            double salary = remps.readDouble();
            long fechaContratacion = remps.readLong();
            long fechaDespido = remps.readLong();

            if (fechaDespido == 0) { 
                Date date = new Date(fechaContratacion);
                System.out.println(String.format("%d- %s- %.2f Lps- %s", code, name, salary, dateFormat.format(date)));
            }
        }
    }

    public boolean isEmployeeActive(int code) throws IOException {
        remps.seek(0);
        while (remps.getFilePointer() < remps.length()) {
            int empCode = remps.readInt();
            if (empCode == code) {
                remps.readUTF(); 
                remps.readDouble(); 
                remps.readLong(); 
                long fechaDespido = remps.readLong();
                return fechaDespido == 0;
            } else {
                remps.readUTF(); 
                remps.readDouble(); 
                remps.readLong(); 
                remps.readLong();
            }
        }
        return false;
    }

    public boolean dismissEmployee(int code) throws IOException {
        remps.seek(0);
        while (remps.getFilePointer() < remps.length()) {
            int empCode = remps.readInt();
            String name = remps.readUTF();
            remps.readDouble(); 
            remps.readLong(); 
            long fechaDespido = remps.readLong();

            if (empCode == code && fechaDespido == 0) {
                remps.seek(remps.getFilePointer() - 8);
                remps.writeLong(Calendar.getInstance().getTimeInMillis());
                System.out.println("Empleado despedido: " + name);
                return true;
            }
        }
        return false;
    }

    public String getEmployeeDetails(int code) throws IOException {
        remps.seek(0);
        while (remps.getFilePointer() < remps.length()) {
            int empCode = remps.readInt();
            String name = remps.readUTF();
            double salary = remps.readDouble();
            long fechaContratacion = remps.readLong();
            long fechaDespido = remps.readLong();

            if (empCode == code) {
                Date date = new Date(fechaContratacion);
                String despidoInfo = fechaDespido == 0 ? "Activo" : "Despedido";
                return String.format(
                    "Codigo: %d\nNombre: %s\nSalario: %.2f Lps\nFecha de Contratacion: %s\nEstado: %s",
                    empCode, name, salary, dateFormat.format(date), despidoInfo
                );
            }
        }
        return null;
    }
}