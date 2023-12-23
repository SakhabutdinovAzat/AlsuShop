package ru.clothingstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.clothingstore.model.news.News;
import ru.clothingstore.service.NewsService;

@Controller
@RequestMapping(value = "/news")
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping()
//    @ApiOperation("Получение списка новостей")
    public String newsList(Model model) {
        model.addAttribute("news", newsService.getLastAddedNews(10));
        return "news/news";
    }

    @GetMapping("/detail/{id}")
    public String getDetailNews(@PathVariable("id") int id, Model model) {
        model.addAttribute("news", newsService.getNewsById(id));
        return "news/detail";
    }

    @ResponseBody
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public News getNews(@PathVariable("id") int id) {
        return newsService.getNewsById(id);
    }

    // Todo delete after tests
    @GetMapping("/test")
    public String index() {
        return "test";
    }
}
