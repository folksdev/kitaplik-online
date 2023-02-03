package com.kitaplik.libraryservice.service;

import com.kitaplik.bookservice.BookId;
import com.kitaplik.bookservice.BookServiceGrpc;
import com.kitaplik.bookservice.Isbn;
import com.kitaplik.libraryservice.client.BookServiceClient;
import com.kitaplik.libraryservice.dto.AddBookRequest;
import com.kitaplik.libraryservice.dto.BookDto;
import com.kitaplik.libraryservice.dto.LibraryDto;
import com.kitaplik.libraryservice.exception.LibraryNotFoundException;
import com.kitaplik.libraryservice.model.Library;
import com.kitaplik.libraryservice.repository.LibraryRepository;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final BookServiceClient bookServiceClient;

    @GrpcClient("book-service")
    private BookServiceGrpc.BookServiceBlockingStub bookServiceBlockingStub;

    public LibraryService(LibraryRepository libraryRepository,
                          BookServiceClient bookServiceClient) {
        this.libraryRepository = libraryRepository;
        this.bookServiceClient = bookServiceClient;
    }

    public LibraryDto getAllBooksInLibraryById(String id) {  // public bir metod, donus degeri LibraryDto olacak, butun kitaplari donecek verdigimiz id degerine gore, String deger alacak
        Library library = getLibraryById(id); // libraryRepository nin findById metodunu id ile cagir, bulumazsan hata firlat

        String libraryId = library.getId();
        List<BookDto> bookDtoList = new ArrayList<>();

        if (library.getUserBook().size() > 2) {
            libraryId += library.getUserBook().size();
        }

        for (String bookId: library.getUserBook()) {
            BookDto bookDto = bookServiceClient.getBookById(bookId).getBody();
            bookDtoList.add(bookDto);
        }

        LibraryDto libraryDto = new LibraryDto(libraryId, bookDtoList);
        return libraryDto; // library dto'yu return et
    }


    private Library getLibraryById(String id) {
        return libraryRepository.findById(id)
                .orElseThrow(() -> new LibraryNotFoundException("Library could not found by id: " + id));
    }

    public LibraryDto createLibrary() {
        Library newLibrary = libraryRepository.save(new Library());
        return new LibraryDto(newLibrary.getId());
    }

    public void addBookToLibrary(AddBookRequest request) {
        BookId bookId = bookServiceBlockingStub.getBookIdByIsbn(Isbn.newBuilder().setIsbn(request.getIsbn()).build());


        //String bookId = bookServiceClient.getBookByIsbn(request.getIsbn()).getBody().getBookId();

        Library library = libraryRepository.findById(request.getId())
                .orElseThrow(() -> new LibraryNotFoundException("Library could not found by id: " + request.getId()));

        library.getUserBook()
                .add(bookId.getBookId());

        libraryRepository.save(library);
    }


    public List<String> getAllLibraries() {

        return libraryRepository.findAll()
                .stream()
                .map(l -> l.getId())
                .collect(Collectors.toList());
    }
}
