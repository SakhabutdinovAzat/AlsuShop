package ru.clothingstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.clothingstore.model.good.Good;
import ru.clothingstore.service.GoodService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/good")
public class GoodController {

    private final GoodService goodService;

    @Autowired
    public GoodController(GoodService goodService) {
        this.goodService = goodService;
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") int id, Model model) {
        model.addAttribute("good", goodService.getGoodById(id, true));

        return "good/detail";
    }

    // TODO Настроить пангинацию
    @GetMapping("/category/{id}")
    public String category(@PathVariable("id") int categoryId, Model model) {
        // Сколько товаров выводить в каталоге
        Integer goodsCount = 12;

        model.addAttribute("category", categoryId);
        model.addAttribute("catalogGoods", new ArrayList<>(goodService.getGoodsByCategory(categoryId, true)));

        return "good/category";
    }

    @PostMapping("/search")
    public String search(@RequestParam("title") String title, Model model){
        List<Good> goods = goodService.searchByTitle(title);
        if (goods.isEmpty())
            return "redirect:/index";
        model.addAttribute("goods", goods);

        return "good/search";
    }
}
