import axiosClient from "./axiosClient";

export function registerUser(userData) {
  return axiosClient.post("/api/v1/auth/register", userData);
}