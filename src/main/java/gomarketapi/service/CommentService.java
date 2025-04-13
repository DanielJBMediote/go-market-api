package gomarketapi.service;

import org.springframework.stereotype.Service;

import gomarketapi.contexts.UserContext;
import gomarketapi.exceptions.NotFoundException;
import gomarketapi.model.CommentBean;
import gomarketapi.model.UserBean;
import gomarketapi.repository.CommentRepository;

@Service
public class CommentService {

  private final CommentRepository commentRepository;
  private final UserContext userContext;

  public CommentService(CommentRepository commentRepository, UserContext userContext) {
    this.commentRepository = commentRepository;
    this.userContext = userContext;
  }

  public CommentBean create(String comment) {
    CommentBean comBean = new CommentBean();
    comBean.setComment(comment);
    comBean.setUser(new UserBean(userContext.getUserId()));
    comBean.setLikes(0);
    comBean.setIsPinned(false);

    return this.commentRepository.save(comBean);
  }

  public CommentBean update(Long commentId, String comment) {
    CommentBean comBean = this.commentRepository.findById(commentId)
        .orElseThrow(() -> new NotFoundException("No comment found."));

    comBean.setComment(comment);
    comBean.setUser(new UserBean(userContext.getUserId()));
    return this.commentRepository.save(comBean);
  }
}
