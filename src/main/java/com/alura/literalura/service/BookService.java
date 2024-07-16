package com.alura.literalura.service;

import com.alura.literalura.model.Author;
import com.alura.literalura.model.Book;
import com.alura.literalura.repository.AuthorRepository;
import com.alura.literalura.repository.BookRepository;
import com.alura.literalura.utils.HttpUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    public Book searchBookByTitle(String title) {
        String url = "https://gutendex.com/books/?search=" + title.replace(" ", "%20");
        String response = HttpUtils.getRequest(url);
        JSONObject jsonResponse = new JSONObject(response);
        JSONArray booksArray = jsonResponse.getJSONArray("results");

        if (booksArray.length() > 0) {
            JSONObject bookJson = booksArray.getJSONObject(0);
            Book book = new Book();
            book.setTitle(bookJson.getString("title"));

            // Extracción correcta del idioma desde JSONArray
            JSONArray languagesArray = bookJson.getJSONArray("languages");
            if (languagesArray.length() > 0) {
                book.setLanguage(languagesArray.getString(0));
            }

            JSONArray authorsArray = bookJson.getJSONArray("authors");
            if (authorsArray.length() > 0) {
                JSONObject authorJson = authorsArray.getJSONObject(0);
                Author author = new Author();
                author.setName(authorJson.getString("name"));
                author.setBirthYear(authorJson.optString("birth_year"));
                author.setDeathYear(authorJson.optString("death_year"));
                book.setAuthor(author);

                authorRepository.save(author);
            }

            bookRepository.save(book);
            return book;
        }

        return null;
    }

    public List<Book> listAllBooks() {
        return bookRepository.findAll();
    }

    public List<Author> listAllAuthors() {
        return authorRepository.findAll();
    }

    public List<Author> listAuthorsAliveInYear(String year) {
        return authorRepository.findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(year, year);
    }

    public List<Book> listBooksByLanguage(String language) {
        return bookRepository.findByLanguage(language);
    }
}
