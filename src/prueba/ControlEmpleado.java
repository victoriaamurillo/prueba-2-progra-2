/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prueba;

import java.io.IOException;
import java.util.Scanner;

public class ControlEmpleado {

    public static void main(String[] args) {
        EmpleadoManager manager = new EmpleadoManager();
        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            System.out.println("**** MENU ****");
            System.out.println("1- Agregar Empleado");
            System.out.println("2- Listar Empleados No Despedidos");
            System.out.println("3- Despedir Empleado");
            System.out.println("4- Buscar Empleado Activo");
            System.out.println("5- Salir");
            System.out.print("Elige una opcion: ");
            option = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            try {
                switch (option) {
                    case 1:
                        System.out.print("Nombre del Empleado: ");
                        String name = scanner.nextLine();
                        System.out.print("Salario del Empleado: ");
                        double salary = scanner.nextDouble();
                        manager.addEmployee(name, salary);
                        System.out.println("Empleado agregado exitosamente.");
                        break;
                    case 2:
                        manager.printActiveEmployees();
                        break;
                    case 3:
                        System.out.print("Codigo del Empleado a Despedir: ");
                        int codeToDismiss = scanner.nextInt();
                        if (!manager.dismissEmployee(codeToDismiss)) {
                            System.out.println("Empleado no encontrado o ya despedido.");
                        }
                        break;
                    case 4:
                        System.out.print("Codigo del Empleado a Buscar: ");
                        int codeToSearch = scanner.nextInt();
                        String employeeDetails = manager.getEmployeeDetails(codeToSearch);
                        if (employeeDetails != null) {
                            System.out.println(employeeDetails);
                        } else {
                            System.out.println("El empleado no esta activo o no existe.");
                        }
                        break;
                    case 5:
                        System.out.println("Byeee...");
                        break;
                    default:
                        System.out.println("Opcion no valida");
                }
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }

        } while (option != 5);

        scanner.close();
    }
}