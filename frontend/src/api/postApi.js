import axiosClient from "./axiosClient";


export function getAllPosts(pageNumber = 0) {
  return axiosClient.get("/posts/", {
    params: { pageNumber, pageSize: 10, sortBy: "postId", sortDir: "desc" },
  });
}

export function getPostById(postId) {
  return axiosClient.get(`/posts/${postId}`);
}

export function createPost(userId, categoryId, postDto) {
  return axiosClient.post(
    `/posts/user/${userId}/category/${categoryId}/posts`,
    postDto
  );
}

export function updatePost(postId, postDto) {
  return axiosClient.put(`/posts/${postId}`, postDto);
}

export function deletePost(postId) {
  return axiosClient.delete(`/posts/${postId}`);
}

export function searchPosts(keyword) {
  return axiosClient.get(`/posts/search/${keyword}`);
}

export const uploadPostImage = (postId, file) => {
  const formData = new FormData();
  formData.append("image", file);

  return axiosClient.post(
    `/posts/image/${postId}`,
    formData,
    {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    }
  );
};

// Builds the URL to actually display an uploaded image.
export function getImageUrl(imageName) {
  return `${import.meta.env.VITE_API_URL}/posts/image/${imageName}`;
}

export function getPostsByCategory(categoryId) {
  return axiosClient.get(`/posts/category/${categoryId}/posts`);
}

export function getPostsByUser(userId) {
  return axiosClient.get(`/api/user/${userId}/posts`);
}