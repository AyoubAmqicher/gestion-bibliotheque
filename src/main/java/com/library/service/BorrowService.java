
package com.library.service;

import com.library.dao.BookDAO;
import com.library.dao.StudentDAO;
import com.library.model.Book;
import com.library.model.Student;
import com.library.dao.BorrowDAO;
import com.library.model.Borrow;
import com.library.util.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class BorrowService {

    private BorrowDAO borrowDAO;
    private BookDAO bookDAO;
    private StudentDAO studentDAO;
    private Connection connection;

    // Constructeur avec BorrowDAO
    public BorrowService() {
        try {
            this.connection = DbConnection.getConnection();
            this.studentDAO = new StudentDAO(this.connection);
            this.bookDAO = new BookDAO(this.connection);
            this.borrowDAO = new BorrowDAO(this.connection);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation du service d'emprunts : " + e.getMessage());
        }
    }

    public BorrowService(BorrowDAO borrowDAO, BookDAO bookDAO, StudentDAO studentDAO) {
        this.borrowDAO = borrowDAO;
        this.bookDAO = bookDAO;
        this.studentDAO = studentDAO;
    }

    public Borrow getBorrow(int borrowId) {
        return borrowDAO.getBorrowById(borrowId);
    }
    // Méthode pour emprunter un livre
    public void borrowBook(Borrow borrow) {
        // Sauvegarde de l'emprunt dans la base de données
        borrowDAO.save(borrow);
    }

    // Afficher les emprunts (méthode fictive, à adapter)
    public void displayBorrows() {
        System.out.println("Liste des emprunts...");
        // Afficher les emprunts enregistrés (adapté selon votre DAO)
    }

    public void returnBook(int borrowId) {
        String sql = "UPDATE borrows SET return_date = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Création de la date actuelle et conversion en java.sql.Date
            java.util.Date utilDate = new java.util.Date();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            statement.setDate(1, sqlDate);
            statement.setInt(2, borrowId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Livre retourné avec succès !");
            } else {
                System.out.println("Emprunt non trouvé.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du retour du livre : " + e.getMessage());
        }
    }

    public boolean borrowBook(int studentId, int bookId, Date borrowDate) {
        // Vérifier que l'étudiant existe
        Student student = studentDAO.getStudentById(studentId);
        if (student == null) {
            System.out.println("Étudiant non trouvé");
            return false;
        }

        // Vérifier que le livre existe
        Book book = bookDAO.getBookById(bookId);
        if (book == null) {
            System.out.println("Livre non trouvé");
            return false;
        }

        // Créer et sauvegarder l'emprunt
        Borrow borrow = new Borrow(0, student, book, borrowDate, null);
        try {
            borrowDAO.save(borrow);
            System.out.println("Livre emprunté avec succès !");
            return true;
        } catch (Exception e) {
            System.err.println("Erreur lors de l'emprunt du livre : " + e.getMessage());
            return false;
        }
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

    public List<Borrow> getAllBorrows() {
        try {
            return borrowDAO.getAllBorrows();
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des emprunts : " + e.getMessage());
            return null;
        }
    }
}
