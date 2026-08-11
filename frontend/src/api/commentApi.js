import axiosClient from "./axiosClient";

export function createComment(postId, content) {
  return axiosClient.post(`/comments/${postId}`, { content });
}

export function deleteComment(commentId) {
  return axiosClient.delete(`/comments/${commentId}`);
}

export function getCategories() {
  return axiosClient.get("/categories");
}