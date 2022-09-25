package ru.practicum.shareit.utils;

public class unknown {
    public void testFindPublicationYearWithDaoException() {
        BookService bookService = new BookService();
        BookDao mockBookDao = Mockito.mock(BookDao.class);
        bookService.setBookDao(mockBookDao);
        Mockito
                .when(mockBookDao.findPublicationDate(Mockito.anyInt()))
                .thenThrow(new DataNotAvailableException("Ошибка при доступе к базе"));
        final DataNotAvailableException exception = Assertions.assertThrows(
                DataNotAvailableException.class,
                () -> bookService.findPublicationYear(5));
        Assertions.assertEquals("Ошибка при доступе к базе", exception.getMessage());
    }
}

