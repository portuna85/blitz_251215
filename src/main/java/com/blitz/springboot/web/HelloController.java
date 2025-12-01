package com.blitz.springboot.web;

import com.blitz.springboot.web.hello.dto.HelloResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello");
    }

    @GetMapping("/dto")
    public ResponseEntity<HelloResponseDto> helloDto(
            @RequestParam("name") String name,
            @RequestParam("amount") int amount) {
        return ResponseEntity.ok(new HelloResponseDto(name, amount));
    }

}
