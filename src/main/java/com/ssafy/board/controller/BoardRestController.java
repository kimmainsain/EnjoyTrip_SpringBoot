package com.ssafy.board.controller;

import com.github.pagehelper.Page;
import com.ssafy.board.dto.Board;
import com.ssafy.board.service.BoardService;
import com.ssafy.paging.PagingResult;
import com.ssafy.user.dto.User;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/board")
@Api(tags = {"게시판"})
public class BoardRestController {
    BoardService boardService;

    @Autowired
    public BoardRestController(BoardService boardService){
        this.boardService = boardService;
    }

    @PostMapping("/{name}") // boolean regist(String name, Board board);
    public ResponseEntity<String> regist(@PathVariable String name, @RequestBody Board board, HttpSession session){
        boolean adminReq = boardService.adminRequired(name);

        if(adminReq){
            Object isAdmin = session.getAttribute("admin");
            if(isAdmin == null){
                return  new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }

        User user = (User) session.getAttribute("user");

        if(user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        board.setAuthorNo(user.getNo());

        boolean rst = boardService.regist(name, board);

        if(rst){
            log.debug("no : {}", board.getArticleNo());
            return new ResponseEntity<>(Integer.toString(board.getArticleNo()), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{name}") // boolean delete(String name, int articleNo);
    public ResponseEntity<Boolean> delete(@PathVariable String name, @RequestBody int articleNo, HttpSession session){
        User user = (User)session.getAttribute("user");
        Object isAdmin = session.getAttribute("admin");

        if(user == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        boolean rst = boardService.delete(name, articleNo, user.getId(), (isAdmin == null ? false : true));

        if(rst){
            return new ResponseEntity<>(true, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{name}/paging") // Page<Board> list(String name);
    public ResponseEntity<PagingResult<Board>> list(@PathVariable String name){
        Page<Board> page = boardService.list(name);
        log.debug(name + " result : {}", page);

        return new ResponseEntity<>(new PagingResult<>(page), HttpStatus.OK);
    }

    @PutMapping("/{name}") // boolean edit(String name, Board board);
    public ResponseEntity<Boolean> edit(@PathVariable String name, @RequestBody Board board, HttpSession session){
        Object isAdmin = session.getAttribute("admin");
        User user = (User)session.getAttribute("user");

        if(user == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        boolean rst = boardService.edit(name, board, user.getId(), isAdmin == null ? false : true);

        if(rst){
            return new ResponseEntity<>(true, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{name}/{articleNo}") // Board select(String name, int articleNo);
    public ResponseEntity<Board> select(@PathVariable String name, @PathVariable int articleNo){
        Board board = boardService.select(name, articleNo);

        if(board == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(board, HttpStatus.OK);
        }
    }

    @GetMapping("/{name}/search/paging") // List<Board> keywordSelect(String name, String keyword);
    public ResponseEntity<PagingResult<Board>> keywordSearch(@PathVariable String name, @RequestParam String keyword) {
        log.debug("name : {}, keyword : {}", name,  keyword);
        Page<Board> list = boardService.keywordSelect(name, keyword);

        return new ResponseEntity<>(new PagingResult<>(list), HttpStatus.OK);
    }
}
