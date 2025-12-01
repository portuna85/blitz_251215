package com.blitz.springboot.web;

import com.blitz.springboot.config.auth.LoginUser;
import com.blitz.springboot.config.auth.dto.SessionUser;
import com.blitz.springboot.service.PostsService;
import com.blitz.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 뷰 렌더링을 위한 컨트롤러
 * SRP: 뷰 페이지 라우팅 책임
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;

    /**
     * 메인 페이지
     *
     * @param model 뷰 모델
     * @param user 로그인 사용자 정보 (세션)
     * @return 메인 페이지 템플릿명
     */
    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
        model.addAttribute("posts", postsService.findAllDesc());
        if (user != null) {
            model.addAttribute("userName", user.getName());
            log.debug("로그인 사용자: {}", user.getName());
        }
        return "index";
    }

    /**
     * 게시글 작성 페이지
     *
     * @return 게시글 작성 페이지 템플릿명
     */
    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }

    /**
     * 게시글 수정 페이지
     *
     * @param id 게시글 ID
     * @param model 뷰 모델
     * @return 게시글 수정 페이지 템플릿명
     */
    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }
}
