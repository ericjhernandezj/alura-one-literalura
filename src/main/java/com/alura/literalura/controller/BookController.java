package com.alura.literalura.controller;

import com.alura.literalura.model.Book;
import com.alura.literalura.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;
import java.util.Scanner;

@ShellComponent
public class BookController {

    @Autowired
    private BookService bookService;

    @ShellMethod("Inicia la aplicacion de terminal")
    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Seleccione una opción:");
            System.out.println("1. Buscar libro por título");
            System.out.println("2. Listar libros registrados");
            System.out.println("3. Listar autores registrados");
            System.out.println("4. Listar autores vivos en determinado año");
            System.out.println("5. Listar libros por idioma");
            System.out.println("0. Salir");

            int option = scanner.nextInt();
            scanner.nextLine();  // Consume the newline

            switch (option) {
                case 1:
                    System.out.print("Ingrese el título del libro: ");
                    String title = scanner.nextLine();
                    Book book = bookService.searchBookByTitle(title);
                    if (book != null) {
                        System.out.println("Libro encontrado y registrado en la base de datos:");
                        System.out.println("Título: " + book.getTitle());
                        System.out.println("Autor: " + book.getAuthor().getName());
                        System.out.println("Idioma: " + book.getLanguage());
                    } else {
                        System.out.println("Libro no encontrado.");
                    }
                    break;
                case 2:
                    List<Book> books = bookService.listAllBooks();
                    books.forEach(b -> {
                        System.out.println("Título: " + b.getTitle() + ", Autor: " + b.getAuthor().getName() + ", Idioma: " + b.getLanguage());
                    });
                    break;
                case 3:
                    bookService.listAllAuthors().forEach(a -> {
                        System.out.println("Autor: " + a.getName() + ", Año de nacimiento: " + a.getBirthYear() + ", Año de muerte: " + a.getDeathYear());
                    });
                    break;
                case 4:
                    System.out.print("Ingrese el año: ");
                    String year = scanner.nextLine();
                    bookService.listAuthorsAliveInYear(year).forEach(a -> {
                        System.out.println("Autor: " + a.getName() + ", Año de nacimiento: " + a.getBirthYear() + ", Año de muerte: " + a.getDeathYear());
                    });
                    break;
                case 5:
                    System.out.print("Ingrese el idioma (es, en, fr, pt, etc.): ");
                    String language = scanner.nextLine();
                    bookService.listBooksByLanguage(language).forEach(b -> {
                        System.out.println("Título: " + b.getTitle() + ", Autor: " + b.getAuthor().getName() + ", Idioma: " + b.getLanguage());
                    });
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }
}
