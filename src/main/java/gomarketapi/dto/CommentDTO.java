package gomarketapi.dto;

import java.time.LocalDateTime;

import gomarketapi.model.CommentBean;

public class CommentDTO {

  private Long id;
  private UserDTO user;
  private String comment;
  private Boolean isPinned;
  private Integer likes;
  private LocalDateTime createdAt;

  public CommentDTO() {
  }

  public CommentDTO(CommentBean comBean) {
    this.id = comBean.getId();
    this.user = new UserDTO(comBean.getUser());
    this.comment = comBean.getComment();
    this.likes = comBean.getLikes();
    this.isPinned = comBean.getIsPinned();
    this.createdAt = comBean.getCreatedAt();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UserDTO getUser() {
    return user;
  }

  public void setUser(UserDTO user) {
    this.user = user;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public Boolean getIsPinned() {
    return isPinned;
  }

  public void setIsPinned(Boolean isPinned) {
    this.isPinned = isPinned;
  }

  public Integer getLikes() {
    return likes;
  }

  public void setLikes(Integer likes) {
    this.likes = likes;
  }

}
