import axios from "axios";

const BASE_URL = "http://localhost:9292/api/posts";

const token = () => localStorage.getItem("token");

const authHeader = () => ({
  headers: {
    Authorization: `Bearer ${token()}`
  }
});

export const likePost = (postId, userId) =>
  axios.post(
    `${BASE_URL}/${postId}/like/${userId}`,
    {},
    authHeader()
  );

export const unlikePost = (postId, userId) =>
  axios.delete(
    `${BASE_URL}/${postId}/like/${userId}`,
    authHeader()
  );

export const getLikeCount = (postId) =>
  axios.get(
    `${BASE_URL}/${postId}/likes`,
    authHeader()
  );

export const hasUserLiked = (postId, userId) =>
  axios.get(
    `${BASE_URL}/${postId}/liked/${userId}`,
    authHeader()
  );