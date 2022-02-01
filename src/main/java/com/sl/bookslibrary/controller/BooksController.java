package com.sl.bookslibrary.controller;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.sl.bookslibrary.dal.domain.Book;
import com.sl.bookslibrary.dal.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

import static com.sl.bookslibrary.utils.AppConstants.COVER_ID_ROOT;

@Controller
@Slf4j
public class BooksController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping(value = "/books/{bookId}")
    public String getBook(@PathVariable String bookId, Model model){

        Optional<Book> opBook = bookRepository.findById(bookId);
        if(opBook.isPresent()){
            Book book = opBook.get();
            cleanseBook(book);
            model.addAttribute("book", book);

            // Clean CoverId . Just get the just one
            String coverImageUrl = "/images/no-image-found.PNG";
            if(!CollectionUtils.isEmpty(book.getCoverIds())) {
                coverImageUrl = COVER_ID_ROOT + book.getCoverIds().get(0) + "-L.jpg";
            }
            model.addAttribute("coverImage", coverImageUrl);

            return "book-details"; //Name of the thyme leaf html
        }
        return "book-not-found";
    }

    /**
     * Ideally the below method is not required if the data was inserted correctly.
     */
    private void cleanseBook(Book book){

        // Clean description
        if(StringUtils.isNotBlank(book.getDescription())) {
            try {
                JSONObject jsonObject = new JSONObject(book.getDescription());
                book.setDescription(jsonObject.optString("value"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
