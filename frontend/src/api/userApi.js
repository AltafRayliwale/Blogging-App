import axiosClient from "./axiosClient";

export function getAllUsers() {
  return axiosClient.get("/api");
}

export function updateUser(userId, userDto) {
  return axiosClient.put(`/api/${userId}`, userDto);
}


export const uploadProfileImage = (userId, file) => {
  const formData = new FormData();
  formData.append("image", file);

  return axiosClient.post(`/api/image/${userId}`, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
};

export const getProfileImageUrl = (imageName) => {
  return `${import.meta.env.VITE_API_URL}/api/image/${imageName}`;
};

export function getUserImageUrl(imageName) {
  return `${import.meta.env.VITE_API_URL}/api/image/${imageName}`;
}