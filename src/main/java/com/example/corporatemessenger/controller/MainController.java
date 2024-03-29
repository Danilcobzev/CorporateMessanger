package com.example.corporatemessenger.controller;

import com.example.corporatemessenger.domen.User;
import com.example.corporatemessenger.domen.dto.MessagePOJO;
import com.example.corporatemessenger.constans.Attribute;
import com.example.corporatemessenger.service.IMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class MainController {

    @Value("${upload.path}")
    private String uploadPath;

    private final IMessageService messageService;

    public MainController(IMessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/")
    public String greeting(Map<String, Object> model)
    {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        Iterable<MessagePOJO> messages = null;

        if (!StringUtils.isEmpty(filter)) {
            messages = messageService.findByTag(filter);
        } else {
            messages = messageService.getAll();
        }
        model.addAttribute(Attribute.MESSAGES, messages);
        model.addAttribute(Attribute.FILTER, filter);
        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @RequestParam String tag,
            Map<String, Object> model,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) throws IOException {
        MessagePOJO message = new MessagePOJO(text, tag, user);

        if (file != null && !StringUtils.isEmpty(file.getOriginalFilename())) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            message.setFilename(resultFilename);
        }

        messageService.save(message);

        List<MessagePOJO> messages = messageService.getAll();

        model.put(Attribute.MESSAGES, messages);

        return "main";
    }
}
