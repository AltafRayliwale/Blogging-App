
import axiosClient from "./axiosClient";

export function getCategories() {
  return axiosClient.get("/categories");
}

export function createCategory(categoryDto) {
  return axiosClient.post("/categories/", categoryDto);
}

export function updateCategory(categoryId, categoryDto) {
  return axiosClient.put(`/categories/${categoryId}`, categoryDto);
}

export function deleteCategory(categoryId) {
  return axiosClient.delete(`/categories/${categoryId}`);
}