package com.library.service;
import com.library.dao.BookDAO; // Importation de BookDAO
import com.library.model.Book;   // Importation de Book
import com.library.util.DbConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class BookService {
    private BookDAO bookDAO;
    private Connection connection;


    public BookService() {
        try {
            this.connection = DbConnection.getConnection();
            if (this.connection == null) {
                throw new SQLException("Impossible d'établir la connexion à la base de données");
            }
            this.bookDAO = new BookDAO(this.connection);
            if (this.bookDAO == null) {
                throw new IllegalStateException("BookDAO n'a pas été initialisé correctement");
            }
        } catch (Exception e) {
            System.err.println("Erreur critique lors de l'initialisation du service de livres : " + e.getMessage());
            // Vous pourriez vouloir relancer l'exception ici
            throw new RuntimeException("Impossible d'initialiser le service de livres", e);
        }
    }

    public BookService(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    // Ajouter un livre
    public void addBook(Book book) {
        bookDAO.add(book);
    }

    // Afficher tous les livres
    public void displayBooks() {
        List<Book> books = bookDAO.getAllBooks();
        for (Book book : books) {
            System.out.println(book);
        }
    }

    // Trouver un livre par ID
    public Book findBookById(int id) {
        return bookDAO.getBookById(id);
    }

    // Supprimer un livre par ID
    public void deleteBook(int id) {
        bookDAO.delete(id);
    }

    // Mise à jour des informations d'un livre
    public void updateBook(Book book) {
        bookDAO.update(book);
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
            }
        }
    }


}
