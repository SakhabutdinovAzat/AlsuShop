package ru.clothingstore.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.clothingstore.model.good.Good;
import ru.clothingstore.model.news.News;
import ru.clothingstore.service.NewsService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller(value = "adminNewsController")
@RequestMapping(value = "/admin/news")
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    // TODO  Реализовать пангинацию        List<News> news = new ArrayList<>(newsService.getNews(100));
    @GetMapping()
    public String index(Model model) {
        List<News> news = new ArrayList<>(newsService.getAllNews());
        news.sort(Comparator.comparing(News::getId));
        model.addAttribute("newsList", news);
        return "admin/news/index";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("news", new News());
        return "admin/news/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("news") @Valid News news, BindingResult bindingResult, Model model,
                      RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()){
            return "admin/news/add";}
        newsService.addNews(news);
        redirectAttributes.addFlashAttribute("success", "News has added: " + news);
        return "redirect:/admin/news";
    }

    @GetMapping(value = {"/{id}/update", "/update"})
    public String update(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttrs) {

        if (id == 0) {
            redirectAttrs.addFlashAttribute("error", "Not the selected good");
            return "redirect:/admin/news";
        }
        News news = newsService.getNewsById(id);
        model.addAttribute("news", news);
        return "admin/news/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("news") @Valid News news, BindingResult bindingResult, Model model,
                         RedirectAttributes redirectAttrs) {

        if (bindingResult.hasErrors()) {
            return "admin/news/update";
        }
        newsService.updateNews(news);
        redirectAttrs.addFlashAttribute("success", "News has updated: " + news);
        return "redirect:/admin/news";
    }

    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id, RedirectAttributes redirectAttrs) {
        News news = newsService.getNewsById(id);
        newsService.deleteNews(id);
        redirectAttrs.addFlashAttribute("success", "News has deleted: " + news);
        return "redirect:/admin/news";
    }

}
