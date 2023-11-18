package ru.clothingstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.clothingstore.model.good.Good;
import ru.clothingstore.service.GoodService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    @GetMapping("/category/{id}")
    public String category(@PathVariable("id") int categoryId, Model model,
                           @RequestParam(value = "offset", defaultValue = "0", required = false) @Min(0) Integer offset,
                           @RequestParam(value = "limit", defaultValue = "3", required = false) @Min(1) @Max(20) Integer limit,
                           @RequestParam(value = "sort", defaultValue = "title", required = false) String sort) {

        Page<Good> goodsPage = goodService.getGoodsByCategory(categoryId, true, offset, limit, sort);
        List<Good> goodList = goodsPage.getContent();
        Good good = goodList.get(0);

        model.addAttribute("category", categoryId);
        model.addAttribute("goodList", goodList);
        model.addAttribute("goodsPage", goodsPage);
        model.addAttribute("url", "good/category/" + categoryId);

        int totalPages = goodsPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
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
