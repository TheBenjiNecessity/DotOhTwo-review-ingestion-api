package com.dotohtwo.review_api.controller;

import com.dotohtwo.review_api.dto.CreateReplyRequest;
import com.dotohtwo.review_api.model.Reply;
import com.dotohtwo.review_api.service.ReplyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/replies")
public class ReplyController {

    private final ReplyService replyService;

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @PostMapping("/{replyId}/replies")
    public ResponseEntity<Reply> createReplyToReply(
            @PathVariable UUID replyId,
            @RequestBody CreateReplyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(replyService.createReply(replyId, request));
    }
}
