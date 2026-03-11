package com.dotohtwo.review_api.service;

import com.dotohtwo.review_api.dto.CreateReplyRequest;
import com.dotohtwo.review_api.model.Reply;
import com.dotohtwo.review_api.model.ReplyKey;
import com.dotohtwo.review_api.repository.ReplyRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class ReplyService {

    private final ReplyRepository replyRepository;

    public ReplyService(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    public Reply createReply(UUID parentId, CreateReplyRequest request) {
        Reply reply = new Reply();
        reply.setKey(new ReplyKey(parentId, UUID.randomUUID()));
        reply.setAuthorId(request.authorId());
        reply.setContent(request.content());
        reply.setCreatedAt(Instant.now());
        return replyRepository.save(reply);
    }
}
