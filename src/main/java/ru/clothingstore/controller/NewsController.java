package ru.clothingstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public String newsList(Model model) {
        model.addAttribute("news", newsService.getLastAddedNews(10));
        return "news/news";
    }

    @GetMapping("/detail/{id}")
    public String getDetailNews(@PathVariable("id") int id, Model model) {
        model.addAttribute("news", newsService.getNewsById(id));
        return "news/detail";
    }
}
