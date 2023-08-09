package com.ogp.icms.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/board")
public class BoardController {

    @GetMapping("/list")
    public String list(@RequestParam(value = "n", defaultValue = "notice") String n, Model model) {
        model.addAttribute("n", n);
        return "board/list";
    }

    @GetMapping("/write")
    public String write() {
        return "board/write";
    }

    @GetMapping("/edit")
    public String edit() {
        return "board/edit";
    }

    @GetMapping("/detail")
    public String detail() {return "board/detail";}

    @GetMapping("/comment/edit")
    public String commentEdit() {return "board/comment/edit";}
}
