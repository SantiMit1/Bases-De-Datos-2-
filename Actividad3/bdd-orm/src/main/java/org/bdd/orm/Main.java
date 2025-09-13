package org.bdd.orm;

import org.bdd.orm.tablas.Estudiante;
import org.bdd.orm.tablas.Materia;
import org.bdd.orm.tablas.Profesor;
import org.bdd.orm.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while(true) {
            for (int i = 0; i < 5; i++) System.out.println();
            System.out.println("1. Crear estudiante");
            System.out.println("2. Modificar estudiante");
            System.out.println("3. Eliminar estudiante");
            System.out.println("4. Crear profesor");
            System.out.println("5. Modificar profesor");
            System.out.println("6. Eliminar profesor");
            System.out.println("7. Crear materia");
            System.out.println("8. Modificar materia");
            System.out.println("9. Eliminar materia");
            System.out.println("10. Inscribir estudiante a materia");
            System.out.println("11. Ver estudiantes");
            System.out.println("12. Ver profesores");
            System.out.println("13. Ver materias");
            System.out.println("0. Salir");
            System.out.println("Seleccione una opción:");

            int opcion = sc.nextInt();
            switch (opcion) {
                case 1 -> crearEstudiante();
                case 2 -> modificarEstudiante();
                case 3 -> eliminarEstudiante();
                case 4 -> crearProfesor();
                case 5 -> modificarProfesor();
                case 6 -> eliminarProfesor();
                case 7 -> crearMateria();
                case 8 -> modificarMateria();
                case 9 -> eliminarMateria();
                case 10 -> inscribirEstudianteAMateria();
                case 11 -> verEstudiantes();
                case 12 -> verProfesores();
                case 13 -> verMaterias();
                case 0 -> {
                    HibernateUtil.getSessionFactory().close();
                    sc.close();
                    System.exit(0);
                }
                default -> System.out.println("Opción no válida.");
            }
        }
    }

    public static void crearEstudiante() {
        System.out.println("Ingrese el nombre del estudiante:");
        String nombre = sc.nextLine();

        Estudiante estudiante = new Estudiante(nombre);

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(estudiante);
            tx.commit();
            System.out.println("Estudiante creado con ID: " + estudiante.getId_estudiante());
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public static void modificarEstudiante() {
        System.out.println("Ingrese el ID del estudiante a modificar:");
        int id = Integer.parseInt(sc.nextLine());
        System.out.println("Ingrese el nuevo nombre del estudiante:");
        String nuevoNombre = sc.nextLine();

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Estudiante estudiante = session.find(Estudiante.class, id);
            if (estudiante != null) {
                estudiante.setNombre(nuevoNombre);
                session.merge(estudiante);
                System.out.println("Estudiante modificado.");
            } else {
                System.out.println("Estudiante no encontrado.");
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public static void eliminarEstudiante() {
        System.out.println("Ingrese el ID del estudiante a eliminar:");
        int id = Integer.parseInt(sc.nextLine());

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Estudiante estudiante = session.find(Estudiante.class, id);
            if (estudiante != null) {
                session.remove(estudiante);
                System.out.println("Estudiante eliminado.");
            } else {
                System.out.println("Estudiante no encontrado.");
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public static void crearProfesor() {
        System.out.println("Ingrese el nombre del profesor:");
        String nombre = sc.nextLine();

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Profesor profesor = new Profesor(nombre);
            session.persist(profesor);
            tx.commit();
            System.out.println("Profesor creado con ID." + profesor.getId_profesor());
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public static void modificarProfesor() {
        System.out.println("Ingrese el ID del profesor a modificar:");
        int id = sc.nextInt();
        System.out.println("Ingrese el nuevo nombre del profesor:");
        String nuevoNombre = sc.nextLine();

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Profesor profesor = session.find(Profesor.class, id);
            if (profesor != null) {
                profesor.setNombre(nuevoNombre);
                session.merge(profesor);
                System.out.println("Profesor modificado.");
            } else {
                System.out.println("Profesor no encontrado.");
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public static void eliminarProfesor() {
        System.out.println("Ingrese el ID del profesor a eliminar:");
        int id = sc.nextInt();

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Profesor profesor = session.find(Profesor.class, id);
            if (profesor != null) {
                session.remove(profesor);
                System.out.println("Profesor eliminado.");
            } else {
                System.out.println("Profesor no encontrado.");
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public static void crearMateria() {
        System.out.println("Ingrese el nombre de la materia:");
        String nombre = sc.nextLine();
        System.out.println("ID del profesor que dicta la materia (0 si no tiene):");
        int idProfesor = sc.nextInt();

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Materia materia = new Materia(nombre);
            if (idProfesor != 0) {
                Profesor profesor = session.find(Profesor.class, idProfesor);
                if (profesor != null) {
                    materia.setProfesor(profesor);
                } else {
                    System.out.println("Profesor no encontrado. La materia se creará sin profesor.");
                }
            }
            session.persist(materia);
            tx.commit();
            System.out.println("Materia creada con ID." + materia.getId_materia());
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public static void eliminarMateria() {
        System.out.println("Ingrese el ID de la materia a eliminar:");
        int id = sc.nextInt();

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Materia materia = session.find(Materia.class, id);
            if (materia != null) {
                session.remove(materia);
                System.out.println("Materia eliminada.");
            } else {
                System.out.println("Materia no encontrada.");
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public static void modificarMateria() {
        System.out.println("Ingrese el ID de la materia a modificar:");
        int id = sc.nextInt();
        System.out.println("Ingrese el nuevo nombre de la materia:");
        String nuevoNombre = sc.nextLine();

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Materia materia = session.find(Materia.class, id);
            if (materia != null) {
                materia.setNombre(nuevoNombre);
                session.merge(materia);
                System.out.println("Materia modificada.");
            } else {
                System.out.println("Materia no encontrada.");
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public static void inscribirEstudianteAMateria() {
        System.out.println("Ingrese el ID del estudiante:");
        int idEstudiante = sc.nextInt();
        System.out.println("Ingrese el ID de la materia:");
        int idMateria = sc.nextInt();

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Estudiante estudiante = session.find(Estudiante.class, idEstudiante);
            Materia materia = session.find(Materia.class, idMateria);
            if (estudiante != null && materia != null) {
                estudiante.getMaterias().add(materia);
                materia.getEstudiantes().add(estudiante);
                session.merge(estudiante);
                System.out.println("Estudiante inscrito en la materia.");
            } else {
                System.out.println("Estudiante o materia no encontrados.");
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public static void verEstudiantes() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Estudiante> estudiantes = session.createQuery("from Estudiante", Estudiante.class).list();
            for (Estudiante estudiante : estudiantes) {
                System.out.println("ID: " + estudiante.getId_estudiante() + ", Nombre: " + estudiante.getNombre());
                System.out.println("Materias inscritas:");
                for (Materia materia : estudiante.getMaterias()) {
                    System.out.println(" - " + materia.getNombre() + ", profesor: " + (materia.getProfesor() != null ? materia.getProfesor().getNombre() : "Sin profesor"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void verProfesores() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Profesor> profesores = session.createQuery("from Profesor", Profesor.class).list();
            for (Profesor profesor : profesores) {
                System.out.println("ID: " + profesor.getId_profesor() + ", Nombre: " + profesor.getNombre());
                System.out.println("Materias que dicta:");
                for (Materia materia : profesor.getMaterias()) {
                    System.out.println(" - " + materia.getNombre());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void verMaterias() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Materia> materias = session.createQuery("from Materia", Materia.class).list();
            for (Materia materia : materias) {
                System.out.println("ID: " + materia.getId_materia() + ", Nombre: " + materia.getNombre() + ", Profesor: " + (materia.getProfesor() != null ? materia.getProfesor().getNombre() : "Sin profesor"));
                System.out.println("Estudiantes inscritos:");
                for (Estudiante estudiante : materia.getEstudiantes()) {
                    System.out.println(" - " + estudiante.getNombre());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

