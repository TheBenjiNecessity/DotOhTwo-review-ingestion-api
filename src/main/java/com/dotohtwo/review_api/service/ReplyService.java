package com.dotohtwo.review_api.service;

import com.dotohtwo.review_api.dto.CreateReplyRequest;
import com.dotohtwo.models.model.Reply;
import com.dotohtwo.models.model.ReplyKey;
import com.dotohtwo.review_api.repository.ReplyRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReplyService {

    private final ReplyRepository replyRepository;

    public ReplyService(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    public List<Reply> getReplies(UUID parentId) {
        return replyRepository.findByKeyParentId(parentId);
    }

    public Optional<Reply> getReply(UUID replyId) {
        return replyRepository.findByKeyReplyId(replyId);
    }

    public Reply createReply(UUID parentId, CreateReplyRequest request) {
        UUID replyId = UUID.randomUUID();
        Reply reply = new Reply();
        reply.setKey(new ReplyKey(parentId, replyId));
        reply.setAuthorId(request.authorId());
        reply.setContent(request.content());
        reply.setCreatedAt(Instant.now());
        return replyRepository.save(reply);
    }
}
