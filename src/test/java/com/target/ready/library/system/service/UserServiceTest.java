package com.target.ready.library.system.service;

import com.target.ready.library.system.repository.BookCategoryRepository;
import com.target.ready.library.system.repository.BookRepository;
import com.target.ready.library.system.repository.UserRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {UserServiceTest.class})
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    BookRepository bookRepository;

    @Mock
    BookCategoryRepository bookCategoryRepository;

    @InjectMocks
    UserService userService;
}
