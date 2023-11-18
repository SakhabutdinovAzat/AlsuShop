package ru.clothingstore.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.clothingstore.model.news.News;
import ru.clothingstore.repository.NewsRepository;
import ru.clothingstore.service.Impl.NewsServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsServiceTest {

    private News news;

    @InjectMocks
    private NewsServiceImpl newsService;

    @Mock
    private NewsRepository newsRepository;

    @BeforeEach
    void before(){
        news = new News();
        news.setId(1);
        news.setNewsImageLink("Image");
        news.setDate(new Date());
    }

    @Test
    void getNews() {
        News news2 = new News();
        news2.setId(2);

        when(newsRepository.findAll()).thenReturn(List.of(news, news2));
        Set<News> newsList = newsService.getAllNews();

        verify(newsRepository, times(1)).findAll();
        Assertions.assertTrue(newsList.containsAll(List.of(news, news2)));
    }


    @Test
    void getNewsById() {
        when(newsRepository.findById(1)).thenReturn(Optional.of(news));
        News newsFromDB = newsService.getNewsById(1);

        verify(newsRepository, times(1)).findById(1);
        Assertions.assertNotNull(newsFromDB);
    }

    @Test
    void addNews() {
        newsService.addNews(news);
        assertNotNull(news.getDate());
        verify(newsRepository, times(1)).save(news);
    }

    @Test
    void updateNews() {
        News news2 = new News();
        news2.setId(1);
        news2.setNewsImageLink("NewImage");
        news2.setDate(new Date());

        when(newsRepository.findById(1)).thenReturn(Optional.of(news));
        newsService.updateNews(news2);

        verify(newsRepository, times(1)).findById(1);
        verify(newsRepository, times(1)).save(news2);
    }

    @Test
    void deleteNews() {
        newsService.deleteNews(1);

        verify(newsRepository, times(1)).deleteById(1);
    }
}