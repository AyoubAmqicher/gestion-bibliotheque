
package com.library.dao;

import com.library.model.Book;
import com.library.model.Borrow;
import com.library.model.Student;
import com.library.util.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowDAO {
    private final StudentDAO studentDAO;
    private final BookDAO bookDAO;
    private final Connection connection;


    public BorrowDAO(Connection connection) {
        this.connection = connection;
        this.studentDAO = new StudentDAO(connection); // même connexion
        this.bookDAO = new BookDAO(connection);       // même connexion
    }

    public List<Borrow> getAllBorrows() {
        List<Borrow> borrows = new ArrayList<>();
        String query = "SELECT * FROM borrows";
        try (Connection connection = DbConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Student student = studentDAO.getStudentById(rs.getInt("student_id"));
                Book book = bookDAO.getBookById(rs.getInt("book_id"));
                Borrow borrow = new Borrow(
                        rs.getInt("id"),
                        student,
                        book,
                        rs.getDate("borrow_date"),
                        rs.getDate("return_date")
                );
                borrows.add(borrow);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrows;
    }

public void save(Borrow borrow) {
    // Code d'insertion SQL
}



//    public void addBorrow(Borrow borrow) {
//        String query = "INSERT INTO borrows (member, book, borrow_date, return_date) VALUES (?, ?, ?, ?)";
//        try (Connection connection = DbConnection.getConnection();
//             PreparedStatement stmt = connection.prepareStatement(query)) {
//            stmt.setString(1, borrow.getMember());
//            stmt.setString(2, borrow.getBook());
//            stmt.setDate(3, new java.sql.Date(borrow.getBorrowDate().getTime()));
//            stmt.setDate(4, new java.sql.Date(borrow.getReturnDate().getTime()));
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public Borrow getBorrowById(int id) {
        String query = "SELECT * FROM borrows WHERE id = ?";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Student student = studentDAO.getStudentById(rs.getInt("student_id"));
                Book book = bookDAO.getBookById(rs.getInt("book_id"));

                return new Borrow(
                        rs.getInt("id"),
                        student,
                        book,
                        rs.getDate("borrow_date"),
                        rs.getDate("return_date")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'emprunt : " + e.getMessage());
        }
        return null;
    }
}
