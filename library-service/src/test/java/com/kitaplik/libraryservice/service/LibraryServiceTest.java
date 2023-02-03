package com.kitaplik.libraryservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kitaplik.libraryservice.client.BookServiceClient;
import com.kitaplik.libraryservice.dto.BookDto;
import com.kitaplik.libraryservice.dto.BookIdDto;
import com.kitaplik.libraryservice.dto.LibraryDto;
import com.kitaplik.libraryservice.exception.LibraryNotFoundException;
import com.kitaplik.libraryservice.model.Library;
import com.kitaplik.libraryservice.repository.LibraryRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LibraryServiceTest {

    private LibraryService libraryService;
    private LibraryRepository libraryRepository;
    private BookServiceClient bookServiceClient;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        libraryRepository = Mockito.mock(LibraryRepository.class);
        bookServiceClient = Mockito.mock(BookServiceClient.class);

        libraryService = new LibraryService(libraryRepository, bookServiceClient);

    }

    @DisplayName("should Return LibraryDto with Detailed BookList With BookDto And Updated LibraryId when the parameter of the getAllBooksInLibraryById LibraryId Exist And Library UserBook List Size More Than Two")
    @Test
    // Adim 1: Test ismini yazilmasi
    void shouldReturnDetailedBookListWithBookDtoAndUpdatedLibraryId_whenLibraryIdExistAndLibraryUserBookListSizeMoreThan2() {
        // Adim 2: Test verilerinin hazirlanmasi
        String id = "libraryId";
        List<String> userBook = Arrays.asList("book1", "book2", "book3");
        Library library = new Library(id, userBook);
        BookDto book1 = new BookDto(new BookIdDto("book1", "isbn"), "title1", 2021, "author1", "press1");
        BookDto book2 = new BookDto(new BookIdDto("book2", "isbn"), "title1", 2021, "author1", "press1");
        BookDto book3 = new BookDto(new BookIdDto("book3", "isbn"), "title1", 2021, "author1", "press1");

        List<BookDto> bookDtoList = Arrays.asList(book1, book2, book3);
        LibraryDto expectedResult = new LibraryDto(id+3, bookDtoList);

        // Adim 3: Bagimli servicelerin davranislarinin belirlenmesi
        Mockito.when(libraryRepository.findById(id)).thenReturn(Optional.of(library));
        Mockito.when(bookServiceClient.getBookById("book1")).thenReturn(ResponseEntity.ok(book1));
        Mockito.when(bookServiceClient.getBookById("book2")).thenReturn(ResponseEntity.ok(book2));
        Mockito.when(bookServiceClient.getBookById("book3")).thenReturn(ResponseEntity.ok(book3));

        // Adim 4: Test edilecek metodun calistirilmasi
        LibraryDto result = libraryService.getAllBooksInLibraryById(id);

        // Adim 5: Test sonuclarinin karsilastirilmasi
        assertEquals(expectedResult, result);

        // Adim 6: Bagimli servislerin calisitirilmasinin kontrol edilmesi
        Mockito.verify(libraryRepository).findById(id);
        Mockito.verify(bookServiceClient).getBookById("book1");
        Mockito.verify(bookServiceClient).getBookById("book2");
        Mockito.verify(bookServiceClient).getBookById("book3");
        Mockito.verify(bookServiceClient, new Times(3)).getBookById(Mockito.any(String.class));

    }

    @DisplayName("should Return LibraryDto with Detailed BookList With BookDto when the parameter of the getAllBooksInLibraryById LibraryId Exist")
    @Test
        // Adim 1: Test ismini yazilmasi
    void shouldReturnDetailedBookListWithBookDto_whenLibraryIdExist() {
        // Adim 2: Test verilerinin hazirlanmasi
        String id = "libraryId";
        List<String> userBook = Arrays.asList("book1", "book2");
        Library library = new Library(id, userBook);
        BookDto book1 = new BookDto(new BookIdDto("book1", "isbn"), "title1", 2021, "author1", "press1");
        BookDto book2 = new BookDto(new BookIdDto("book2", "isbn"), "title1", 2021, "author1", "press1");

        List<BookDto> bookDtoList = Arrays.asList(book1, book2);
        LibraryDto expectedResult = new LibraryDto(id, bookDtoList);

        // Adim 3: Bagimli servicelerin davranislarinin belirlenmesi
        Mockito.when(libraryRepository.findById(id)).thenReturn(Optional.of(library));
        Mockito.when(bookServiceClient.getBookById("book1")).thenReturn(ResponseEntity.ok(book1));
        Mockito.when(bookServiceClient.getBookById("book2")).thenReturn(ResponseEntity.ok(book2));

        // Adim 4: Test edilecek metodun calistirilmasi
        LibraryDto result = libraryService.getAllBooksInLibraryById(id);

        // Adim 5: Test sonuclarinin karsilastirilmasi
        assertEquals(expectedResult, result);

        // Adim 6: Bagimli servislerin calisitirilmasinin kontrol edilmesi
        Mockito.verify(libraryRepository).findById(id);
        Mockito.verify(bookServiceClient).getBookById("book1");
        Mockito.verify(bookServiceClient).getBookById("book2");
        Mockito.verify(bookServiceClient, new Times(2)).getBookById(Mockito.any(String.class));

    }


    @DisplayName("should Throw LibraryNotFoundException when the parameter of the getAllBooksInLibraryById LibraryId Does Not Exist")
    @Test
        // Adim 1: Test ismini yazilmasi
    void shouldThrowLibraryNotFoundException_whenLibraryIdDoesNotExist() {
        // Adim 2: Test verilerinin hazirlanmasi
        String id = "libraryId";

        // Adim 3: Bagimli servicelerin davranislarinin belirlenmesi
        Mockito.when(libraryRepository.findById(id)).thenReturn(Optional.empty());

        // Adim 4: Test edilecek metodun calistirilmasi
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> libraryService.getAllBooksInLibraryById(id))
                .isInstanceOf(LibraryNotFoundException.class)
                .hasMessageContaining("Library could not found by id: " + id);

        /*
        assertThrows(LibraryNotFoundException.class,
                () -> libraryService.getAllBooksInLibraryById(id));
        */


        // Adim 6: Bagimli servislerin calisitirilmasinin kontrol edilmesi
        Mockito.verify(libraryRepository).findById(id);
        Mockito.verifyNoInteractions(bookServiceClient);

    }

    @AfterEach
    void tearDown() {

    }

}